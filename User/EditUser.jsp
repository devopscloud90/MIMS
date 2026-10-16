<%@page session="false" import="mims.beans.*,java.sql.*,java.util.*"%>
<%
 HttpSession session = request.getSession(false);
 if(session!=null){
   String id = request.getParameter("id");
   String usertype = request.getParameter("usertype");
   UserBean user = new UserBean(id);
   if(user!=null){
   %>
   <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    <script language="JavaScript1.2" src="/MIMS/Scripts/validate.js"></script>
   </head>
   <body class="BD">
   <center>
   <form action="EditUser.jsp" onsubmit="return validate()">
    <input type="hidden" name="usertype" id="usertype" value="<%=usertype%>"/>
	<input type="hidden" name="odoid" id="odoid" value="<%=user.getUserTypeId()%>"/>
    <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
    <tr>
      <th colspan = "2" align="center" class="labeltext">
         Edit <%=user.getName()%> Details
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
     <td>Address</td><td><textarea name="address" id="address" rows="3" cols="20" required/><%=user.getAddress()%> </textarea></td>
    </tr>

    <tr>
     <td>Domain</td>
     <td>
      <select name="doid" id="doid" style="width:35%" required/>
        <option value="">---Select---</option>
        <%
	      ArrayList<String> domains = DomainBean.getAllObjects();
          if(domains!=null){
	       for(String did:domains){
	 	     DomainBean domain = new DomainBean(did);%>			  
			 <option value="<%=did%>" <%=(user.getUserTypeId().equals(did))?"selected":""%>><%=domain.getName()%></option>
	       <%}
		  }
		 %>
      </select>
     </td>
    </tr>

    <tr align = "center">
      <td><input type = "submit" name="ok" id="ok" value = "UPDATE" class="Button"></td>
      <td><input type = "reset" name="cancel" id="cancel" value = "CLEAR" class="Button"></td>
    </tr>

    </table>
   </form>
   <%}
	String userid = request.getParameter("userid");
    String name = request.getParameter("name");
    String phno = request.getParameter("phno");
    String email = request.getParameter("email");
    String address = request.getParameter("address");
    usertype = request.getParameter("usertype");
    String doid = request.getParameter("doid");
    String odoid = request.getParameter("odoid");
    String button = request.getParameter("ok");
    if("UPDATE".equals(button)){
	   Statement stmt = DBUtil.createStatement("");
	   stmt.addBatch("update userdetails set name='"+name+"',phonenumber='"+phno+"',mail_id='"+email+"',address='"+address+"' where id="+userid);
	   stmt.addBatch("update logindetails set usertypeid='"+doid+"' where id="+userid);
	   int n[] = stmt.executeBatch();
	   stmt.close();
       if(n.length!=0 && n.length==2){
		  if(!doid.equals(odoid)){
		    DomainBean bean = new DomainBean(doid);
			if(usertype.equals("Advisor")){
              bean.updateAdvisorsCount("increment");
	        }else if(usertype.equals("Domain Expert")){
              bean.updateExpertsCount("increment");
	        }
			bean = new DomainBean(odoid);
			if(usertype.equals("Advisor")){
              bean.updateAdvisorsCount("decrement");
	        }else if(usertype.equals("Domain Expert")){
              bean.updateExpertsCount("decrement");
	        }
		  }
		 %>
         <jsp:forward page="ViewUsers.jsp">
	       <jsp:param name="msg" value="Success"/>
   	       <jsp:param name="usertype" value="<%=usertype%>"/>
           <jsp:param name="userid" value="<%=userid%>"/>
     	 </jsp:forward>
	   <%}else{%>
         <jsp:forward page="ViewUsers.jsp">
           <jsp:param name="msg" value="Failed"/>
   	       <jsp:param name="usertype" value="<%=usertype%>"/>
	       <jsp:param name="userid" value="<%=userid%>"/>
         </jsp:forward>
	  <%}}%>
  </center></body>
<%}%>