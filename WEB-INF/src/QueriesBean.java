package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
public class QueriesBean{
  private String id;
  private String inventionId;
  private String query;
  private String replyId;
  private String reply;
  public QueriesBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    stmt = DBUtil.createStatement("");
    String query = "select * from queries where id='"+id+"'";
    rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      inventionId = rs.getString("inventionid");
      query = rs.getString("query");
      replyId = rs.getString("replyId");
      reply = rs.getString("reply");
     }
     rs.close();
	 stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from queries";
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
     String query = "select * from queries where inventionid='"+id+"'";
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
  public static ArrayList<String> getObjectsByReplier(String id){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from queries where replyId='"+id+"'";
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
	 rs = stmt.executeQuery("select max(id) from queries");
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
  public String getQuery(){
     if(query==null)
        return "";
     else
        return query.trim();
  }
  public String getReplierId(){
     if(replyId==null)
        return "";
     else
        return replyId.trim();
  }  
  public String getReplierName(){
     if(replyId==null)
        return "";
     else{
		UserBean user = new UserBean(replyId);
        return user.getName();
	 }
  }
  public String getReply(){
     if(reply==null)
        return "";
     else
        return reply.trim();
  }
}