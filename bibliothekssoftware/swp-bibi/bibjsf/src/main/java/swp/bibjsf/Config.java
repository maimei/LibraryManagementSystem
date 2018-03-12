/**
 * Copyright (c) 2013 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package swp.bibjsf;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;


/**
 * Provides configuration of this web application. Currently, only logging
 * is configured.
 *
 * @author D. Lüdemann, Karsten Hölscher
 */
public class Config extends HttpServlet {

    /**
     * Unique ID for serialization.
     */
    private static final long serialVersionUID = 3009804349219074150L;

    /**
     * Initializes this application using the given servlet configuration.
     * This method is called by the Application Container with appropriate
     * parameters if the Container starts or the application is deployed.
     *
     * @param config Servlet configuration
     * @throws ServletException in case of problems with the servlet configuration
     */
    @Override
    public final void init(final ServletConfig config) throws ServletException {
        initLogging(config);
        super.init(config);
        

       
    }

    /**
     * Initializes logging. A servlet configuration is needed to locate
     * the configuration file.
     *
     * @param config Servlet configuration
     */
    private void initLogging(ServletConfig config) {
        String log4jLocation = config
                .getInitParameter("log4j-properties-location");

        ServletContext sc = config.getServletContext();

        if (log4jLocation == null) {
            System.err
                    .println("No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        } else {
            String webAppPath = sc.getRealPath("/");
            String log4jProp = webAppPath + log4jLocation;
            File f = new File(log4jProp);
            if (f.exists()) {
                System.out.println("Initializing log4j with: " + log4jProp);
                PropertyConfigurator.configure(log4jProp);
            } else {
                System.err
                        .println(log4jProp
                                + " file not found, so initializing log4j with BasicConfigurator");
                BasicConfigurator.configure();
            }
        }
    }
}