package cqfportal.shared;

import java.io.Serializable;

public class IntRate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double tenor;
	public double rate;
	
	public IntRate() {}
	
	public IntRate(double tenor, double rate) {
		this.tenor = tenor;
		this.rate = rate;
	}
	
	public String toString() {
		return " [tenor = " + tenor + ", rate = " + rate + "]";
	}
}
