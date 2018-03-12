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

/**
 * A constraint for a data base query. Its purpose is to abstract from the
 * underlying constraint language of the data source (e.g., SQL).
 * 
 * @author koschke
 * 
 */
public class Constraint {

	/**
	 * The type of an attribute for which a constraint can be specified. We
	 * currently separate only two types of attribute comparisons: = and like.
	 * For String, like or = will be used; for Integer only = can be used. For
	 * all others, = will be used, too.
	 * 
	 */
	public enum AttributeType {
		INTEGER, STRING;
	}

	/**
	 * The attribute of the element upon which the constraint is to be placed.
	 */
	private String attribute;

	/**
	 * The property the attribute must fulfill.
	 */
	private String property;

	/**
	 * The type of an attribute.
	 */
	private AttributeType type;

/**
     * Operator für den Constraint, z.B. 'like', '=', '>=', '<' usw.
     */
	private String operator;

	/**
	 * logischer Operator für den Constraint, entweder 'AND' oder 'OR'.
	 */
	private String logicalOperator;

	/**
	 * Returns the type of this attribute.
	 * 
	 * @return type of this attribute
	 */
	public final AttributeType getType() {
		return type;
	}

	/**
	 * Sets type of this attribute.
	 * 
	 * @param type
	 *            new type of this attribute
	 */
	public final void setType(final AttributeType type) {
		this.type = type;
	}

	/**
	 * Returns the attribute of the element upon which the constraint is to be
	 * placed.
	 * 
	 * @return attribute of the element upon which the constraint is to be
	 *         placed
	 */
	public final String getAttribute() {
		return attribute;
	}

	/**
	 * Returns the property the attribute must fulfill.
	 * 
	 * @return property the attribute must fulfill.
	 */
	public final String getProperty() {
		return property;
	}

	/**
	 * Liefert den Operator des Constraints.
	 * 
	 * @return der Operator.
	 */
	public final String getOperator() {
		return operator;
	}

	/**
	 * Liefert den logischen Operator des Constraints.
	 * 
	 * @return der logische Operator.
	 */
	public final String getLogicalOperator() {
		return logicalOperator;
	}
	
	/**
	 * Setzt den logischen Operator des Constraints.
	 * 
	 * @return der logische Operator.
	 */
	public final void setLogicalOperator(final String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	/**
	 * Constructor for constraint.
	 * 
	 * @param attribute
	 *            the attribute of the element upon which the constraint is to
	 *            be placed
	 * @param property
	 *            the property the attribute must fulfill
	 * @param type
	 *            the attribute type
	 */
	public Constraint(final String attribute, final String operator,
			final String property, final String logicalOperator,
			final AttributeType type) {
		this.attribute = attribute;
		this.operator = operator;
		this.property = property;
		this.logicalOperator = logicalOperator;
		this.type = type;
	}

	@Override
	public final String toString() {
		return "(" + attribute + ", " + operator + " " + property + " "
				+ logicalOperator + " " + type + ")";
	}
	
	public void setAttribute( String attribute) {
		this.attribute = attribute;
	}
	
	public void setProperty( String property) {
		this.property = property;
	}
}
