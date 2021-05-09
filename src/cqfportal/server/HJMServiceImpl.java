package cqfportal.server;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cqfportal.client.apps.finalproject.HJMService;
import cqfportal.shared.Caplet;
import cqfportal.shared.HJMFactorData;
import cqfportal.shared.ZCB;



public class HJMServiceImpl extends RemoteServiceServlet implements HJMService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = 
			Logger.getLogger(HJMServiceImpl.class.getName()); 

	@Override
	public double getZCBPrice(ZCB zcb, HJMFactorData calib, int numPaths) {						
			//System.out.println("In HJMServiceIMpl.getZCBPrice()");	
		long start = System.currentTimeMillis();
		
		HJMVols vols = new HJMVolGenerator().getHJMVols(calib);															
		HJMPricer pricer = new HJMPricer(calib, vols, numPaths);
						
		double price = pricer.getZCBPrice(zcb);
		
		long elapsed = System.currentTimeMillis() - start;
		logger.info("Pricing request- HJMInputs: " + calib + ", ZCB: " + 
				zcb + ", Num paths: " + numPaths + " Price [" + price + "] calculate in " + elapsed + " millis");
		
		return price;
	}

	@Override
	public double getCapletPrice(Caplet caplet, HJMFactorData calib, int numPaths) {
		long start = System.currentTimeMillis();
		
		HJMVols vols = new HJMVolGenerator().getHJMVols(calib);															
		HJMPricer pricer = new HJMPricer(calib, vols, numPaths);
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//double price = 0.23;
		double price = pricer.getCapletPrice(caplet);
		
		long elapsed = System.currentTimeMillis() - start;
		logger.info("Pricing request- HJMInputs: " + calib + ", Caplet/Floorlet: " + 
				caplet + ", Num paths: " + numPaths + " Price [" + price + "] calculate in " + elapsed + " millis");
		
		return price;
	}	
}
