<%@page session="false" import="mims.beans.*,java.sql.*,java.util.*"%>
<%
    String invid = request.getParameter("invid");
 InventionBean invention = new InventionBean(invid);
 if(invention!=null){
 %>
  <head>
    <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    <script language="JavaScript1.2" src="/MIMS/Scripts/validate.js"></script>
  </head>
  <body class="BD">
  <center>
   <table align = "center" border="1" cellpadding="10pt" cellspacing="0pt">
    <tr>
      <th colspan = "2" align="center" class="labeltext">
         The following are the <%=invention.getTitle()%> details
      </th>
    </tr>

    <tr>
      <td>Invention Id </td><td><%=invention.getId()%></td>
    </tr>

    <tr>
      <td>Title</td><td><%=invention.getTitle()%></td>
    </tr>

    <tr>
      <td>Description</td><td><%=invention.getThesis()%></td>
    </tr>

    <tr>
      <td>Domain</td><td><%=invention.getDomainName()%></td>
    </tr>

    <tr>
      <td>Inventor</td><td><%=invention.getUserName()%></td>
    </tr>
    
	<tr>
	  <td>Status</td><td><%=invention.getStatus()%></td>
	</tr>

    </table>
   </form>
   </center>
  </body>
  <%}%>