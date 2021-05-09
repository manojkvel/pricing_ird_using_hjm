package cqfportal.shared;

import java.io.Serializable;
import java.util.ArrayList;


public class TermStructure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ArrayList<IntRate> intRates = new ArrayList<IntRate>();
	
	//empty constructor just added for RPC to work
	public TermStructure() {}
		
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (IntRate element : intRates) {
			s.append(element.toString());
		}
		
		return s.toString();			
	}
	
	public Double getRateForTenor(double tenor) {
		for (IntRate intRate: intRates) {
			if (tenor == intRate.tenor)
				return intRate.rate;
		}
		
		//tenor not found		
		return null;
	}
}
