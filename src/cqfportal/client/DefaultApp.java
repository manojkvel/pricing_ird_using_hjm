package cqfportal.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;

public class DefaultApp implements IApp {
	
	private String title = "Default App";
	final private ArrayList<IView> children = new ArrayList<IView>();
	private FlowPanel rootFlowPanel;
	
	@Override
	public DefaultApp setTitle(String title) {
		this.title = title;
		return this;
	}
	
	@Override
	public DefaultApp addView(IView view) {
		children.add(view);
		return this;
	}
	
	@Override
	public String getTitle() {		
		return title;
	}

	@Override
	public Iterable<IView> getAllViews() {	
		return children;
//		ArrayList<IVanashreeView> children = new ArrayList<IVanashreeView>();
//		
//		//hjm
//		ArrayList<IVanashreeView> hjmChildren = new ArrayList<IVanashreeView>();
//		hjmChildren.add(new VanashreeDefaultView("Pricing"));
//		hjmChildren.add(new VanashreeDefaultView("Callibration"));
//		
//		IVanashreeView hjm = new VanashreeDefaultView("HJM", hjmChildren);
//		
//		//uvm
//		IVanashreeView uvm = new VanashreeDefaultView("UVM");
//		
//		children.add(hjm);
//		children.add(uvm);
//				
//		return children;
	}

	@Override
	public IApp setContent(FlowPanel fp) {
		rootFlowPanel.clear();
		rootFlowPanel.add(fp);
		
		return this;
	}

	@Override
	public IApp setContainer(FlowPanel fp) {
		this.rootFlowPanel = fp;
		return this;
	}	
}
