package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
public class RequestBean{
  private String id;
  private String name;
  private String userId;
  private String mobile;
  private String email;
  private String address;
  private String domainId;
  private String reviewDate;
  private String reviewerId;
  private String comments;
  private String status;
  public RequestBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    stmt = DBUtil.createStatement("");
    String query = "select * from requests where id="+id;
    rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      name = rs.getString("name");
      mobile = rs.getString("phonenumber");
      email = rs.getString("mail_id");
      address = rs.getString("address");
      domainId = rs.getString("domainid");
	  reviewDate = rs.getString("reviewdate");
      reviewerId = rs.getString("reviewerid");
      comments = rs.getString("comments");
      status = rs.getString("status");
	  userId = rs.getString("userId");
     }
     rs.close();
	 stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from requests";
	 ArrayList<String> list = null; 
	 try{
	 stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery(query);
	 if(rs.next()){
	   rs.previous();
	   list = new ArrayList<String>();
	   while(rs.next()){
		 list.add(rs.getString(1));
	   }
     rs.close();
	 stmt.close();
     }}catch(Exception e){e.printStackTrace();}
     if(list!=null)
		 Collections.sort(list);
	 return list;
  }
  public static ArrayList<String> getObjectsByStatus(String status){
     String query = "select * from requests where status='"+status+"'";
	 ArrayList<String> list = null;
     Statement stmt = null;
 	 ResultSet rs = null;
	 try{
	 stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery(query);
	 if(rs.next()){
	   rs.previous();
	   list = new ArrayList<String>(); 
	   while(rs.next()){
		 list.add(rs.getString(1));
	   }
	 rs.close();
     stmt.close();
     }}catch(Exception e){e.printStackTrace();}
     if(list!=null)
		 Collections.sort(list);
	 return list;
  }
  public static ArrayList<String> getObjectsByReviewer(String userid){
     String query = "select * from requests where reviewerid='"+userid+"'";
	 ArrayList<String> list = null;
	 Statement stmt = null;
 	 ResultSet rs = null;
	 try{
	 stmt = DBUtil.createStatement("READ");
	 rs = stmt.executeQuery(query);
	 if(rs.next()){
	   rs.previous();
	   list = new ArrayList<String>(); 
	   while(rs.next()){
		 list.add(rs.getString(1));
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
	 rs = stmt.executeQuery("select max(id) from requests");
	 if(rs.next())
		n = rs.getInt(1);
     rs.close();
	 stmt.close();
	 }catch(Exception e){e.printStackTrace();}
	 return n+1;
  }
  public static RequestBean getObject(String id){
	  RequestBean bean = new RequestBean(id);
      return bean;
  }
  public String getId(){
    if(id==null)
      return "";
    else
      return id.trim();
  }
  public String getName(){
    if(name==null)
      return "";
   else
      return name.trim();
  }  
  public String getUserId(){
    if(userId==null)
      return "";
   else
      return userId.trim();
  }
  public String getMobile(){
     if(mobile==null)
        return "";
     else
        return mobile.trim();
  }
  public String getEmail(){
     if(email==null)
        return "";
     else
        return email.trim();
  }
  public String getAddress(){
     if(address==null)
        return "";
     else
        return address.trim();
  }
  public String getDomainId(){
     if(domainId==null)
        return "";
     else
        return domainId.trim();
  }
  public String getDomainName(){
     if(domainId==null)
        return "";
     else{
		DomainBean domain = new DomainBean(domainId);
        return domain.getName();
	 }
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
  public String getReviewerName(){
     if(reviewerId==null)
        return "";
     else{
		 UserBean user = new UserBean(reviewerId);
        return user.getName();
	 }
  }
  public String getComments(){
     if(comments==null)
        return "";
     else
        return comments.trim();
  }  
  public String getStatus(){
     if(status==null)
        return "";
     else
        return status.trim();
  }
}