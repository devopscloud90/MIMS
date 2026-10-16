<%@page session="false" import="mims.beans.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
   String doid = request.getParameter("id");
   DomainBean dept = new DomainBean(doid);
   if(dept!=null){
   %>
   <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    <script type="text/javaScript1.2" src="/MIMS/Scripts/validate.js"></script>
	 <script language="JavaScript1.2">
	   function validate(){
        if(!isEmpty(document.getElementById('doid')))
         if(!isEmpty(document.getElementById('doname')))
		   return true;
	    return false;
       }
      </script>
   </head>
   <body class="BD">
   <form onsubmit="return validate()">
   <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
   <tr>
    <th colspan = "2" align="center" class="labeltext">
      Edit the <%=dept.getName()%> Domain Details
    </th>
   </tr>

   <tr>
    <td>Domain Id (Cannot be Edited)</td>
	<td>
	  <input type="text" name="doid" id="doid" readonly='readonly' value="<%=dept.getId()%>">
	  <div class="error" id="doidError"></div>
	</td>
   </tr>

   <tr>
    <td>Domain Name</td>
	<td>
	 <input type="text" name="doname" id="doname" value="<%=dept.getName()%>">
	 <div class="error" id="donameError">
	</td>
   </tr>

   <tr>
    <td>Domain Description</td>
    <td>
  	 <input type="text" name="desc" id="desc" value="<%=dept.getDescription()%>">
    </td>
   </tr>

   <tr>
    <td>Advisors Count</td>
    <td>
  	 <input type="text" name="advisors" id="advisors" readonly="readonly" value="<%=dept.getAdvisorsCount()%>">
    </td>
   </tr>

   <tr>
    <td>Experts Count</td>
    <td>
  	 <input type="text" name="experts" id="experts" readonly="readonly" value="<%=dept.getExpertsCount()%>">
    </td>
   </tr>

   <tr align = "center">
    <td><input type = "submit" name="ok" id="ok" value="UPDATE" class="Button"></td>
    <td><input type = "reset" name="clear" id="clear" value="UNDO" class="Button"></td>
   </tr>

   </table>
   </form>
  <%} 
   doid = request.getParameter("doid");
   String name = request.getParameter("doname");
   String desc = request.getParameter("desc");
   String button = request.getParameter("ok");
   if("UPDATE".equals(button)){
    PreparedStatement pstmt = DBUtil.prepareStatement("update domains set name=?,description=? where id=?");
    pstmt.setString(1,name);
    pstmt.setString(2,desc);
    pstmt.setString(3,doid);
    int n = pstmt.executeUpdate();
    pstmt.close();
    if(n>0){%>   
	 <jsp:forward page="ViewDomains.jsp">
	   <jsp:param name="msg" value="Success"/>
       <jsp:param name="id" value="<%=doid%>"/>
       <jsp:param name="ok" value=""/>
  	 </jsp:forward>
   <%}else{%>
	 <jsp:forward page="ViewDomains.jsp">
	   <jsp:param name="msg" value="Failed"/>
       <jsp:param name="id" value="<%=doid%>"/>
       <jsp:param name="ok" value=""/>
  	 </jsp:forward>
   <%}
 }
}%>
