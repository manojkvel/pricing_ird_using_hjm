package cqfportal.shared;

import java.io.Serializable;

public class ZCB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public double maturity;
	public String currency;
	
	public String toString() {
		return "maturity = " + maturity;
	}
	
	public ZCB() {}
}
