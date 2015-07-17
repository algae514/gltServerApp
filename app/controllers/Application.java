package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import play.libs.Json;
import play.libs.Jsonp;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.TestPage;
import views.html.addAdminComment;
import views.html.addCategory;
import views.html.addEvent;
import views.html.addUserComment;

import com.fasterxml.jackson.databind.JsonNode;
import com.models.Comment;
import com.models.Event;
import com.utils.H2DBHelper;
import com.utils.H2DBTableCreator;

public class Application extends Controller {
	
	private static H2DBHelper dbUtil = H2DBHelper.getInstance();
	
	public static Result testPage() {
		return ok(TestPage.render());
	}

	
	

	public static Result createDB() {
		H2DBTableCreator.main(null);
		return ok(addCategory.render());
	}
	
	
	public static Result addCategoryC() {
		return ok(addCategory.render());
	}

	public static Result showAddEvent() {
		return ok(addEvent.render());
	}

	public static Result addDashBoardComment() {
		return ok(addAdminComment.render());
	}

	public static Result addUserCommentEventC() {
		return ok(addUserComment.render());
	}

	
	
	
	
// add category
	public static Result addCategory(String categoryName) {
		System.out.println(" categoryName are : "+categoryName);
		/*System.out.println(" categoryDescription are : "+categoryDescription);
		System.out.println(" rules are : "+rules);*/
		try {
			dbUtil.addCategory(categoryName,"","");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok();
	}

	
	// add event 
	public static Result addEvent(String eventName,String time,String categoryName) {
		System.out.println(" categoryName are : "+categoryName);
		System.out.println(" categoryName are : "+time);
		System.out.println(" categoryName are : "+eventName);
		try {
			dbUtil.saveEvent(eventName,time,categoryName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok();
	}
	
	
	public static Result addDashBoardNotification(String categoryName,String eventName , String notification) {
		System.out.println(" categoryName are : "+categoryName);
		System.out.println(" categoryName are : "+notification);
		try {
			dbUtil.saveDashBoardNotification(categoryName,notification,eventName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok();
	}

	public static Result addUserCommentEvent(String categoryName,String notification,String userId) {
		System.out.println(" categoryName are : "+categoryName);
		System.out.println(" categoryName are : "+notification);
		System.out.println(" categoryName are : "+userId);
		try {
			dbUtil.saveUserComment(categoryName,notification,userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(addUserComment.render());
	}


	
	

	public static Result getCategory() {
		try {
			ArrayList<String> categories = dbUtil.getCategorites();
			
			JsonNode json = Json.toJson(categories);
			response().setHeader("Access-Control-Allow-Origin", "*");
			System.out.println(" sending categories "+json);
			return ok(json);
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(addCategory.render());
	}



	public static Result getEvent(String category) {
		try {
			ArrayList<Event> events = dbUtil.getEvents(category);
			
			JsonNode json = Json.toJson(events);
			response().setHeader("Access-Control-Allow-Origin", "*");
			System.out.println(" sending json "+json);
			return ok(json);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		
		return ok();
	}

		

	public static Result getDashBoard(String category,String eventName) {
		try {
			ArrayList<Comment> dashBoardNotes = dbUtil.getDashoardNotes(category,eventName);
			JsonNode json = Json.toJson(dashBoardNotes);
			response().setHeader("Access-Control-Allow-Origin", "*");
			System.out.println(" sending DASHBOAD NOTES : \n\n\n "+json);
			return ok(json);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(addCategory.render());
	}

	

	public static Result getUserNotes(String category) {
		try {
			ArrayList<Comment> userNotes = dbUtil.getUserNotes(category);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(addCategory.render());
	}

	

}
