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
package fr.paris.lutece.plugins.deviceregistration.business.deviceregistration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * This is the business class for the object DeviceRegistration
 */
public class DeviceRegistration implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @Size( max = 50, message = "#i18n{deviceregistration.validation.deviceregistration.CustomerId.size}" )
    private String _strCustomerId;

    @Size( max = 50, message = "#i18n{deviceregistration.validation.deviceregistration.ConnectionId.size}" )
    private String _strConnectionId;

    @NotEmpty( message = "#i18n{deviceregistration.validation.deviceregistration.RegistrationToken.notEmpty}" )
    @Size( max = 255, message = "#i18n{deviceregistration.validation.deviceregistration.RegistrationToken.size}" )
    private String _strRegistrationToken;

    @NotEmpty( message = "#i18n{deviceregistration.validation.deviceregistration.TokenIssuer.notEmpty}" )
    @Size( max = 50, message = "#i18n{deviceregistration.validation.deviceregistration.TokenIssuer.size}" )
    private String _strTokenIssuer;

    public DeviceRegistration()
    {

    }

    public DeviceRegistration(String customerId, String connectionId, String registrationToken, String tokenIssuer) {
        this._strCustomerId = customerId;
        this._strConnectionId = connectionId;
        this._strRegistrationToken = registrationToken;
        this._strTokenIssuer = tokenIssuer;
    }

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     *
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the CustomerId
     *
     * @return The CustomerId
     */
    public String getCustomerId( )
    {
        return _strCustomerId;
    }

    /**
     * Sets the CustomerId
     *
     * @param strCustomerId
     *            The CustomerId
     */
    public void setCustomerId( String strCustomerId )
    {
        _strCustomerId = strCustomerId;
    }

    /**
     * Returns the ConnectionId
     *
     * @return The ConnectionId
     */
    public String getConnectionId( )
    {
        return _strConnectionId;
    }

    /**
     * Sets the ConnectionId
     *
     * @param strConnectionId
     *            The ConnectionId
     */
    public void setConnectionId( String strConnectionId )
    {
        _strConnectionId = strConnectionId;
    }

    /**
     * Returns the RegistrationToken
     *
     * @return The RegistrationToken
     */
    public String getRegistrationToken( )
    {
        return _strRegistrationToken;
    }

    /**
     * Sets the RegistrationToken
     *
     * @param strToken
     *            The RegistrationToken
     */
    public void setRegistrationToken( String strToken )
    {
        _strRegistrationToken = strToken;
    }

    public String getTokenIssuer( )
    {
        return _strTokenIssuer;
    }

    public void setTokenIssuer( String strTokenIssuer )
    {
        this._strTokenIssuer = strTokenIssuer;
    }

}
