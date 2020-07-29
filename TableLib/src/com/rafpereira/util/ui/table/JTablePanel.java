package com.rafpereira.util.ui.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.rafpereira.util.ui.filter.Filter;
import com.rafpereira.util.ui.filter.FilterableComponent;
import com.rafpereira.util.ui.filter.FilterableObject;

/**
 * Abstraction of a table panel, that implements some standard methods for table panels.<br><br>
 * A <b>"table panel"</b> encapsulates a JTable (Swing) in a scroll panel, and simplify the usage, focusing on the definition of the columns and
 * how to present those columns. <br><br>
 * It also uses the filter-lib to apply filters to its items.<br>
 * If a filter is applied externally (the items are set already filtered, e.g. from a WHERE-clause query), it is possible to set
 * that a filter is being used, using the setItems(items, boolean) method.
 * 
 * @author rafaeldearaujopereira
 * @since 1.0
 * @version 1.0
 * @param <T> Type of the objects shown in the table.
 */
public abstract class JTablePanel<T> extends JPanel implements FilterableComponent {

	/**
	 * Recommended when serialized (JPanel).
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Items assigned to the table.
	 */
	private Collection<T> items;

	/**
	 * The table encapsulated in the table panel.
	 */
	private JTable table;

	/**
	 * A title for the table.
	 */
	private JLabel labelTitle;

	/**
	 * The text to describe the quantity of items.
	 */
	private JLabel labelQuantityDescription = new JLabel("");

	/**
	 * The quantity of items.
	 */
	private JLabel labelQuantity = new JLabel("0");

	/**
	 * The height of this panel.
	 */
	private int height = 0;

	/**
	 * The width of this panel.
	 */
	private int width = 0;

	/**
	 * The panel that informs that the table is currently filtered.
	 */
	private JPanel panelFilter = null;

	/**
	 * The name of the filter.
	 * (This information isn't been used)
	 */
	private JLabel labelFilterName = null;

	/**
	 * The list of filters assigned to the table.
	 */
	private ArrayList<Filter> filters = new ArrayList<Filter>();

	/**
	 * Timer for auto updating the table.
	 */
	private Timer updateTimer = null;

	/**
	 * Default cell render to align content to left orientation.
	 */
	protected static DefaultTableCellRenderer alignedLeft = new DefaultTableCellRenderer();

	/**
	 * Default cell render to align content to center orientation.
	 */
	protected static DefaultTableCellRenderer alignedCenter = new DefaultTableCellRenderer();

	/**
	 * Default cell render to align content to right orientation.
	 */
	protected static DefaultTableCellRenderer alignedRight = new DefaultTableCellRenderer();

	/**
	 * Default formatter for integer values.
	 */
	protected static DecimalFormat integerFormat = new DecimalFormat();

	/**
	 * Default formatter for currency values (without the currency symbol).
	 */
	protected static NumberFormat currencyFormat = new DecimalFormat();

	/**
	 * Default formatter for date values.
	 */
	protected static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

	static {
		alignedLeft.setHorizontalAlignment(SwingConstants.LEFT);
		alignedCenter.setHorizontalAlignment(SwingConstants.CENTER);
		alignedRight.setHorizontalAlignment(SwingConstants.RIGHT);

		integerFormat.setGroupingUsed(true);
		integerFormat.setDecimalSeparatorAlwaysShown(false);

		DecimalFormatSymbols dfs = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols();
		dfs.setCurrencySymbol("");
	}
	
	/**
	 * Default constructor.<br><br>
	 * Defines the title, dimension and show quantity (boolean).
	 * @param title Title of the table (optional)
	 * @param width Width of the panel.
	 * @param height Height of the panel.
	 * @param showQuantity <b>true</b> when it must show a line with the quantity.
	 */
	public JTablePanel(String title, int width, int height, boolean showQuantity) {
		this.setLayout(new BoxLayout(this,  BoxLayout.Y_AXIS));
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.width = width;
		this.height = height;
		
		if (title != null) {
			labelTitle = new JLabel(title);
			JPanel panelTitle = new JPanel();
			panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.X_AXIS));
			panelTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
			panelTitle.add(new JLabel("  "));
			panelTitle.add(labelTitle);
			panelTitle.add(new JLabel("  "));
			
			panelFilter = new JPanel();
			panelFilter.setLayout(new BoxLayout(panelFilter, BoxLayout.X_AXIS));
			panelFilter.setBorder(new LineBorder(Color.BLACK));
			panelFilter.setBackground(Color.GREEN);
			labelFilterName = new JLabel(" ");
			labelFilterName.setFont(new Font("Lucida", Font.BOLD, 8));
			panelFilter.add(labelFilterName);
			panelFilter.setMinimumSize(new Dimension(12, 12));
			panelFilter.setMaximumSize(new Dimension(12, 12));
			panelFilter.setVisible(false);
			panelTitle.add(panelFilter);
			
			this.add(panelTitle);
		}
		
		table = getTable();
		JScrollPane scrollPane = new JScrollPane(table);
		
		this.add(scrollPane);
		
		if (showQuantity) {
			JPanel panelQuantity = new JPanel();
			panelQuantity.setLayout(new BoxLayout(panelQuantity, BoxLayout.X_AXIS));
			panelQuantity.add(labelQuantityDescription);
			panelQuantity.add(labelQuantity);
			
			this.add(panelQuantity);
		}
	}
	
	/**
	 * Gets the list of columns' names. Must be implemented by each table panel.
	 * @return List of columns' names, as String array.
	 */
	protected abstract String[] getColumnsNames();

	/**
	 * Gets the list of values for each object. Must be implemented by each table panel, and must have the same length of the columns' names list.
	 * @param obj Object from items.
	 * @return List of values for each object.
	 */
	protected abstract Object[] getRow(T obj);

	/**
	 * Checks if the component contains a specific filter.
	 * @param filter filter to be checked.
	 * @return <b>true</b> when the component has the filter.
	 */
	@Override
	public boolean containsFilter(Filter filter) {
		return this.filters.contains(filter);
	}

	/**
	 * Clears the current list of filters added to the component.
	 */
	@Override
	public void clearFilters() {
		for (Filter filter : filters) {
			filter.removeSubscriber(this);
		}
		this.filters.clear();
		panelFilter.setVisible(false);
		updateView();
	}

	/**
	 * Adds a filter to the component.
	 * @param filter filter to be added.
	 */
	@Override
	public void addFilter(Filter filter) {
		filters.add(filter);
		filter.addSubscriber(this);
		if (filter.isActive()) {
			panelFilter.setVisible(true);
			updateView();
		}
	}

	/**
	 * Gets the list of items already filtered.
	 * @return filtered items.
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<T> getFilteredItems() {
		Collection<FilterableObject> filteredItems = (Collection<FilterableObject>) items;
		boolean filtered = false;
		for (Filter filter : filters) {
			if (filter.isActive() && filter.getFilterObject() != null) {
				panelFilter.setVisible(true);
				filteredItems = filter.applyFilter((Collection<FilterableObject>) filteredItems);
				filtered = true;
			}
		}
		this.panelFilter.setVisible(filtered);
		return (Collection<T>) filteredItems;
	}

	/**
	 * Gets the default table model (using the list of columns' names).
	 * @return Table Model.
	 */
	protected DefaultTableModel getTableModel() {
		DefaultTableModel tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableModel.setColumnIdentifiers(getColumnsNames());
		return tableModel;
	}

	/**
	 * Gets the table font. Must override in order to change the table font.
	 * @return Table font.
	 */
	protected Font getTableFont() {
		return new Font("Lucida", 0, 10);
	}

	/**
	 * Configures the view of the table. Must override to configure as you wish.
	 */
	protected void configureView() {
		Font tableFont = getTableFont();
		table.setFont(tableFont);
		table.setRowHeight(20);
		table.setPreferredScrollableViewportSize(new Dimension(width, height));
	}
	
	/**
	 * Gets the table component.
	 * @return Table component.
	 */
	private JTable getTable() {
		if (table == null) {
			table = new JTable(getTableModel());
			configureView();
			table.setFillsViewportHeight(true);
		}
		return table;
	}

	/**
	 * Applies filters and update the view of the table.
	 */
	@Override
	public void updateView() {
		if (this.items == null) {
			this.items = new ArrayList<T>();
		}
		Collection<T> filteredItems = getFilteredItems();
		labelQuantity.setText(integerFormat.format(filteredItems.size()));
		Object[][] rows = new Object[filteredItems.size()][];
		int iItem = 0;
		for (T item : filteredItems) {
			rows[iItem] = getRow(item);
			iItem++;
		}
		DefaultTableModel tableModel = (DefaultTableModel) getTable().getModel();
		while (table.getRowCount() > 0) {
			tableModel.removeRow(0);
		}
		for (int iRow = 0; iRow < rows.length; iRow++) {
			tableModel.addRow(rows[iRow]);
		}
		tableModel.fireTableDataChanged();
	}

	/**
	 * Define the description label for the quantity.
	 * @param description Label for the description.
	 */
	public void setLabelQuantityDescription(JLabel description) {
		labelQuantityDescription = description;
	}
	
	/**
	 * Utility to ease the construction of String arrays from ArrayList of strings. 
	 * @param columns List of Strings.
	 * @return String array.
	 */
	protected String[] toStringArray(ArrayList<String> columns) {
		String[] columnsArr = new String[columns.size()];
		for (int iCol = 0; iCol < columnsArr.length; iCol++) {
			columnsArr[iCol] = columns.get(iCol);
		}
		return columnsArr;
	}
	
	/**
	 * Defines the set of items, and provokes the update of the table view. It also defines the "filtered" status.
	 * @param newItems New set of items.
	 * @param preFiltered <b>true</b> when the table must show as filtered (when the items were filtered before been provided).
	 */
	public void setItems(Collection<T> newItems, boolean preFiltered) {
		setItems(newItems);
		panelFilter.setVisible(preFiltered);
	}

	/**
	 * Defines the set of items, and provokes the update of the table view.
	 * @param newItems New set of items.
	 */
	public void setItems(Collection<T> newItems) {
		this.items = newItems;
		updateView();
	}
	
	/**
	 * Defines the auto update interval for the table, in milliseconds.<br>
	 * When it is defined a interval (> 0), it starts the updating timer.
	 * @param milliseconds Interval in milliseconds.
	 */
	public void setUpdatingInterval(int milliseconds) {
		if (updateTimer != null) {
			updateTimer.stop();
		}
		if (milliseconds == 0) {
			updateTimer = null;
		} else {
			updateTimer = new Timer(milliseconds, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateView();
				}
			});
			updateTimer.start();
		}
	}

	/**
	 * Defines the font to be used in the title, and quantity information (when enabled).
	 * @param font Font. 
	 */
	public void setDescriptionFont(Font font) {
		if (labelTitle != null) {
			labelTitle.setFont(font);
		}
		if (labelQuantityDescription != null) {
			labelQuantityDescription.setFont(font);
		}
		if (labelQuantity != null) {
			labelQuantity.setFont(font);
		}
	}
	
}
