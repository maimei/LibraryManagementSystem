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

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 *
 * Provides utilities for Java reflection.
 *
 * @author koschke
 *
 */
public class Reflection {

    /**
     * Returns all fields of class type including those inherited from its transitive superclasses
     * except for those inherited from Object.
     *
     * @param fields where to add the fields
     * @param type the class whose fields are to be gathered
     * @return all fields of class type including those inherited from its transitive superclasses
     */
    public static HashMap<String, Field> getTransitiveFields(HashMap<String, Field> fields, Class<?> type) {
    	if (type != null && !type.equals(Object.class)) {
    		for (Field field: type.getDeclaredFields()) {
    			fields.put(field.getName(), field);
    		}
    		Class<?> superclass = type.getSuperclass();
    		if (superclass != null && !superclass.equals(Object.class)) {
    			fields = getTransitiveFields(fields, type.getSuperclass());
    		}
    	}
        return fields;
    }
}
