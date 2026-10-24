<%@page session="false" language="java" import="mims.beans.UserBean,java.util.*" %>
<%
  String button = request.getParameter("ok");
  if("Login".equals(button) || "Sign in".equals(button)){
  String uid = request.getParameter("UserId");
  String pwd = request.getParameter("Password");
  ArrayList<String> list = UserBean.getObjectsByStatus("Active");
  if(list!=null){
  UserBean user = null;
  Iterator it = list.iterator();
  while(it.hasNext()){
	user = new UserBean((String)it.next());
	Boolean condition = user.getUserName().equals(uid) && user.getPassword().equals(pwd);
	if("Inventor".equals(user.getUserType())){
      condition = user.getUserName().equals(uid);
	  if(condition)
        condition = user.getDecPassword().equals(pwd);
	}
    if(condition)
     {
		HttpSession session = request.getSession(true);
		session.setAttribute("id",user.getId());
		session.setAttribute("usertype",user.getUserType());
		%>
		  <jsp:forward page="/Login/Home.jsp" />
		<%
	 }
	}
   %>
  <jsp:forward page="/Login/Login.jsp?error=Invalid User" />
 <%}else{%>
  <jsp:forward page="/Login/Login.jsp?error=Please Configure the Database First" />
 <%}}else{%>
 <jsp:forward page="/Login/Login.jsp?error=Please Login to the Application First" />
 <%}%>