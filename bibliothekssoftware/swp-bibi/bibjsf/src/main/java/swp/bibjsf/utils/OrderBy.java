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

/**
 * An ordering specification for fields of an entity. Abstracts from
 * SQL order by specification.
 */
package swp.bibjsf.utils;

/**
 * @author koschke
 *
 */
public class OrderBy {

    /**
     * The attribute by which an order should be established.
     */
    private String attribute;

    /**
     * If true, the order is ascending, otherwise descending.
     */
    private boolean ascending;

    /**
     * Constructor for ascending orderings.
     *
     * @param attribute attribute by which an order should be established
     * @param ascending ordering specification
     */
    public OrderBy(String attribute, boolean ascending) {
        this.attribute = attribute;
        this.ascending = ascending;
    }

    /**
     * Constructor for ascending orderings.
     *
     * @param attribute attribute by which an order should be established
     */
    public OrderBy(String attribute) {
        this.attribute = attribute;
        this.ascending = true;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

}
