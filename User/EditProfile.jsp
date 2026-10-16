<%@page session="false" import="mims.beans.*,java.sql.*,java.util.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
   String id = (String)session.getAttribute("id");
   UserBean user = new UserBean(id);
   %>
    <head>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
      <script language="JavaScript1.2" src="/MIMS/Scripts/validate.js"></script>
    </head>
    <body class="BD">
	<center>
    <form action = "EditProfile.jsp" >
	<input type="hidden" name="usertype" id="usertype" value="<%=user.getUserType()%>"/>
    <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
    <tr>
     <th colspan = "2" align="center" class="labeltext">
      Edit your profile here
     </th>
    </tr>

    <tr>
     <td>User Id (Cannot be edited)</td><td><input type="text" name="userid" id="userid" readonly='readonly' value="<%=user.getId()%>"></td>
    </tr>

    <tr>
     <td>Name</td><td><input type="text" name="name" id="name" value="<%=user.getName()%>" required/></td>
    </tr>

    <tr>
     <td>Phone Number</td><td><input type="text" name="phno" id="phno" maxlength="11" value="<%=user.getMobile()%>" onkeydown="return allowNumeric(event)" required/></td>
    </tr>

    <tr>
     <td>Email</td><td><input type="email" name="email" id="email" value="<%=user.getEmail()%>" required/></td>
    </tr>

    <tr>
     <td>Address</td><td><textarea name="address" id="address" rows="3" cols="20" required/><%=user.getAddress()%></textarea></td>
    </tr>

    <tr>
     <td>Login Id</td><td><input type="text" name="uname" id="uname" value="<%=user.getUserName()%>" required/></td>
    </tr>

    <tr>
     <td>Password</td><td><input type="password" name="pwd" id="pwd" value="<%=(user.getUserType().equals("Inventor"))?user.getDecPassword():user.getPassword()%>" required/></td>
    </tr>

    <tr align = "center">
     <td><input type="submit" value="UPDATE" name="ok" id="ok" class="Button"></td>
     <td><input type="reset" value="CLEAR"  name="cancel" id="cancel" class="Button"></td>
    </tr>

    </table>
    </form>
	</center>
	</body>
	<%
	 String userid = request.getParameter("userid");
     String name = request.getParameter("name");
     String phno = request.getParameter("phno");
     String email = request.getParameter("email");
     String address = request.getParameter("address");
     String uname = request.getParameter("uname");
     String pwd = request.getParameter("pwd");
     String usertype = request.getParameter("usertype");
	 String button = request.getParameter("ok");
	 String msg = request.getParameter("msg");
	 if("Inventor".equals(usertype)){
		 user = new UserBean(userid);
		 pwd = user.getEncPassword(userid,pwd);
	 }
     if("UPDATE".equals(button)){
	  Statement stmt = DBUtil.createStatement("");
	  stmt.addBatch("update userdetails set name='"+name+"',phonenumber='"+phno+"',mail_id='"+email+"',address='"+address+"' where id="+userid);
	  stmt.addBatch("update logindetails set username='"+uname+"',password='"+pwd+"' where id="+userid);
	  int n[] = stmt.executeBatch();
	  stmt.close();
      if(n.length!=0 && n.length==2){%>
	    <jsp:forward page="EditProfile.jsp">
	      <jsp:param name="msg" value="Success"/>
	  	  <jsp:param name="ok" value=""/>
	    </jsp:forward>
	   <%}else{%>
        <jsp:forward page="EditProfile.jsp">
	      <jsp:param name="msg" value="Failed"/>
	  	  <jsp:param name="ok" value=""/>
	    </jsp:forward>
	   <%}
	  }
	   
	  if("Success".equals(msg)){%>
        <script>alert("Profile Updated Successfully");</script>
      <%}else if ("Failed".equals(msg)){%>
        <script>alert("Profile Updation Failed");</script>
      <%}%>
<%}%>