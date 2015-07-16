package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.models.Comment;
import com.models.Event;

public class H2DBHelper {

	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/test";

	// Database credentials
	static final String USER = "username";
	static final String PASS = "password";

	static final String insertCategory = "INSERT INTO CATEGORY"
			+ "(categoryName, categoryDescription, rules, CREATED_DATE) VALUES"
			+ "(?,?,?,?)";

	static final String insertEvent = "INSERT INTO EVENT"
			+ "(eventName, time, categoryName, CREATED_DATE) VALUES"
			+ "(?,?,?,?)";

	static final String insertDashBoardNotification = "INSERT INTO DashBoardNotification"
			+ "(categoryName, notification,  CREATED_DATE) VALUES" + "(?,?,?)";

	static final String insertUserComment = "INSERT INTO UserComment"
			+ "(categoryName, notification, userId,  CREATED_DATE) VALUES"
			+ "(?,?,?,?)";

	static final String selectCategory = "SELECT * FROM CATEGORY  ORDER BY CREATED_DATE DESC";
	static final String selectEvent = "SELECT * FROM EVENT where categoryName = ? ORDER BY CREATED_DATE DESC";
	static final String selectDashBoardNotification = "SELECT * FROM DashBoardNotification  where categoryName = ?  ORDER BY CREATED_DATE ASC";
	static final String selectUserComment = "SELECT * FROM EVENT where categoryName = ?  ORDER BY CREATED_DATE DESC";

	PreparedStatement insertCategoryPS = null;
	PreparedStatement insertEventPS = null;
	PreparedStatement insertDashBoardNotePS = null;
	PreparedStatement insertuserNotePS = null;

	PreparedStatement selectCategoryPS = null;
	PreparedStatement selectEventPS = null;
	PreparedStatement selectDashBoardNotePS = null;
	PreparedStatement selectUserNotePS = null;

	static final String getCategory = "password";
	static final String getEvent = "password";
	static final String getDashBoardNotification = "password";
	static final String getUserComment = "password";

	Connection conn = null;
	PreparedStatement stmt = null;

	private H2DBHelper() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			System.out.println(" being initiated...");
			// create insert PS
			insertCategoryPS = conn.prepareStatement(insertCategory);
			insertEventPS = conn.prepareStatement(insertEvent);
			insertDashBoardNotePS = conn
					.prepareStatement(insertDashBoardNotification);
			insertuserNotePS = conn.prepareStatement(insertUserComment);

			// select querires
			selectCategoryPS = conn.prepareStatement(selectCategory);
			selectEventPS = conn.prepareStatement(selectEvent);
			selectDashBoardNotePS = conn
					.prepareStatement(selectDashBoardNotification);
			selectUserNotePS = conn.prepareStatement(selectUserComment);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static H2DBHelper getInstance() {
		return new H2DBHelper();
	}

	public void addCategory(String categoryName, String categoryDescription,
			String rules) throws SQLException {
		// TODO Auto-generated method stub

		insertCategoryPS.setString(1, categoryName);
		insertCategoryPS.setString(2, categoryDescription);
		insertCategoryPS.setString(3, rules);
		insertCategoryPS.setTimestamp(4, getCurrentTimeStamp());
		insertCategoryPS.executeUpdate();

	}

	public void saveEvent(String eventName, String time, String categoryName)
			throws SQLException {
		// TODO Auto-generated method stubthod stub

		insertEventPS.setString(1, eventName);
		insertEventPS.setString(2, time);
		insertEventPS.setString(3, categoryName);
		insertEventPS.setTimestamp(4, getCurrentTimeStamp());
		insertEventPS.executeUpdate();
		System.out.println(" save Event !!!");

	}

	public void saveDashBoardNotification(String categoryName,
			String notification) throws SQLException {
		// TODO Auto-generated method stub
		// + "(categoryName, notification,  CREATED_DATE) VALUES"

		insertDashBoardNotePS.setString(1, categoryName);
		insertDashBoardNotePS.setString(2, notification);
		insertDashBoardNotePS.setTimestamp(3, getCurrentTimeStamp());
		insertDashBoardNotePS.executeUpdate();
		System.out.println(" save dashboard !!!");

	}

	public void saveUserComment(String categoryName, String notification,
			String userId) throws SQLException {
		// TODO Auto-generated method stub
		// + "(categoryName, notification, userId,  CREATED_DATE) VALUES"

		insertuserNotePS.setString(1, categoryName);
		insertuserNotePS.setString(2, notification);
		insertuserNotePS.setString(3, userId);
		insertuserNotePS.setTimestamp(4, getCurrentTimeStamp());
		insertuserNotePS.executeUpdate();

		System.out.println(" save user note  !!!");
	}

	private java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

	public ArrayList<String> getCategorites() throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<String> cats = new ArrayList();
		ResultSet rs = selectCategoryPS.executeQuery();

		while (rs.next()) {

			String CATEGORY = rs.getString("CATEGORY");
			cats.add(CATEGORY);
			System.out.println("CATEGORY : " + CATEGORY);

		}
		return cats;
	}

	
	
	public ArrayList<Event> getEvents(String category) throws SQLException {
		ArrayList<Event> eventsa = new ArrayList();
		
		selectEventPS.setString(1, category);
		
		ResultSet rs = selectEventPS.executeQuery();

		while (rs.next()) {

			String eventName = rs.getString("eventName");
			String time = rs.getString("time");
			
			Event event = new Event();
			event.eventName = eventName;
			event.eventSchedule = time;
			
			eventsa.add(event);
			System.out.println("event : " + eventName);

		}
		return eventsa;
		
	}

	
	
	
	public ArrayList<String> getDashoardNotes(String category)
			throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<String> cats = new ArrayList();
		selectDashBoardNotePS.setString(1, category);
		ResultSet rs = selectDashBoardNotePS.executeQuery();

		while (rs.next()) {

			String notification = rs.getString("notification");
			cats.add(notification);
			System.out.println("dash notification : " + notification);

		}
		return cats;

	}

	public ArrayList<Comment> getUserNotes(String category) throws SQLException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		selectUserNotePS.setString(1, category);
		
		ResultSet rs = selectUserNotePS.executeQuery();

		while (rs.next()) {
			Comment comment = new Comment();

			String notification = rs.getString("notification");
			comment.commentText = notification;
			String userId = rs.getString("userId");
			comment.commentBy = userId;
			Timestamp timestamp = rs.getTimestamp("CREATED_DATE");
			comment.timeOfComment = timestamp.toGMTString();

			comments.add(comment);
			System.out.println("user notification : " + notification);

		}
		return comments;
	}

}
