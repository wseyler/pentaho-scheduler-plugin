/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2023 Hitachi Vantara. All rights reserved.
 */

package org.pentaho.mantle.client.dialogs.scheduling;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import org.pentaho.gwt.widgets.client.genericfile.GenericFileNameUtils;
import org.pentaho.gwt.widgets.client.utils.NameUtils;
import org.pentaho.gwt.widgets.client.utils.string.StringUtils;
import org.pentaho.mantle.client.environment.EnvironmentHelper;

/**
 * @author Rowell Belen
 */
public class OutputLocationUtils {

  private OutputLocationUtils() {
  }

  public static void validateOutputLocation( final String outputLocation, final Command successCallback,
                                             final Command errorCallback ) {

    if ( StringUtils.isEmpty( outputLocation ) ) {
      return;
    }

    final String url = EnvironmentHelper.getFullyQualifiedURL()
      + "plugin/scheduler-plugin/api/generic-files/folders/"
      + NameUtils.URLEncode( GenericFileNameUtils.encodePath( outputLocation ) );

    RequestBuilder builder = new RequestBuilder( RequestBuilder.HEAD, url );
    // This header is required to force Internet Explorer to not cache values from the GET response.
    builder.setHeader( "If-Modified-Since", "01 Jan 1970 00:00:00 GMT" );
    try {
      builder.sendRequest( null, new RequestCallback() {
        public void onError( Request request, Throwable exception ) {
          if ( errorCallback != null ) {
            errorCallback.execute();
          }
        }

        public void onResponseReceived( Request request, Response response ) {
          if ( response.getStatusCode() == Response.SC_NO_CONTENT ) {
            if ( successCallback != null ) {
              successCallback.execute();
            }
          } else {
            if ( errorCallback != null ) {
              errorCallback.execute();
            }
          }
        }
      } );
    } catch ( RequestException e ) {
      if ( errorCallback != null ) {
        errorCallback.execute();
      }
    }
  }

  public static String getPreviousLocationPath( String path ) {
    return GenericFileNameUtils.getParentPath( path );
  }
}
