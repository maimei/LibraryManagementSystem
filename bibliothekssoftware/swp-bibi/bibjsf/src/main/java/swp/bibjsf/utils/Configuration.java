/*
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

package swp.bibjsf.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * An interface to the configuration file that can be edited by the user
 * to configure system attributes such as data to be printed on book tags
 * or to set the Google API key.
 *
 * @author koschke
 *
 */
public class Configuration {

	protected static final Logger logger = Logger.getLogger(Configuration.class);

    /**
     * Name of the configuration file. It's a simple property file
     * having key value pairs such as:
     *    API_KEY="whateveryourkeyis"
     */
    private final static String config_filename = "bibi_config.properties";

    /**
     * Returns the value for given key in the configuration file.
     *
     * @return the value for key; empty string if there is no such key set.
     */
    public static String getKey(final String key) {
        if (key != null && !key.isEmpty()) {
            Properties configFile = new Properties();
            try {
                // This returns the parent-most classloader which has access to all resources.
                // The Class#getClassLoader() will only return the (child) classloader of the
                // class in question, which may not per se have access to the desired resource.
                // It will always work in environments with a single classloader, but not always
                // in environments with a complex hierarchy of classloaders like webapps.
                final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

                // The file must be located in /WEB-INF/classes. The /WEB-INF folder is not in
                // the root of the classpath. The /WEB-INF/classes folder is. So we need to load
                // the properties files relative to that, e.g.:
                //    classLoader.getResourceAsStream("/auth.properties");
                // If we opt for using the Thread#getContextClassLoader(), remove the leading /.
                final InputStream resourceAsStream = classLoader.getResourceAsStream(config_filename);
                if (resourceAsStream != null) {
                    configFile.load(resourceAsStream);
                    return configFile.getProperty(key);
                } else {
                    logger.error("Cannot open configuration file " + config_filename);
                    return "";
                }
            } catch (IOException e) {
            	logger.error("Cannot open configuration file " + config_filename + ": " + e.getLocalizedMessage());
                return "";
            }
        } else {
        	logger.debug("no key given");
        	return "";
        }
    }
}
