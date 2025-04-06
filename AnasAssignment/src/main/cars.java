package main;

public class cars {
	private int CarID;
	private String Make;
	private String Model;
	private int Year;
	private double Price;
	private int Stock;
	private String VIN;
	public cars(int carID, String make, String model, int year, double price, int stock, String vIN) {
		super();
		CarID = carID;
		Make = make;
		Model = model;
		Year = year;
		Price = price;
		Stock = stock;
		VIN = vIN;
	}
	public int getCarID() {
		return CarID;
	}
	public void setCarID(int carID) {
		CarID = carID;
	}
	public String getMake() {
		return Make;
	}
	public void setMake(String make) {
		Make = make;
	}
	public String getModel() {
		return Model;
	}
	public void setModel(String model) {
		Model = model;
	}
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getStock() {
		return Stock;
	}
	public void setStock(int stock) {
		Stock = stock;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	

}
