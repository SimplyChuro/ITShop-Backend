package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import additions.Secured;

import play.libs.concurrent.HttpExecutionContext;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.mindrot.jbcrypt.BCrypt;

import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class LoginController extends Controller {
	
	public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	public final static String AUTH_TOKEN = "authToken";
	
	private HttpExecutionContext httpExecutionContext;
	private User user;
	private JsonNode jsonNode;
	private ObjectNode response;
	
	@Inject
    public LoginController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	public static User getUser() {
	    return (User) Http.Context.current().args.get("user");
	}
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	// returns an authToken
	public CompletionStage<Result> login() {
	    return calculateResponse().thenApplyAsync(answer -> {
	    jsonNode = request().body().asJson();
	    response = Json.newObject();
		    try {
			    user = User.find.query().where()
			    		.conjunction()
			    		.eq("email", jsonNode.findPath("email").textValue())
			    		.endJunction()
			    		.findOne();	 
			    
			    if (BCrypt.checkpw(jsonNode.findPath("password").textValue(), user.getPassword())) {
			    	if(user.hasAuthToken()) {
			    		response.put(AUTH_TOKEN, user.getAuthToken());
			    		response.put("userID", user.id);
			    		response.put("adminChecker", user.admin);
			    			    		
			    		return ok(response);
			    	} else {
				        String authToken = user.createToken();
				        response.put(AUTH_TOKEN, authToken);
				        response.put("userID", user.id);
			    		response.put("adminChecker", user.admin);
			    	
				        return ok(response);
			        }
		        } else {
		        	response.put("error_message", "Incorrect email or password");
			    	return notFound(response);
		    	}
		    } catch(Exception e) {
		    	response.put("error_message", "Incorrect email or password");
		    	return notFound(response);
		    }
	    }, httpExecutionContext.current());
	}
	
	
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> logout() {
	    return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = getUser();
				user.deleteAuthToken();
			    return ok(Json.toJson(""));
			} catch (Exception e) {
				return badRequest();
			}
	    }, httpExecutionContext.current());
    }

}