package org.gems.ajax.client.figures;

import java.util.List;

import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.edit.EditPartManager;
import org.gems.ajax.client.edit.cmd.AddChildCommand;
import org.gems.ajax.client.model.ModelHelper;
import org.gems.ajax.client.model.Type;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.client.util.GraphicsConstants;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PaletteFigure extends AbstractFloatingFigure implements GraphicsConstants{

	private class CreateButton extends Button {
		private Object type_;
		private ModelResource resource_;
		
		public CreateButton(ModelResource r, Object t, String label){
			super(label);
			type_ = t;
			resource_ = r;
			addClickListener(new ClickListener() {
				
				public void onClick(Widget sender) {
					Object inst = attachedTo_.getModelHelper().createInstance(resource_,type_);
					EditPart ep = attachedTo_.getEditDomain().getEditor().load(attachedTo_, inst);
					AddChildCommand add = new AddChildCommand();
					add.setChild(ep);
					add.setTarget(attachedTo_);
					attachedTo_.getEditDomain().getCommandStack().execute(add);
				}
			});
		}
	}
	
	private EditPart attachedTo_;
	private Panel header_;
	private HTML headerCenter_ = new HTML("<b>Actions</b>");
	private Panel componentContainer_ = new VerticalPanel();
	
	public PaletteFigure() {
		super();
		setStylePrimaryName(PALETTE_PANEL_STYLE);
		
		header_ = new HorizontalPanel();
		SimplePanel left = new SimplePanel();
		left.setStylePrimaryName(PALETTE_PANEL_HEADER_LEFT_STYLE);
		header_.add(left);
		headerCenter_.setStylePrimaryName(PALETTE_PANEL_HEADER_CENTER_STYLE);
		header_.add(headerCenter_);
		SimplePanel right = new SimplePanel();
		right.setStylePrimaryName(PALETTE_PANEL_HEADER_RIGHT_STYLE);
		header_.add(right);
		add(header_,NORTH);
		componentContainer_.setStylePrimaryName(PALETTE_PANEL_CENTER_STYLE);
		add(componentContainer_,CENTER);
	}

	public void updateSize() {
		setSize("100px", "100px");
		
		int h = getOffsetHeight() - header_.getOffsetHeight();
		if(componentContainer_.getOffsetHeight() < h)
			componentContainer_.setSize(componentContainer_.getOffsetWidth()+"px", h+"px");
	}
	
	public void setHeight(String height){
		super.setHeight(height);
	}
	
	public void setWidth(String width) {
		headerCenter_.setSize(width, headerCenter_.getOffsetHeight()+"px");
	}

	public int getXOff() {
		return Util.half(getOffsetWidth())
				+ Util
						.half(getAttachedTo().getDiagramWidget()
								.getOffsetWidth());
	}

	public int getYOff() {
		int h = getOffsetHeight();
		int ah = getAttachedTo().getDiagramWidget().getOffsetHeight();
		return Util.half(ah - h) + h;
	}

	public void attach(DiagramElement panel) {
		componentContainer_.clear();
		
		attachedTo_ = EditPartManager.getEditPart(panel);
		if (attachedTo_ != null) {
			ModelHelper mh = attachedTo_.getModelHelper();
			Object model = attachedTo_.getModel();
			ModelResource res = mh.getContainingResource(model);
			Type[] types = mh.getTypes(model);
			for (Type type : types) {
				List childts = mh.getAllowedChildTypes(type);
				for (Object o : childts) {
					Button b = new CreateButton(res, o, "+ " + o);
					componentContainer_.add(b);
				}
			}
		}
		super.attach(panel);
		
		Util.bringToFront(this);
	}

}
