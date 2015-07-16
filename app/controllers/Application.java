package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.addAdminComment;
import views.html.addCategory;
import views.html.addEvent;
import views.html.addUserComment;
import views.html.TestPage;

import com.models.Comment;
import com.models.Event;
import com.utils.H2DBHelper;

public class Application extends Controller {
	
	private static H2DBHelper dbUtil = H2DBHelper.getInstance();
	
	public static Result testPage() {
		return ok(TestPage.render());
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
	public static Result addCategory(String categoryName,String categoryDescription,String rules) {
		System.out.println(" categoryName are : "+categoryName);
		System.out.println(" categoryDescription are : "+categoryDescription);
		System.out.println(" rules are : "+rules);
		try {
			dbUtil.addCategory(categoryName,categoryDescription,rules);
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
	
	
	public static Result addDashBoardNotification(String categoryName,String notification) {
		System.out.println(" categoryName are : "+categoryName);
		System.out.println(" categoryName are : "+notification);
		try {
			dbUtil.saveDashBoardNotification(categoryName,notification);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(addCategory.render());
	}



	public static Result getEvent(String category) {
		try {
			ArrayList<Event> categories = dbUtil.getEvents(category);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(addCategory.render());
	}

		

	public static Result getDashBoard(String category) {
		try {
			ArrayList<String> dashBoardNotes = dbUtil.getDashoardNotes(category);
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
