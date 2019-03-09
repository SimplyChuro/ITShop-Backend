package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints.Required;
import play.libs.Json;

@Entity
@Table(name = "pictures")
public class Picture extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
		
    @Constraints.Required
	public String url;
    
    //Foreign Key
    @ManyToOne @JsonIgnore
    public Product product;
    
	public static final Finder<Long, Picture> find = new Finder<>(Picture.class);

	
	public Picture() {}
	
	public Picture(String url, Product product) {
		this.url = url;
		this.product = product;
	}
	
}