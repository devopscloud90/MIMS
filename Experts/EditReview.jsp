<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
	String revid = request.getParameter("id");
	ReviewBean bean = new ReviewBean(revid);
	%>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" /> 
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
    <form name="f1" id="f1" action="EditReview.jsp" onsubmit="return validate()">
      <input type="hidden" id="revId" name="revId" readonly="readonly" value="<%=bean.getId()%>"/>

	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      
	  <tr>
        <th colspan = "2" align="center" class="labeltext">
          Add Review Comments
        </th>
      </tr>

       <tr>
         <td>Enter Comments</td>
		 <td><textarea name="comments" id="comments" rows="5" cols="25"></textarea></td>
       </tr>
       
       <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="ADD" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
       </tr>
    <%
	String revId = request.getParameter("revId");
	String comments = request.getParameter("comments");
    String buttons = request.getParameter("ok");
	String msg = request.getParameter("msg");
	if("ADD".equals(buttons)){
      Statement stmt = DBUtil.createStatement("");
	  String query = "update reviews set comments='"+comments+"' where id='"+revId+"'";
      int n = stmt.executeUpdate(query);
	  if(n==1){%>		 
	  <jsp:forward page="ViewReviews.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="ViewReviews.jsp">
	    <jsp:param name="msg" value="Failed" />
    	<jsp:param name="ok" value=""/>
	  </jsp:forward>
    <%}} 
     if("Success".equals(msg)){%>
      <script>alert("Review Scheduled Successfully");</script>
     <%}else if ("Failed".equals(msg)){%>
      <script>alert("Review Schedule Failed");</script>
     <%}
}%>