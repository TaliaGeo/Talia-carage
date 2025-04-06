package main;

public class orders {
	private int OrderId;
	private int CarID;
	private int CustomerID;
	private int EmployeeID;
	private int Quantity;
	private Double TotalPrice;
	public orders(int orderId, int carID, int customerID, int employeeID, int quantity, Double totalPrice) {
		super();
		OrderId = orderId;
		CarID = carID;
		CustomerID = customerID;
		EmployeeID = employeeID;
		Quantity = quantity;
		TotalPrice = totalPrice;
	}
	public int getOrderId() {
		return OrderId;
	}
	public void setOrderId(int orderId) {
		OrderId = orderId;
	}
	public int getCarID() {
		return CarID;
	}
	public void setCarID(int carID) {
		CarID = carID;
	}
	public int getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(int customerID) {
		CustomerID = customerID;
	}
	public int getEmployeeID() {
		return EmployeeID;
	}
	public void setEmployeeID(int employeeID) {
		EmployeeID = employeeID;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public Double getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		TotalPrice = totalPrice;
	}
	

}
