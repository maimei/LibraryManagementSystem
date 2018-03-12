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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Realizes a {@code RuntimeException} that is thrown by the rest service if there is a problem.
 *
 * @author K. Hölscher
 */
public class BibJSFException extends WebApplicationException {

    /**
     * The serialization-ID
     */
    private static final long serialVersionUID = 6551722944856466404L;

    /**
     * Creates a new exception without further information.
     */
    public BibJSFException() {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Creates a new exception with the transmitted messages as information.
     *
     * @param message
     *           the message
     */
    public BibJSFException(final String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message).type("text/plain").build());
    }

}
