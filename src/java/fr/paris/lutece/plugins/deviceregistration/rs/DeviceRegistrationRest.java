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
package fr.paris.lutece.plugins.deviceregistration.rs;

import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistration;
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationRequest;
import fr.paris.lutece.plugins.deviceregistration.dto.DeviceRegistrationResponse;
import fr.paris.lutece.plugins.deviceregistration.exception.DeviceRegistrationException;
import fr.paris.lutece.plugins.deviceregistration.service.DeviceRegistrationService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.service.mapper.GenericUncaughtExceptionMapper;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * DeviceRegistrationRest
 */
@Path( RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_PATH_V1 )
@Api( RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_PATH_V1 )
public class DeviceRegistrationRest
{

    @GET
    @Path(Constants.TOKEN_PATH)
    @Produces( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Get all registration token by identity (GUID/CUID) " )
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "Registration Token found", response = DeviceRegistrationResponse.class ),
            @ApiResponse( code = 400, message = GenericUncaughtExceptionMapper.ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 404, message = "Registration Token not found" )
    } )
    public Response getRegistrationToken( @ApiParam( allowEmptyValue = true ) @QueryParam( Constants.CONNECTION_ID ) String connectionId,
            @ApiParam( allowEmptyValue = true ) @QueryParam( Constants.CUSTOMER_ID ) String customerId,
            @ApiParam( required = true ) @HeaderParam( Constants.TOKEN_ISSUER ) String issuer ) throws DeviceRegistrationException
    {
        final List<String> registrationTokenList = DeviceRegistrationService.getInstance( ).getRegistrationTokensByCriteria( customerId, connectionId,
                issuer );

        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( new DeviceRegistrationResponse( customerId, connectionId, registrationTokenList ) ) ) )
                .build( );
    }

    @POST
    @Path(Constants.TOKEN_PATH)
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Create new device registration" )
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "Device registration created", response = DeviceRegistrationResponse.class ),
            @ApiResponse( code = 400, message = GenericUncaughtExceptionMapper.ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 409, message = "Token already exists" )
    } )
    public Response createDeviceRegistration(
            @ApiParam( value = "Data of the deviceRegistration to create", required = true, type = "DeviceRegistrationRequest" ) DeviceRegistrationRequest request,
            @ApiParam( required = true ) @HeaderParam( Constants.TOKEN_ISSUER ) String clientCode ) throws DeviceRegistrationException
    {
        final DeviceRegistration deviceRegistration = DeviceRegistrationService.getInstance().createDeviceRegistration(request.getCustomerId(), request.getConnectionId(), request.getRegistrationToken(), clientCode);
        final DeviceRegistrationResponse response = new DeviceRegistrationResponse( deviceRegistration.getCustomerId( ), deviceRegistration.getConnectionId( ), List.of( deviceRegistration.getRegistrationToken( ) ) );
        return Response.status( Response.Status.CREATED ).entity( JsonUtil.buildJsonResponse( new JsonResponse( response ) ) ).build( );
    }

    @DELETE
    @Path(Constants.TOKEN_PATH)
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Delete device registration" )
    @ApiResponses( value = {
            @ApiResponse( code = 204, message = "Device registration deleted" ),
            @ApiResponse( code = 400, message = GenericUncaughtExceptionMapper.ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 404, message = "Nothing was deleted" )
    } )
    public Response deleteRegistrationToken(
            @ApiParam( value = "Data of the deviceRegistration to delete", required = true, type = "DeviceRegistrationRequest" ) DeviceRegistrationRequest request,
            @ApiParam( name = Constants.TOKEN_ISSUER, required = true ) @HeaderParam( Constants.TOKEN_ISSUER ) String clientCode )
            throws DeviceRegistrationException
    {
        DeviceRegistrationService.getInstance( ).deleteDeviceRegistration( request.getCustomerId(), request.getConnectionId(), request.getRegistrationToken(), clientCode );
        return Response.status( Response.Status.NO_CONTENT ).build( );
    }
}
