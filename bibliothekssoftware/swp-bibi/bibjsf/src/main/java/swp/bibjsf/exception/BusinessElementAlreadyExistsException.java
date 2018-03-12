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

package swp.bibjsf.exception;

/**
 * Thrown when a user is to be added but already exists in the database.
 *
 * @author koschke
 *
 */
public class BusinessElementAlreadyExistsException extends Exception {

    /**
     * Unique serialization ID.
     */
    private static final long serialVersionUID = -7189811776865267657L;

    /**
     * Exception without message.
     */
    public BusinessElementAlreadyExistsException() {
        super();
    }

    /**
     * Exception with message.
     *
     * @param message
     *            additional message to describe context of failure
     */
    public BusinessElementAlreadyExistsException(final String message) {
        super(message);
    }
}
