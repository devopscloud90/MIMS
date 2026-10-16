package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
public class OppBean{
  private String id;
  private String domainId;
  private String offer;
  private String description;
  private String status;
  public OppBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    stmt = DBUtil.createStatement("");
    String query = "select * from opportuinities where id='"+id+"'";
    rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      domainId = rs.getString("domainid");
      offer = rs.getString("opportuinity");
      description = rs.getString("description");
      status = rs.getString("status");
     }
     rs.close();
	 stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from opportuinities";
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
  public static ArrayList<String> getObjectsByDomain(String id){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from opportuinities where domainId='"+id+"'";
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
  public static ArrayList<String> getObjectsByStatus(String status){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from opportuinities where status='"+status+"'";
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
	 rs = stmt.executeQuery("select max(id) from opportuinities");
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
  public String getName(){
     if(offer==null)
        return "";
     else
        return offer.trim();
  }
  public String getDescription(){
     if(description==null)
        return "";
     else
        return description.trim();
  } 
  public String getStatus(){
     if(status==null)
        return "";
     else
        return status.trim();
  }
}