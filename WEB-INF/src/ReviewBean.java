package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
public class ReviewBean{
  private String id;
  private String inventionId;
  private String reviewDate;
  private String reviewerId;
  private String comments;
  public ReviewBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    stmt = DBUtil.createStatement("");
    String query = "select * from reviews where id="+id;
    rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      inventionId = rs.getString("inventionid");
      reviewDate = rs.getString("reviewdate");
      reviewerId = rs.getString("reviewerid");
      comments = rs.getString("comments");
     }
     rs.close();
	 stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from reviews";
	 ArrayList<String> list = null; 
	 try{
	 stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery(query);
	 if(rs.next()){
	   rs.previous();
	   list = new ArrayList<String>();
	   while(rs.next()){
		 list.add(rs.getString("id"));
	   }
     rs.close();
	 stmt.close();
     }}catch(Exception e){e.printStackTrace();}
     if(list!=null)
		 Collections.sort(list);
	 return list;
  }
  public static ArrayList<String> getObjectsByInvention(String id){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from reviews where inventionid='"+id+"'";
	 ArrayList<String> list = null; 
	 try{
	 stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery(query);
	 if(rs.next()){
	   rs.previous();
	   list = new ArrayList<String>();
	   while(rs.next()){
		 list.add(rs.getString("id"));
	   }
     rs.close();
	 stmt.close();
     }}catch(Exception e){e.printStackTrace();}
     if(list!=null)
		 Collections.sort(list);
	 return list;
  }  
  public static ArrayList<String> getObjectsByReviewer(String id){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from reviews where reviewerid='"+id+"'";
	 ArrayList<String> list = null; 
	 try{
	 stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery(query);
	 if(rs.next()){
	   rs.previous();
	   list = new ArrayList<String>();
	   while(rs.next()){
		 list.add(rs.getString("id"));
	   }
     rs.close();
	 stmt.close();
     }}catch(Exception e){e.printStackTrace();}
     if(list!=null)
		 Collections.sort(list);
	 return list;
  }  
  
  public static Integer generateId(){
     Integer n = 0;
	 Statement stmt = null;
	 ResultSet rs = null;
	 try{
     stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery("select max(id) from reviews");
	 if(rs.next())
		n = rs.getInt(1);
     rs.close();
	 stmt.close();
	 }catch(Exception e){e.printStackTrace();}
	 return n+1;
  }
  public String getId(){
    if(id==null)
      return "";
    else
      return id.trim();
  }
  public String getInventionId(){
    if(inventionId==null)
      return "";
   else
      return inventionId.trim();
  }
  public String getReviewDate(){
     if(reviewDate==null)
        return "";
     else
        return reviewDate.trim();
  }
  public String getReviewerId(){
     if(reviewerId==null)
        return "";
     else
        return reviewerId.trim();
  }
  public String getComments(){
     if(comments==null)
        return "";
     else
        return comments.trim();
  }
}