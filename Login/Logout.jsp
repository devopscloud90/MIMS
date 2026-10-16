<%@page session="false"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
   session.setAttribute("id",null);
   session.invalidate();
   %><jsp:forward page="./Login.jsp?success=Logout Successfull" /><%
  }else{
%><jsp:forward page="./Login.jsp?auth=Session Expired" /><%
}%>