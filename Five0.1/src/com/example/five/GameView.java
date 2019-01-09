package com.example.five;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

	/**
	 * ��Ļ�Ŀ�
	 */
	private static int screenWidth = Screen.screenWidth;
	/**
	 * ��Ļ�ĸ�
	 */
	private static int screenHeight = Screen.screenHeight;
	/**
	 * ���̵�����
	 */
	public static final int ROWS = 15;
	/**
	 * ���̵�����
	 */
	public static final int COLS = 15;
	/**
	 * ���������̵ķֲ�0Ϊ���ӣ�1Ϊ����,2Ϊ����
	 */
	private ChessType[][] chessMap = new ChessType[ROWS][COLS];
	private static float PADDING = ((float) (screenWidth) / (COLS - 1)) / 2;
	private static float PADDING_LEFT = ((float) (screenWidth) / (COLS - 1)) / 2;
	private static float PADDING_TOP = ((float) (screenHeight) / (ROWS - 1)) / 2;
	private static float ROW_MARGIN = ((float) (screenHeight - PADDING * 2))
			/ (ROWS - 1);
	private static float COL_MARGIN = ((float) (screenWidth - PADDING * 2))
			/ (COLS - 1);
	private static float MARGIN;
	// �ж���Ϸ�Ƿ����
	private boolean gameOver = false;
	// ��activity
	private Context context = null;
	// ���Ե�������ɫ
	private ChessType computerType = ChessType.BLACK;
	// ��ҵ�������ɫ
	private ChessType playerType = ChessType.WHITE;
	private ComputerPlayer computerPlayer = new ComputerPlayer(chessMap,
			computerType, playerType);

	public GameView(Context context) {
		super(context);
		this.context = context;
		this.setBackgroundResource(R.drawable.bg);
		PADDING_LEFT = ((screenWidth) / (COLS - 1)) / 2;
		PADDING_TOP = ((screenHeight) / (ROWS - 1)) / 2;
		PADDING = PADDING_LEFT < PADDING_TOP ? PADDING_LEFT : PADDING_TOP;
		ROW_MARGIN = ((screenHeight - PADDING * 2)) / (ROWS - 1);
		COL_MARGIN = ((screenWidth - PADDING * 2)) / (COLS - 1);
		MARGIN = ROW_MARGIN < COL_MARGIN ? ROW_MARGIN : COL_MARGIN;
		PADDING_LEFT = (screenWidth - (COLS - 1) * MARGIN) / 2;
		PADDING_TOP = (screenHeight - (ROWS - 1) * MARGIN) / 2;
		// �����ӽ��г�ʼ��
		initChess();
		// System.out.println(PADDING_LEFT + " " + PADDING_TOP);
	}

	/**
	 * �����ӽ��г�ʼ��
	 */
	public void initChess() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				chessMap[i][j] = ChessType.NONE;
			}
		}
		invalidate();
	}
	/**
	 * ��Ϸ���¿�ʼ
	 */
	public void reStart(){
		initChess();
		gameOver = false;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		// ��ӡ��
		for (int i = 0; i < ROWS; i++) {
			canvas.drawLine(PADDING_LEFT, i * MARGIN + PADDING_TOP, (COLS - 1)
					* MARGIN + PADDING_LEFT, i * MARGIN + PADDING_TOP, paint);
		}
		// ��ӡ��
		for (int i = 0; i < COLS; i++) {
			canvas.drawLine(PADDING_LEFT + i * MARGIN, PADDING_TOP,
					PADDING_LEFT + i * MARGIN, MARGIN * (ROWS - 1)
							+ PADDING_TOP, paint);
		}
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				// System.out.print(chessMap[r][c] + " ");
				if (chessMap[r][c] == ChessType.NONE)
					continue;
				if (chessMap[r][c] == ChessType.BLACK) {
					paint.setColor(Color.BLACK);
					canvas.drawCircle(r * MARGIN + PADDING_LEFT, c * MARGIN
							+ PADDING_TOP, MARGIN / 2, paint);
				} else if (chessMap[r][c] == ChessType.WHITE) {
					paint.setColor(Color.WHITE);
					canvas.drawCircle(r * MARGIN + PADDING_LEFT, c * MARGIN
							+ PADDING_TOP, MARGIN / 2, paint);
				}
			}
			// System.out.println();
		}
	}

	/**
	 * �ж��Ƿ�ʤ��
	 */
	public boolean hasWin(int r, int c) {
		ChessType chessType = chessMap[r][c];
		System.out.println(chessType);
		int count = 1;
		// ��������
		for (int i = r + 1; i < r + 5; i++) {
			if (i >= GameView.ROWS)
				break;
			if (chessMap[i][c] == chessType) {
				count++;
			} else
				break;
		}
		for (int i = r - 1; i > r - 5; i--) {
			if (i < 0)
				break;
			if (chessMap[i][c] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" "+"1");
		if (count >= 5)
			return true;
		// ��������
		count = 1;
		for (int i = c + 1; i < c + 5; i++) {
			if (i >= GameView.COLS)
				break;
			if (chessMap[r][i] == chessType)
				count++;
			else
				break;
		}
		for (int i = c - 1; i > c - 5; i--) {
			if (i < 0)
				break;
			if (chessMap[r][i] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"2");
		if (count >= 5)
			return true;
		// б��"\"
		count = 1;
		for (int i = r + 1, j = c + 1; i < r + 5; i++, j++) {
			if (i >= GameView.ROWS || j >= GameView.COLS) {
				break;
			}
			if (chessMap[i][j] == chessType)
				count++;
			else
				break;
		}
		for (int i = r - 1, j = c - 1; i > r - 5; i--, j--) {
			if (i < 0 || j < 0)
				break;
			if (chessMap[i][j] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"3");
		if (count >= 5)
			return true;
		// б��"/"
		count = 1;
		for (int i = r + 1, j = c - 1; i < r + 5; i++, j--) {
			if (i >= GameView.ROWS || j < 0)
				break;
			if (chessMap[i][j] == chessType)
				count++;
			else
				break;
		}
		for (int i = r - 1, j = c + 1; i > r - 5; i--, j++) {
			if (i < 0 || j >= GameView.COLS)
				break;
			if (chessMap[i][j] == chessType)
				count++;
			else
				break;
		}
		// System.out.println(count +" " +"4");
		if (count >= 5)
			return true;
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int r = Math.round((x - this.PADDING_LEFT) / this.MARGIN);
		int c = Math.round((y - this.PADDING_TOP) / this.MARGIN);
		// System.out.println(r + " " + c);
		if (!(r >= 0 && r < ROWS && c >= 0 && c < COLS)) {
			return false;
		}
		if (!gameOver) {
			if (chessMap[r][c] == ChessType.NONE) {
				chessMap[r][c] = this.playerType;
				if (this.hasWin(r, c)) {
					// ���ʤ��
					this.gameOver = true;
					new AlertDialog.Builder(context).setTitle("��ʾ")
							.setMessage("���ʤ��").setPositiveButton("ȷ��", null)
							.show();
				}
				Point p = computerPlayer.start();
				chessMap[p.x][p.y] = this.computerType;
				if (this.hasWin(p.x, p.y)) {
					// ����ʤ��
					this.gameOver = true;
					new AlertDialog.Builder(context).setTitle("��ʾ")
							.setMessage("����ʤ��").setPositiveButton("ȷ��", null)
							.show();
				}
			}
		} else {
			new AlertDialog.Builder(context)
				.setTitle("��ʾ")
				.setMessage("��Ϸ�ѽ���,�Ƿ����¿�ʼ?")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						reStart();
					}
				})
				.setNegativeButton("ȡ��", null).show();
		}
		this.invalidate();
		return super.onTouchEvent(event);
	}

}
