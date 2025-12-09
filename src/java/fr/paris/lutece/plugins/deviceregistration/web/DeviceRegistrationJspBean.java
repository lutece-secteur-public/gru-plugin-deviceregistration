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

import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistration;
import fr.paris.lutece.plugins.deviceregistration.business.deviceregistration.DeviceRegistrationHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.url.UrlItem;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides the user interface to manage DeviceRegistration features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDeviceRegistrations.jsp", controllerPath = "jsp/admin/plugins/deviceregistration/", right = "DEVICEREGISTRATION_MANAGEMENT" )
public class DeviceRegistrationJspBean extends AbstractJspBean<Integer, DeviceRegistration>
{

    // Rights
    public static final String RIGHT_MANAGEDEVICEREGISTRATION = "DEVICEREGISTRATION_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_MANAGE_DEVICEREGISTRATIONS = "/admin/plugins/deviceregistration/manage_deviceregistrations.html";
    private static final String TEMPLATE_CREATE_DEVICEREGISTRATION = "/admin/plugins/deviceregistration/create_deviceregistration.html";
    private static final String TEMPLATE_MODIFY_DEVICEREGISTRATION = "/admin/plugins/deviceregistration/modify_deviceregistration.html";

    // Parameters
    private static final String PARAMETER_ID_DEVICEREGISTRATION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DEVICEREGISTRATIONS = "deviceregistration.manage_deviceregistrations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DEVICEREGISTRATION = "deviceregistration.modify_deviceregistration.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DEVICEREGISTRATION = "deviceregistration.create_deviceregistration.pageTitle";

    // Markers
    private static final String MARK_DEVICEREGISTRATION_LIST = "deviceregistration_list";
    private static final String MARK_DEVICEREGISTRATION = "deviceregistration";

    private static final String JSP_MANAGE_DEVICEREGISTRATIONS = "jsp/admin/plugins/deviceregistration/ManageDeviceRegistrations.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DEVICEREGISTRATION = "deviceregistration.message.confirmRemoveDeviceRegistration";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "deviceregistration.model.entity.deviceregistration.attribute.";

    // Views
    private static final String VIEW_MANAGE_DEVICEREGISTRATIONS = "manageDeviceRegistrations";
    private static final String VIEW_CREATE_DEVICEREGISTRATION = "createDeviceRegistration";
    private static final String VIEW_MODIFY_DEVICEREGISTRATION = "modifyDeviceRegistration";

    // Actions
    private static final String ACTION_CREATE_DEVICEREGISTRATION = "createDeviceRegistration";
    private static final String ACTION_MODIFY_DEVICEREGISTRATION = "modifyDeviceRegistration";
    private static final String ACTION_REMOVE_DEVICEREGISTRATION = "removeDeviceRegistration";
    private static final String ACTION_CONFIRM_REMOVE_DEVICEREGISTRATION = "confirmRemoveDeviceRegistration";

    // Infos
    private static final String INFO_DEVICEREGISTRATION_CREATED = "deviceregistration.info.deviceregistration.created";
    private static final String INFO_DEVICEREGISTRATION_UPDATED = "deviceregistration.info.deviceregistration.updated";
    private static final String INFO_DEVICEREGISTRATION_REMOVED = "deviceregistration.info.deviceregistration.removed";

    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";

    // Session variable to store working values
    private DeviceRegistration _deviceregistration;
    private List<Integer> _listIdDeviceRegistrations;
    private HashMap<String, String> _mapFilterCriteria = new HashMap<>( );
    private String _optionOrderBy;

    /**
     * Build the Manage View
     *
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEVICEREGISTRATIONS, defaultView = true )
    public String getManageDeviceRegistrations( HttpServletRequest request )
    {
        _deviceregistration = null;

        // new search only if in pagination mode
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) == null )
        {
            // if sorting request : new search with the existing filter criteria, ordered
            // example of order by parameter : orderby=name
            if ( StringUtils.isNotBlank( (String) request.getParameter( PARAMETER_SEARCH_ORDER_BY ) ) )
            {

                String strOrderByColumn = (String) request.getParameter( PARAMETER_SEARCH_ORDER_BY );
                String strSortMode = getSortMode( );

                _listIdDeviceRegistrations = DeviceRegistrationHome.getIdDeviceRegistrationsList( _mapFilterCriteria, strOrderByColumn, strSortMode );

            }
            else
            {
                // reload the filter criteria and search
                _mapFilterCriteria = (HashMap<String, String>) getFilterCriteriaFromRequest( request );
                _listIdDeviceRegistrations = DeviceRegistrationHome.getIdDeviceRegistrationsList( _mapFilterCriteria, null, null );
            }

            // set CurrentPageIndex of Paginator to null in aim of displays the first page of results
            resetCurrentPageIndexOfPaginator( );
        }

        Map<String, Object> model = getPaginatedListModel( request, MARK_DEVICEREGISTRATION_LIST, _listIdDeviceRegistrations, JSP_MANAGE_DEVICEREGISTRATIONS );

        addSearchParameters( model, _mapFilterCriteria ); // allow the persistence of search values in inputs search bar inputs

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEVICEREGISTRATIONS, TEMPLATE_MANAGE_DEVICEREGISTRATIONS, model );

    }

    /**
     * Get Items from Ids list
     *
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
    @Override
    List<DeviceRegistration> getItemsFromIds( List<Integer> listIds )
    {
        List<DeviceRegistration> listDeviceRegistration = DeviceRegistrationHome.getDeviceRegistrationsListByIds( listIds );
        // keep original order
        return listDeviceRegistration.stream( ).sorted( Comparator.comparingInt( notif -> listIds.indexOf( notif.getId( ) ) ) ).collect( Collectors.toList( ) );
    }

    @Override
    int getPluginDefaultNumberOfItemPerPage( )
    {
        return AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
    }

    /**
     * reset the _listIdDeviceRegistrations list
     */
    public void resetListId( )
    {
        _listIdDeviceRegistrations = new ArrayList<>( );
    }

    /**
     * Returns the form to create a deviceregistration
     *
     * @param request
     *            The Http request
     * @return the html code of the deviceregistration form
     */
    @View( VIEW_CREATE_DEVICEREGISTRATION )
    public String getCreateDeviceRegistration( HttpServletRequest request )
    {
        _deviceregistration = ( _deviceregistration != null ) ? _deviceregistration : new DeviceRegistration( );

        Map<String, Object> model = getModel( );
        model.put( MARK_DEVICEREGISTRATION, _deviceregistration );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_DEVICEREGISTRATION ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DEVICEREGISTRATION, TEMPLATE_CREATE_DEVICEREGISTRATION, model );
    }

    /**
     * Process the data capture form of a new deviceregistration
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_DEVICEREGISTRATION )
    public String doCreateDeviceRegistration( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _deviceregistration, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_DEVICEREGISTRATION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _deviceregistration, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DEVICEREGISTRATION );
        }

        DeviceRegistrationHome.create( _deviceregistration );
        addInfo( INFO_DEVICEREGISTRATION_CREATED, getLocale( ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DEVICEREGISTRATIONS );
    }

    /**
     * Manages the removal form of a deviceregistration whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DEVICEREGISTRATION )
    public String getConfirmRemoveDeviceRegistration( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEVICEREGISTRATION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DEVICEREGISTRATION ) );
        url.addParameter( PARAMETER_ID_DEVICEREGISTRATION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DEVICEREGISTRATION, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a deviceregistration
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage deviceregistrations
     */
    @Action( ACTION_REMOVE_DEVICEREGISTRATION )
    public String doRemoveDeviceRegistration( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEVICEREGISTRATION ) );

        DeviceRegistrationHome.remove( nId );
        addInfo( INFO_DEVICEREGISTRATION_REMOVED, getLocale( ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DEVICEREGISTRATIONS );
    }

    /**
     * Returns the form to update info about a deviceregistration
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DEVICEREGISTRATION )
    public String getModifyDeviceRegistration( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEVICEREGISTRATION ) );

        if ( _deviceregistration == null || ( _deviceregistration.getId( ) != nId ) )
        {
            Optional<DeviceRegistration> optDeviceRegistration = DeviceRegistrationHome.findByPrimaryKey( nId );
            _deviceregistration = optDeviceRegistration.orElseThrow( ( ) -> new AppException( ERROR_RESOURCE_NOT_FOUND ) );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_DEVICEREGISTRATION, _deviceregistration );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_DEVICEREGISTRATION ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DEVICEREGISTRATION, TEMPLATE_MODIFY_DEVICEREGISTRATION, model );
    }

    /**
     * Process the change form of a deviceregistration
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_DEVICEREGISTRATION )
    public String doModifyDeviceRegistration( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _deviceregistration, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_DEVICEREGISTRATION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _deviceregistration, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DEVICEREGISTRATION, PARAMETER_ID_DEVICEREGISTRATION, _deviceregistration.getId( ) );
        }

        DeviceRegistrationHome.update( _deviceregistration );
        addInfo( INFO_DEVICEREGISTRATION_UPDATED, getLocale( ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_DEVICEREGISTRATIONS );
    }
}
