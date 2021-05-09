package cqfportal.client.apps.finalproject;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cqfportal.shared.HJMFactorData;
import cqfportal.shared.PCAModel;

public interface PCAServiceAsync {

	void getPCAResults(AsyncCallback<HJMFactorData> callback);

	void performPCA(String fileName, AsyncCallback<PCAModel> callback);

}
