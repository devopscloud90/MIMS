<%@page import="mims.beans.*,java.util.*"%>
<head>
 <style type="text/CSS">
   .headers{
     background:#333333;
	 color:#FFFFFF
   }
 </style>
<LINK href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet">
</head>
<body class="BD">

 <form action="/MIMS/Admin/Details.jsp" method="POST" target="myframe">
  <table width="100%" border="1">
   <tr class="headers">
     <th width="50%">Inventions History</th>
     <th width="50%">User Details</th>
   </tr>
   <tr>
    <td>
	 <table width="100%" border="1">
	  <tr align="left">
       <th>Invention Id</th>
       <th>Domain</th>
      </tr>
	  <tr align="center">
       <td>
		 <select name="invid" id="invid">
           <option value="--">-select-</option>
		   <%
		     ArrayList<String> inventions = InventionBean.getAllObjects();
			 if(inventions!=null){
			  for(String invention:inventions){%>
			   <option value="<%=invention%>"><%=invention%></option>
			 <%}
			 }
		   %>
		 </select>
	   </td>
       <td>
		 <select name="domain" id="domain">
	       <option value="--">---Select---</option>
             <%
	           ArrayList<String> domains = DomainBean.getAllObjects();
               if(domains!=null){
	           for(String id:domains){
	 	         DomainBean domain = new DomainBean(id);%>			  
			     <option value="<%=id%>"><%=domain.getName()%></option>
	          <%}
		     }
		     %>      
		 </select>
	   </td>
      </tr>
	 </table>
	</td>
    <td>
	 <table width="100%" border="1">
	  <tr align="left">
       <th>User Id</th>
       <th>User Type</th>
      </tr>
	  <tr align="center">
       <td>
	     <select name="userId" id="userId">
           <option value="--">-select-</option>
		   <%
		     ArrayList<String> users = UserBean.getAllObjects();
			 if(users!=null){
			  for(String id:users){
			   UserBean user = new UserBean(id);%>
			   <option value="<%=user.getId()%>"><%=user.getId()%></option>
			 <%}
			 }
		   %>
		 </select>
	   </td>
       <td>
	     <select name="utype" id="utype">
           <option value="--">-select-</option>
           <option value="Admin">Administrator</option>
           <option value="Advisor">Advisor</option>
           <option value="Domain Expert">Domain Expert</option>
           <option value="Inventor">Inventor</option>
		 </select>
	   </td>
      </tr>
	 </table>
	</td>
   </tr>
   
   <tr>
     <td colspan='3' align="center">
	  <input type="submit" name="ok" id="ok" class="button" value="GET DETAILS" />&nbsp;&nbsp;&nbsp;
	  &nbsp;&nbsp;&nbsp;<input type="reset" name="cls" id="cls" class="button" value="CLEAR" />
   </td>
   </tr>
  </table>
 </form>
 <iframe name="myframe" id="myframe" frameborder="0" width="100%" height="70%" src="/MIMS/Admin/Details.jsp"></iframe>
</body>