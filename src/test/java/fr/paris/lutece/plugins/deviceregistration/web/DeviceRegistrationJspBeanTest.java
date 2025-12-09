/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.deviceregistration.web;

import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.test.LuteceTestCase;
import org.junit.Ignore;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import java.io.IOException;
import java.util.List;

/**
 * This is the business class test for the object DeviceRegistration
 */
public class DeviceRegistrationJspBeanTest extends LuteceTestCase
{
    private static final String CUSTOMERID1 = "CustomerId1";
    private static final String CUSTOMERID2 = "CustomerId2";
    private static final String CONNECTIONID1 = "ConnectionId1";
    private static final String CONNECTIONID2 = "ConnectionId2";
    private static final String REGISTRATION_TOKEN1 = "RegistrationToken1";
    private static final String REGISTRATION_TOKEN2 = "RegistrationToken2";
    private static final String TOKEN_ISSUER1 = "TokenIssuer1";
    private static final String TOKEN_ISSUER2 = "TokenIssuer2";

    @Ignore
    public void testJspBeans( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MockServletConfig config = new MockServletConfig( );

        // display admin DeviceRegistration management JSP
        DeviceRegistrationJspBean jspbean = new DeviceRegistrationJspBean( );
        String html = jspbean.getManageDeviceRegistrations( request );
        assertNotNull( html );

        // display admin DeviceRegistration creation JSP
        html = jspbean.getCreateDeviceRegistration( request );
        assertNotNull( html );

        // action create DeviceRegistration
        request = new MockHttpServletRequest( );

        response = new MockHttpServletResponse( );
        AdminUser adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        request.addParameter( "customer_id", CUSTOMERID1 );
        request.addParameter( "connection_id", CONNECTIONID1 );
        request.addParameter( "registration_token", REGISTRATION_TOKEN1 );
        request.addParameter( "token_issuer", TOKEN_ISSUER1 );
        request.addParameter( "action", "createDeviceRegistration" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createDeviceRegistration" ) );
        request.setMethod( "POST" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

        // display modify DeviceRegistration JSP
        request = new MockHttpServletRequest( );
        request.addParameter( "customer_id", CUSTOMERID1 );
        request.addParameter( "connection_id", CONNECTIONID1 );
        request.addParameter( "registration_token", REGISTRATION_TOKEN1 );
        request.addParameter( "token_issuer", TOKEN_ISSUER1 );
        List<Integer> listIds = DeviceRegistrationHome.getIdDeviceRegistrationsList( null, null, null );
        assertFalse( listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        jspbean = new DeviceRegistrationJspBean( );

        assertNotNull( jspbean.getModifyDeviceRegistration( request ) );

        // action modify DeviceRegistration
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );

        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        request.addParameter( "customer_id", CUSTOMERID2 );
        request.addParameter( "connection_id", CONNECTIONID2 );
        request.addParameter( "registration_token", REGISTRATION_TOKEN2 );
        request.addParameter( "token_issuer", TOKEN_ISSUER2 );
        request.setRequestURI( "jsp/admin/plugins/example/ManageDeviceRegistrations.jsp" );
        // important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createDeviceRegistration, qui est l'action par défaut
        request.addParameter( "action", "modifyDeviceRegistration" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyDeviceRegistration" ) );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

        // get remove DeviceRegistration
        request = new MockHttpServletRequest( );
        // request.setRequestURI("jsp/admin/plugins/example/ManageDeviceRegistrations.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        jspbean = new DeviceRegistrationJspBean( );
        request.addParameter( "action", "confirmRemoveDeviceRegistration" );
        assertNotNull( jspbean.getModifyDeviceRegistration( request ) );

        // do remove DeviceRegistration
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.setRequestURI( "jsp/admin/plugins/example/ManageDeviceRegistrationts.jsp" );
        // important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createDeviceRegistration, qui est l'action par défaut
        request.addParameter( "action", "removeDeviceRegistration" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeDeviceRegistration" ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        request.setMethod( "POST" );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

    }
}
