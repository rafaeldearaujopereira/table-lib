package com.rafpereira.samples.tablelib.ui.table;

import java.util.ArrayList;

import javax.swing.table.TableColumnModel;

import com.rafpereira.samples.tablelib.model.Employee;
import com.rafpereira.util.ui.table.JTablePanel;

/**
 * A sample of the JTablePanel usage.
 * @author rafaeldearaujopereira
 * @since 1.0
 * @version 1.0
 */
public class EmployeeTablePanel extends JTablePanel<Employee> {

	/**
	 * Default constructor.
	 * @param title The title for the table panel (null, if you don't want a title).
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 * @param showQuantity <b>true</b> when it must show a line at the end of the panel with the quantity of items.
	 */
	public EmployeeTablePanel(String title, int width, int height, boolean showQuantity) {
		super(title, width, height, showQuantity);
		setQuantityDescription("Qty of employees: ");
	}

	/**
	 * Recommended when serialized (JPanel).
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Implements the list of names.
	 */
	@Override
	protected String[] getColumnsNames() {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Name");
		columns.add("Birthday Date");
		columns.add("Contacts on LinkedIn");
		columns.add("Salary");
		return toStringArray(columns);
	}

	/**
	 * Implements the way to present each column value.
	 */
	@Override
	protected Object[] getRow(Employee emp) {
		ArrayList<Object> fields = new ArrayList<>();
		fields.add(emp.getName());
		fields.add((emp.getBirthdayDate() != null) ? dateFormat.format(emp.getBirthdayDate()) : "");
		fields.add(integerFormat.format(emp.getLinkedinFriends()));
		fields.add(currencyFormat.format(emp.getSalary()));
		return fields.toArray();
	}


	/**
	 * Configure some table view options (column alignment, width, cell renderer).
	 */
	@Override
	protected void configureView() {
		super.configureView();
		TableColumnModel cm = getTable().getColumnModel();
		cm.getColumn(1).setCellRenderer(alignedCenter);
		cm.getColumn(1).setMinWidth(100);
		cm.getColumn(1).setMaxWidth(100);
		cm.getColumn(2).setCellRenderer(alignedRight);
		cm.getColumn(3).setCellRenderer(alignedRight);
	}
	
}
