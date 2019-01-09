package com.example.five;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private Screen screen;
	private GameView gameView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getScreen();// �õ���Ļ�Ĵ�С
		gameView = new GameView(this);
		setContentView(gameView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		menu.add("���¿�ʼ");
		menu.add("�˳�");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("���¿�ʼ")) {
			gameView.reStart();
		} else if (item.getTitle().equals("�˳�")) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * �õ���Ļ�Ĵ�С
	 */
	private void getScreen() {
		int height = this.getWindowManager().getDefaultDisplay().getHeight();
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		screen = new Screen(width, height);
	}

}
