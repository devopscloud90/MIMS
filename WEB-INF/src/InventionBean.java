package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
public class InventionBean{
  private String id;
  private String title;
  private String domainid;
  private String userid;
  private String thesis;
  private String finaldoc;
  private String registereddate;
  private String patentdate;
  private String status;
  public InventionBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    stmt = DBUtil.createStatement("");
    String query = "select * from inventions where id="+id;
    rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      domainid = rs.getString("domainid");
      userid = rs.getString("userid");
      title = rs.getString("title");
      thesis = rs.getString("thesis");
      finaldoc = rs.getString("finaldoc");
      registereddate = rs.getString("registereddate");
      patentdate = rs.getString("patentdate");
      status = rs.getString("status");
     }
     rs.close();
	 stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from inventions";
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
  public static ArrayList<String> getObjectsByInventor(String id){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from inventions where userid='"+id+"'";
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
     String query = "select * from inventions where domainid='"+id+"'";
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
	 rs = stmt.executeQuery("select max(id) from inventions");
	 if(rs.next())
		n = rs.getInt(1);
     rs.close();
	 stmt.close();
	 }catch(Exception e){e.printStackTrace();}
	 return n+1;
  }
  public static InventionBean getObject(String id){
	  InventionBean bean = new InventionBean(id);
      return bean;
  }
  public String getId(){
    if(id==null)
      return "";
    else
      return id.trim();
  }
  public String getTitle(){
    if(title==null)
      return "";
   else
      return title.trim();
  }
  public String getThesis(){
     if(thesis==null)
        return "";
     else
        return thesis.trim();
  }
  public String getFinalDoc(){
     if(finaldoc==null)
        return "";
     else
        return finaldoc.trim();
  }
  public String getRegisteredDate(){
     if(registereddate==null)
        return "";
     else
        return registereddate.trim();
  }
  public String getPatentDate(){
     if(patentdate==null)
        return "";
     else
        return patentdate.trim();
  }
  public String getDomainId(){
     if(domainid==null)
        return "";
     else
		return domainid.trim();
  }
  public String getDomainName(){
     if(domainid==null)
        return "";
     else{
		DomainBean domain = new DomainBean(domainid);
        return domain.getName();
	 }
  }  
  public String getUserId(){
     if(userid==null)
        return "";
     else
		return userid.trim();
  }
  public String getUserName(){
     if(userid==null)
        return "";
     else{
		UserBean user = new UserBean(userid);
        return user.getName();
	 }
  }
  public String getStatus(){
     if(status==null)
        return "";
     else
        return status.trim();
  }
}