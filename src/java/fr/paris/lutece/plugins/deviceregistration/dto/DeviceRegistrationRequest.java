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
package fr.paris.lutece.plugins.deviceregistration.dto;

import fr.paris.lutece.plugins.deviceregistration.rs.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel
public class DeviceRegistrationRequest
{
    @ApiModelProperty( value = Constants.CUSTOMER_ID )
    @Size( max = 50, message = "#i18n{deviceregistration.validation.deviceregistration.CustomerId.size}" )
    private String customerId;

    @ApiModelProperty( value = Constants.CONNECTION_ID )
    @Size( max = 50, message = "#i18n{deviceregistration.validation.deviceregistration.ConnectionId.size}" )
    private String connectionId;

    @ApiModelProperty( value = Constants.REGISTRATION_TOKENS )
    @NotEmpty( message = "#i18n{deviceregistration.validation.deviceregistration.RegistrationToken.notEmpty}" )
    @Size( max = 255, message = "#i18n{deviceregistration.validation.deviceregistration.RegistrationToken.size}" )
    private String registrationToken;

    public DeviceRegistrationRequest( )
    {
    }

    public DeviceRegistrationRequest( final String customerId, final String connectionId, final String registrationToken )
    {
        this.customerId = customerId;
        this.connectionId = connectionId;
        this.registrationToken = registrationToken;
    }

    public String getCustomerId( )
    {
        return this.customerId;
    }

    public String getConnectionId( )
    {
        return this.connectionId;
    }

    public String getRegistrationToken( )
    {
        return this.registrationToken;
    }
}
