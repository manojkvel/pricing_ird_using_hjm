package cqfportal.client.apps.finalproject;

import java.util.ArrayList;
import java.util.List;

public class BatchResponseCollector<T> {
	private int numBatches;
	
	private List<T> responses = new ArrayList<T>();
	
	public BatchResponseCollector(int nBatches) {
		numBatches = nBatches;
	}
	
	public void recordResponse(T response) {
		responses.add(response);		
	}
	
	public double getFractionComplete() {
		return (responses.size() + 0.0)/numBatches;
	}
	
	public List<T> getAllResponsesIfCompleteElseNull() {
		if (responses.size() == numBatches) 
			return responses;
		
		//not yet complete
		return null;
	}
			
//	public String getPriceOrStatus() {
//		String priceOrStatus;
//		
//		NumberFormat percentFormat = NumberFormat.getPercentFormat();		
//		if (numBatches == responses.size()) {			
//			//all requests completed
//			double price = getAvgPrice();
//			
//			priceOrStatus = NumberFormat.getFormat("#.00%").format(price);
//			//priceOrStatus = percentFormat.format(price);
//		} else {
//			priceOrStatus = "Running Simulation... " + percentFormat.format((responses.size() + 0.0)/numBatches);
//		}
//		
//		return priceOrStatus;
//	}
	
	public boolean isComplete() {
		boolean complete = (responses.size() == numBatches);
		return complete;
	}
	
//	private double getAvgPrice() {
//		double sum = 0.0;
//		
//		for (Double price : responses) {
//			sum += price;
//		}
//		
//		double avgPrice = sum/responses.size();
//		
//		return avgPrice;
//	}
	
}
