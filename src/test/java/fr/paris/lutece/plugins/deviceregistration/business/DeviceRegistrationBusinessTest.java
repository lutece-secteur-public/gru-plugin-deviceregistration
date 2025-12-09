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
package fr.paris.lutece.plugins.deviceregistration.business;

import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistration;
import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationHome;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.Optional;

/**
 * This is the business class test for the object DeviceRegistration
 */
public class DeviceRegistrationBusinessTest extends LuteceTestCase
{
    private static final String CUSTOMERID1 = "CustomerId1";
    private static final String CUSTOMERID2 = "CustomerId2";
    private static final String CONNECTIONID1 = "ConnectionId1";
    private static final String CONNECTIONID2 = "ConnectionId2";
    private static final String REGISTRATION_TOKEN1 = "RegistrationToken1";
    private static final String REGISTRATION_TOKEN2 = "RegistrationToken2";
    private static final String TOKEN_ISSUER1 = "TokenIssuer1";
    private static final String TOKEN_ISSUER2 = "TokenIssuer2";

    /**
     * test DeviceRegistration
     */
    public void testBusiness( )
    {
        // Initialize an object
        DeviceRegistration deviceRegistration = new DeviceRegistration( );
        deviceRegistration.setCustomerId( CUSTOMERID1 );
        deviceRegistration.setConnectionId( CONNECTIONID1 );
        deviceRegistration.setRegistrationToken( REGISTRATION_TOKEN1 );
        deviceRegistration.setTokenIssuer( TOKEN_ISSUER1 );

        // Create test
        DeviceRegistrationHome.create( deviceRegistration );
        Optional<DeviceRegistration> optDeviceRegistrationStored = DeviceRegistrationHome.loadByRegistrationToken( deviceRegistration.getRegistrationToken( ) );

        DeviceRegistration deviceRegistrationStored = optDeviceRegistrationStored.orElse( new DeviceRegistration( ) );
        assertEquals( deviceRegistrationStored.getCustomerId( ), deviceRegistration.getCustomerId( ) );
        assertEquals( deviceRegistrationStored.getConnectionId( ), deviceRegistration.getConnectionId( ) );
        assertEquals( deviceRegistrationStored.getRegistrationToken( ), deviceRegistration.getRegistrationToken( ) );

        // Update test
        deviceRegistration.setId( deviceRegistrationStored.getId( ) );
        deviceRegistration.setCustomerId( CUSTOMERID2 );
        deviceRegistration.setConnectionId( CONNECTIONID2 );
        deviceRegistration.setRegistrationToken( REGISTRATION_TOKEN2 );
        deviceRegistration.setTokenIssuer( TOKEN_ISSUER2 );
        DeviceRegistrationHome.update( deviceRegistration );
        optDeviceRegistrationStored = DeviceRegistrationHome.loadByRegistrationToken( deviceRegistration.getRegistrationToken( ) );
        deviceRegistrationStored = optDeviceRegistrationStored.orElse( new DeviceRegistration( ) );

        assertEquals( deviceRegistrationStored.getCustomerId( ), deviceRegistration.getCustomerId( ) );
        assertEquals( deviceRegistrationStored.getConnectionId( ), deviceRegistration.getConnectionId( ) );
        assertEquals( deviceRegistrationStored.getRegistrationToken( ), deviceRegistration.getRegistrationToken( ) );

        // List test
        DeviceRegistrationHome.getDeviceRegistrationsList( );

        // Delete test
        DeviceRegistrationHome.remove( deviceRegistration.getId( ) );
        optDeviceRegistrationStored = DeviceRegistrationHome.findByPrimaryKey( deviceRegistration.getId( ) );
        deviceRegistrationStored = optDeviceRegistrationStored.orElse( null );
        assertNull( deviceRegistrationStored );

    }

}
