package cqfportal.shared;

import java.io.Serializable;

public class PCAModel implements Serializable{

	private double[][] yieldCurveData;
	private double[] eigenValues;
	private double[][] eigenVectors;
	private double[][] covMatrix;
	private double[][] prominentEVs;
	private double[] prominentLambdas;
	private HJMFactorData inputs;
	
	
	public double[] getProminentLambdas() {
		return prominentLambdas;
	}

	public void setProminentLambdas(double[] prominentLambdas) {
		this.prominentLambdas = prominentLambdas;
	}

	public HJMFactorData getInputs() {
		return inputs;
	}

	public void setInputs(HJMFactorData inputs) {
		this.inputs = inputs;
	}

	public double[] getProminentLamdas() {
		return prominentLambdas;
	}

	public void setProminentLamdas(double[] prominentLamdas) {
		this.prominentLambdas = prominentLamdas;
	}

	public double[][] getProminentEVs() {
		return prominentEVs;
	}

	public void setProminentEVs(double[][] prominentEVs) {
		this.prominentEVs = prominentEVs;
	}

	private String[] headers;
	private int noOfRows;
	private int noOfCols;

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public int getNoOfCols() {
		return noOfCols;
	}

	public void setNoOfCols(int noOfCols) {
		this.noOfCols = noOfCols;
	}

	public double[] getEigenValues() {
		return eigenValues;
	}

	public void setEigenValues(double[] eigenValues) {
		this.eigenValues = eigenValues;
	}

	public double[][] getEigenVectors() {
		return eigenVectors;
	}

	public void setEigenVectors(double[][] eigenVectors) {
		this.eigenVectors = eigenVectors;
	}

	public double[][] getCovMatrix() {
		return covMatrix;
	}

	public void setCovMatrix(double[][] covMatrix) {
		this.covMatrix = covMatrix;
	}

	public double[][] getYieldCurveData() {
		return yieldCurveData;
	}

	public void setYieldCurveData(double[][] yieldCurveData) {
		this.yieldCurveData = yieldCurveData;
	}
	
	
	
	
	
}
