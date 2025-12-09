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
package fr.paris.lutece.plugins.deviceregistration.utils;

import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistration;
import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationHome;
import fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistory;
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationRequest;
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationResponse;

import java.util.List;

public class DeviceRegistrationUtils
{

    private DeviceRegistrationUtils( )
    {
    }

    public static DeviceRegistrationResponse toResponse( final DeviceRegistration deviceRegistration )
    {
        return new DeviceRegistrationResponse( deviceRegistration.getCustomerId( ), deviceRegistration.getConnectionId( ),
                List.of( deviceRegistration.getRegistrationToken( ) ) );
    }

    public static DeviceRegistration fromRequest( final DeviceRegistrationRequest request )
    {
        DeviceRegistration deviceRegistration = new DeviceRegistration( );
        deviceRegistration.setCustomerId( request.getCustomerId( ) );
        deviceRegistration.setConnectionId( request.getConnectionId( ) );
        deviceRegistration.setRegistrationToken( request.getRegistrationToken( ) );
        deviceRegistration.setTokenIssuer( request.getTokenIssuer( ) );
        return deviceRegistration;
    }

    public static DeviceRegistrationHistory toHistory( final DeviceRegistration deviceRegistration )
    {
        final DeviceRegistrationHistory deviceRegistrationHistory = new DeviceRegistrationHistory( );
        deviceRegistrationHistory.setCustomerId( deviceRegistration.getCustomerId( ) );
        deviceRegistrationHistory.setConnectionId( deviceRegistration.getConnectionId( ) );
        deviceRegistrationHistory.setRegistrationToken( deviceRegistration.getRegistrationToken( ) );
        deviceRegistrationHistory.setTokenIssuer( deviceRegistration.getTokenIssuer( ) );
        return deviceRegistrationHistory;
    }
}
