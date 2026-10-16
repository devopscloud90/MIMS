package mims.beans;
import java.sql.Statement;
import java.sql.ResultSet;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
public class UserBean{
  private String id;
  private String name;
  private String mobile;
  private String email;
  private String address;
  private String userName;
  private String password;
  private String tpassword;
  private String keys;
  private String userType;
  private String userTypeId;
  private String startDate;
  private String endDate;
  private String status;
  public UserBean(String id){
   Statement stmt = null;
   ResultSet rs = null;
   try{ 
    stmt = DBUtil.createStatement("");
    String query = "select * from userdetails u,logindetails l";
    query += " where u.id=l.id and u.id='"+id+"'";
    rs = stmt.executeQuery(query);
    if(rs.next()){
      this.id = id;
      name = rs.getString("name");
      mobile = rs.getString("phonenumber");
      email = rs.getString("mail_id");
      address = rs.getString("address");
      userName = rs.getString("username");
      password = rs.getString("password");
      tpassword = rs.getString("initpassword");
	  keys = rs.getString("keys");
	  userType = rs.getString("usertype");
      userTypeId = rs.getString("usertypeid");
	  startDate = rs.getString("startdate");
      endDate = rs.getString("enddate");
      status = rs.getString("status");
     }
     rs.close();
	 stmt.close();
    }catch(Exception e){e.printStackTrace();}
  }
  public static ArrayList<String> getAllObjects(){
     Statement stmt = null;
	 ResultSet rs = null;
     String query = "select * from userdetails u,logindetails l where u.id=l.id";
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
     String query = "select * from userdetails u,logindetails l";
     query += " where u.id=l.id and l.status='"+status+"'";
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
  public static ArrayList<String> getObjectsByUserType(String usertype){
     String query = "select * from userdetails u,logindetails l";
     query += " where u.id=l.id and l.usertype='"+usertype+"'";
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
	 rs = stmt.executeQuery("select max(id) from userdetails");
	 if(rs.next())
		n = rs.getInt(1);
     rs.close();
	 stmt.close();
	 }catch(Exception e){e.printStackTrace();}
	 return n+1;
  }
  public static UserBean getObject(String id){
	  UserBean user = new UserBean(id);
      return user;
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
  public String getUserName(){
     if(userName==null)
        return "";
     else
        return userName.trim();
  }
  public String getPassword(){
     if(password==null)
        return "";
     else
        return password.trim();
  }    
  public String getEncPassword(String id,String password){
     if(password==null)
        return "";
     else{
		  UserBean user = new UserBean(id);
		  HashMap<String,String> map = user.getKeys();
          return Cryptography.encrypt(password,new BigInteger(map.get("2")),new BigInteger(map.get("1")));
	 }
  }
  public String getDecPassword(){
     if(password==null)
        return "";
     else{
		  HashMap<String,String> map = this.getKeys();
          return Cryptography.decrypt(this.getPassword(),new BigInteger(map.get("3")),new BigInteger(map.get("1")));
	 }
  }
  public String getInitPassword(){
     if(tpassword==null)
        return "";
     else
        return tpassword.trim();
  }
  public String getUserType(){
     if(userType==null)
        return "";
     else
        return userType.trim();
  }
  public String getUserTypeId(){
     if(userTypeId==null)
        return "";
     else
        return userTypeId.trim();
  }
  public String getStartDate(){
     if(startDate==null)
        return "";
     else
        return startDate.trim();
  }
  public String getEndDate(){
     if(endDate==null)
        return "";
     else
        return endDate.trim();
  }
  public HashMap<String,String> getKeys(){
	  HashMap<String,String> map = null;
	  if(keys!=null){
		  StringTokenizer tokens = new StringTokenizer(keys,"O");
		  if(tokens.countTokens()>0){
		  Integer i = 1;
		  map = new HashMap<String,String>();
		  while(tokens.hasMoreTokens()){
            map.put(i.toString(),tokens.nextToken());
			i++;
		  }		  
		  }
	  }
	  return map;
  }
  public String getStatus(){
     if(status==null)
        return "";
     else
        return status.trim();
  }
  public static String getTodayDate(){
     SimpleDateFormat dtformat = new SimpleDateFormat("dd-MM-yyyy");
	 Date d = new Date();
     return (dtformat.format(d));
  }
  public static int refreshStatus(){
	 ArrayList<String> users = UserBean.getObjectsByUserType("Inventor");
 	 Statement stmt = DBUtil.createStatement("");
	 int j = 0;
     int n[] = null;
	 try{
	 if(users!=null){
	  for(String id : users){
        UserBean user = new UserBean(id);
		if(user.getStartDate().equals(user.getTodayDate())){
			stmt.addBatch("update logindetails set status='Active' where id="+id);
			j++;
		}
		else
		 if(user.getEndDate().equals(user.getTodayDate())){
			stmt.addBatch("update logindetails set status='Inactive' where id="+id);
			j++;
		 }
	  }
	 }
	   n = stmt.executeBatch();
	 }catch(Exception e){e.printStackTrace();}
	 if(n.length!=0 && n.length==j)
       return j;
	 else
	   return 0;
  }
}