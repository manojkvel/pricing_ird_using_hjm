package cqfportal.client.apps.finalproject;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cqfportal.shared.Caplet;
import cqfportal.shared.HJMFactorData;
import cqfportal.shared.ZCB;

public interface HJMServiceAsync {

	void getZCBPrice(ZCB zcb, HJMFactorData inputs, int numPaths,
			AsyncCallback<Double> callback);

	void getCapletPrice(Caplet caplet, HJMFactorData inputs, int numPaths,
			AsyncCallback<Double> callback);

}
