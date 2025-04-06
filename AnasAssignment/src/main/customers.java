package main;

public class customers {
	private int CustomerID;
	  private String FirstName;
	private  String LastName;
	 private String Email;
	 private String phone;
	private  String Address;
	 private String City;
	 private String State;
	 private String ZipCode;
	public customers(int customerID, String firstName, String lastName, String email, String phone, String address,
			String city, String state, String zipCode) {
		super();
		CustomerID = customerID;
		FirstName = firstName;
		LastName = lastName;
		Email = email;
		this.phone = phone;
		Address = address;
		City = city;
		State = state;
		ZipCode = zipCode;
	}
	public int getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(int customerID) {
		CustomerID = customerID;
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
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}

}
