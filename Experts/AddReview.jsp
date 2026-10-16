<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
	String invid = request.getParameter("id");
	InventionBean bean = new InventionBean(invid);
	%>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" /> 
	<link href="/MIMS/CSS/DatePicker.css" type="text/css" rel="stylesheet" /> 
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/DatePicker.js"></script>
  </head>
  <body class="BD">
  <center>
    <form name="f1" id="f1" action="AddReview.jsp" onsubmit="return validate()">
      <input type="hidden" id="userid" name="userid" value="<%=userid%>"/>
      <input type="hidden" id="invId" name="invId" value="<%=bean.getId()%>"/>
	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      
	  <tr>
        <th colspan = "2" align="center" class="labeltext">
          Schedule Review Details
        </th>
      </tr>

      <tr>
        <td>Review Id(System Generated)</td>
	    <td><input type="text" id="revId" name="revId" readonly="readonly" value="<%=ReviewBean.generateId()%>"/></td>
       </tr>

       <tr>
         <td>Invention Title</td>
	     <td><input type="text" id="title" name="title" readonly="readonly" value="<%=bean.getTitle()%>"/></td>
       </tr>
  
       <tr>
         <td>Review Date</td>
		 <td><input type="text" name="reviewdate" id="reviewdate" readonly="readonly" onclick="displayDatePicker('reviewdate',false,'dmy','-');"></td>
       </tr>
       
       <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="SAVE" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
       </tr>
    <%
    String invId = request.getParameter("invId");
	String revId = request.getParameter("revId");
    userid = request.getParameter("userid");
	String domain = request.getParameter("domain");
    String title = request.getParameter("title");
    String reviewdate = request.getParameter("reviewdate");
    String buttons = request.getParameter("ok");
	String msg = request.getParameter("msg");
	if("SAVE".equals(buttons)){
      Statement stmt = DBUtil.createStatement("");
	  String query = "insert into reviews values ('"+revId+"',"+invId+",'"+reviewdate+"','"+userid+"','')";
      int n = stmt.executeUpdate(query);
	  if(n==1){%>		 
	  <jsp:forward page="AddReview.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="AddReview.jsp">
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