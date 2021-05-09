package cqfportal.client.apps.finalproject;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cqfportal.shared.HJMFactorData;
import cqfportal.shared.PCAModel;

@RemoteServiceRelativePath("pcaService")
public interface PCAService extends RemoteService {

	HJMFactorData getPCAResults();
	
	PCAModel performPCA(String fileName);
}
