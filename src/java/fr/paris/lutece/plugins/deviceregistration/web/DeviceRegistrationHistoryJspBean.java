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
package fr.paris.lutece.plugins.deviceregistration.web;

import fr.paris.lutece.plugins.deviceregistration.business.history.ChangeType;
import fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistory;
import fr.paris.lutece.plugins.deviceregistration.business.history.DeviceRegistrationHistoryHome;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.AbstractPaginator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides the user interface to manage DeviceRegistration History
 */
@Controller( controllerJsp = "ManageDeviceRegistrationsHistory.jsp", controllerPath = "jsp/admin/plugins/deviceregistration/", right = "DEVICEREGISTRATION_MANAGEMENT" )
public class DeviceRegistrationHistoryJspBean extends AbstractJspBean<Integer, DeviceRegistrationHistory>
{
    // Templates
    private static final String TEMPLATE_MANAGE_DEVICEREGISTRATIONS = "/admin/plugins/deviceregistration/manage_deviceregistrations_history.html";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DEVICEREGISTRATIONS_HISTORY = "deviceregistration.manage_deviceregistrationshistory.pageTitle";
    // Markers
    private static final String MARK_DEVICEREGISTRATION_HISTORY_LIST = "deviceregistrationhistory_list";

    private static final String JSP_MANAGE_DEVICEREGISTRATIONS_HISTORY = "jsp/admin/plugins/deviceregistration/ManageDeviceRegistrationsHistory.jsp";

    // Views
    private static final String VIEW_MANAGE_DEVICEREGISTRATIONS_HISTORY = "manageDeviceRegistrationsHistory";

    // Session variable to store working values
    private List<Integer> _listIdDeviceRegistrationsHistory;
    private HashMap<String, String> _mapFilterCriteria = new HashMap<>( );

    /**
     * Build the Manage View
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEVICEREGISTRATIONS_HISTORY, defaultView = true )
    public String getManageDeviceRegistrationsHistory( HttpServletRequest request )
    {
        // new search only if in pagination mode
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) == null )
        {
            // if sorting request : new search with the existing filter criteria, ordered
            // example of order by parameter : orderby=name
            if ( StringUtils.isNotBlank( (String) request.getParameter( PARAMETER_SEARCH_ORDER_BY ) ) )
            {

                String strOrderByColumn = (String) request.getParameter( PARAMETER_SEARCH_ORDER_BY );
                String strSortMode = getSortMode( );

                _listIdDeviceRegistrationsHistory = DeviceRegistrationHistoryHome.getIdDeviceRegistrationsHistoryList( _mapFilterCriteria, strOrderByColumn,
                        strSortMode );

            }
            else
            {
                // reload the filter criteria and search
                _mapFilterCriteria = (HashMap<String, String>) getFilterCriteriaFromRequest( request );
                _listIdDeviceRegistrationsHistory = DeviceRegistrationHistoryHome.getIdDeviceRegistrationsHistoryList( _mapFilterCriteria, null, null );
            }

            // set CurrentPageIndex of Paginator to null in aim of displays the first page of results
            resetCurrentPageIndexOfPaginator( );
        }

        Map<String, Object> model = getPaginatedListModel( request, MARK_DEVICEREGISTRATION_HISTORY_LIST, _listIdDeviceRegistrationsHistory,
                JSP_MANAGE_DEVICEREGISTRATIONS_HISTORY );
        model.put( "change_type_list", ChangeType.values( ) );

        _mapFilterCriteria.computeIfPresent( "created_at", ( k, v ) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
            return LocalDateTime.parse( v, formatter ).toLocalDate( ).format( DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) );
        } );
        addSearchParameters( model, _mapFilterCriteria ); // allow the persistence of search values in inputs search bar inputs

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEVICEREGISTRATIONS_HISTORY, TEMPLATE_MANAGE_DEVICEREGISTRATIONS, model );

    }

    /**
     * Get Items from Ids list
     *
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
    @Override
    List<DeviceRegistrationHistory> getItemsFromIds( List<Integer> listIds )
    {
        List<DeviceRegistrationHistory> listDeviceRegistration = DeviceRegistrationHistoryHome.getDeviceRegistrationsHistoryListByIds( listIds );
        // keep original order
        return listDeviceRegistration.stream( ).sorted( Comparator.comparingInt( notif -> listIds.indexOf( notif.getId( ) ) ) ).collect( Collectors.toList( ) );
    }

    @Override
    int getPluginDefaultNumberOfItemPerPage( )
    {
        return AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
    }

}
