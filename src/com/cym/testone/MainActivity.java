package com.cym.testone;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	private TextView tvScore;
	private int score;
	private static MainActivity mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvScore = (TextView) findViewById(R.id.textViewScore);
	}

	public MainActivity() {
		mainActivity = this;
	}

	public void celar() {
		score = 0;
		showScore();
	}

	public int getScore() {
		return score;
	}

	public void addScore(int s) {
		score += s;
		showScore();
	}

	public void showScore() {
		tvScore.setText(score + "");
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

}
