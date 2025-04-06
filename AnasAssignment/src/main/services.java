package main;

public class services {
	private int ServiceID;
	private int CarID;
	private String ServiceDate;
	private String ServiceDescription;
	private double Cost;
	public services(int serviceID, int carID, String serviceDate, String serviceDescription, double cost) {
		super();
		ServiceID = serviceID;
		CarID = carID;
		ServiceDate = serviceDate;
		ServiceDescription = serviceDescription;
		Cost = cost;
	}
	public int getServiceID() {
		return ServiceID;
	}
	public void setServiceID(int serviceID) {
		ServiceID = serviceID;
	}
	public int getCarID() {
		return CarID;
	}
	public void setCarID(int carID) {
		CarID = carID;
	}
	public String getServiceDate() {
		return ServiceDate;
	}
	public void setServiceDate(String serviceDate) {
		ServiceDate = serviceDate;
	}
	public String getServiceDescription() {
		return ServiceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		ServiceDescription = serviceDescription;
	}
	public double getCost() {
		return Cost;
	}
	public void setCost(double cost) {
		Cost = cost;
	}
	

}
