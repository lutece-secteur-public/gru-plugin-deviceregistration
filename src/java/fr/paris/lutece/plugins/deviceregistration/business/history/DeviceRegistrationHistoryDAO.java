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

import fr.paris.lutece.plugins.deviceregistration.business.AbstractFilterDao;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DeviceRegistrationHistoryDAO extends AbstractFilterDao implements IDeviceRegistrationHistoryDAO
{

    public static final String SQL_QUERY_SELECTALL = "SELECT id_history, change_type, created_at, customer_id, connection_id, registration_token, token_issuer FROM deviceregistration_deviceregistration_history";
    public static final String SQL_QUERY_SELECTALL_ID = "SELECT id_history FROM deviceregistration_deviceregistration_history";

    public static final String SQL_QUERY_INSERT = "INSERT INTO deviceregistration_deviceregistration_history ( change_type, customer_id, connection_id, registration_token, token_issuer ) VALUES ( ?, ?, ?, ?, ? )";

    public static final String SQL_QUERY_SELECTALL_BY_IDS = SQL_QUERY_SELECTALL + " WHERE id_history IN ( ";

    public DeviceRegistrationHistoryDAO( )
    {
        initMapSql( DeviceRegistrationHistory.class );
    }

    @Override
    public void insert( final DeviceRegistrationHistory deviceRegistrationHistory, final Plugin plugin )
    {
        try ( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int index = 1;
            daoUtil.setString( index++, deviceRegistrationHistory.getChangeType( ).name( ) );
            daoUtil.setString( index++, deviceRegistrationHistory.getCustomerId( ) );
            daoUtil.setString( index++, deviceRegistrationHistory.getConnectionId( ) );
            daoUtil.setString( index++, deviceRegistrationHistory.getRegistrationToken( ) );
            daoUtil.setString( index, deviceRegistrationHistory.getTokenIssuer( ) );

            daoUtil.executeUpdate( );
        }
    }

    @Override
    public List<Integer> selectIdDeviceRegistrationsHistoryList( Plugin plugin, Map<String, String> mapFilterCriteria, String strColumnToOrder,
            String strSortMode )
    {
        List<Integer> deviceRegistrationHistoryList = new ArrayList<>( );

        String strSelectStatement = prepareSelectStatement( SQL_QUERY_SELECTALL_ID, mapFilterCriteria, strColumnToOrder, strSortMode );

        try ( DAOUtil daoUtil = new DAOUtil( strSelectStatement, plugin ) )
        {

            int nIndex = 1;

            for ( Map.Entry<String, String> filter : mapFilterCriteria.entrySet( ) )
            {

                if ( StringUtils.isNotBlank( filter.getValue( ) ) && _mapSql.containsKey( filter.getKey( ) ) )
                {
                    daoUtil.setString( nIndex++, filter.getValue( ) );
                    if ( "created_at".equals( filter.getKey( ) ) )
                    {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
                        LocalDateTime dateTime = LocalDateTime.parse( filter.getValue( ), formatter );
                        daoUtil.setString( nIndex++, dateTime.plusDays( 1 ).format( formatter ) );
                    }
                }
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                deviceRegistrationHistoryList.add( daoUtil.getInt( 1 ) );
            }

            return deviceRegistrationHistoryList;
        }
    }

    @Override
    public List<DeviceRegistrationHistory> getDeviceRegistrationsHistoryListByIds( Plugin plugin, List<Integer> listIds )
    {
        List<DeviceRegistrationHistory> deviceRegistrationList = new ArrayList<>( );

        StringBuilder builder = new StringBuilder( );

        if ( !listIds.isEmpty( ) )
        {
            builder.append( "?,".repeat( listIds.size( ) ) ).deleteCharAt( builder.length( ) - 1 );

            String stmt = SQL_QUERY_SELECTALL_BY_IDS + builder.toString( ) + ")";

            try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
            {
                int index = 1;
                for ( Integer n : listIds )
                {
                    daoUtil.setInt( index++, n );
                }

                daoUtil.executeQuery( );
                while ( daoUtil.next( ) )
                {
                    deviceRegistrationList.add( loadFromDaoUtil( daoUtil ) );
                }
            }
        }
        return deviceRegistrationList;
    }

    private DeviceRegistrationHistory loadFromDaoUtil( final DAOUtil daoUtil )
    {
        DeviceRegistrationHistory deviceRegistrationHistory = new DeviceRegistrationHistory( );
        int index = 1;
        deviceRegistrationHistory.setId( daoUtil.getInt( index++ ) );
        deviceRegistrationHistory.setChangeType( ChangeType.valueOf( daoUtil.getString( index++ ) ) );
        deviceRegistrationHistory.setCreatedAt( daoUtil.getTimestamp( index++ ) );
        deviceRegistrationHistory.setCustomerId( daoUtil.getString( index++ ) );
        deviceRegistrationHistory.setConnectionId( daoUtil.getString( index++ ) );
        deviceRegistrationHistory.setRegistrationToken( daoUtil.getString( index++ ) );
        deviceRegistrationHistory.setTokenIssuer( daoUtil.getString( index ) );
        return deviceRegistrationHistory;
    }
}
