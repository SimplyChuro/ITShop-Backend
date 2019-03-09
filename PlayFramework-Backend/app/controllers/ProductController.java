package controllers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import additions.PictureFilter;
import additions.S3Signature;
import additions.Secured;
import models.Product;

import models.Category;
import models.Picture;
import models.ProductCategory;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;

public class ProductController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private User userChecker;
	private List<Product> products;
	private Product product;
	private JsonNode jsonNode;
	private PictureFilter filter = new PictureFilter();
	
	
	@Inject
    public ProductController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get product
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				product = Product.find.byId(id);
		       
				return ok(Json.toJson(product));
			} catch(Exception e) {
				return notFound();
			}
		}, httpExecutionContext.current());
	}
	
	//Get products
	public CompletionStage<Result> getAll(Integer category, String name) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				products = new ArrayList<>();
				
				if (name != null && !name.isEmpty()) {
					if (!(category == 0)) {
						List<ProductCategory> productCategories = ProductCategory.find.query().where()
								.conjunction()
								.eq("category_id", category)
								.endJunction()
								.findList();
						for(ProductCategory cat : productCategories) {
							if(cat.product.name.equals(name)) {
								products.add(cat.product);
							}
						}
						
					} else {
						products = Product.find.query().where()
								.conjunction()
								.icontains("name", name)
								.endJunction()
								.orderBy("name asc")
								.findList();
					}
					
				} else if (!(category == 0)) {
					List<ProductCategory> productCategories = ProductCategory.find.query().where()
							.conjunction()
							.eq("category_id", category)
							.endJunction()
							.findList();
					for(ProductCategory cat : productCategories) {
						products.add(cat.product);
					}
					
				} else {
					products = Product.find.query().where()
							.conjunction()
							.endJunction()
							.findList();
				}
				
				return ok(Json.toJson(products));
			} catch(Exception e) {
				return notFound();
			}
		}, httpExecutionContext.current());
	}
	
	//Create product
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				userChecker = LoginController.getUser();
				
				if(userChecker.admin) {
					jsonNode = request().body().asJson().get("product");
					Long cat_id = jsonNode.get("category_id").asLong();
					
					product = new Product();
					product.saveProduct(jsonNode);
					
					Category childCategory = Category.find.byId(cat_id);
					Category parentCategory = Category.find.byId(childCategory.parent_id);
					
					ProductCategory categoryConnectionChild = new ProductCategory();
					categoryConnectionChild.product = product;
					categoryConnectionChild.category = childCategory;
					categoryConnectionChild.save();
					
					ProductCategory categoryConnectionParent = new ProductCategory();
					categoryConnectionParent.product = product;
					categoryConnectionParent.category = parentCategory;
					categoryConnectionParent.save();
					
					return ok(Json.toJson(product));
				} else {
					return forbidden();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update product
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				userChecker = LoginController.getUser();
				if(userChecker.admin) {
					jsonNode = request().body().asJson().get("product");
					
					product = Product.find.byId(id);
					product.updateProduct(jsonNode);
						
					try {
						Long cat_id = jsonNode.get("category_id").asLong();
						Category childCategory = Category.find.byId(cat_id);
						Category parentCategory = Category.find.byId(childCategory.parent_id);
						
						for(ProductCategory category : product.productcategory) {
							if(category.category.parent_id == null && category.category.id != childCategory.id) {
								category.category = childCategory;
								category.update();
							} else {
								if(category.category.id != parentCategory.id) {
									category.category = parentCategory;					
									category.update();
								}
							}
						}
						
					} catch(Exception e) {
						
					} finally {
						return ok(Json.toJson(product));	
					}
				} else {
					return forbidden();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete product
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				userChecker = LoginController.getUser();
				if(userChecker.admin) {
					
					product = Product.find.byId(id);
					for(Picture picture : product.pictures) {
						picture.delete();
					}
					
					for(ProductCategory cat : product.productcategory) {
						cat.delete();
					}
				
					product.delete();
							
					return ok(Json.toJson(""));
				} else {
					return forbidden();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Validate upload
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> validate(String name, String type, Integer size) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				userChecker = LoginController.getUser();
				
				if(filter.isValidPicture(name, type, size)) {
					S3Signature s3 = new S3Signature(name, type, size, "images/products/");
					
		           	return ok(s3.getS3EmberNode());
				} else {
					return badRequest();	
				}
			}catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
}