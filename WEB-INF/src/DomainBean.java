package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
public class DomainBean{
  private String id;
  private String name;
  private String description;
  private String advisors_count;
  private String experts_count;
  public DomainBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    String query = "select * from domains";
    query += " where id='"+id+"'";
    stmt = DBUtil.createStatement("");
	rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      name = rs.getString("name");
      description = rs.getString("description");
      advisors_count = rs.getString("advisors_count");
      experts_count = rs.getString("experts_count");
     }
    rs.close();
	stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     String query = "select * from domains order by id";
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
	 rs = stmt.executeQuery("select max(id) from domains");
	 if(rs.next())
		 n = rs.getInt(1);
	 rs.close();
	 stmt.close();
	 }catch(Exception e){e.printStackTrace();}
	 return n+1;
  }
  public String getId(){
	  if(id == null)
		return "";
	  else
	    return id.trim();
  }
  public String getName(){
	  if(name == null)
		return "";
	  else
	    return name.trim();
  }
  public String getDescription(){
	  if(description == null)
		return "";
	  else
	    return description.trim();
  } 
  public String getAdvisorsCount(){
	  if(advisors_count == null)
		return "";
	  else
	    return advisors_count.trim();
  }
  public String getExpertsCount(){
	  if(experts_count == null)
		return "";
	  else
	    return experts_count.trim();
  }
  public void updateAdvisorsCount(String operation){
	Integer count = Integer.parseInt(advisors_count);
	Statement stmt = null;
	if(operation.equals("increment"))
      count = count+1;
	else
	 if(operation.equals("decrement"))
       count = count-1;
	if(count>-1){
	 try{
	  String query = "update domains set advisors_count="+count+" where id="+id;
      stmt = DBUtil.createStatement("");
	  int n = stmt.executeUpdate(query);
	 }catch(Exception e){e.printStackTrace();}
	}
  }  
  public void updateExpertsCount(String operation){
	Integer count = Integer.parseInt(experts_count);
	Statement stmt = null;
	if(operation.equals("increment"))
      count = count+1;
    else
	 if(operation.equals("decrement"))
       count = count-1;
    if(count>-1){
	 try{
	  String query = "update domains set experts_count="+count+" where id="+id;
      stmt = DBUtil.createStatement("");
	  int n = stmt.executeUpdate(query);
	 }catch(Exception e){e.printStackTrace();}
	}
  }
}