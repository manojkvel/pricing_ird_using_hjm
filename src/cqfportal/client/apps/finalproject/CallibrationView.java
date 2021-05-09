package cqfportal.client.apps.finalproject;


import java.util.ArrayList;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;

import cqfportal.client.IViewRenderer;
import cqfportal.shared.HJMFactorData;
import cqfportal.shared.IntRate;
import cqfportal.shared.PCAModel;
import cqfportal.shared.Utils;


public class CallibrationView implements IViewRenderer {

	private TextBox vol1Constant;
	private TextBox vol2Constant;
	private TextBox vol2CoeffofT;
	private TextBox vol2CoeffofTSquare;
	private TextBox vol2CoeffofTCube;
	private TextBox vol3Constant;
	private TextBox vol3CoeffofT;
	private TextBox vol3CoeffofTSquare;
	private TextBox vol3CoeffofTCube;
	
//	private TextBox day0IntRateZero;
//	private TextBox day0IntRateHalf;
//	private TextBox day0IntRateOne;
//	private TextBox day0IntRateOneHalf;
//	private TextBox day0IntRateTwo;
//	private TextBox day0IntRateTwoHalf;

	@Override
	public void renderFirstTime(FlowPanel rootFlowPanel) {
		HTML html = new HTML("<br/><br/>Please perform PCA to populate Volatility factors");
		
		if(!PCAView.showCalibrationView){
			rootFlowPanel.add(html);
		}else{
			ScrollPanel tenor0Panel = new ScrollPanel();
			FlexTable tenor0Table = new FlexTable();
		
		//rootFlowPanel		
		
		//Currency
		FlexTable flex = new FlexTable();
		flex.setText(0, 0, "Currency");
		ListBox currencies = new ListBox();
		currencies.addItem("GBP");
		currencies.setEnabled(false);
		flex.setWidget(0,  1, currencies);
		rootFlowPanel.add(flex);
//		
//		SplitLayoutPanel splitPanel = new SplitLayoutPanel();
//		splitPanel.addWest(getPrincipalComponentInputs(), 128);
//						
//		splitPanel.add(getDay0Inputs());
//		
//		rootFlowPanel.add(splitPanel);	

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(getVolPrincipalComponentFlowPanel());
		horizontalPanel.add(getDay0IntRateFlowPanel(tenor0Panel,tenor0Table));
		
		populateHJMInputs(PCAView.calibratedInputs);
		
		
		
		rootFlowPanel.add(horizontalPanel);
		}
	}
	
	public HJMFactorData getHJMInputs() {
		HJMFactorData inputs = new HJMFactorData();
				
		//vol principal components
		inputs.vol1Constant = Double.parseDouble(vol1Constant.getText());
		
		inputs.vol2Constant = Double.parseDouble(vol2Constant.getText());
		inputs.vol2CoeffOfT = Double.parseDouble(vol2CoeffofT.getText());
		inputs.vol2CoeffOfTSquare = Double.parseDouble(vol2CoeffofTSquare.getText());
		inputs.vol2CoeffOfTCube = Double.parseDouble(vol2CoeffofTCube.getText());
				
		inputs.vol3Constant = Double.parseDouble(vol3Constant.getText());
		inputs.vol3CoeffOfT = Double.parseDouble(vol3CoeffofT.getText());
		inputs.vol3CoeffOfTSquare = Double.parseDouble(vol3CoeffofTSquare.getText());
		inputs.vol3CoeffOfTCube = Double.parseDouble(vol3CoeffofTCube.getText());
				
		//day 0 interest rates
//		inputs.day0TermStructure.intRates.add(
//				new IntRate(0, Double.parseDouble(day0IntRateZero.getText())));
//		inputs.day0TermStructure.intRates.add(
//				new IntRate(0.5, Double.parseDouble(day0IntRateHalf.getText())));
//		inputs.day0TermStructure.intRates.add(
//				new IntRate(1, Double.parseDouble(day0IntRateOne.getText())));
//		inputs.day0TermStructure.intRates.add(
//				new IntRate(1.5, Double.parseDouble(day0IntRateOneHalf.getText())));
//		inputs.day0TermStructure.intRates.add(
//				new IntRate(2, Double.parseDouble(day0IntRateTwo.getText())));
//		inputs.day0TermStructure.intRates.add(
//				new IntRate(2.5, Double.parseDouble(day0IntRateTwoHalf.getText())));
								
		return PCAView.calibratedInputs;
	}

	private ScrollPanel getDay0IntRateFlowPanel(ScrollPanel tenor0Panel,FlexTable tenor0Table) {
		
		FlowPanel fp = new FlowPanel();
//		
		fp.add(
				new HTML("<br><font color=\"blue\">Term Structure of Day 0 Interest Rates</font> "));
		
			for (int i = 0; i < PCAView.calibratedInputs.maturities.length; i++) {
				tenor0Table.setHTML(0, i, "<th border=\"1\">"
						+ PCAView.calibratedInputs.maturities[i] + "</th>");
			}
			
				for (int i = 0; i < PCAView.calibratedInputs.maturities.length; i++) {
					tenor0Table
							.setHTML(
									1 ,
									i,
									"<td border=\"1\">"
											+ PCAView.calibratedInputs.day0TermStructure.intRates.get(i)
											+ "</td>");
				}
			
				tenor0Table.setBorderWidth(1);
				tenor0Table.getRowFormatter().addStyleName(0,
					"headerRow");
				tenor0Panel.setSize("400px", "180px");
				fp.add(tenor0Table);
				tenor0Panel.add(fp);
		
//		FlowPanel fp = new FlowPanel();
//		FlexTable flex;
//		fp.add(
//				new HTML("<br><font color=\"blue\">Term Structure of Day 0 Interest Rates</font> "));
//		
//		flex = new FlexTable();		
//		
//		//0 - Instantaneous spot rate
//		flex.setText(0, 0, "0 years");
//		day0IntRateZero = getInputTextBox();
//		flex.setWidget(0, 1, day0IntRateZero);
//		
//		//0.5
//		flex.setText(1, 0, "0.5 years");
//		day0IntRateHalf = getInputTextBox();
//		flex.setWidget(1, 1, day0IntRateHalf);
//		
//		//1 year
//		flex.setText(2, 0, "1 year");
//		day0IntRateOne = getInputTextBox();
//		flex.setWidget(2, 1, day0IntRateOne);
//		
//		//1.5 years
//		flex.setText(3, 0, "1.5 year");
//		day0IntRateOneHalf = getInputTextBox();
//		day0IntRateOneHalf.setEnabled(false);
//		flex.setWidget(3, 1, day0IntRateOneHalf);
//		
//		//2 years
//		flex.setText(4, 0, "2 year");
//		day0IntRateTwo = getInputTextBox();
//		day0IntRateTwo.setEnabled(false);
//		flex.setWidget(4, 1, day0IntRateTwo);
//		
//		//2.5 years
//		flex.setText(5, 0, "2.5 years");
//		day0IntRateTwoHalf = getInputTextBox();
//		day0IntRateTwoHalf.setEnabled(false);
//		flex.setWidget(5, 1, day0IntRateTwoHalf);
//		
//
//		fp.add(flex);
//		fp.addStyleName("leftPaddedPanel");
		
		return tenor0Panel;
	}

	private FlowPanel getVolPrincipalComponentFlowPanel() {
			
		FlowPanel fp = new FlowPanel();
		fp.add(
				new HTML("<br><font color=\"blue\">Volatility Principal Components </font> (T refers to time to maturity)"));
		
		//first component
		fp.add(new HTML("First principal component (Vol_1)"));
		HorizontalPanel p = new HorizontalPanel();
		p.add(new HTML("Vol_1 = &nbsp"));		
		vol1Constant = getInputTextBox();
		p.add(vol1Constant);
						
		fp.add(p);
		
		//second component
		fp.add(new HTML("<br>Second principal component (Vol_2)"));	
		p = new HorizontalPanel();
		p.add(new HTML("Vol_2 = &nbsp"));	
		vol2Constant = getInputTextBox();
		p.add(vol2Constant);
		
		p.add(new HTML("&nbsp + T * &nbsp"));
		vol2CoeffofT = getInputTextBox();
		p.add(vol2CoeffofT);
		
		p.add(new HTML("&nbsp + T^2 * &nbsp"));
		vol2CoeffofTSquare = getInputTextBox();
		p.add(vol2CoeffofTSquare);
		
		p.add(new HTML("&nbsp + T^3 * &nbsp"));
		vol2CoeffofTCube = getInputTextBox();
		p.add(vol2CoeffofTCube);
		
		fp.add(p);
		
		//third component
		fp.add(new HTML("<br>Third principal component (Vol_3)"));	
		p = new HorizontalPanel();
		p.add(new HTML("Vol_3 = &nbsp"));	
		vol3Constant = getInputTextBox();
		p.add(vol3Constant);
		
		p.add(new HTML("&nbsp + T * &nbsp"));
		vol3CoeffofT = getInputTextBox();
		p.add(vol3CoeffofT);
		
		p.add(new HTML("&nbsp + T^2 * &nbsp"));
		vol3CoeffofTSquare = getInputTextBox();
		p.add(vol3CoeffofTSquare);
		
		p.add(new HTML("&nbsp + T^3 * &nbsp"));
		vol3CoeffofTCube = getInputTextBox();
		p.add(vol3CoeffofTCube);
		
		fp.add(p);
		
		return fp;
	}
	
	private TextBox getInputTextBox() {
		final TextBox t = new TextBox();
		t.setHeight("8px");
		t.setWidth("100px");
		
		t.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if (! Utils.isNumeric(t.getText())) {
					t.selectAll();	
					Window.alert("Please enter a valid decimal number");					
					//event.stopPropagation();
				}
			}
		});
		
		
		return t;
	}
	
	private void populateHJMInputs(HJMFactorData hjmInputs) {
		//vol principal components		
		vol1Constant.setText(Double.toString(hjmInputs.vol1Constant));
				
		vol2Constant.setText(Double.toString(hjmInputs.vol2Constant));
		vol2CoeffofT.setText(Double.toString(hjmInputs.vol2CoeffOfT));
		vol2CoeffofTSquare.setText(Double.toString(hjmInputs.vol2CoeffOfTSquare));
		vol2CoeffofTCube.setText(Double.toString(hjmInputs.vol2CoeffOfTCube));
		
		vol3Constant.setText(Double.toString(hjmInputs.vol3Constant));
		vol3CoeffofT.setText(Double.toString(hjmInputs.vol3CoeffOfT));
		vol3CoeffofTSquare.setText(Double.toString(hjmInputs.vol3CoeffOfTSquare));
		vol3CoeffofTCube.setText(Double.toString(hjmInputs.vol3CoeffOfTCube));
		
		ArrayList<IntRate> intRates = hjmInputs.day0TermStructure.intRates;
		
		//day 0 interest rates
//		day0IntRateZero.setText("" + intRates.get(0).rate); //gimic to convert double to string
//		day0IntRateHalf.setText("" + intRates.get(1).rate);
//		day0IntRateOne.setText("" + intRates.get(2).rate);
//		day0IntRateOneHalf.setText("" + intRates.get(3).rate);
//		day0IntRateTwo.setText("" + intRates.get(4).rate);
//		day0IntRateTwoHalf.setText("" + intRates.get(5).rate);		
	}	
	
	private HJMFactorData getDefaultInputs() {
		HJMFactorData inputs = new HJMFactorData();
		
		//vol principal components
		inputs.vol1Constant = 0.0064306548;
		
		inputs.vol2Constant = -0.0035565431;
		inputs.vol2CoeffOfT = -0.0005683999;
		inputs.vol2CoeffOfTSquare = 0.0001181915;
		inputs.vol2CoeffOfTCube = -0.0000035939;
		
		inputs.vol3Constant = -0.0047506715;
		inputs.vol3CoeffOfT = 0.0017541783;
		inputs.vol3CoeffOfTSquare = -0.0001415249;
		inputs.vol3CoeffOfTCube = 0.0000031274;
		
		//day 0 interest rates
		inputs.day0TermStructure.intRates.add(new IntRate(0, 0.046138361));
		inputs.day0TermStructure.intRates.add(new IntRate(0.5, 0.045251174));
		inputs.day0TermStructure.intRates.add(new IntRate(1, 0.042915805));
//		inputs.day0TermStructure.intRates.add(new IntRate(1.5, 0.04283311));
//		inputs.day0TermStructure.intRates.add(new IntRate(2, 0.043497719));
//		inputs.day0TermStructure.intRates.add(new IntRate(2.5, 0.044053792));				

		return inputs;
	}
}
