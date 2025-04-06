package main;

public class employees {
private int EmployeeID;
private String FirstName;
private String LastName;
private String position;
private double Salary;
private String HireDate;
public employees(int employeeID, String firstName, String lastName, String position, double salary, String hireDate) {
	super();
	EmployeeID = employeeID;
	FirstName = firstName;
	LastName = lastName;
	this.position = position;
	Salary = salary;
	HireDate = hireDate;
}
public int getEmployeeID() {
	return EmployeeID;
}
public void setEmployeeID(int employeeID) {
	EmployeeID = employeeID;
}
public String getFirstName() {
	return FirstName;
}
public void setFirstName(String firstName) {
	FirstName = firstName;
}
public String getLastName() {
	return LastName;
}
public void setLastName(String lastName) {
	LastName = lastName;
}
public String getPosition() {
	return position;
}
public void setPosition(String position) {
	this.position = position;
}
public double getSalary() {
	return Salary;
}
public void setSalary(double salary) {
	Salary = salary;
}
public String getHireDate() {
	return HireDate;
}
public void setHireDate(String hireDate) {
	HireDate = hireDate;
}

}
