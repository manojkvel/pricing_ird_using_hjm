package cqfportal.client.apps.finalproject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cqfportal.client.IViewRenderer;

public class UploadView implements IViewRenderer {

	private FormPanel form = new FormPanel();
	private FileUpload fu = new FileUpload();

	@Override
	public void renderFirstTime(FlowPanel rootFlowPanel) {
		// TODO Auto-generated method stub

		VerticalPanel contentPanel = new VerticalPanel();

		contentPanel.setBorderWidth(1);

		contentPanel.add(new Label(
				" Add Yield Curve Data in CSV format using the below widget"));

		contentPanel.add(getFileUploaderWidget(rootFlowPanel));

		rootFlowPanel.add(contentPanel);

	}

	@SuppressWarnings("deprecation")
	public Widget getFileUploaderWidget(FlowPanel rootFlowPanel) {
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL() + "fileupload");
		final DialogBox mask = new DialogBox();

		VerticalPanel holder = new VerticalPanel();

		//fu.setName("upload");
		holder.add(fu);
		holder.add(new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				fu.setName(fu.getFilename().substring(
						fu.getFilename().lastIndexOf("\\") + 1));
				GWT.log("You selected: " + fu.getFilename(), null);
				form.submit();
			}
		}));

		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (!"".equalsIgnoreCase(fu.getFilename())) {
					mask.setModal(true);
					mask.setGlassEnabled(true);
					mask.setPopupPosition(300, 200);
					mask.setText("Uploading "+fu.getName());
					mask.center();
				} else {
					event.cancel(); // cancel the event
				}

			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				mask.hide();
				Window.alert(fu.getName()+" uploaded to your context path \n Please proceed to Principal Component Analysis");
				PCAView.pcaFileName=fu.getName();
				
				
				
			}
		});

		form.add(holder);

		return form;
	}

}
