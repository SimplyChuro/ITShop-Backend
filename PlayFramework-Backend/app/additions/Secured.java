package additions;

import java.util.Optional;

import com.typesafe.config.ConfigFactory;

import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

	public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	public final static String AUTH_TOKEN = "custom.admin.token";
	
	@Override
    public Result onUnauthorized(Http.Request ctx) {
        return unauthorized();
    }

	@Override
	public Optional<String> getUsername(Request req) {
		String authTokenHeaderValue = req.header(AUTH_TOKEN_HEADER).get();
		
		if(ConfigFactory.load().getString(AUTH_TOKEN).equals(authTokenHeaderValue)) {
			Session ses = req.session().adding("token", authTokenHeaderValue);
			return ses.getOptional("token");
		} else {
			return null;
		}

	}

}