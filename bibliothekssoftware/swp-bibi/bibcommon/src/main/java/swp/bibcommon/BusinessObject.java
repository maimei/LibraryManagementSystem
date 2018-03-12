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

package swp.bibcommon;

/**
 * Super class of all business objects with unique ID.
 *
 * @author koschke
 *
 */
public abstract class BusinessObject {

    /* ************************************
     * Unique ID of a reader (mandatory)
     * ***********************************/

    /**
     * The undefined ID.
     */
    public static final int UndefinedID = -1;

    /**
     * Unique instance ID for a reader set by the data base.
     */
    protected int id = UndefinedID;

    /**
     * Returns the unique ID.
     *
     * @return the unique id
     */
    public final int getId() {
        return id;
    }

    /**
     * True if reader has a valid ID.
     *
     * @return true if reader has a valid ID
     */
    public final boolean hasId() {
        return id != UndefinedID;
    }

    /**
     * Sets the unique ID.
     *
     * @param id the id to set
     */
    public final void setId(final int id) {
        this.id = id;
    }

}
