
public class Car {
	
	protected int year;
	protected String key;
	protected String make;
	public Car(String key,String make,int year) {
		this.key = key;
		this.make = make;
		this.year = year;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public Car(Car c) {
        this.key = c.key;
        this.make = c.make;
        this.year = c.year;
    }
	public String toString() {
		return "License:" + key + " Make:" + make + " Year:" + year + "\n";
	}
	
}
