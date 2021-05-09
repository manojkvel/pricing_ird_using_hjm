package cqfportal.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;

public class DefaultView implements IView  {
	private String title;
	private String clickableText;
	final private ArrayList<IView> children = new ArrayList<IView>();
	private IViewRenderer renderer;
	private FlowPanel rootFlowPanel = new FlowPanel();
	private IApp app;
	
	public DefaultView() {
		//TODO move to css
		rootFlowPanel.getElement().getStyle().setProperty("margin", "15px");
	}
		
	@Override
	public IView setTitle(String title) {
		this.title = title;
		return this;
	}		
	
	@Override
	public DefaultView setRenderer(IViewRenderer renderer) {
		this.renderer = renderer;
		return this;
	}		
	
	@Override
	public Iterable<IView> getAllChildren() {		
		return children;
	}
	
	@Override
	public String getClickableText() {		
		return clickableText;		
	}

	@Override
	public boolean hasChildren() {
		return (!children.isEmpty());
	}
	
	@Override
	public String getTitle() {		
		return title;
	}

	@Override
	public IView setClickableText(String clickableText) {
		this.clickableText = clickableText;
		return this;
	}

	@Override
	public IView addChild(IView view) {
		children.add(view);
		return this;
	}

	@Override
	public FlowPanel getRootFlowPanel() {
		return rootFlowPanel;
	}

	@Override
	public IViewRenderer getRenderer() {		
		return renderer;
	}
	
	@Override
	public IApp getApp() {
		return app;
	}

	@Override
	public IView setApp(IApp app) {
		this.app = app;
		return this;
	}

	@Override
	public IView show() {
		app.setContent(rootFlowPanel);
		return this;
	}

	@Override
	public IView renderFirstTime() {
		renderer.renderFirstTime(rootFlowPanel);
		return this;
	}
}
