package cqfportal.client;

import com.google.gwt.user.client.ui.FlowPanel;

public interface IApp {	
	public String getTitle();
	public IApp setTitle(String title);
	
	public Iterable<IView> getAllViews();	
	public IApp addView(IView view);
	
	public IApp setContainer(FlowPanel container);
	public IApp setContent(FlowPanel content);
}

