package org.gems.ajax.client.figures;

import org.gems.ajax.client.connection.ConnectionDecoration;
import org.gems.ajax.client.util.Util;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractFloatingFigure extends DockPanel {

	private DiagramElementListener listener_ = new DiagramElementListener() {
	
			public void onResize(AbstractDiagramElement p, String w, String h) {
				updatePosition(Util.getDiagramX(p), Util.getDiagramY(p), p
						.getOffsetWidth());
			}
	
			public void onMove(DiagramElement p) {
				updatePosition(Util.getDiagramX(p.getDiagramWidget()), Util.getDiagramY(p.getDiagramWidget()), p.getDiagramWidget()
						.getOffsetWidth());
			}
	
			public void onChildRemoved(AbstractDiagramElement p) {
			}
	
			public void onChildAdded(AbstractDiagramElement p) {
			}
	
		};
	private DiagramElement panel_;
	private GEMSDiagram diagram_;

	public AbstractFloatingFigure() {
		super();
	}

	protected void onAttach() {
		super.onAttach();
		updateSize();
	}

	public void attach(GEMSDiagram dig, ConnectionDecoration d) {
		diagram_ = dig;
		dig.add(this,Util.getDiagramX(d.getWidget())-Util.half(Util.getOffsetWidth(this)),Util.getDiagramY(d.getWidget())-Util.getOffsetHeight(this));
	}

	public void attach(GEMSDiagram d, Widget w) {
		diagram_ = d;
		d.add(this, Util.getDiagramX(w), Util.getDiagramY(w));
	}

	public void attach(GEMSDiagram d, int x, int y) {
		diagram_ = d;
		d.add(this, x, y);
	}

	public void attach(DiagramElement panel) {
		diagram_ = panel.getDiagram();
		panel_ = panel;
		
		int x = Util.getDiagramX(panel.getDiagramWidget()) -
		Util.half(panel.getDiagramWidget().getOffsetWidth());	
		
		int y = Util.getDiagramY(panel.getDiagramWidget());		
		panel.getDiagram().add(
				this,
				x,
				y);
		
		updateSize();
		updatePosition(Util.getDiagramX(panel.getDiagramWidget()), Util.getDiagramY(panel.getDiagramWidget()), panel.getDiagramWidget().getOffsetWidth());
		panel.addDiagramElementListener(listener_);
	}

	public void updatePosition(int x, int y, int width) {
		if (diagram_ != null && getParent() == diagram_)
			diagram_.setWidgetPosition(this, getXOff() + x
					+ (Util.half(width) - Util.half(getOffsetWidth())), getYOff() + y
					- getOffsetHeight());
	}

	public void detach() {
		if (diagram_ != null) {
			diagram_.remove(this);
			if (panel_ != null) {
				panel_.removeDiagramElementListener(listener_);
			}
			diagram_ = null;
			panel_ = null;
		}
		else {
			System.out.println("");
		}
	}

	public int getXOff(){
		return 0;
	}
	
	public int getYOff(){
		return 0;
	}
	
	public DiagramElement getAttachedTo() {
		return panel_;
	}

	public abstract void updateSize();
}