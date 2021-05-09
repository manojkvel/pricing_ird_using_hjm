package cqfportal.shared;

import java.io.Serializable;

public class Caplet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Caplet() {} //to ensure GWT rpc serialization works
	
	public double maturity;
	public boolean isCaplet;
	public double strike;
	public double refTenor;		
	
	public String toString() {
		return "Caplet/Floorlet - maturity = " + maturity +
				" isCaplet = " + isCaplet + 
				" strike = " + strike +
				" refTenor = " + refTenor;
	}
	
	public double getPayoff(double rateAtMaturity) {
		
		double payoff;
		
		if (isCaplet)
			payoff = Math.max(rateAtMaturity - strike, 0);
		else {
			payoff = Math.max(strike - rateAtMaturity, 0);
		}
		
		return payoff;
	}
}
