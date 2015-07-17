package com.utils;
import java.sql.*;

public class H2DBTableCreator {

	
	
	
	 static final String JDBC_DRIVER = "org.h2.Driver";  
	   static final String DB_URL = "jdbc:h2:~/test";

	   //  Database credentials
	   static final String USER = "username";
	   static final String PASS = "password";
	   
	   
	   
	   public static void main(String[] args) {
		   Connection conn = null;
		   
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      
		      
		      
		      // create CATEGORY TABLE
		      System.out.println("Creating table in given database...");
		      String sql = "CREATE TABLE CATEGORY " +
		                   "(id NUMBER(50), " +
		                   " categoryName VARCHAR(50), " + 
		                   " categoryDescription VARCHAR(500), " + 
		                   " rules VARCHAR(1500), " +
		                   " CREATED_DATE TIMESTAMP  " +
		                   ")";
		      createTable(conn,sql);
		      
		      
		      // create EVENT TABLE
		      sql = "CREATE TABLE EVENT  " +
	                   "(id NUMBER(50), " +
	                   " categoryName VARCHAR(50), " + 
	                   " eventName VARCHAR(50), " + 
	                   " time VARCHAR(20), " +
	                   " CREATED_DATE TIMESTAMP  " +
	                   ")";
		      createTable(conn,sql);
		      

		      // create DashBoardNotification TABLE
		      sql = "CREATE TABLE DashBoardNotification  " +
	                   "(id NUMBER(50), " +
	                   " categoryName VARCHAR(50), " +
	                   " eventName VARCHAR(50), " + 
	                   " notification VARCHAR(500), " + 
	                   " CREATED_DATE TIMESTAMP  " +
	                   ")";
		      createTable(conn,sql);
		      
		      
		      // create UserComment TABLE
		      sql = "CREATE TABLE UserComment  " +
	                   "(id NUMBER(50), " +
	                   " categoryName VARCHAR(50), " + 
	                   " notification VARCHAR(500), " + 
	                   " userId VARCHAR(50), " +
	                   " CREATED_DATE TIMESTAMP  " +
	                   ")";
		      createTable(conn,sql);
		      
		      
		      
		      
		      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         
				
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");}//end main



	private static void createTable(Connection conn, String sql) throws SQLException {
		Statement stmt = null;
		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
	      System.out.println("Created table in given database...");
	      
	      
		
		// TODO Auto-generated method stub
		
	}
	   
}
