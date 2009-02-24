package org.gems.ajax.client.edit.cmd;

import org.gems.ajax.client.edit.Command;
import org.gems.ajax.client.edit.EditPart;
import org.gems.ajax.client.figures.PaletteFigure;

public class ShowPaletteCommand implements Command{

	private PaletteFigure figure_ = new PaletteFigure();
	
	private boolean attached_ = false;
	
	private EditPart target_;
	
	public boolean canExecute() {
		return target_ != null;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		attached_ = true;
		figure_.attach(target_.getFigure());
	}

	public String getName() {
		return "Show Palette";
	}

	public void redo() {
	}

	public void setTarget(EditPart ep) {
		target_ = ep;
	}

	public void undo() {
		if(attached_){
			figure_.detach();
			attached_ = false;
		}
	}

}
