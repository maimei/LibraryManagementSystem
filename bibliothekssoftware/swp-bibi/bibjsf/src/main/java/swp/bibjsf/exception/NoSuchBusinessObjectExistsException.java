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
 * Is thrown if the sought medium does not exist in the data base.
 *
 * @author K. Hölscher
 */
public class NoSuchBusinessObjectExistsException extends Exception {

    /**
     * Unique serialization ID.
     */
    private static final long serialVersionUID = 8429484316933252389L;

    /**
     * Creates a new exception without further information.
     */
    public NoSuchBusinessObjectExistsException() {
        super();
    }

    /**
     * Creates a new exception with the transmitted messages as information.
     *
     * @param message
     *           message
     */
    public NoSuchBusinessObjectExistsException(final String message) {
        super(message);
    }

}
