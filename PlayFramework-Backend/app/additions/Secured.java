package additions;

import controllers.LoginController;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {
	
    @Override
    public String getUsername(Context ctx) {
        String authTokenHeaderValues = ctx.request().header(LoginController.AUTH_TOKEN_HEADER).get();
        if (authTokenHeaderValues != null) {
            User user = User.findByAuthToken(authTokenHeaderValues);
            if (user != null) {
                ctx.args.put("user", user);
                return user.email;
            }     
        }
       
        return null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized();
    }

}