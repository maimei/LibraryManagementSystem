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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
 * Interface to the messages files consisting of the user messages
 * in different languages (internationalization). All user messages
 * except for internal error and logging messages should be
 * externalized to messages files.
 *
 * @author koschke
 *
 */
public class Messages {

    private static final String bundleName = "messages"; // "swp.bibjsf.properties.messages";

	/**
     * Logger of this class
     */
    private static Logger logger = Logger.getLogger(Messages.class);

    /**
     * Resource bundle containing user messages.
     */
    private static ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
    //ResourceBundle.getBundle("swp.bibjsf.properties.messages",
    //        Locale.GERMAN, Thread.currentThread().getContextClassLoader());

    /**
     * Returns message for key retrieved from the bundle messages (localization).
     *
     * @param key key for message
     * @return message for key
     */
    public static synchronized String get(String key) {
        if (key == null) {
            return "";
        }
    	if (bundle == null) {
    		bundle = ResourceBundle.getBundle(bundleName);
    	}
    	if (bundle == null) {
    		logger.error("no resource bundle found");
    		return "";
    	}
    	try {
    	    return bundle.getString(key);
    	} catch (MissingResourceException e1) {
    	    logger.error("no message for " + key);
    	    return "";
    	} catch (ClassCastException e2) {
    	    logger.error("message for " + key + " is no string");
    	    return "";
    	}
    }
}
