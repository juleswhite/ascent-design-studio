package org.gems.ajax.client.figures.properties;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

public class IntegerPropertyEditor extends NumericPropertyEditor {

	public IntegerPropertyEditor() {
		super();
	}
	
	public void prepareEditor() {
		super.prepareEditor();
		getEditor().addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if(!Character.isDigit(keyCode) && keyCode!=KeyboardListener.KEY_LEFT && keyCode!=KeyboardListener.KEY_RIGHT && keyCode!=KeyboardListener.KEY_ENTER){
					cancelKey();
				}
				else if(keyCode==KeyboardListener.KEY_ENTER){
					stopEditing();
				}
			}			
		});
	}

	public int getInt(){
		return Integer.parseInt(getText());
	}

	public String getValue() {
		return ""+getInt();
	}

}
