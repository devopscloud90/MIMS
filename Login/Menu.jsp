<%@page session="false" import="mims.beans.UserBean"%>
<%
  HttpSession session = request.getSession(false);
  if(session!=null){
    String id = (String)session.getAttribute("id");
    if(id!=null){
      UserBean user = new UserBean(id);
      String userType = user.getUserType();
      %>
      <nav aria-label="Main navigation">
        <h2 class="side-nav-title">Workspace</h2>
        <ul class="side-nav-list">
          <li><a href="/MIMS/Login/Welcome.jsp" target="result">Home</a></li>
          <%if("Admin".equals(userType)){%>
            <li><a href="/MIMS/Admin/AddDomain.jsp" target="result">Add Domain</a></li>
            <li><a href="/MIMS/Admin/ViewDomains.jsp" target="result">View Domains</a></li>
            <li><a href="/MIMS/User/AddUser.jsp?usertype=Advisor" target="result">Add Advisor</a></li>
            <li><a href="/MIMS/User/ViewUsers.jsp?usertype=Advisor" target="result">View Advisors</a></li>
            <li><a href="/MIMS/User/AddUser.jsp?usertype=Domain%20Expert" target="result">Add Domain Expert</a></li>
            <li><a href="/MIMS/User/ViewUsers.jsp?usertype=Domain%20Expert" target="result">View Domain Experts</a></li>
            <li><a href="/MIMS/Admin/ViewRequests.jsp" target="result">Invention Requests</a></li>
            <li><a href="/MIMS/Admin/ResetPWD.jsp" target="result">Reset Password</a></li>
            <li><a href="/MIMS/Admin/Report.jsp" target="result">Reports</a></li>
          <%}else if("Advisor".equals(userType)){%>
            <li><a href="/MIMS/Advisor/ViewSchedules.jsp" target="result">My Reviews</a></li>
            <li><a href="/MIMS/Advisor/ViewApproved.jsp" target="result">Add Invention</a></li>
            <li><a href="/MIMS/Advisor/ViewInventions.jsp" target="result">View Inventions</a></li>
          <%}else if("Inventor".equals(userType)){%>
            <li><a href="/MIMS/Inventor/ViewInventions.jsp" target="result">View Inventions</a></li>
            <li><a href="/MIMS/Inventor/ViewJobs.jsp" target="result">View Opportunities</a></li>
          <%}else if("Domain Expert".equals(userType)){%>
            <li><a href="/MIMS/Experts/ViewInventions.jsp" target="result">View Inventions</a></li>
            <li><a href="/MIMS/Experts/ScheduleReviews.jsp" target="result">Schedule Reviews</a></li>
            <li><a href="/MIMS/Experts/ViewReviews.jsp" target="result">View Reviews</a></li>
            <li><a href="/MIMS/Experts/PostJobs.jsp" target="result">Post Jobs</a></li>
            <li><a href="/MIMS/Experts/ViewJobs.jsp" target="result">View Jobs</a></li>
          <%}%>
          <li><a href="/MIMS/User/EditProfile.jsp" target="result">Edit Profile</a></li>
        </ul>
      </nav>
      <%
    }
  }
%>
