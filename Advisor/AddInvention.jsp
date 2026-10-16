<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String reqTo = (String)session.getAttribute("id");
    String reqId = request.getParameter("reqid");
	RequestBean req = null;
	%>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" /> 
	<link href="/MIMS/CSS/DatePicker.css" type="text/css" rel="stylesheet" /> 
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/DatePicker.js"></script>
  </head>
  <body class="BD">
  <center>
    <form name="f1" id="f1" action="AddInvention.jsp" onsubmit="return validate()">

	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      <%
    	if(reqId != null ){
	      req = new RequestBean(reqId);
		  if("Approved".equals(req.getStatus())){	  %>  
              <input type="hidden" name="reqId" id="reqId" value="<%=req.getDomainId()%>">
              <input type="hidden" id="userId" name="userId" value="<%=req.getUserId()%>"/>
			  <input type="hidden" id="reqBy" name="reqBy" value="<%=req.getName()%>"/>
			  <input type="hidden" name="domain" id="domain" value="<%=req.getDomainId()%>">
      <tr>
        <th colspan = "2" align="center" class="labeltext">
          Add Invention Details
        </th>
      </tr>

      <tr>
        <td>Invention Id(System Generated)</td>
	    <td><input type="text" id="invId" name="invId" value="<%=InventionBean.generateId()%>"/></td>
       </tr>

       <tr>
         <td>Invention Title</td>
	     <td><input type="text" id="title" name="title" required/></td>
       </tr>
  
       <tr>
         <td>Invention Description</td>
		 <td><textarea name="thesis" id="thesis" cols="20" rows="5"></textarea></td>
       </tr>
       
       <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="SAVE" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
       </tr>
    <%}else{%>
        <jsp:forward page="ViewApproved.jsp">
	      <jsp:param name="msg" value="Error"/>
		  <jsp:param name="ok" value=""/>
	    </jsp:forward>
      
	   <%}}
    reqId = request.getParameter("reqId"); 
    String invId = request.getParameter("invId"); 
	String date = UserBean.getTodayDate();
	String domain = request.getParameter("domain");
	String userId = request.getParameter("userId");
	String reqBy = request.getParameter("reqBy");
    String title = request.getParameter("title");
    String thesis = request.getParameter("thesis");
    String buttons = request.getParameter("ok");
	String msg = request.getParameter("msg");
	if("SAVE".equals(buttons)){
      Statement stmt = DBUtil.createStatement("");
	  String query = "insert into inventions values ('"+invId+"',"+domain+",'"+title+"','"+thesis+"','','"+date+"','','Processing','"+userId+"')";
      int n = stmt.executeUpdate(query);
	  if(n==1){
		  n = stmt.executeUpdate("update requests set status='Completed' where id='"+reqId+"'");
		  Cryptography.provisionUser(userId,reqBy,domain,"D:\\Inventors.csv");
	  %>
	  <jsp:forward page="ViewApproved.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="ViewApproved.jsp">
	    <jsp:param name="msg" value="Failed" />
    	<jsp:param name="ok" value=""/>
	  </jsp:forward>
    <%}
    }}%>