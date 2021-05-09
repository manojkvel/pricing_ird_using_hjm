/**
 * 
 */
package cqfportal.client.apps.finalproject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import cqfportal.client.IViewRenderer;
import cqfportal.shared.HJMFactorData;
import cqfportal.shared.PCAModel;

/**
 * @author Workstation
 * 
 */
public class PCAView implements IViewRenderer {
	/**
	 * The constants used in this Content Widget.
	 */

	private PCAServiceAsync pcaService = GWT.create(PCAService.class);
	private CallibrationView callibrationView;
	public static String pcaFileName="";

	public PCAView setCallibrationView(CallibrationView c) {
		 callibrationView = c;
		return this;
	}

	public static interface CwConstants extends Constants {
		String cwFileUploadButton();

		String cwFileUploadDescription();

		String cwFileUploadName();

		String cwFileUploadNoFileError();

		String cwFileUploadSelectFile();

		String cwFileUploadSuccessful();
	}
	public static HJMFactorData calibratedInputs;
	public static boolean showCalibrationView = false; 

private final CwConstants constants = new CwConstants() {

		@Override
		public String cwFileUploadSuccessful() {
			// TODO Auto-generated method stub
			return "File Upload Success";
		}

		@Override
		public String cwFileUploadSelectFile() {
			// TODO Auto-generated method stub
			return "fileUpload.getFilename()";
		}

		@Override
		public String cwFileUploadNoFileError() {
			// TODO Auto-generated method stub
			return "Found No File by name <" + pcaFileName +">.\n Did you upload the data file?";
		}

		@Override
		public String cwFileUploadName() {
			// TODO Auto-generated method stub
			return "fileUpload.getName()";
		}

		@Override
		public String cwFileUploadDescription() {
			// TODO Auto-generated method stub
			return "Uploas a CSV file for Yield Curves";
		}

		@Override
		public String cwFileUploadButton() {
			// TODO Auto-generated method stub
			return "Perform PCA";
		}
	};

	//FileUpload fileUpload;

	@Override
	public void renderFirstTime(final FlowPanel rootFlowPanel) {

		//fileUpload = new FileUpload();
		
		//fileUpload.ensureDebugId("cwFileUpload");
		rootFlowPanel.add(new Label("Please Click 'Perform PCA' Button to start the Priincipal Component Analysis"));
		rootFlowPanel.add(new Label(PCAView.pcaFileName));
		//rootFlowPanel.add(fileUpload);
		final DialogBox mask = new DialogBox();
		
		ScrollPanel scroller = new ScrollPanel();
		final FlexTable eigenValues = new FlexTable();
		final Image pcaChart = new Image(GWT.getModuleBaseURL()+"PCAChart.JPG");
//		final Button pcaButton = new Button("Show PCA Chart"){
//			@Override
//			public HandlerRegistration addClickHandler(ClickHandler handler) {
//				// TODO Auto-generated method stub
//				Window.open(GWT.getModuleBaseURL()+"PCAChart.JPG", "PCA Chart", "");
//				return super.addClickHandler(handler);
//			}
//		};
//		pcaButton.setEnabled(false);
		
		
		
		eigenValues.setTitle("Eigen Values");
		//scroller.setWidth("500");
		//scroller.setHeight("20");
		scroller.setSize("1000px", "180px");


		ScrollPanel scrollerEVectors = new ScrollPanel();
		final FlexTable eigenVectors = new FlexTable();
		eigenVectors.setTitle("Eigen Vectors");
		//scroller.setWidth("500");
		//scroller.setHeight("20");
		scrollerEVectors.setSize("1000px", "120px");
		
		
		ScrollPanel scrollerCov = new ScrollPanel();
		final FlexTable cov = new FlexTable();
		scrollerCov.setTitle("Covariance Matrix");
		//scroller.setWidth("500");
		//scroller.setHeight("20");
		scrollerCov.setSize("1000px", "120px");
		// Add a button to upload the file
		Button uploadButton = new Button(constants.cwFileUploadButton());
		uploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				if (pcaFileName.length() == 0) {
					Window.alert(constants.cwFileUploadNoFileError());
				} else {
					
					AsyncCallback<PCAModel> callBack = new AsyncCallback<PCAModel>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
							caught.printStackTrace();
							Window.alert("PCA Failed");

						}

						@Override
						public void onSuccess(PCAModel result) {
							for (int i = 0; i < result.getNoOfCols(); i++) {
								cov.setHTML(0, i, "<th border=\"1\">"
										+ result.getHeaders()[i] + "</th>");
							}
							for (int row = 1; row < result.getNoOfCols() + 1; row++) {
								for (int i = 0; i < result.getNoOfCols(); i++) {
									cov
											.setHTML(
													row ,
													i,
													"<td border=\"1\">"
															+ String.valueOf(result.getCovMatrix()[row-1][i])
															+ "</td>");
								}
							}
							cov.setBorderWidth(1);
							cov.getRowFormatter().addStyleName(0,
									"headerRow");

							for (int i = 0; i < result.getNoOfCols(); i++) {
								eigenValues.setHTML(0, i, "<th border=\"1\">"
										+ result.getHeaders()[i] + "</th>");
							}
							for (int i = 0; i < result.getNoOfCols(); i++) {
								eigenValues.setHTML(1, i, "<td border=\"1\">"
										+ String.valueOf(result.getEigenValues()[i] + "</td>"));
							}
							eigenValues.setBorderWidth(1);
							eigenValues.getRowFormatter().addStyleName(0,
									"headerRow");

							for (int i = 0; i < result.getNoOfCols(); i++) {
								eigenVectors.setHTML(0, i, "<th border=\"1\">"
										+ result.getHeaders()[i] + "</th>");
							}
							for (int row = 1; row < result.getNoOfCols() + 1; row++) {
								for (int i = 0; i < result.getNoOfCols(); i++) {
									eigenVectors.setHTML(
											row,
											i,
											"<td border=\"1\">"
													+ String.valueOf(result.getEigenVectors()[row-1][i])
													+ "</td>");
								}
							}
							eigenVectors.setBorderWidth(1);
							eigenVectors.getRowFormatter().addStyleName(0,
									"headerRow");
							//Window.alert(GWT.getModuleBaseURL()+"PCAChart.JPG");
							VerticalPanel imagePanel = new VerticalPanel();
							imagePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
							//Window.alert(GWT.getModuleBaseURL()+"PCAChart.JPG");
							final DialogBox win = new DialogBox();
							imagePanel.add(new Image(GWT.getModuleBaseURL()+"PCAChart.JPG"));
							
							
							imagePanel.setHeight("1200");
							
							win.setGlassEnabled(true);
							Button closeButton = new Button("Close Button");
							closeButton.addClickHandler(new ClickHandler() {public void onClick(ClickEvent event) {win.hide();}});
							imagePanel.add(closeButton);
							win.add(imagePanel);
							win.show();
							//pcaChart.setUrl(GWT.getModuleBaseURL()+"PCAChart.JPG");
//							
							;
//							if(DOM.getElementById("PCAChart")!=null){rootFlowPanel.remove(IMG_WIDGET_INDEX);};
//							 Image newChart=new Image(GWT.getModuleBaseURL()+"PCAChart.JPG");
//							 newChart.getElement().setId("PCAChart");
//							 IMG_WIDGET_INDEX=rootFlowPanel.getWidgetCount();
//							 Window.alert(IMG_WIDGET_INDEX+"");
//							rootFlowPanel.add(newChart);
							//pcaButton.setEnabled(true);
							calibratedInputs=result.getInputs();
							showCalibrationView=true;
							callibrationView.renderFirstTime(rootFlowPanel);
							PricingView.pcaPerformed=true;
							//pcaChart.setVisible(true);
							mask.hide();
						}

					};
					// Window.alert(fileUpload.getFilename());
					// Window.alert(fileUpload.getFilename().substring(
					// fileUpload.getFilename().lastIndexOf("\\") + 1));
//					pcaService.performPCA(
//							fileUpload.getFilename()
//									.substring(
//											fileUpload.getFilename()
//													.lastIndexOf("\\") + 1),
//							callBack);
					
					pcaService.performPCA(pcaFileName,callBack);
					
					mask.setModal(true);
					mask.setGlassEnabled(true);
					mask.setPopupPosition(300, 200);
					mask.setText("Performing PCA on "+pcaFileName);
					mask.center();
				}
			}

		});
		
		pcaChart.setVisible(false);
		scrollerCov.add(cov);
		scroller.add(eigenValues);
		scrollerEVectors.add(eigenVectors);
		rootFlowPanel.add(uploadButton);
		rootFlowPanel.add(new Label(""));
		rootFlowPanel.add(new Label("Covariance Matrix"));
		rootFlowPanel.add(scrollerCov);
		rootFlowPanel.add(new Label(""));
		rootFlowPanel.add(new Label("Eigen Values:"));
		rootFlowPanel.add(scroller);
		rootFlowPanel.add(new Label(""));
		rootFlowPanel.add(new Label("Eigen Vectors:"));
		rootFlowPanel.add(scrollerEVectors);
		//rootFlowPanel.add(pcaButton);
		

	}
	public static int IMG_WIDGET_INDEX;

}
