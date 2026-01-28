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
package fr.paris.lutece.plugins.deviceregistration.business.history;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;

public final class DeviceRegistrationHistoryHome
{

    private static final IDeviceRegistrationHistoryDAO dao = SpringContextService.getBean( "deviceregistration.deviceRegistrationHistoryDAO" );
    private static final Plugin plugin = PluginService.getPlugin( "deviceregistration" );

    /**
     * Private constructor to avoid instantiation
     */
    private DeviceRegistrationHistoryHome( )
    {
    }

    public static List<Integer> getIdDeviceRegistrationsHistoryList( final Map<String, String> mapFilterCriteria, final String strColumnToOrder, final String strSortMode )
    {
        return dao.selectIdDeviceRegistrationsHistoryList( plugin, mapFilterCriteria, strColumnToOrder, strSortMode );
    }

    public static List<DeviceRegistrationHistory> getDeviceRegistrationsHistoryListByIds( final List<Integer> listIds )
    {
        return dao.getDeviceRegistrationsHistoryListByIds( plugin, listIds );
    }

    /**
     * Create an instance of DeviceRegistrationHistory
     *
     * @param deviceRegistrationHistory
     *            The instance wich contains the informations to store
     */
    public static void create( final DeviceRegistrationHistory deviceRegistrationHistory )
    {
        dao.insert( deviceRegistrationHistory, plugin );
    }
}
