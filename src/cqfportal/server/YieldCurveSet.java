package cqfportal.server;

import java.util.ArrayList;

import cqfportal.shared.TermStructure;

public class YieldCurveSet {
	public ArrayList<Double> times = new ArrayList<Double>();
	public ArrayList<TermStructure> termStructures = new ArrayList<TermStructure>();
}
