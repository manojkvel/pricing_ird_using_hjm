package cqfportal.client;

import com.google.gwt.user.client.ui.FlowPanel;

public interface IView {
	public String getTitle();
	public IView setTitle(String title);
	
	public Iterable<IView> getAllChildren();	
	public boolean hasChildren();
	public IView addChild(IView view);
		
	public String getClickableText();
	public IView setClickableText(String text);
	
	public FlowPanel getRootFlowPanel();	
	
	public IViewRenderer getRenderer();				
	public IView setRenderer(IViewRenderer renderer);
	
	public IApp getApp();
	public IView setApp(IApp app);
	
	public IView show();
	public IView renderFirstTime();
}