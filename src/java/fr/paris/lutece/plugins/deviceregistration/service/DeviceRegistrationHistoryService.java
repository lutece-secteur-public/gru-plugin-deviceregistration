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
import fr.paris.lutece.plugins.deviceregistration.business.history.ChangeType;
import fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistory;
import fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistoryHome;

public class DeviceRegistrationHistoryService
{

    private static DeviceRegistrationHistoryService instance;

    /**
     * Private contructor to avoid instantiation
     */
    private DeviceRegistrationHistoryService( )
    {
    }

    /**
     * Create a new instance of DeviceRegistrationHistoryService or return the existing one
     * 
     * @return instance of DeviceRegistrationHistoryService
     */
    public static DeviceRegistrationHistoryService getInstance( )
    {
        if ( instance == null )
        {
            instance = new DeviceRegistrationHistoryService( );
        }
        return instance;
    }

    /**
     * Create a new entry in deviceregistration_deviceregistration_history table
     *
     * @param deviceRegistration
     *            Deviceregistration related to history's entry
     * @param changeType
     *            type of change (CREATED, DELETED)
     */
    public void createDeviceRegistrationHistory( DeviceRegistration deviceRegistration, ChangeType changeType )
    {
        DeviceRegistrationHistory deviceRegistrationHistory = new DeviceRegistrationHistory( );
        deviceRegistrationHistory.setCustomerId( deviceRegistration.getCustomerId( ) );
        deviceRegistrationHistory.setConnectionId( deviceRegistration.getConnectionId( ) );
        deviceRegistrationHistory.setRegistrationToken( deviceRegistration.getRegistrationToken( ) );
        deviceRegistrationHistory.setTokenIssuer( deviceRegistration.getTokenIssuer( ) );
        deviceRegistrationHistory.setChangeType( changeType );
        DeviceRegistrationHistoryHome.create( deviceRegistrationHistory );
    }
}
