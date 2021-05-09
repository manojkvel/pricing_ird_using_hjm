package cqfportal.shared;

import java.io.Serializable;

public class HJMFactorData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//vol principal components
	public double vol1Constant;
	
	public double vol2Constant;
	public double vol2CoeffOfT;
	public double vol2CoeffOfTSquare;
	public double vol2CoeffOfTCube;
	
	public double vol3Constant;
	public double vol3CoeffOfT;
	public double vol3CoeffOfTSquare;
	public double vol3CoeffOfTCube;
	
	//term structure of day 0 interest rates		
	public TermStructure day0TermStructure = new TermStructure();
	
	public String[] maturities;
	
	public HJMFactorData() {}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		//vol principal components
		s.append("vol1Constant = " + vol1Constant);
		
		s.append(", vol2Constant = " + vol2Constant);
		s.append(", vol2CoeffOfT = " + vol2CoeffOfT);
		s.append(", vol2CoeffOfTSquare = " + vol2CoeffOfTSquare);
		s.append(", vol2CoeffOfTCube = " + vol2CoeffOfTCube);
		
		s.append(", vol3Constant = " + vol3Constant);
		s.append(", vol3CoeffOfT = " + vol3CoeffOfT);
		s.append(", vol3CoeffOfTSquare = " + vol3CoeffOfTSquare);
		s.append(", vol3CoeffOfTCube = " + vol3CoeffOfTCube);
		s.append(" term structure: " + day0TermStructure);
		
		
		return s.toString();		
	}
}
