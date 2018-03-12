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

package swp.bibjsf.presentation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import swp.bibcommon.BusinessObject;
import swp.bibjsf.businesslogic.BusinessObjectHandler;
import swp.bibjsf.exception.DataSourceException;
import swp.bibjsf.exception.NoSuchBusinessObjectExistsException;
import swp.bibjsf.utils.Constraint;
import swp.bibjsf.utils.Messages;
import swp.bibjsf.utils.OrderBy;
import swp.bibjsf.utils.Reflection;

/**
 * A model for data tables for business objects.
 * 
 * @author koschke
 * 
 * @param <Element>
 *            a business object (e.g., reader or book)
 */
public class TableDataModel<Element extends BusinessObject> extends
		LazyDataModel<Element> {

	/*
	 * Note: This class does not have any own memory of elements. It will always
	 * query the data base. Having a cache of elements brings the risk of data
	 * redundancy.
	 */

	/**
	 * Filter die von Anfang an gelten
	 */
	private Map<String, String[]> initiallyFilters = new HashMap<String, String[]>();

	/**
	 * Globaler Filter über alle Attribute
	 */
	private String globalFilter = null;

	/**
	 * Gibt an, ob der globale Filter verwendet werden soll
	 */
	private boolean useGlobalFilter = false;
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(TableDataModel.class);

	/**
	 * Unique number for serialization.
	 */
	private static final long serialVersionUID = 939650263357576256L;

	/**
	 * The business handler that takes care of the service requests.
	 */
	private final BusinessObjectHandler<Element> handler;

	/**
	 * Constructor.
	 * 
	 * @throws DataSourceException
	 *             thrown in case of problems with the data source
	 */
	public TableDataModel(BusinessObjectHandler<Element> newHandler)
			throws DataSourceException {
		this.handler = newHandler;
		// we must count all elements
		setRowCount(handler.getNumber(null));
	}

	/**
	 * Returns all elements in the range [from .. to] fulfilling the search
	 * criteria ordered by <code>order</code>. Sets the row count to the number
	 * of all elements fulfilling the search criteria not just those retrieved
	 * for the range [from .. to].
	 * 
	 * @param constraints
	 *            constraints that must be fulfilled
	 * @param from
	 *            lower bound of the range of fetched elements
	 * @param to
	 *            upper bound of the range of fetched elements
	 * @param order
	 *            ordering criteria
	 * 
	 * @throws DataSourceException
	 *             thrown in case of problems with the data source
	 * @throws NoSuchBusinessObjectExistsException
	 */
	private List<Element> retrieveRows(List<Constraint> constraints,
			final int from, final int to, List<OrderBy> order)
			throws DataSourceException, NoSuchBusinessObjectExistsException {

		List<Element> elements = handler.get(constraints, from, to, order);

		// totalRowCount needs to be provided so that paginator can display
		// itself
		// according to the logical number of rows to display.
		setRowCount(handler.getNumber(constraints));
		return elements;
	}

	/**
	 * Creates a list of constraints from the search field content.
	 * 
	 * @param filters
	 * @return list of constraints from the search field content
	 */
	private List<Constraint> toTableConstraints(Map<String, String> filters) {
		List<Constraint> constraints = new LinkedList<Constraint>();
		if (filters != null) {
			Class<?> clazz = handler.getPrototype().getClass();
			HashMap<String, Field> fields = Reflection.getTransitiveFields(
					new HashMap<String, Field>(), clazz);
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				final String key = entry.getKey();
				try {
					Field field = fields.get(key);
					if (field == null) {
						logger.error("No such field '" + key + "'");
					} else {
						logger.info(field.getType());
						if (field.getType().isAssignableFrom(String.class)) {
							constraints.add(new Constraint(key, "LIKE", "%"
									+ entry.getValue() + "%", "AND", null));
						} else {
							constraints.add(new Constraint(key, "=", entry
									.getValue(), "AND", null));

						}
					}
				} catch (SecurityException e) {
					logger.error("Must not access field '" + key + "' "
							+ e.getLocalizedMessage());
				}
			}
		}
		return constraints;
	}

	/**
	 * Erstellt eine Liste von COnstraints aus den intialen Filtern
	 * 
	 * @author Bernd Poppinga
	 * @param filters
	 *            Map mit Filtern ( Attribut, [Wert,Operator])
	 * @return Liste mit Constraints
	 */
	private List<Constraint> toInitiallyConstraints(
			Map<String, String[]> filters) {
		List<Constraint> constraints = new LinkedList<Constraint>();
		if (filters != null) {
			Class<?> clazz = handler.getPrototype().getClass();
			HashMap<String, Field> fields = Reflection.getTransitiveFields(
					new HashMap<String, Field>(), clazz);
			for (Map.Entry<String, String[]> entry : filters.entrySet()) {
				final String key = entry.getKey();
				try {
					Field field = fields.get(key);
					if (field == null) {
						logger.error("No such field '" + key + "'");
					} else {
						constraints.add(new Constraint(key,
								entry.getValue()[1], entry.getValue()[0],
								"AND", null));

					}
				} catch (SecurityException e) {
					logger.error("Must not access field '" + key + "' "
							+ e.getLocalizedMessage());
				}
			}
		}
		return constraints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.primefaces.model.LazyDataModel#load(int, int, java.lang.String,
	 * org.primefaces.model.SortOrder, java.util.Map)
	 * 
	 * Required for lazy loading. DataTable calls load whenever a paging,
	 * sorting or filtering occurs with the following parameters: - first:
	 * Offset of first data to start from - pageSize: Number of data to load -
	 * sortField: Name of sort field (e.g. "model" for sortBy="#{....model}") -
	 * sortOrder: SortOrder enum. - filter: Filter map with field name as key
	 * (e.g. "model" for filterBy="#{....model}") and value.
	 * 
	 * Note: there is a variant load operation with the signature: List<Element>
	 * load(int first, int pageSize, String sortField, SortOrder sortOrder,
	 * Map<String,String> filters) That load operation is required in case of
	 * single ordering. The data table, however, offers multi-ordering (several
	 * fields can be used for the ordering at once).
	 */
	@Override
	public List<Element> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, String> filters) {
		logger.debug("ReaderDataModel.load() [multi]: " + first + " "
				+ pageSize);

		try {
			List<Constraint> constraints = new ArrayList<>();
			// Filter aus der Tabelle zu Constraints hinzufügen
			if (filters != null && !filters.isEmpty())
				constraints.addAll(toTableConstraints(filters));

			// Initiale Filter zu Constraints hinzufügen
			if (initiallyFilters != null && !initiallyFilters.isEmpty())
				constraints.addAll(toInitiallyConstraints(initiallyFilters));

			// Globale Filter zu Constraints hinzufügen
			if (useGlobalFilter && globalFilter != null
					&& !globalFilter.isEmpty()) {
				constraints.addAll(createGlobalFilterConstraint());
			}
			return retrieveRows(constraints, first, first + pageSize - 1,
					toOrdering(multiSortMeta));
		} catch (DataSourceException | NoSuchBusinessObjectExistsException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Failure in Data Source", e.getLocalizedMessage()));
			return null;
		}
	}

	/**
	 * Returns the ordering criteria obtained passed here from the multi-sort
	 * table.
	 * 
	 * @param multiSortMeta
	 * @return ordering criteria
	 */
	private List<OrderBy> toOrdering(List<SortMeta> multiSortMeta) {
		if (multiSortMeta != null) {
			List<OrderBy> result = new ArrayList<OrderBy>();
			for (SortMeta sm : multiSortMeta) {
				final SortOrder sortOrder = sm.getSortOrder();
				switch (sortOrder) {
				case ASCENDING:
					result.add(new OrderBy(sm.getSortField(), true));
					break;
				case DESCENDING:
					result.add(new OrderBy(sm.getSortField(), false));
					break;
				case UNSORTED: /* nothing to be done */
					break;
				}
			}
			return result;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.primefaces.model.SelectableDataModel#getRowData(java.lang.String)
	 * 
	 * Required for selection.
	 */
	@Override
	public Element getRowData(String rowKey) {
		try {
			final int key = Integer.parseInt(rowKey);
			return handler.get(key);

		} catch (DataSourceException e) {
			logger.error("no element found for row key '" + rowKey + "'");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages
							.get("noReadersFound"), e.getLocalizedMessage()));
			return null;
		} catch (Exception e) {
			logger.error("ReaderDataModel.getRowData(): Unexpected row key = '"
					+ rowKey + "' " + e.getMessage());
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.primefaces.model.SelectableDataModel#getRowKey(java.lang.Object)
	 * 
	 * Required for selection.
	 */
	@Override
	public Object getRowKey(Element reader) {
		return reader.getId();
	}

	/**
	 * Fügt einen initialen FIlter hinzu
	 * 
	 * @author Bernd Poppinga
	 * @param key
	 *            Attribut
	 * @param value
	 *            Parameter
	 * @param logic
	 *            logischer Operator
	 */
	public void addInitiallyFilter(String key, String value, String logic) {
		String[] array = { value, logic };
		initiallyFilters.put(key, array);
	}

	/**
	 * Setzt die initialen FIlter zurück
	 */
	private void resetInitiallyFilters() {
		initiallyFilters.clear();
	}

	/**
	 * Setzt den globalen Filter
	 * 
	 * @param globalFilter
	 *            globaler Filter
	 */
	public void setGlobalFilter(String globalFilter) {
		this.globalFilter = globalFilter;
	}

	/**
	 * Gibt den globalen Filter zurück
	 * 
	 * @return globaler Filter
	 */
	public String getGlobalFilter() {
		return globalFilter;
	}

	/**
	 * Erstellt eine Liste von Constraints aus den globalen Filtern. Dabei
	 * können höchstens 5 Filter benutzt werden.
	 * 
	 * @author Bernd Poppinga
	 * @return Liste mit Constraints
	 */
	private List<Constraint> createGlobalFilterConstraint() {
		List<Constraint> constraints = new LinkedList<Constraint>();
		if (globalFilter != null && globalFilter != "") {
			for (String filter : Arrays.copyOfRange(globalFilter.split(" "), 0,
					5)) {
				if (filter != null) {
					Class<?> clazz = handler.getPrototype().getClass();
					List<Constraint> constraint = new LinkedList<Constraint>();
					HashMap<String, Field> fields = Reflection
							.getTransitiveFields(new HashMap<String, Field>(),
									clazz);

					for (Entry<String, Field> field : fields.entrySet()) {
						if (!field.getKey().equals("UndefinedID")
								&& !field.getKey().equals("serialVersionUID")
								&& !field.getKey().equals("rating")
								&& !field.getKey().equals("ratingCount")
								&& !field.getKey().equals("dateOfPublication")
								&& !field.getKey().equals("ImageURL")
								&& !field.getKey().equals("MediumType")
								&& !field.getKey().equals("Price")) {
							{
								Field tmpfield = fields.get(field.getKey());
								logger.info(field.getKey());
								logger.info(field.getValue().getType());

								if (tmpfield.getType().isAssignableFrom(
										String.class)) {
									constraint.add(new Constraint(field
											.getKey(), "LIKE", "%" + filter
											+ "%", "OR", null));
									logger.info("like");

								}
							}

						}
					}
					constraint.get(0).setAttribute(
							"(" + constraint.get(0).getAttribute());
					constraint.get(constraint.size() - 1).setLogicalOperator(
							") AND");
					constraints.addAll(constraint);
				}
			}
			constraints.get(constraints.size() - 1).setLogicalOperator(")");
		}
		return constraints;

	}

	/**
	 * Aktiviert die globalen Filter
	 */
	public void activateGlobalFilter() {
		useGlobalFilter = true;
	}

	/**
	 * Setzt nur den globalen Filter zurück
	 */
	public void resetGlobalFilter() {
		globalFilter = null;
		useGlobalFilter = false;
	}

	/**
	 * Setzt alle Filter zurück
	 */
	public void reset() {
		resetGlobalFilter();
		resetInitiallyFilters();
	}

}