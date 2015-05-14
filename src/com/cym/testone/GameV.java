package com.cym.testone;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameV extends GridLayout {
	Card[][] cards = new Card[4][4];

	public GameV(Context context) {
		super(context);
		initView();
	}

	public GameV(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public GameV(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * onSizeChanged()初次进入游戏时会被调用
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 计算卡片的宽度
		int WIDTH = Math.min(w, h) - 10;// -5 为了使屏幕边缘留出缝隙
		int width = WIDTH / 4;
		addCards(width, width);
		startGame();
	}

	private void startGame() {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cards[x][y].setNum(0);
			}
		}
		addRandomNum();
		addRandomNum();
	}

	private void addRandomNum() {
		List<Point> points = new ArrayList<Point>();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cards[x][y].getNum() <= 0) {
					points.add(new Point(x, y));
				}
			}
		}
		// 随机从points里面获取一个point 获取他的x y 坐标 来确定方格的位置
		Point point = points.remove((int) (Math.random() * points.size()));
		// 随机获取card
		int startNum = Math.random() > 0.8 ? 4 : 2;
		cards[point.x][point.y].setNum(startNum);
	}

	private void addCards(int width, int height) {
		Card card;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				card = new Card(getContext());
				card.setNum(0);
				addView(card, width, height);
				cards[x][y] = card;

			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		setColumnCount(4);
		setBackgroundColor(Color.parseColor("#BBADA0"));
		this.setOnTouchListener(new OnTouchListener() {
			private float startX, startY, endX, endY, offHor, offVir;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					endX = event.getX();
					endY = event.getY();
					offHor = endX - startX;
					offVir = endY - startY;
					if (Math.abs(offHor) > Math.abs(offVir)) {
						if (offHor > 10) {
							slideRight();
						} else if (offHor < -10) {
							slideLeft();
						}
					} else if (Math.abs(offHor) < Math.abs(offVir)) {
						if (offVir > 10) {
							slideDown();
						} else if (offVir < -10) {
							slideUp();
						}
					}
					break;
				}
				return true;
			}

			private void checkCompetition() {
				boolean complete = true;
				ALL: for (int y = 0; y < 4; y++) {
					for (int x = 0; x < 4; x++) {
						if (cards[x][y].getNum() == 0
								|| (x > 0 && cards[x][y]
										.equals(cards[x - 1][y]))
								|| (x < 3 && cards[x][y]
										.equals(cards[x + 1][y]))
								|| (y > 0 && cards[x][y]
										.equals(cards[x][y - 1]))
								|| (y < 3 && cards[x][y]
										.equals(cards[x][y + 1]))) {
							complete = false;
							break ALL;
						}
					}
				}
				if (complete) {
					new AlertDialog.Builder(getContext())
							.setMessage(
									"GameOver！Your Score is"
											+ MainActivity.getMainActivity()
													.getScore())
							.setNegativeButton("重玩",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											startGame();
										}
									})
							.setPositiveButton("关闭",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											MainActivity.getMainActivity()
													.finish();
										}
									}).show();
				}

			}

			private void slideDown() {
				boolean merge = false;
				for (int x = 0; x < 4; x++) {
					for (int y = 3; y >= 0; y--) {
						for (int y1 = y - 1; y1 >= 0; y1--) {// 从当前位置开始向后遍历，当前位置为cards[x][y]

							if (cards[x][y1].getNum() > 0) {// 获取到一个值
								if (cards[x][y].getNum() <= 0) {// 当前位置上是空值
									cards[x][y].setNum(cards[x][y1].getNum());// 把遍历到的值
																				// 移动到当前卡片上
									cards[x][y1].setNum(0);
									y++;
									merge = true;
								} else if (cards[x][y].equals(cards[x][y1])) {
									cards[x][y].setNum(cards[x][y].getNum() * 2);
									cards[x][y1].setNum(0);
									MainActivity.getMainActivity().addScore(
											cards[x][y].getNum());
									merge = true;
								}
								break;
							}
						}
					}
				}
				if (merge) {
					addRandomNum();
					checkCompetition();
				}
			}

			private void slideUp() {
				boolean merge = false;
				for (int x = 0; x < 4; x++) {
					for (int y = 0; y < 4; y++) {
						for (int y1 = y + 1; y1 < 4; y1++) {// 从当前位置开始向后遍历，当前位置为cards[x][y]

							if (cards[x][y1].getNum() > 0) {// 获取到一个值
								if (cards[x][y].getNum() <= 0) {// 当前位置上是空值
									cards[x][y].setNum(cards[x][y1].getNum());// 把遍历到的值
																				// 移动到当前卡片上
									cards[x][y1].setNum(0);
									y--;
									merge = true;
								} else if (cards[x][y].equals(cards[x][y1])) {
									cards[x][y].setNum(cards[x][y].getNum() * 2);
									cards[x][y1].setNum(0);
									MainActivity.getMainActivity().addScore(
											cards[x][y].getNum());
									merge = true;
								}
								break;
							}
						}
					}
				}
				if (merge) {
					addRandomNum();
					checkCompetition();
				}
			}

			private void slideLeft() {
				boolean merge = false;
				for (int y = 0; y < 4; y++) {
					for (int x = 0; x < 4; x++) {
						for (int x1 = x + 1; x1 < 4; x1++) {// 从当前位置开始向后遍历，当前位置为cards[x][y]

							if (cards[x1][y].getNum() > 0) {// 获取到一个值
								if (cards[x][y].getNum() <= 0) {// 当前位置上是空值
									cards[x][y].setNum(cards[x1][y].getNum());// 把遍历到的值
																				// 移动到当前卡片上
									cards[x1][y].setNum(0);
									x--;
									merge = true;

								} else if (cards[x][y].equals(cards[x1][y])) {
									cards[x][y].setNum(cards[x][y].getNum() * 2);
									cards[x1][y].setNum(0);
									MainActivity.getMainActivity().addScore(
											cards[x][y].getNum());
									merge = true;
								}
								break;
							}
						}
					}
				}
				if (merge) {
					addRandomNum();
					checkCompetition();
				}
			}

			// 修改中...
			private void slideRight() {
				boolean merge = false;
				for (int y = 0; y < 4; y++) {
					for (int x = 3; x >= 0; x--) {
						for (int x1 = x - 1; x1 >= 0; x1--) {// 从当前位置开始向后遍历，当前位置为cards[x][y]

							if (cards[x1][y].getNum() > 0) {// 获取到一个值
								if (cards[x][y].getNum() <= 0) {// 当前位置上是空值
									cards[x][y].setNum(cards[x1][y].getNum());// 把遍历到的值
																				// 移动到当前卡片上
									cards[x1][y].setNum(0);
									x++;
									merge = true;
								} else if (cards[x][y].equals(cards[x1][y])) {
									cards[x][y].setNum(cards[x][y].getNum() * 2);
									cards[x1][y].setNum(0);
									MainActivity.getMainActivity().addScore(
											cards[x][y].getNum());
									merge = true;
								}
								break;
							}
						}
					}
				}
				if (merge) {
					addRandomNum();
					checkCompetition();
				}
			}
		});
	}

}
