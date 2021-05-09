package cqfportal.client.apps.finalproject;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;

import cqfportal.client.IViewRenderer;
import cqfportal.shared.Caplet;
import cqfportal.shared.ZCB;

public class PricingView implements IViewRenderer {
					
	private CallibrationView callibrationView;
	private HJMServiceAsync hjmPriceService = GWT.create(HJMService.class);
	public static boolean pcaPerformed=false;
	final int PATHS_PER_BATCH = 50;
	final int DAYS_IN_YEAR = 365;
			
	public PricingView setCallibrationView(CallibrationView c) {
		callibrationView = c;
		return this;
	}			
	
	@Override
	public void renderFirstTime(FlowPanel rootFlowPanel) {
		//rootFlowPanel.add(new HTML("pricing view"));
		
		
		final FlowPanel zcbPanel = getZCBPanel();
		//zcbPanel.setWidth("1000px");
		
		final FlowPanel capletPanel = getCapletPanel();
		
		final HorizontalPanel pricingHorizontalPanel = new HorizontalPanel();
		
		pricingHorizontalPanel.setSpacing(40);
		pricingHorizontalPanel.add(zcbPanel);
		pricingHorizontalPanel.add(capletPanel);
		pricingHorizontalPanel.setBorderWidth(1);
		//rootFlowPanel.setWidth("100px");
				
		rootFlowPanel.add(pricingHorizontalPanel);								
	}
		
	private FlowPanel getZCBPanel() {
		
		final HTML panelTitle = 
				new HTML("<b>Zero Coupon Bond</b><hr>");
		
		final FlowPanel flowPanel = new FlowPanel();		
		flowPanel.add(panelTitle);
		
		final FlexTable paramsFlexTable = new FlexTable();
		paramsFlexTable.setText(0, 0, "Maturity (max: Today + 5 years)");
		
		final DateBox zcbMaturity;		
		zcbMaturity = getMaturityDateBox();
		paramsFlexTable.setWidget(0, 1, zcbMaturity);
		//zcbMaturity.setText("1");
		paramsFlexTable.setText(1, 0, "Currency");
		
		ListBox currencyDropDownBox = new ListBox();
		currencyDropDownBox.addItem("GBP");
		currencyDropDownBox.addItem("EUR");
		currencyDropDownBox.setEnabled(false);
		paramsFlexTable.setWidget(1, 1, currencyDropDownBox);
		
		paramsFlexTable.setText(2, 0, "No. of paths");
		
		final ListBox zcbNumPaths;				
		zcbNumPaths = getNumPathsListBox();
		zcbNumPaths.setWidth("150px");
		paramsFlexTable.setWidget(2, 1, zcbNumPaths);
		paramsFlexTable.setText(3, 0, "Price (% of Notional)");
		final TextBox priceTextBox = new TextBox();
		priceTextBox.setReadOnly(true);
		paramsFlexTable.setWidget(3, 1, priceTextBox);
		final Button zcbPriceButton;
		zcbPriceButton = new Button("Get Price");
		
		zcbPriceButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {	
				if(!pcaPerformed){
					Window.alert("Please perform PCA to use Pricing.");
					return;
				}
																							
				int numPaths = getZCBNumPaths();
				if (numPaths < PATHS_PER_BATCH)
					numPaths = PATHS_PER_BATCH;
				
				int numBatches = numPaths/PATHS_PER_BATCH;
				
				final BatchResponseCollector<Double> batch = new BatchResponseCollector<Double>(numBatches);
																
				enableZCBInputs(false);
				
				AsyncCallback<Double> callback = new AsyncCallback<Double>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Double result) {
						batch.recordResponse(result);
						
						if (batch.isComplete()) {
							
							List<Double> responses = batch.getAllResponsesIfCompleteElseNull();
							
							double sum = 0.0;
							int i=0;
							for (Double response : responses) {
								
								GWT.log("Batch "+i+" price = "+response);
								i++;
								sum += response;
							}
							
							double avg = sum/responses.size();
							priceTextBox.setText(NumberFormat.getFormat("#.0000%").format(avg));
							
							enableZCBInputs(true);
						} else {
							//show % complete
							double fraction = batch.getFractionComplete();
							priceTextBox.setText("Pricing... (" + NumberFormat.getPercentFormat().format(fraction) + ")");
						}																
					}																				
					
					
				};								
					
				priceTextBox.setText("Pricing...");
				for (int i = 0; i < numBatches; i++) 
				{
					hjmPriceService.getZCBPrice(getZCB(), 
							callibrationView.getHJMInputs(), PATHS_PER_BATCH,  
							callback);	
					
					
				}						
			}		
			
			private void enableZCBInputs(boolean enable) {
				zcbMaturity.setEnabled(enable);
				zcbNumPaths.setEnabled(enable);
				zcbPriceButton.setEnabled(enable);					
			}
			
			private ZCB getZCB() {
				ZCB zcb = new ZCB();
								
				zcb.maturity = (CalendarUtil.getDaysBetween(new Date(), zcbMaturity.getValue()) + 0.0)/DAYS_IN_YEAR;
						
				
				return zcb;
			}
			
			private int getZCBNumPaths() {
				int i = zcbNumPaths.getSelectedIndex();
				int numPaths = Integer.parseInt(zcbNumPaths.getItemText(i));
				
				return numPaths;
			}
		});
		
		paramsFlexTable.setWidget(4, 1, zcbPriceButton);	
		
		Label status = new Label("");
		status.addStyleName("transientStatus");
		paramsFlexTable.setWidget(5, 1, status);
										
		flowPanel.add(paramsFlexTable);	
		
		return flowPanel;
	}

	private ListBox getNumPathsListBox() {
		ListBox lb = new ListBox();
		
		lb.addItem("100");
		lb.addItem("500");
		lb.addItem("1000");
		lb.addItem("5000");
		lb.addItem("10000");
		
		lb.setSelectedIndex(2);
		
		return lb;
	}
	
	private DateBox getMaturityDateBox() {
		DateBox dateBox = new DateBox();
		
		//set format to DD-MMM-YY
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MMM-yyyy");
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		
		//set value to today + 1 year	
		Date date = new Date();
		CalendarUtil.addMonthsToDate(date, 12);
		dateBox.setValue(date);			
						
		return dateBox;
	}
					
	private FlowPanel getCapletPanel() {
			
		final HTML panelTitle = new HTML("<b>Caplet/Floorlet</b><hr>");
		
		final FlowPanel flowPanel = new FlowPanel();		
		flowPanel.add(panelTitle);
		
		//maturity
		final FlexTable paramsFlexTable = new FlexTable();
		paramsFlexTable.setText(0, 0, "Maturity (max: Today + 5 years)");
		final DateBox maturity;		
		maturity = getMaturityDateBox();
		paramsFlexTable.setWidget(0, 1, maturity);		
		
		//currency
		paramsFlexTable.setText(1, 0, "Currency");
		final ListBox currencyDropDownBox = new ListBox();
		currencyDropDownBox.addItem("GBP");
		currencyDropDownBox.addItem("EUR");
		currencyDropDownBox.setEnabled(false);
		paramsFlexTable.setWidget(1, 1, currencyDropDownBox);
		
		//type
		paramsFlexTable.setText(2, 0, "Type");
		final ListBox type = new ListBox();
		type.addItem("Caplet");
		type.addItem("Floorlet");
		paramsFlexTable.setWidget(2, 1, type);
		
		//strike
		paramsFlexTable.setText(3, 0, "Strike % (e.g: 3.5%)");
		final TextBox capletStrike = new TextBox();
		capletStrike.setText("4%");
		paramsFlexTable.setWidget(3, 1, capletStrike);
		
		//reference rate
		paramsFlexTable.setText(4, 0, "Reference Rate");
		final ListBox refListBox = new ListBox();
		refListBox.addItem("6 month");
		refListBox.addItem("12 month");
		paramsFlexTable.setWidget(4, 1, refListBox);
						
		//no. of paths
		paramsFlexTable.setText(5, 0, "No. of paths");
		final ListBox capletNumPaths;		
		capletNumPaths = getNumPathsListBox();		
		paramsFlexTable.setWidget(5, 1, capletNumPaths);
		
		//price
		paramsFlexTable.setText(6, 0, "Price (% of Notional)");
		final TextBox priceTextBox = new TextBox();
		priceTextBox.setReadOnly(true);
		paramsFlexTable.setWidget(6, 1, priceTextBox);
		
		final Button capletPriceButton;
		capletPriceButton = new Button("Get Price");
		
		capletPriceButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {		
				
				if(!pcaPerformed){
					Window.alert("Please perform PCA to use Pricing.");
					return;
				}
																																										
				int numPaths = getCapletNumPaths();
				if (numPaths < PATHS_PER_BATCH)
					numPaths = PATHS_PER_BATCH;
				
				int numBatches = numPaths/PATHS_PER_BATCH;
				
				final BatchResponseCollector<Double> batch = new BatchResponseCollector<Double>(numBatches);
																
				enableCapletInputs(false);
				
				AsyncCallback<Double> callback = new AsyncCallback<Double>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Double result) {
						batch.recordResponse(result);
						
						if (batch.isComplete()) {
							
							List<Double> responses = batch.getAllResponsesIfCompleteElseNull();
							
							double sum = 0.0;
							for (Double response : responses) {
								sum += response;
							}
							
							double avg = sum/responses.size();
							priceTextBox.setText(NumberFormat.getFormat("#.0000%").format(avg));
							
							enableCapletInputs(true);
						} else {
							//show % complete
							double fraction = batch.getFractionComplete();
							priceTextBox.setText("Pricing... (" + NumberFormat.getPercentFormat().format(fraction) + ")");
						}																
					}					
				};
					
				priceTextBox.setText("Pricing...");
				for (int i = 0; i < numBatches; i++) {
					hjmPriceService.getCapletPrice(getCaplet(), 
							callibrationView.getHJMInputs(), 
							PATHS_PER_BATCH, callback);										
				}						
			}

			private Caplet getCaplet() {
				Caplet caplet = new Caplet();
								
				caplet.maturity = (CalendarUtil.getDaysBetween(new Date(), maturity.getValue()) + 0.0)/DAYS_IN_YEAR;
				caplet.isCaplet = (type.getItemText(type.getSelectedIndex()).equals("Caplet"));
				
				if (refListBox.getItemText(refListBox.getSelectedIndex()).equals("6 month")) 
					caplet.refTenor = 0.5;
				else caplet.refTenor = 1;
						
				String strikeText = capletStrike.getText();
				if (strikeText.contains("%")) {
					strikeText = strikeText.replace("%", "");
					caplet.strike = Double.parseDouble(strikeText)/100;
				} else {
					caplet.strike = Double.parseDouble(strikeText);
				}
				
				return caplet;
			}							
			
			private int getCapletNumPaths() {
				int i = capletNumPaths.getSelectedIndex();
				int numPaths = Integer.parseInt(capletNumPaths.getItemText(i));
				
				return numPaths;
			}
			
			private void enableCapletInputs(boolean enable) {
				maturity.setEnabled(enable);
				capletNumPaths.setEnabled(enable);
				capletPriceButton.setEnabled(enable);					
			}
		});
				
		
		//GetPrice button
		paramsFlexTable.setWidget(7, 1, capletPriceButton);
		
		//status, error
		paramsFlexTable.setText(8, 1, "");
					
		flowPanel.add(paramsFlexTable);	
		
		flowPanel.addStyleName("leftPaddedPanel");
		
		return flowPanel;
	}	
}
