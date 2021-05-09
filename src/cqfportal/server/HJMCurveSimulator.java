package cqfportal.server;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import cqfportal.shared.HJMFactorData;
import cqfportal.shared.IntRate;
import cqfportal.shared.TermStructure;

public class HJMCurveSimulator {	
	private Random rand = new Random(System.nanoTime());
	
	private static Logger logger = 
			Logger.getLogger(HJMCurveSimulator.class.getName()); 
		
	public YieldCurveSet getCurveSet(HJMFactorData calib, HJMVols vols, double maturity) {
		
		//just get for 3 tenors - 0, 0.5 and 1
		YieldCurveSet ycs = new YieldCurveSet();
		
		ycs.times.add(0.0);
		ycs.termStructures.add(calib.day0TermStructure);
		ArrayList<IntRate> prevIntRates = calib.day0TermStructure.intRates;	
		
//		//System.out.println("simulating curve set -- day0Termstructure: " 
//				+ calib.day0TermStructure);
				
		final double dt = 0.01;
		double t = dt;
						int mat=0;
		while (t <= maturity) {
			//while(mat<calib.maturities.length){
				t=Double.parseDouble(calib.maturities[mat]);
			ArrayList<Double> slopes = new ArrayList<Double>();			
			//System.out.println("SLopes"+slopes);
			for (int i = 0; i < prevIntRates.size(); i++) {
				double slope;
				if (i != prevIntRates.size() -1) {
					//System.out.println("Rate(i+1) "+(prevIntRates.get(i + 1).rate)+" Rate(i):"+prevIntRates.get(i).rate);
					//System.out.println("Tenor(i+1) "+(prevIntRates.get(i + 1).tenor)+" Tenor(i):"+prevIntRates.get(i).tenor);
					double numer= (prevIntRates.get(i + 1).rate - prevIntRates.get(i).rate);
					double denom = (prevIntRates.get(i + 1).tenor - prevIntRates.get(i).tenor);
					//System.out.println("Numer/Denom"+numer+"/"+denom);
					//slope = numer/denom;
					slope = numer /denom ;				
				} else {
					//System.out.println("Rate(i) "+(prevIntRates.get(i).rate)+" Rate(i-1):"+prevIntRates.get(i-1).rate);
					//System.out.println("Tenor(i) "+(prevIntRates.get(i).tenor)+" Tenor(i-1):"+prevIntRates.get(i-1).tenor);
					double numer= (prevIntRates.get(i).rate - prevIntRates.get(i-1).rate);
					double denom = (prevIntRates.get(i).tenor - prevIntRates.get(i-1).tenor);
					//System.out.println("Numer/Denom"+numer+"/"+denom);
					//slope = (prevIntRates.get(i).rate - prevIntRates.get(i - 1).rate);
					slope = numer / denom;				
				}
				slopes.add(slope);
			}
			//System.out.println("SLopes"+slopes);
			
			TermStructure ts = new TermStructure();
			////System.out.println("prevIntRates.get(i).rate + vols.m.get(i) * dt + (vols.vol1 * rand() + vols.vol2.get(i) * rand() + vols.vol3.get(i) * rand()) * Math.sqrt(dt) + slopes.get(i) * d");
			////System.out.println("Term Structure"+prevIntRates.get(0).rate 
//						+","+ vols.m.get(0) +","+ dt
//						+","+ (vols.vol1 +","+ rand() +","+ vols.vol2.get(0) +","+ rand() +","+ vols.vol3.get(0) +","+ rand()) +","+ Math.sqrt(dt)
//						+","+ slopes.get(0) * dt);
			for (int i = 0; i < prevIntRates.size(); i++) {
				double tenor = prevIntRates.get(i).tenor;
				double rate = prevIntRates.get(i).rate 
						+ vols.m.get(i) * dt
						+ (vols.vol1 * rand() + vols.vol2.get(i) * rand() + vols.vol3.get(i) * rand()) * Math.sqrt(dt)
						+ slopes.get(i) * dt;
				ts.intRates.add(new IntRate(tenor, rate));
			}
			
			ycs.times.add(t);
			ycs.termStructures.add(ts);
			
			prevIntRates = ts.intRates;
			
			t = t + dt;
			mat++;
		}
				
		return ycs;		
	}		
	
	private double rand() {	
		
		double r = rand.nextGaussian();		
		
//		double r = rand.nextDouble() + rand.nextDouble() + rand.nextDouble() +
//				rand.nextDouble() + rand.nextDouble() + rand.nextDouble() + 
//				rand.nextDouble() + rand.nextDouble() + rand.nextDouble() + 
//				rand.nextDouble() + rand.nextDouble() + rand.nextDouble() - 6;
		
		return r;
	}	
}
