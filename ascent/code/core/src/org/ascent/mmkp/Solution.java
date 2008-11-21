package org.ascent.mmkp;

import java.util.List;

public class Solution {
	private MMKPProblem problem_;
	private List<Item> items_;
	private int overFlow_ = -1;
	private int value_ = -1;

	public Solution(List<Item> items, MMKPProblem p) {
		super();
		problem_ = p;
		items_ = items;
	}

	public List<Item> getItems() {
		return items_;
	}

	public void setItems(List<Item> items) {
		items_ = items;
	}

	public int getOverflow() {
		if(items_ == null)
			return 0;
		
		if (overFlow_ == -1) {
			int overflow = 0;
			for (int i = 0; i < problem_.getResources().length; i++) {
				int consumed = 0;
				for (Item item : items_)
					consumed += problem_.getConsumedResources()[item.getIndex()][i];

				int delta = problem_.getResources()[i] - consumed;
				if (delta < 0)
					overflow += -1 * delta;
			}
			overFlow_ = overflow;
		}

		return overFlow_;
	}

	public int getValue() {
		return value_;
	}

	public void setValue(int value) {
		value_ = value;
	}

}

