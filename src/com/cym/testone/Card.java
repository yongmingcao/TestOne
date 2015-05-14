package com.cym.testone;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	private int num = 0;
	private TextView textView = null;

	public Card(Context context) {
		super(context);
		textView = new TextView(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		params.setMargins(5, 5, 0, 0);
		textView.setBackgroundColor(0x33EFE3D6);
		textView.setTextSize(40);
		textView.setGravity(Gravity.CENTER);
		addView(textView, params);

	}

	public boolean equals(Card card) {
		return getNum() == card.getNum();
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		if (num <= 0) {
			textView.setText("");
		} else {
			textView.setText(num + "");
		}
	}

}
