package controllers;

import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Console extends Controller {

	
    public static void index() {    	
    	redirect("/processdefinitions");
    }

}