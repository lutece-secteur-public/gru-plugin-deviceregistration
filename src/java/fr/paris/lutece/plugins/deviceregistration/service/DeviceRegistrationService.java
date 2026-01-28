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

import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistration;
import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationHome;
import fr.paris.lutece.plugins.deviceregistration.business.history.ChangeType;
import fr.paris.lutece.plugins.deviceregistration.exception.DeviceRegistrationException;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeviceRegistrationService
{

    private static DeviceRegistrationService instance;

    private DeviceRegistrationService( )
    {
    }

    public static DeviceRegistrationService getInstance( )
    {
        if ( instance == null )
        {
            instance = new DeviceRegistrationService( );
        }
        return instance;
    }

    public DeviceRegistration createDeviceRegistration( final String customerId, final String connectionId, String registrationToken, final String tokenIssuer )
            throws DeviceRegistrationException
    {

        final DeviceRegistration deviceRegistration = new DeviceRegistration( customerId, connectionId, registrationToken, tokenIssuer );
        DeviceRegistrationValidator.validate( deviceRegistration );

        final DeviceRegistration createdDeviceRegistration = DeviceRegistrationHome.create( deviceRegistration );

        DeviceRegistrationHistoryService.getInstance( ).createDeviceRegistrationHistory( createdDeviceRegistration, ChangeType.CREATED );

        return createdDeviceRegistration;

    }

    public void deleteDeviceRegistration( final String customerId, final String connectionId, String registrationToken, final String tokenIssuer ) throws DeviceRegistrationException
    {

        if ( Objects.isNull( customerId ) && Objects.isNull( connectionId ) && Objects.isNull( registrationToken ) )
        {
            throw new DeviceRegistrationException( "Missing criteria for deleting entries" );
        }

        if ( Objects.isNull( tokenIssuer ) )
        {
            throw new DeviceRegistrationException( "Missing token issuer concerned by operation" );
        }

        final Map<String, String> criterias = this.prepareCriteria( customerId, connectionId, registrationToken, tokenIssuer );
        final List<DeviceRegistration> deviceRegistrations = DeviceRegistrationHome.findListByCriteria( criterias );

        if ( deviceRegistrations.isEmpty( ) )
        {
            throw new DeviceRegistrationException( Response.Status.NOT_FOUND, "Nothing to delete" );
        }

        deviceRegistrations.stream( ).peek( deviceRegistration -> DeviceRegistrationHome.remove( deviceRegistration.getId( ) ) )
                .forEach( deviceRegistration -> DeviceRegistrationHistoryService.getInstance( ).createDeviceRegistrationHistory( deviceRegistration,
                        ChangeType.DELETED ) );
    }

    public List<String> getRegistrationTokensByCriteria( final String customerId, final String connectionId, final String tokenIssuer )
            throws DeviceRegistrationException
    {
        if ( Objects.isNull( customerId ) && Objects.isNull( connectionId ) )
        {
            throw new DeviceRegistrationException( "Missing criteria for retrieving Registration Token" );
        }

        if ( Objects.isNull( tokenIssuer ) )
        {
            throw new DeviceRegistrationException( "Missing client code is request" );
        }

        final List<String> registrationTokens = DeviceRegistrationHome.findListByCriteria( this.prepareCriteria( customerId, connectionId, null, tokenIssuer ) ).stream( )
                .map( DeviceRegistration::getRegistrationToken ).collect( Collectors.toList( ) );

        if ( registrationTokens.isEmpty( ) )
        {
            throw new DeviceRegistrationException( Response.Status.NOT_FOUND, "No result found" );
        }

        return registrationTokens;
    }

    private Map<String, String> prepareCriteria( final String customerId, final String connectionId, String registrationToken, String tokenIssuer )
    {
        final Map<String, String> criteria = new HashMap<>( );

        if ( connectionId != null )
        {
            criteria.put( "connection_id", connectionId );
        }
        if ( customerId != null )
        {
            criteria.put( "customer_id", customerId );
        }
        if ( registrationToken != null )
        {
            criteria.put( "registration_token", registrationToken );
        }
        if ( tokenIssuer != null )
        {
            criteria.put( "token_issuer", tokenIssuer );
        }

        return criteria;
    }
}
