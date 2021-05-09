package cqfportal.client;


import org.gwt.advanced.client.util.ThemeHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import cqfportal.client.apps.finalproject.CallibrationView;
import cqfportal.client.apps.finalproject.PCAView;
import cqfportal.client.apps.finalproject.PricingView;
import cqfportal.client.apps.finalproject.UploadView;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Container implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	final TabLayoutPanel masterTabPanel = new TabLayoutPanel(2.5, Unit.EM);
	
	public void onModuleLoad() {
				
				

		IApp cqfApp = getAppDetails("Manoj Veluchuri - CQF");					
		addAppToMasterTabPanel(cqfApp);
		//masterTabPanel.add(app.getMasterWidget(), app.getTitle());
					   
	    // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen for
	    // resize events on the window to ensure that its children are informed of
	    // possible size changes.
	    
		RootLayoutPanel rp = RootLayoutPanel.get();
		
	    //RootPanel.get("widgets").add(masterTabPanel);
	    //RootPanel.get("widgets").add(new HTML("html widget"));
	    rp.add(masterTabPanel);		   
	}
	
	private IApp getAppDetails(String appTitle) {	
		
		//for now there is only 1 app. 
		//in actual situation app details will be souced from a database or a config file
		IApp app = new DefaultApp()
			.setTitle("Manoj Veluchuri - CQF");
		
		//HJM 
		//HJM children		
		//IViewRenderer ren//
		FlowPanel fp = new FlowPanel();
		fp.add(new Label("hjm pricing"));
		
		CallibrationView cView = new CallibrationView();
		IView pricing = new DefaultView()
			.setTitle("Pricing")
			.setClickableText("Pricing")			
			.setApp(app)
			.setRenderer(new PricingView().setCallibrationView(cView))
			.renderFirstTime();				
					
//		IView callibration = new DefaultView()
//			.setTitle("Calibration")
//			.setClickableText("Calibration")			
//			.setApp(app)
//			.setRenderer(cView)
//			.renderFirstTime();
				
		IView pca = new DefaultView()
		.setTitle("PCA")
		.setClickableText("PCA")			
		.setApp(app)
		.setRenderer(new PCAView().setCallibrationView(cView)).renderFirstTime();	
		
		IView fileUpload = new DefaultView()
		.setTitle("Upload Data")
		.setClickableText("Upload Data")			
		.setApp(app)
		.setRenderer(new UploadView()).renderFirstTime();	
		
		IView hjm = new DefaultView()
			.setTitle("HJM")
			.setClickableText("HJM")
			.addChild(fileUpload)
			.addChild(pca)
			.addChild(pricing)
			//.addChild(callibration)
			.setApp(app);
						
		//uvm
	/*	IView digitalOption = new DefaultView()
			.setTitle("Digital Option")
			.setClickableText("Digital Option")			
			.setApp(app)
			.setRenderer(new DigitalOptionView())
			.renderFirstTime();
		
		IView uvm = new DefaultView()
			.setTitle("UVM")
			.setClickableText("UVM")
			.addChild(digitalOption);
							*/
		app.addView(hjm);
			//.addView(uvm);
							
		return app;
	}
	
	private void addAppToMasterTabPanel(IApp app) {					
		SplitLayoutPanel p = new SplitLayoutPanel();
		
		FlowPanel navPanel = getNavPanel(app.getAllViews());		
		
		FlowPanel mainPanel = new FlowPanel();
		app.setContainer(mainPanel);
		
		p.addWest(navPanel, 128);
		p.add(mainPanel);
		
		masterTabPanel.add(p, app.getTitle());																		
	}
	
	private FlowPanel getNavPanel(Iterable<IView> views) {
		FlowPanel p = new FlowPanel();
		
		for (final IView view : views) {
			if (view.hasChildren()) {							
				p.add(getDisclosurePanelForView(view));
			} else {								
				//p.add(clickableLabel);
				Anchor anchor = getClickableHyperlink(view);			
				
				p.add(anchor);
				p.add(new HTML()); //add new line
			}
		}
		
		return p;
	}
	
	private Anchor getClickableHyperlink(final IView view) {
		
		Anchor clickableText = new Anchor(view.getClickableText());		
				
		clickableText.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.show();
				
			}
		});
				
		return clickableText;
	}
			
	private DisclosurePanel getDisclosurePanelForView(IView view) {
		if (! view.hasChildren())
			return null;
		
		DisclosurePanel dp = new DisclosurePanel(view.getClickableText());
		dp.setOpen(true);
		FlowPanel fp = new FlowPanel();
		
		for (IView child : view.getAllChildren()) {
			//fp.add(getClickableLabel(child));
			fp.add(getClickableHyperlink(child));
			fp.add(new HTML()); //to introduce new line
		}
		
		dp.add(fp);						
		return dp;
	}
}
