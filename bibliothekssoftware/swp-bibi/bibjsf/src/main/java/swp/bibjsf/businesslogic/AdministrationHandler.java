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

package swp.bibjsf.businesslogic;

import javax.naming.NamingException;

import swp.bibjsf.exception.DataSourceException;

/**
 * Handler for administrative tasks for the data source.
 * 
 * @author koschke, Helena Meißner
 */
public class AdministrationHandler extends BusinessHandler {

	/**
	 * Unique ID for serialization.
	 */
	private static final long serialVersionUID = 8051928717924656362L;

	/** geschützer Handler für AdministationHandler.
	 * @throws DataSourceException
	 * @throws NamingException
	 */
	protected AdministrationHandler() throws DataSourceException,
			NamingException {
		super();
	}

	/**
	 * Returns the only instance of AdministrationHandler (singleton).
	 */
	private static volatile AdministrationHandler instance;

	/**
	 * Returns the only instance of AdministrationHandler (singleton).
	 * 
	 * @return the only instance of AdministrationHandler (singleton); may be
	 *         null
	 * @throws DataSourceException
	 *             thrown in case of problems with the data source
	 */
	public static synchronized AdministrationHandler getInstance()
			throws DataSourceException {

		if (instance == null) {
			try {
				instance = new AdministrationHandler();
			} catch (Exception e) {
				throw new DataSourceException(e.getMessage());
			}
		}
		return instance;
	}

	/**
	 * Re-sets the database by dropping all tables. Use with care.
	 * 
	 * @throws DataSourceException
	 */
	public final void resetDB() throws DataSourceException {
		persistence.reset();
	}

	/**
	 * Creates a backup of the database.
	 * 
	 * @throws DataSourceException
	 */
	public final void backupDB() throws DataSourceException {
		persistence.backup();
	}

	/**
	 * Restores the database content using a previous backup if one exists (in
	 * which case all current data are lost). If none exists, an exception is
	 * thrown.
	 * 
	 * @throws DataSourceException
	 */
	public final void restoreDB(String path) throws DataSourceException {
		persistence.restore(path);
	}
}