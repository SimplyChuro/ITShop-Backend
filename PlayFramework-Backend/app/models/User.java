package models;

import java.util.*;
import javax.persistence.*;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.*;
import play.data.validation.*;

@Entity
@Table(name = "users")
public class User extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @JsonIgnore
	private String authToken;
	
	@Column(length = 256, nullable = false)
	public String name;
    
    @Column(length = 256, nullable = false)
	public String surname;
    
    @Column(length = 256, unique = true, nullable = false)
	public String email;

    @JsonIgnore
	private String password;
    
    @Constraints.Required
   	public Boolean admin;

	
	public static final Finder<Long, User> find = new Finder<>(User.class);
	
	public void setBase() {
		admin = false;
		save();
	}
		
    //Token commands
    
    public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }
   
    public Boolean hasAuthToken() {
    	if(authToken != null) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public String getAuthToken() {
    	return authToken;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
    }
    
    public String getPassword() {
    	return password;
    }
	
    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try  {
            return find.query().where().eq("authToken", authToken).findOne();
        } catch (Exception e) {
            return null;
        }
    }

}