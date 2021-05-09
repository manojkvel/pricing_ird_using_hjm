package cqfportal.client.apps.finalproject;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import cqfportal.client.IViewRenderer;


public class DigitalOptionView implements IViewRenderer {

	@Override
	public void renderFirstTime(FlowPanel rootFlowPanel) {
		HTML html = new HTML("This is implemented in Excel. Click <a href=\"UVM.xlsm\">here</a> to download. (Requires Excel 2007 or above)");
		rootFlowPanel.add(html);		
	}
}
