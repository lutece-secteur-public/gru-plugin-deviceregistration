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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for DeviceRegistration objects
 */
public final class DeviceRegistrationHome
{
    // Static variable pointed at the DAO instance
    private static IDeviceRegistrationDAO _dao = SpringContextService.getBean( "deviceregistration.deviceRegistrationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "deviceregistration" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DeviceRegistrationHome( )
    {
    }

    /**
     * Create an instance of the deviceRegistration class
     *
     * @param deviceRegistration
     *            The instance of the DeviceRegistration which contains the informations to store
     * @return The instance of deviceRegistration which has been created with its primary key.
     */
    public static DeviceRegistration create( DeviceRegistration deviceRegistration )
    {
        _dao.insert( deviceRegistration, _plugin );

        return deviceRegistration;
    }

    /**
     * Get a specific instance of DeviceRegistration
     * 
     * @param registrationToken
     *            token of the instance we're looking for
     * @return an Optional of the object
     */
    public static Optional<DeviceRegistration> loadByRegistrationToken( String registrationToken )
    {
        return _dao.loadByRegistrationToken( registrationToken, _plugin );
    }

    /**
     * Update of the deviceRegistration which is specified in parameter
     *
     * @param deviceRegistration
     *            The instance of the DeviceRegistration which contains the data to store
     * @return The instance of the deviceRegistration which has been updated
     */
    public static DeviceRegistration update( DeviceRegistration deviceRegistration )
    {
        _dao.store( deviceRegistration, _plugin );

        return deviceRegistration;
    }

    /**
     * Remove the deviceRegistration whose identifier is specified in parameter
     *
     * @param nKey
     *            The deviceRegistration Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a deviceRegistration whose identifier is specified in parameter
     *
     * @param nKey
     *            The deviceRegistration primary key
     * @return an instance of DeviceRegistration
     */
    public static Optional<DeviceRegistration> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Return a list of deviceRegistration
     * 
     * @param criteria
     *            contains search inputs
     * @return list of DeviceRegistration
     */
    public static List<DeviceRegistration> findListByCriteria( Map<String, String> criteria )
    {
        return _dao.findListByCriteria( criteria, _plugin );
    }

    /**
     * Load the data of all the deviceRegistration objects and returns them as a list
     *
     * @return the list which contains the data of all the deviceRegistration objects
     */
    public static List<DeviceRegistration> getDeviceRegistrationsList( )
    {
        return _dao.selectDeviceRegistrationsList( _plugin );
    }

    /**
     * Load the id of all the deviceRegistration objects and returns them as a list
     *
     * @param mapFilterCriteria
     *            contains search bar names/values inputs
     * @param strColumnToOrder
     *            contains the column name to use for orderBy statement in case of sorting request (must be null)
     * @param strSortMode
     *            contains the sortMode in case of sorting request : ASC or DESC (must be null)
     * @return the list which contains the id of all the project objects
     */
    public static List<Integer> getIdDeviceRegistrationsList( Map<String, String> mapFilterCriteria, String strColumnToOrder, String strSortMode )
    {
        return _dao.selectIdDeviceRegistrationsList( _plugin, mapFilterCriteria, strColumnToOrder, strSortMode );
    }

    /**
     * Load the data of all the avant objects and returns them as a list
     *
     * @param listIds
     *            liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<DeviceRegistration> getDeviceRegistrationsListByIds( List<Integer> listIds )
    {
        return _dao.selectDeviceRegistrationsListByIds( _plugin, listIds );
    }

}
