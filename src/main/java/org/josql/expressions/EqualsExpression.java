/*
 * Copyright 2004-2007 Gary Bentley 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.josql.expressions;

import org.josql.Query;
import org.josql.QueryExecutionException;

import org.josql.internal.SimpleUtils;

/**
 * This class represents an "=" or "!=" expression. This class also provides the
 * ability for the developer to prefix the "=" or "!=" with "$" to indicate that
 * a case-insensitive comparison should be made, in which case the LHS and RHS
 * are converted to strings first.
 */
public class EqualsExpression extends BinaryExpression {

	private boolean not = false;

	public boolean ignoreCase = false;

	public void setIgnoreCase(boolean v) {

		this.ignoreCase = v;

	}

	public boolean isIgnoreCase() {

		return this.ignoreCase;

	}

	public boolean isNot() {

		return this.not;

	}

	public void setNot(boolean v) {

		this.not = v;

	}

	/**
	 * Return whether this expression evaluates to <code>true</code>.
	 * 
	 * @param o
	 *            The current object to perform the expression on.
	 * @param q
	 *            The Query object.
	 * @return <code>true</code> if the expression evaluates to
	 *         <code>true</code>, <code>false</code> otherwise.
	 * @throws QueryExecutionException
	 *             If the expression cannot be evaluated.
	 */
	public boolean isTrue(Object o, Query q) throws QueryExecutionException {

		// Get the value for the lhs.
		Object l = null;

		try {

			l = this.left.getValue(o, q);

		} catch (Exception e) {

			throw new QueryExecutionException(
			        "Unable to get value for LHS of expression: " + this, e);

		}

		Object r = null;

		try {

			r = this.right.getValue(o, q);

		} catch (Exception e) {

			throw new QueryExecutionException(
			        "Unable to get value for RHS of expression: " + this, e);

		}

		if ((l == null) && (r == null)) {

			// Debatable about whether 2 nulls make a true...
			if (this.not) {

				return false;

			}

			return true;

		}

		if ((l == null) || (r == null)) {

			if (this.not) {

				return true;

			}

			// One of them is null, can't do any checking...
			return false;

		}

		// If we are here then both are NOT NULL.
		return SimpleUtils.equals(l, r, this.ignoreCase, this.not,
		        expectedValueType);
	}

	/**
	 * Return a string version of the expression. In the form:
	 * {@link Expression#toString() Expression} [ $ ] [ ! ] =
	 * {@link Expression#toString() Expression}
	 * 
	 * @return The string version.
	 */
	public String toString() {

		String pred = "=";

		if (this.not) {

			pred = "!=";

		}

		if (this.ignoreCase) {

			pred = "$" + pred;

		}

		if (this.isBracketed()) {

			return "(" + this.left.toString() + " " + pred + " "
			        + this.right.toString() + ")";

		}

		return this.left.toString() + " " + pred + " " + this.right.toString();

	}

}
