<%@page session="false" import="mims.beans.*,java.util.*,java.sql.*"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
	String userid = (String)session.getAttribute("id");
	UserBean user = new UserBean(userid);
    %>
    <head>
      <link href="/MIMS/CSS/styles.css" type="text/css" rel="stylesheet" />
    </head>
    <body class='BD'>
    <center>
    <%	
     ArrayList<String> reviews = ReviewBean.getObjectsByReviewer(user.getId());
	 if(reviews!=null){
 	   %>
       List of Reviews
       <table class='notebook'>
		<tr class="row_title">
		  <th>ID</th>
		  <th>Invention</th>
		  <th>Review Date</th>
		  <th>Reviewer</th>
		  <th>Comments</th>
		</tr>
	    <%
	     int rCount=0;
	     for (String id:reviews){
           ReviewBean review = new ReviewBean(id);
		  %>
	      <tr class= <%=(rCount%2!=0)? "row_even" : "row_odd"%>>
		   <td align='center'><%=review.getId()%></td>
		   <td align='center'>
		     <%InventionBean inv = new InventionBean(review.getInventionId());%>
			 <%=inv.getTitle()%>
		   </td>
		   <td align='center'><%=review.getReviewDate()%></td>
  		   <td align='center'>
		     <%UserBean user1 = new UserBean(review.getReviewerId());%>
			 <%=user1.getName()%>
		   </td>
           <td align='center'>
		     <%if("".equals(review.getComments())){%>
			   <a href="EditReview.jsp?id=<%=id%>">Add</a>
			 <%}else{%>
			  <%=review.getComments()%>
			 <%}%>
		   </td>
		  </tr>
		  <%
		   rCount++;
		  }%>
	  </table>
	<%}else{%>
	  <font color="#cc0000">No Reviews Scheduled</font>
</center>
</body>
<%}}%>