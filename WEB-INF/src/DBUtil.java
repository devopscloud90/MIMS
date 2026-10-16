package mims.beans;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
public class DBUtil{
  private static Connection con;
  public static Statement createStatement(String str){
	 Statement stmt = null;
     try{
       if(con==null || con.isClosed()){
          Class.forName("oracle.jdbc.driver.OracleDriver");
          con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mims","mims");
       }
	   if("READ".equals(str))
	     stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	   else
         stmt = con.createStatement();
     }catch(Exception e){
           e.printStackTrace();
      }
    return stmt;
  }
  public static PreparedStatement prepareStatement(String str){
	 PreparedStatement pstmt = null;
     try{
       if(con==null || con.isClosed()){
          Class.forName("oracle.jdbc.driver.OracleDriver");
          con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mims","mims");
       }
     pstmt = con.prepareStatement(str);	 
     }catch(Exception e){
           e.printStackTrace();
      }
   return pstmt;
  }
}