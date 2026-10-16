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
    <script LANGUAGE="Javascript" SRC="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
    <form name="f1" id="f1" action="EditInvention.jsp" onsubmit="return validate()">
      <input type="hidden" id="invid" name="invid" value="<%=bean.getId()%>"/>

	  <table align = "center" class="inputform" cellspacing="10pt" cellspacing="10pt">
      
	  <tr>
        <th colspan = "2" align="center" class="labeltext">
          Edit Invention Details
        </th>
      </tr>

      <tr>
        <td>Invention Title</td>
        <td><input type="text" readonly="readonly" value="<%=bean.getTitle()%>"><td>
      </tr>

      <tr>
        <td>Description</td>
        <td><input type="text" readonly="readonly" value="<%=bean.getThesis()%>"><td>
      </tr>
	  
	  <tr>
        <td>Domain</td>
        <td><input type="text" readonly="readonly" value="<%=bean.getDomainName()%>"><td>
      </tr>
	  
	  <tr>
         <td>Thesis</td>
		 <td><textarea name="finaldoc" id="finaldoc" rows="5" cols="25"><%=bean.getFinalDoc()%></textarea></td>
      </tr>
       
       <tr align="center">
         <td><input type="submit" name="ok" id="ok" value="ADD" class="Button"/></td>
         <td><input type="reset" name="clear" id="clear" value="CLEAR" class="Button"/></td>
       </tr>
    <%
	invid = request.getParameter("invid");
	String finaldoc = request.getParameter("finaldoc");
    String buttons = request.getParameter("ok");
	if("ADD".equals(buttons)){
      Statement stmt = DBUtil.createStatement("");
	  String query = "update inventions set finaldoc='"+finaldoc+"' where id='"+invid+"'";
      int n = stmt.executeUpdate(query);
	  if(n==1){%>		 
	  <jsp:forward page="ViewInventions.jsp">
	    <jsp:param name="msg" value="Success"/>
		<jsp:param name="ok" value=""/>
	  </jsp:forward>
      <%}else{%>
      <jsp:forward page="ViewInventions.jsp">
	    <jsp:param name="msg" value="Failed" />
    	<jsp:param name="ok" value=""/>
	  </jsp:forward>
    <%}}
}%>