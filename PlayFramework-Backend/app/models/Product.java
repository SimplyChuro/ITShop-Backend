package models;

import java.util.*;
import javax.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.*;

@Entity
@Table(name = "products")
public class Product extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public String name;
	
	public String brand;
	
	public Double price;

	@Column(columnDefinition = "varchar(2048)")
	public String description;


	//Foreign Keys
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<Picture> pictures;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<ProductCategory> productcategory;


	public static final Finder<Long, Product> find = new Finder<>(Product.class);
	
	
	public void saveProduct(JsonNode objectNode) {
		
		name = objectNode.findPath("name").asText();
		brand = objectNode.findPath("brand").asText();
		price = objectNode.findPath("price").asDouble();
		description = objectNode.findPath("description").asText();
		
		save();
		
		for(JsonNode pictureNode : objectNode.get("pictures")) {

			System.out.println(pictureNode);
			Picture picture = new Picture();
			picture.url = pictureNode.findPath("url").asText();
			picture.product = this;
			picture.save();
			
		}

	}
	
	public void updateProduct(JsonNode objectNode) {
		
		name = objectNode.findPath("name").asText();
		brand = objectNode.findPath("brand").asText();
		price = objectNode.findPath("price").asDouble();
		description = objectNode.findPath("description").asText();
	
		
		for(Picture picture : pictures) {	
			Boolean checker = false;
			try {
				for(JsonNode pictureNode : objectNode.get("pictures")) {

					if(picture.id == pictureNode.findPath("id").asLong()) {
						checker = true;
					}
				}
				
				if(!(checker == true)) {
					picture.delete();
				}
				
			} catch(Exception e) {
				
			}
		}
	}
	
}