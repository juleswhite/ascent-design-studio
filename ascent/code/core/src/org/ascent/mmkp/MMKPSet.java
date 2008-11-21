package org.ascent.mmkp;

import java.util.List;

public class MMKPSet {
	private Item currentItem_;
	private List<Item> items_;

	public MMKPSet(List<Item> items) {
		super();
		items_ = items;
	}

	public MMKPSet(Item currentItem, List<Item> items) {
		super();
		currentItem_ = currentItem;
		items_ = items;
	}

	public Item getCurrentItem() {
		return currentItem_;
	}

	public void setCurrentItem(Item currentItem) {
		currentItem_ = currentItem;
	}

	public List<Item> getItems() {
		return items_;
	}

	public void setItems(List<Item> items) {
		items_ = items;
	}
}
