package main;

public class payments {
	private int PaymentID;
	private int OrderID;
	private String PaymentDate;
	private String PaymentMethod;
	private double Amount;
	public payments(int paymentID, int orderID, String paymentDate, String paymentMethod, double amount) {
		super();
		PaymentID = paymentID;
		OrderID = orderID;
		PaymentDate = paymentDate;
		PaymentMethod = paymentMethod;
		Amount = amount;
	}
	public int getPaymentID() {
		return PaymentID;
	}
	public void setPaymentID(int paymentID) {
		PaymentID = paymentID;
	}
	public int getOrderID() {
		return OrderID;
	}
	public void setOrderID(int orderID) {
		OrderID = orderID;
	}
	public String getPaymentDate() {
		return PaymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		PaymentDate = paymentDate;
	}
	public String getPaymentMethod() {
		return PaymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	

}
