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
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationRequest;
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationResponse;
import fr.paris.lutece.plugins.deviceregistration.exception.DeviceRegistrationException;
import fr.paris.lutece.plugins.deviceregistration.utils.DeviceRegistrationUtils;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.*;
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

    public DeviceRegistrationResponse createDeviceRegistration( final DeviceRegistrationRequest request ) throws DeviceRegistrationException
    {

        validateRequest( request );

        DeviceRegistration createdDeviceRegistration = DeviceRegistrationHome.create( DeviceRegistrationUtils.fromRequest( request ) );

        DeviceRegistrationHistoryService.getInstance( ).createDeviceRegistrationHistory( createdDeviceRegistration, ChangeType.CREATED );

        return DeviceRegistrationUtils.toResponse( createdDeviceRegistration );

    }

    private void validateRequest( DeviceRegistrationRequest request ) throws DeviceRegistrationException
    {
        if ( Objects.isNull( request.getCustomerId( ) ) && Objects.isNull( request.getConnectionId( ) ) )
        {
            throw new DeviceRegistrationException( "Problem with parameters for creating an entry in Database" );
        }

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = BeanValidationUtil.validate( request );
        if ( !violations.isEmpty( ) )
        {
            List<String> reasons = violations.stream( ).map( ConstraintViolation::getMessage ).filter( Objects::nonNull ).collect( Collectors.toList( ) );
            throw new DeviceRegistrationException( Response.Status.BAD_REQUEST, reasons );
        }

        if ( DeviceRegistrationHome.loadByRegistrationToken( request.getRegistrationToken( ) ).isPresent( ) )
        {
            throw new DeviceRegistrationException( Response.Status.CONFLICT, "Token already exist" );
        }
    }

    public void deleteDeviceRegistration( DeviceRegistrationRequest request ) throws DeviceRegistrationException
    {

        if ( Objects.isNull( request.getCustomerId( ) ) && Objects.isNull( request.getConnectionId( ) ) && Objects.isNull( request.getRegistrationToken( ) ) )
        {
            throw new DeviceRegistrationException( "Missing criteria for deleting entries" );
        }

        if ( Objects.isNull( request.getTokenIssuer( ) ) )
        {
            throw new DeviceRegistrationException( "Missing token issuer concerned by operation" );
        }

        final Map<String, String> criterias = prepareCriteria( request );
        List<DeviceRegistration> deviceRegistrations = DeviceRegistrationHome.findListByCriteria( criterias );

        if ( deviceRegistrations.isEmpty( ) )
        {
            throw new DeviceRegistrationException( Response.Status.NOT_FOUND, "Nothing to delete" );
        }

        deviceRegistrations.stream( ).peek( deviceRegistration -> DeviceRegistrationHome.remove( deviceRegistration.getId( ) ) )
                .forEach( deviceRegistration -> DeviceRegistrationHistoryService.getInstance( ).createDeviceRegistrationHistory( deviceRegistration,
                        ChangeType.DELETED ) );
    }

    public List<String> getRegistrationTokensByCriteria( final String customerId, final String connectionId, final String clientCode )
            throws DeviceRegistrationException
    {

        if ( Objects.isNull( customerId ) && Objects.isNull( connectionId ) )
        {
            throw new DeviceRegistrationException( "Missing criteria for retrieving Registration Token" );
        }

        if ( Objects.isNull( clientCode ) ) {
            throw new DeviceRegistrationException("Mission client code is request");
        }

        List<String> registrationTokens = DeviceRegistrationHome
                .findListByCriteria( prepareCriteria( new DeviceRegistrationRequest( customerId, connectionId, null, clientCode ) ) ).stream( )
                .map( DeviceRegistration::getRegistrationToken ).collect( Collectors.toList( ) );

        if ( registrationTokens.isEmpty( ) )
        {
            throw new DeviceRegistrationException( Response.Status.NOT_FOUND, "No result found" );
        }

        return registrationTokens;
    }

    private Map<String, String> prepareCriteria( DeviceRegistrationRequest request )
    {
        Map<String, String> criteria = new HashMap<>( );

        if ( request != null )
        {
            if ( request.getConnectionId( ) != null )
            {
                criteria.put( "connection_id", request.getConnectionId( ) );
            }
            if ( request.getCustomerId( ) != null )
            {
                criteria.put( "customer_id", request.getCustomerId( ) );
            }
            if ( request.getRegistrationToken( ) != null )
            {
                criteria.put( "registration_token", request.getRegistrationToken( ) );
            }
            if ( request.getTokenIssuer( ) != null )
            {
                criteria.put( "token_issuer", request.getTokenIssuer( ) );
            }
        }

        return criteria;
    }
}
