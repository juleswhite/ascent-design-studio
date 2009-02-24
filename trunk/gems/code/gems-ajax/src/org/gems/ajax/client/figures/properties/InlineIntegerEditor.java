package org.gems.ajax.client.figures.properties;

public class InlineIntegerEditor extends InlineEditor {
	public InlineIntegerEditor(int v) {
		super(new IntegerPropertyEditor(), new TextPropertyViewer(""+v));
	}
}
