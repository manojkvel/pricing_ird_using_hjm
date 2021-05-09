package cqfportal.server;

import java.util.ArrayList;

import cqfportal.shared.HJMFactorData;
import cqfportal.shared.IntRate;

public class HJMVolGenerator {
			
	public HJMVols getHJMVols(HJMFactorData calib) {
		
		HJMVols vols = new HJMVols();
		
		vols.vol1 =  calib.vol1Constant;
		
		//populate arraylists for vol2, vol3 and m
		int numTenors = calib.day0TermStructure.intRates.size();
		
		ArrayList<IntRate> intRates = calib.day0TermStructure.intRates;
		
		for (int i = 0; i < numTenors; i++) {
			vols.vol2.add(getVol2(intRates.get(i).tenor, calib));
		}
		
		for (int i = 0; i < numTenors; i++) {
			vols.vol3.add(getVol3(intRates.get(i).tenor, calib));
		}
		
		for (int i = 0; i < numTenors; i++) {
			vols.m.add(getM(intRates.get(i).tenor, calib));
		}
		
		return vols;
	}

	
	private double getVol2(double tenor, HJMFactorData calib) {
		return calib.vol2Constant + tenor * calib.vol2CoeffOfT +
				tenor * tenor * calib.vol2CoeffOfTSquare +
				tenor * tenor * tenor * calib.vol2CoeffOfTCube;
	}
	
	private double getVol3(double tenor, HJMFactorData calib) {
		return calib.vol3Constant + tenor * calib.vol3CoeffOfT +
				tenor * tenor * calib.vol3CoeffOfTSquare +
				tenor * tenor * tenor * calib.vol3CoeffOfTCube;
	}	
	
	private double getM(double tau, HJMFactorData calib) {
		if (0 == tau)
			return 0;
		
		double m1;
		
		double dtau = 0.01;		
		int N = (int) (tau / dtau);
		dtau = tau / N;
		
		//trapezium rule to compute M1
		double vol1 = calib.vol1Constant;
		m1 = 0.5 * vol1;		
		for (int i = 1; i < N -1; i++) {
			m1 = m1 + vol1;
		}
		m1 = m1 + 0.5* vol1;
		m1 = vol1 * dtau * m1;
		
		//m2
		double m2 = 0.5 * getVol2(0, calib);
		for (int i = 1; i < N - 1; i++) {
			m2 = m2 + getVol2(i * dtau, calib);
		}
		m2 = m2 + 0.5 * getVol2(tau, calib);
		m2 = getVol2(tau, calib) * dtau * m2;
		
		//m3
		double m3 = 0.5 * getVol3(0, calib);
		for (int i = 1; i < N - 1; i++) {
			m3 = m3 + getVol3(i * dtau, calib);
		}
		m3 = m3 + 0.5 * getVol3(tau, calib);
		m3 = getVol3(tau, calib) * dtau * m3;
		
		return m1 + m2 + m3;		
	}	
}
