package controller;

public class DataSet {

	private double balance = 0.0;
	private boolean hasStock = false;
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public boolean hasStock() {
		return hasStock;
	}
	
	public void setHasStock(boolean hasStock) {
		this.hasStock = hasStock;
	}
	
	public void reset() {
		this.hasStock = false;
		this.balance = 0.0;
	}	
}
