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

import fr.paris.lutece.plugins.deviceregistration.business.AbstractFilterDao;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides Data Access methods for DeviceRegistration objects
 */
public final class DeviceRegistrationDAO extends AbstractFilterDao implements IDeviceRegistrationDAO
{
    // Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO deviceregistration_deviceregistration ( customer_id, connection_id, registration_token, token_issuer ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM deviceregistration_deviceregistration WHERE id_device_registration = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE deviceregistration_deviceregistration SET customer_id = ?, connection_id = ?, registration_token = ?, token_issuer = ? WHERE id_device_registration = ?";

    private static final String SQL_QUERY_SELECTALL = "SELECT id_device_registration, customer_id, connection_id, registration_token, token_issuer FROM deviceregistration_deviceregistration";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_device_registration FROM deviceregistration_deviceregistration";
    private static final String SQL_QUERY_SELECTALL_BY_REGISTRATIONTOKEN = SQL_QUERY_SELECTALL + " WHERE registration_token = ?";

    private static final String SQL_QUERY_SELECTALL_BY_IDS = SQL_QUERY_SELECTALL + " WHERE id_device_registration IN (  ";
    private static final String SQL_QUERY_SELECT_BY_ID = SQL_QUERY_SELECTALL + " WHERE id_device_registration = ?";

    /**
     * Constructor
     */
    public DeviceRegistrationDAO( )
    {
        this.initMapSql( DeviceRegistration.class ); // Maps with name and type of each databases column associated to the business class attributes
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( final DeviceRegistration deviceRegistration, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, deviceRegistration.getCustomerId( ) );
            daoUtil.setString( nIndex++, deviceRegistration.getConnectionId( ) );
            daoUtil.setString( nIndex++, deviceRegistration.getRegistrationToken( ) );
            daoUtil.setString( nIndex, deviceRegistration.getTokenIssuer( ) );

            daoUtil.executeUpdate( );
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<DeviceRegistration> load( final int nKey, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            return getDeviceRegistration( daoUtil );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( final int nKey, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public Optional<DeviceRegistration> loadByRegistrationToken( final String registrationToken, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_REGISTRATIONTOKEN, plugin ) )
        {
            daoUtil.setString( 1, registrationToken );
            daoUtil.executeQuery( );

            return getDeviceRegistration( daoUtil );
        }
    }

    private @NonNull Optional<DeviceRegistration> getDeviceRegistration( final DAOUtil daoUtil )
    {
        DeviceRegistration deviceRegistration = null;
        if ( daoUtil.next( ) )
        {
            deviceRegistration = this.loadFromDaoUtil( daoUtil );
        }

        return Optional.ofNullable( deviceRegistration );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( final DeviceRegistration deviceRegistration, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setString( nIndex++, deviceRegistration.getCustomerId( ) );
            daoUtil.setString( nIndex++, deviceRegistration.getConnectionId( ) );
            daoUtil.setString( nIndex++, deviceRegistration.getRegistrationToken( ) );
            daoUtil.setString( nIndex++, deviceRegistration.getTokenIssuer( ) );
            daoUtil.setInt( nIndex, deviceRegistration.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DeviceRegistration> selectDeviceRegistrationsList( final Plugin plugin )
    {
        final List<DeviceRegistration> deviceRegistrationList = new ArrayList<>( );
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                deviceRegistrationList.add( this.loadFromDaoUtil( daoUtil ) );
            }

            return deviceRegistrationList;
        }
    }

    @Override
    public List<DeviceRegistration> findListByCriteria( final Map<String, String> criteria, final Plugin plugin )
    {
        final List<DeviceRegistration> deviceRegistrationList = new ArrayList<>( );

        final String strSelectStatement = prepareSelectStatement( SQL_QUERY_SELECTALL, criteria, null, null );

        try ( final DAOUtil daoUtil = new DAOUtil( strSelectStatement, plugin ) )
        {
            int nIndex = 1;

            for ( final Map.Entry<String, String> filter : criteria.entrySet( ) )
            {

                if ( StringUtils.isNotBlank( filter.getValue( ) ) && _mapSql.containsKey( filter.getKey( ) ) )
                {
                    daoUtil.setString( nIndex++, filter.getValue( ) );
                }
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                deviceRegistrationList.add( this.loadFromDaoUtil( daoUtil ) );
            }

            return deviceRegistrationList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdDeviceRegistrationsList( final Plugin plugin, final Map<String, String> mapFilterCriteria, final String strColumnToOrder, final String strSortMode )
    {
        final List<Integer> deviceRegistrationList = new ArrayList<>( );

        final String strSelectStatement = prepareSelectStatement( SQL_QUERY_SELECTALL_ID, mapFilterCriteria, strColumnToOrder, strSortMode );

        try ( final DAOUtil daoUtil = new DAOUtil( strSelectStatement, plugin ) )
        {

            int nIndex = 1;

            for ( final Map.Entry<String, String> filter : mapFilterCriteria.entrySet( ) )
            {

                if ( StringUtils.isNotBlank( filter.getValue( ) ) && _mapSql.containsKey( filter.getKey( ) ) )
                {
                    daoUtil.setString( nIndex++, filter.getValue( ) );
                }
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                deviceRegistrationList.add( daoUtil.getInt( 1 ) );
            }

            return deviceRegistrationList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectDeviceRegistrationsReferenceList( final Plugin plugin )
    {
        final ReferenceList deviceRegistrationList = new ReferenceList( );
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                deviceRegistrationList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

            return deviceRegistrationList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DeviceRegistration> selectDeviceRegistrationsListByIds( final Plugin plugin, final List<Integer> listIds )
    {
        final List<DeviceRegistration> deviceRegistrationList = new ArrayList<>( );

        final StringBuilder builder = new StringBuilder( );

        if ( !listIds.isEmpty( ) )
        {
            builder.append("?,".repeat(listIds.size()));
            final String placeHolders = builder.deleteCharAt( builder.length( ) - 1 ).toString( );
            final String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";

            try ( final DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
            {
                int index = 1;
                for ( Integer n : listIds )
                {
                    daoUtil.setInt( index++, n );
                }

                daoUtil.executeQuery( );
                while ( daoUtil.next( ) )
                {
                    deviceRegistrationList.add( this.loadFromDaoUtil( daoUtil ) );
                }
            }
        }
        return deviceRegistrationList;

    }

    private DeviceRegistration loadFromDaoUtil( final DAOUtil daoUtil )
    {

        final DeviceRegistration deviceRegistration = new DeviceRegistration( );
        int nIndex = 1;

        deviceRegistration.setId( daoUtil.getInt( nIndex++ ) );
        deviceRegistration.setCustomerId( daoUtil.getString( nIndex++ ) );
        deviceRegistration.setConnectionId( daoUtil.getString( nIndex++ ) );
        deviceRegistration.setRegistrationToken( daoUtil.getString( nIndex++ ) );
        deviceRegistration.setTokenIssuer( daoUtil.getString( nIndex ) );

        return deviceRegistration;
    }
}
