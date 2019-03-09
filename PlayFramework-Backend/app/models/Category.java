package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints.Required;
import play.libs.Json;

@Entity
@Table(name = "categories")
public class Category extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String name;
    
    @Constraints.Required
 	public Long parent_id;
	
    //Foreign Keys
    @OneToMany(fetch = FetchType.LAZY, mappedBy="category") @JsonIgnore
    public List<ProductCategory> productcategory;
    
	public static final Finder<Long, Category> find = new Finder<>(Category.class);

	
	//Constructor	
	public Category() {
		
	}
	
	public Category(String name, Long parent_id, List<ProductCategory> products) {
		this.name = name;
		this.parent_id = parent_id;
		this.productcategory = products;
	}
		
}