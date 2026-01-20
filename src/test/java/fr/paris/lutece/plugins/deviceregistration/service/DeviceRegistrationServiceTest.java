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
package fr.paris.lutece.plugins.deviceregistration.service;

import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationRequest;
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationResponse;
import fr.paris.lutece.plugins.deviceregistration.exception.DeviceRegistrationException;
import fr.paris.lutece.test.LuteceTestCase;

import javax.ws.rs.core.Response;
import java.util.List;

public class DeviceRegistrationServiceTest extends LuteceTestCase
{
    private static final String CUSTOMER_ID_TOO_LONG = "a".repeat( 51 );
    private static final String CONNECTION_ID_TOO_LONG = "b".repeat( 51 );
    private static final String REGISTRATION_TOKEN_TOO_LONG = "c".repeat( 256 );

    private static final String CUSTOMER_ID = "validCustomerId";
    private static final String CONNECTION_ID = "validConnectionId";
    private static final String REGISTRATION_TOKEN = "validRegistrationToken";

    private static final String INVALID_TOKEN_ISSUER = "invalidTokenIssuer";

    private static final String TOKEN_ISSUER = "TestTokenIssuer";

    public void testCreateDeviceRegistrationWithoutIdentity( )
    {
        DeviceRegistrationRequest request = new DeviceRegistrationRequest( null, null, "token" );
        try
        {
            DeviceRegistrationService.getInstance( ).createDeviceRegistration( request, null );
            fail( );
        }
        catch( DeviceRegistrationException e )
        {
            assertNotNull( e.getResponse( ) );
        }
    }

    public void testCreateWithTooLongValues( )
    {

        try
        {
            DeviceRegistrationService.getInstance( ).createDeviceRegistration( initInvalidRequest( ), INVALID_TOKEN_ISSUER );
            fail( );
        }
        catch( DeviceRegistrationException e )
        {
            assertNotNull( e.getResponse( ) );
            assertEquals( Response.Status.BAD_REQUEST, e.getResponse( ).getStatus( ) );
            assertEquals( 3, e.getResponse( ).getMessages( ).size( ) );
        }
    }

    public void testGetDeviceRegistrationWithoutCriteria( )
    {
        try
        {
            DeviceRegistrationService.getInstance( ).getRegistrationTokensByCriteria( null, null, TOKEN_ISSUER );
            fail( );
        }
        catch( DeviceRegistrationException e )
        {
            assertNotNull( e.getResponse( ) );
            assertEquals( Response.Status.BAD_REQUEST, e.getResponse( ).getStatus( ) );
        }
    }

    public void testDeleteWithoutParameters( )
    {
        try
        {
            DeviceRegistrationService.getInstance( ).deleteDeviceRegistration( new DeviceRegistrationRequest( ), null );
            fail( );
        }
        catch( DeviceRegistrationException e )
        {
            assertNotNull( e.getResponse( ) );
            assertEquals( Response.Status.BAD_REQUEST, e.getResponse( ).getStatus( ) );
        }
    }

    public void testDeleteWithUnknownsParameters( )
    {
        try
        {
            DeviceRegistrationService.getInstance( )
                    .deleteDeviceRegistration( new DeviceRegistrationRequest( CUSTOMER_ID, CONNECTION_ID, REGISTRATION_TOKEN ), INVALID_TOKEN_ISSUER );
            fail( );
        }
        catch( DeviceRegistrationException e )
        {
            assertNotNull( e.getResponse( ) );
            assertEquals( Response.Status.NOT_FOUND, e.getResponse( ).getStatus( ) );
        }
    }

    public void testCompleteProcess( )
    {
        // Creation with valid parameters
        try
        {
            DeviceRegistrationResponse response = DeviceRegistrationService.getInstance( ).createDeviceRegistration( initValidRequest( ), TOKEN_ISSUER);
            assertNotNull( response );
            assertEquals( CUSTOMER_ID, response.getCustomerId( ) );
            assertEquals( CONNECTION_ID, response.getConnectionId( ) );
            assertTrue( response.getRegistrationTokens( ).contains( REGISTRATION_TOKEN ) );
        }
        catch( DeviceRegistrationException e )
        {
            fail( "CreateWithValidValues" );
        }
        // Creation with existing token (should fail)
        try
        {
            DeviceRegistrationService.getInstance( ).createDeviceRegistration( initValidRequest( ), TOKEN_ISSUER );
            fail( "CreateWithExistingToken" );
        }
        catch( DeviceRegistrationException e )
        {
            assertNotNull( e.getResponse( ) );
            assertEquals( Response.Status.CONFLICT, e.getResponse( ).getStatus( ) );
        }
        // Get token with criteria
        try
        {
            List<String> registrationTokens = DeviceRegistrationService.getInstance( ).getRegistrationTokensByCriteria( CUSTOMER_ID, CONNECTION_ID,
                    TOKEN_ISSUER );
            assertNotNull( registrationTokens );
            assertTrue( registrationTokens.contains( REGISTRATION_TOKEN ) );
        }
        catch( DeviceRegistrationException e )
        {
            fail( "GetDeviceRegistrationWithCriteria" );
        }
        // Delete Token with parameters
        try
        {
            DeviceRegistrationService.getInstance( )
                    .deleteDeviceRegistration( new DeviceRegistrationRequest( CUSTOMER_ID, CONNECTION_ID, REGISTRATION_TOKEN ), TOKEN_ISSUER );
        }
        catch( DeviceRegistrationException e )
        {
            fail( "DeleteWithParameters" );
        }
    }

    private DeviceRegistrationRequest initValidRequest( )
    {
        return new DeviceRegistrationRequest( CUSTOMER_ID, CONNECTION_ID, REGISTRATION_TOKEN );
    }

    private DeviceRegistrationRequest initInvalidRequest( )
    {
        return new DeviceRegistrationRequest( CUSTOMER_ID_TOO_LONG, CONNECTION_ID_TOO_LONG, REGISTRATION_TOKEN_TOO_LONG );
    }

}
