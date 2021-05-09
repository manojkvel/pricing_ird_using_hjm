package cqfportal.server;

import java.util.logging.Logger;

import cqfportal.shared.Caplet;
import cqfportal.shared.HJMFactorData;
import cqfportal.shared.TermStructure;
import cqfportal.shared.ZCB;

public class HJMPricer {
	private HJMFactorData calib;
	private HJMVols vols;	
	private int numPaths;
	
	private static Logger logger = 
			Logger.getLogger(HJMPricer.class.getName()); 
			
	public HJMPricer(HJMFactorData c, HJMVols v, int nPaths) {
		calib = c;
		vols = v;		
		numPaths = nPaths;
	}
				
	private double getDF(YieldCurveSet ycs) {
		double dt = ycs.times.get(1) - ycs.times.get(0);
		
		double price = 1.0;
		
		for (int i = 0; i < ycs.termStructures.size(); i++) {
			double rate = ycs.termStructures.get(i).intRates.get(0).rate;
			
			if (rate < 0) 
				logger.severe("negative rate - " + rate);
			price = price * Math.exp(-rate * dt);
		}
		
		return price;
	}
	
	public double getZCBPrice(ZCB zcb) {
		
		double sum = 0.0;
		for (int i = 0; i < numPaths; i++) {
			double price = getZCBPriceSinglePath(zcb);
			//System.out.println("ZCB Price :"+price+" for Path :"+i);
			sum += price;
		}
		
		double avgPrice = sum/numPaths;
		return avgPrice;
	}	
	
	public double getCapletPrice(Caplet caplet) {
		
		double sum = 0.0;
		for (int i = 0; i < numPaths; i++) {
			double price = getCapletPriceSinglePath(caplet);
			sum += price;
		}
		
		double avgPrice = sum/numPaths;
		return avgPrice;
	}	
	
	private double getZCBPriceSinglePath(ZCB zcb) {
				
		YieldCurveSet ycs = new HJMCurveSimulator().getCurveSet(calib, vols, zcb.maturity);		
		double df = getDF(ycs);				
		return df;						
	}
	
	private double getCapletPriceSinglePath(Caplet caplet) {
		
		YieldCurveSet ycs = new HJMCurveSimulator().getCurveSet(calib, vols, caplet.maturity);				
		
		TermStructure maturityTS = ycs.termStructures.get(ycs.termStructures.size() - 1);
		
		Double rateAtMaturity = maturityTS.getRateForTenor(caplet.refTenor);
		if (null == rateAtMaturity) {
			throw new RuntimeException("Yield curve does not have rate for tenor [" + caplet.refTenor + "]");
		}
		
		double payoff = caplet.getPayoff(rateAtMaturity);
		
		double price = 0;
		if (0 != payoff)
			price = payoff * getDF(ycs);
		
		return price;						
	}	
}
