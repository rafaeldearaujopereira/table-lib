package com.rafpereira.samples.tablelib;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.rafpereira.samples.tablelib.model.Employee;
import com.rafpereira.samples.tablelib.ui.table.EmployeeTablePanel;

public class EmployeeSamplePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public EmployeeSamplePanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		EmployeeTablePanel employeesTable = new EmployeeTablePanel("Employees", 600, 300, true);
		this.add(employeesTable);
		
		ArrayList<Employee> employees = new ArrayList<>();
		employees.add(new Employee("Paul Smith", "1984-12-20", 1520, new BigDecimal(24000.50)));
		employees.add(new Employee("Anna Jones", "1984-12-20", 1520, new BigDecimal(37023.99)));
		employees.add(new Employee("Scott Tiger", "1970-01-01", 200, new BigDecimal(15000.00)));
		
		employeesTable.setItems(employees);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Employee Sample");
				frame.add(new EmployeeSamplePanel());
				frame.setResizable(false);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
	
}
