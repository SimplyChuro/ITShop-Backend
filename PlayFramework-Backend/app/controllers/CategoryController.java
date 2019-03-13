package controllers;

import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import additions.MockData;
import additions.Secured;
import models.Category;
import models.ProductCategory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class CategoryController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private JsonNode jsonNode;
	private Category category;
	private List<Category> categories;
	
	@Inject
    public CategoryController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get one category	
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				category = Category.find.byId(id);
				return ok(Json.toJson(category));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get categories
	public CompletionStage<Result> getAll() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				categories = Category.find.all();
				if(categories.isEmpty()) {
					MockData data = new MockData();
					data.generate();
				}
				return ok(Json.toJson(categories));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Create category  
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				jsonNode = request.body().asJson().get("category");	
				category = new Category();
				if(jsonNode.findValue("parent_id") == null || jsonNode.findValue("parent_id").asText().equals("null")) {
					category.parent_id = null;
				} else {
					category.parent_id = jsonNode.findValue("parent_id").asLong();
				}
				category.name = jsonNode.findValue("name").asText();
				
				category.save();
				return ok(Json.toJson(category));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update category  
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				jsonNode = request.body().asJson().get("category");
				
				category = Category.find.byId(id);
				if(jsonNode.findValue("parent_id") == null || jsonNode.findValue("parent_id").asText().equals("null")) {
					category.parent_id = null;
				} else {
					category.parent_id = jsonNode.findValue("parent_id").asLong();
				}
				category.name = jsonNode.findValue("name").asText();
				
				category.update();
				return ok(Json.toJson(category));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete category 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				category = Category.find.byId(id);
				
				if(!(category.parent_id == null)) {
					categories = Category.find.query().where()
							.conjunction()
							.eq("parent_id", id)
							.endJunction()
							.findList();
					
					for(Category cat : categories) {
						for(ProductCategory productCat : cat.productcategory) {
							productCat.delete();
						}
						
						cat.delete();
						
					}
				}
				
				for(ProductCategory cat : category.productcategory) {
					cat.delete();
				}
				
				category.delete();
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
}