<jsp:useBean id="managedeviceregistrationDeviceRegistration" scope="session"
             class="fr.paris.lutece.plugins.deviceregistration.web.DeviceRegistrationJspBean" />
<% String strContent = managedeviceregistrationDeviceRegistration.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
