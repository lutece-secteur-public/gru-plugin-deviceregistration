<jsp:useBean id="managedeviceregistrationDeviceRegistrationHistory" scope="session"
             class="fr.paris.lutece.plugins.deviceregistration.web.DeviceRegistrationHistoryJspBean" />
<% String strContent = managedeviceregistrationDeviceRegistrationHistory.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
