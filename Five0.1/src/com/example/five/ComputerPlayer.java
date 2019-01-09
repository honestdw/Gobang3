package com.example.five;

import java.util.Random;

import android.graphics.Point;
/**
 * AI类
 * 参考了别人的算法写出来的
 * @author Administrator
 */
public class ComputerPlayer {
	private ChessType[][] chessMap;
	private int[][] computerMap = new int[GameView.ROWS][GameView.COLS];
	private int[][] playerMap = new int[GameView.ROWS][GameView.COLS];
	// 电脑的棋子颜色
	private ChessType computerType = ChessType.BLACK;
	// 玩家的棋子颜色
	private ChessType playerType = ChessType.WHITE;
	private ChessStatus[] chessStatus = new ChessStatus[4];

	public ComputerPlayer(ChessType[][] chessMap, ChessType computerType,
			ChessType playerType) {
		this.chessMap = chessMap;
		this.playerType = playerType;
		this.computerType = computerType;
	}

	/**
	 * 初始化
	 */
	private void initValue() {
		for (int r = 0; r < GameView.ROWS; r++) {
			for (int c = 0; c < GameView.COLS; c++) {
				computerMap[r][c] = 0;
				playerMap[r][c] = 0;
			}
		}
		for (int i = 0; i < chessStatus.length; i++) {
			chessStatus[i] = ChessStatus.DIED;
		}
	}

	/**
	 * 电脑开始下子
	 */
	public Point start() {
		Point p = getBestPoint();
		//chessMap[p.x][p.y] = computerType;
		return p;
	}

	/**
	 * 得到最优点
	 * 
	 * @return 得到最优的点
	 */
	private Point getBestPoint() {
		initValue();
		for (int r = 0; r < GameView.ROWS; r++) {
			for (int c = 0; c < GameView.COLS; c++) {
				if (chessMap[r][c] == ChessType.NONE) {
					this.computerMap[r][c] = getValue(r, c, this.computerType);
					this.playerMap[r][c] = getValue(r, c, this.playerType);
				}
			}
		}
		int pcMax = 0, playerMax = 0;
		Random rd = new Random();
		Point pcPoint = new Point(-1, -1);
		Point playerPoint = new Point();
		// 分别选出pc估分和玩家估分的最大值
		for (int r = 0; r < GameView.ROWS; r++) {
			for (int c = 0; c < GameView.COLS; c++) {
				// 选出电脑估分的最大值
				if (pcMax == computerMap[r][c]) {
					if (rd.nextInt(10) % 2 == 0) {
						pcMax = computerMap[r][c];
						pcPoint.x = r;
						pcPoint.y = c;
					}
				} else if (pcMax < computerMap[r][c]) {
					pcMax = computerMap[r][c];
					pcPoint.x = r;
					pcPoint.y = c;
				}
				// 选出玩家估分的最大值
				if (playerMax == playerMap[r][c]) {
					if (rd.nextInt(10) % 2 == 0) {
						playerMax = playerMap[r][c];
						playerPoint.x = r;
						playerPoint.y = c;
					}
				} else if (playerMax < playerMap[r][c]) {
					playerMax = playerMap[r][c];
					playerPoint.x = r;
					playerPoint.y = c;
				}
			}
		}
		// 两者都在90~100分之间，优先选择PC
		if (pcMax >= 90 && pcMax < 100 && playerMax >= 90 && playerMax < 100) {
			return pcPoint;
		} else {
			return playerMax > pcMax ? playerPoint : pcPoint;
		}
	}

	/**
	 * 得到该位置的分数
	 * 
	 * @param r
	 *            该棋子所在的行数
	 * @param c
	 *            该棋子所在的列数
	 * @param chessType
	 *            棋子的类型
	 * @return 得到的分数
	 */
	private int getValue(int r, int c, ChessType chessType) {
		int[] dir = new int[4];
		dir[0] = this.getHorCount(r, c, chessType);
		dir[1] = this.getVerCount(r, c, chessType);
		dir[2] = this.getSloRCount(r, c, chessType);
		dir[3] = this.getSloLCount(r, c, chessType);

		// 成五
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] >= 5)
				return ScoreTable.FIVE;
		}
		int temp = 0;
		// 双活四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED)
				temp++;
			if (temp == 2)
				return ScoreTable.DOUBLE_ALIVE_FOUR;
		}

		int t1 = 0, t2 = 0;
		// 活四死四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.ALIVE)
				t1 = 1;
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED)
				t2 = 1;
			if (t1 == 1 && t2 == 1)
				return ScoreTable.ALIVE_FOUR_AND_DEAD_FOUR;
		}
		// 活四活三
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED)
				t1 = 1;
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.ALIVE)
				t2 = 1;
			if (t1 == 1 && t2 == 1)
				return ScoreTable.ALIVE_FOUR_AND_ALIVE_THREE;
		}
		// 活四死三
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED)
				t1 = 1;
			if (dir[i] == 3 && chessStatus[i] != ChessStatus.DIED)
				t2 = 1;
			if (t1 == 1 && t2 == 1) {
				return ScoreTable.ALIVE_FOUR_AND_DEAD_THREE;
			}
		}
		// 活四活二
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] != ChessStatus.DIED)
				t1 = 1;
			if (dir[i] == 2 && chessStatus[i] != ChessStatus.ALIVE)
				t2 = 1;
			if (t1 == 1 && t2 == 1)
				return ScoreTable.ALIVE_FOUR_AND_ALIVE_TWO;
		}

		// 活四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.ALIVE) {
				return ScoreTable.ALIVE_FOUR;
			}
		}

		// 双死四
		temp = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.DIED)
				temp++;
			if (temp == 2)
				return ScoreTable.DOUBLE_DEAD_FOUR;
		}

		// 死四活3
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.DIED)
				t1 = 1;
			if (dir[i] == 3 && chessStatus[i] != ChessStatus.DIED)
				t2 = 1;
			if (t1 == 1 && t2 == 1) {
				return ScoreTable.DEAD_FOUR_AND_ALIVE_THREE;
			}
		}

		// 死四活2
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.DIED)
				t1 = 1;
			if (dir[i] == 2 && chessStatus[i] != ChessStatus.DIED)
				t2 = 1;
			if (t1 == 1 && t2 == 1)
				return ScoreTable.DEAD_FOUR_AND_ALIVE_TWO;
		}

		// 双活三
		temp = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && chessStatus[i] != ChessStatus.DIED)
				temp++;
			if (temp == 2)
				return ScoreTable.DOUBLE_ALIVE_THREE;
		}
		// 活死三
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.ALIVE)
				t1 = 1;
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.DIED)
				t2 = 1;
			if (t1 == 1 && t2 == 1)
				return ScoreTable.ALIVE_THREE_AND_DEAD_THREE;
		}

		// 活三
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.ALIVE)
				return ScoreTable.ALIVE_THREE;
		}

		// 死四
		for (int i = 0; i < dir.length; i++) {
			if (dir[i] == 4 && chessStatus[i] == ChessStatus.DIED)
				return ScoreTable.DEAD_FOUR;
		}

		// 半活死3
		t1 = 0;
		t2 = 0;
		for (int i = 0; i < 4; i++) {
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.DIED)
				t1 = 1;
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.HALFALIVE)
				t2 = 1;
			if (t1 == 1 && t2 == 1)
				return ScoreTable.ALIVE_THREE_AND_DEAD_THREE;
		}

		// 双活2
		temp = 0;
		for (int i = 0; i < 4; i++) {
			if (dir[i] == 2 && chessStatus[i] == ChessStatus.ALIVE)
				temp++;
			if (temp == 2)
				return ScoreTable.DOUBLE_ALIVE_TWO;
		}

		// 死3
		for (int i = 0; i < 4; i++)
			if (dir[i] == 3 && chessStatus[i] == ChessStatus.DIED)
				return ScoreTable.DEAD_THREE;

		// 活2
		for (int i = 0; i < 4; i++)
			if (dir[i] == 2 && chessStatus[i] == ChessStatus.ALIVE)
				return ScoreTable.ALIVE_TWO;

		// 死2
		for (int i = 0; i < 4; i++)
			if (dir[i] == 2 && chessStatus[i] == ChessStatus.DIED)
				return ScoreTable.DEAD_TWO;
		return 0;
	}

	/**
	 * 横向搜索
	 * 
	 * @param r
	 *            该棋子所在的行数
	 * @param c
	 *            该棋子所在的列数
	 * @param chessType
	 *            棋子的类型
	 * @return 得到的个数
	 */
	private int getHorCount(int r, int c, ChessType chessType) {
		int count = 1;
		int t1 = c + 1;
		int t2 = c - 1;
		for (int j = c + 1; j < c + 5; j++) {
			if (j >= GameView.COLS) {
				chessStatus[0] = ChessStatus.DIED;
				break;
			}
			if (chessMap[r][j] == chessType) {
				count++;
				if (count >= 5)
					return count;
			} else {
				chessStatus[0] = (chessMap[r][j] == ChessType.NONE) ? ChessStatus.ALIVE
						: ChessStatus.DIED;
				t1 = j;
				break;
			}
		}

		for (int j = c - 1; j > c - 5; j--) {
			if (j < 0) {
				if (chessStatus[0] == ChessStatus.DIED && count < 5) {
					return 0;
				}
				chessStatus[0] = ChessStatus.DIED;
				break;
			}
			if (chessMap[r][j] == chessType) {
				count++;
				if (count >= 5)
					return count;
			} else {
				if (chessStatus[0] == ChessStatus.DIED) {
					if (count < 5 && chessMap[r][j] != ChessType.NONE) {
						return 0;
					}
				} else {
					chessStatus[0] = (chessMap[r][j]) == ChessType.NONE ? ChessStatus.ALIVE
							: ChessStatus.DIED;
					t2 = j;// 记录遇到的空格
					// 当两端都活的时候，看是否可以延伸
					if (chessStatus[0] == ChessStatus.ALIVE) {
						int tempCount1 = count, tempCount2 = count;
						boolean isAlive1 = false, isAlive2 = false;
						for (int i = t1 + 1; i < t1 + 5; i++) {
							if (i >= GameView.ROWS)
								break;
							if (chessMap[r][i] == chessType) {
								tempCount1++;
							} else {
								isAlive1 = (chessMap[r][i] == ChessType.NONE) ? true
										: false;
								break;
							}
						}
						for (int i = t2 - 1; i > t2 - 5; i--) {
							if (i < 0)
								break;
							if (chessMap[r][i] == chessType) {
								tempCount2++;
							} else {
								isAlive2 = (chessMap[r][i] == ChessType.NONE) ? true
										: false;
								break;
							}
						}
						// 如果两头都是空，直接跳出
						if (tempCount1 == count && tempCount2 == count)
							break;
						if (tempCount1 == tempCount2) {
							count = tempCount1;
							chessStatus[0] = (isAlive1 && isAlive2) ? ChessStatus.HALFALIVE
									: ChessStatus.DIED;
						} else {
							count = (tempCount1 > tempCount2) ? tempCount1
									: tempCount2;
							if (count >= 5)
								return 0;
							if (tempCount1 > tempCount2)
								chessStatus[0] = (isAlive1) ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							else
								chessStatus[0] = (isAlive2) ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
						}
					}

				}
				break;
			}
		}
		return count;
	}

	/**
	 * 纵向搜索
	 * 
	 * @param chessType
	 *            要搜索的棋子类型
	 * @param r
	 *            棋子所在的行
	 * @param c
	 *            棋子所在的列
	 * @return
	 */
	private int getVerCount(int r, int c, ChessType chessType) {
		int t1 = r + 1;
		int t2 = r - 1;
		int count = 1;
		for (int i = r + 1; i < r + 5; i++) {
			if (i >= GameView.ROWS) {
				chessStatus[1] = ChessStatus.DIED;
				break;
			}
			if (chessMap[i][c] == chessType) {
				count++;
				if (count >= 5) {
					return count;
				}
			} else {
				chessStatus[1] = (chessMap[i][c] == ChessType.NONE) ? ChessStatus.ALIVE
						: ChessStatus.DIED;
				t1 = i;
				break;
			}
		}

		for (int i = r - 1; i > r - 5; i--) {
			if (i < 0) {
				if (chessStatus[1] == ChessStatus.DIED && count < 5) {
					return 0;
				}
				chessStatus[1] = ChessStatus.DIED;
				break;
			}
			if (chessMap[i][c] == chessType) {
				count++;
				if (count >= 5) {
					return count;
				}
			} else {
				if (chessStatus[1] == ChessStatus.DIED) {
					if (chessMap[i][c] != ChessType.NONE && count < 5) {
						return 0;
					}
				} else {
					chessStatus[1] = chessMap[i][c] == ChessType.NONE ? ChessStatus.ALIVE
							: ChessStatus.DIED;
					t2 = i;
					// 如果两头都活，看是否还可以延伸
					if (chessStatus[1] == ChessStatus.ALIVE) {
						int tempCount1 = count, tempCount2 = count;
						boolean isAlive1 = false, isAlive2 = false;
						for (int j = t1 + 1; j < t1 + 5; j++) {
							if (j >= GameView.ROWS) {
								// chessStatus[1] = ChessStatus.DIED;
								break;
							}
							if (chessMap[j][c] == chessType) {
								tempCount1++;
							} else {
								isAlive1 = (chessMap[j][c] == ChessType.NONE) ? true
										: false;
								break;
							}
						}

						for (int j = t2 - 1; j > t2 - 5; j--) {
							if (j < 0) {
								break;
							}
							if (chessMap[j][c] == chessType) {
								tempCount2++;
							} else {
								isAlive2 = (chessMap[j][c] == ChessType.NONE) ? true
										: false;
								break;
							}
						}

						if (tempCount1 == count && tempCount2 == count) {
							break;
						}
						if (tempCount1 == tempCount2) {
							count = tempCount1;
							chessStatus[1] = (isAlive1 && isAlive2) ? ChessStatus.HALFALIVE
									: ChessStatus.DIED;
						} else {
							count = (tempCount1 > tempCount2) ? tempCount1
									: tempCount2;
							if (count >= 5)
								return 0;
							if (tempCount1 > tempCount2) {
								chessStatus[1] = isAlive1 ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							} else {
								chessStatus[1] = isAlive2 ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							}
						}
					}
					break;
				}
			}
		}
		return count;
	}

	/**
	 * 斜向"\"
	 */
	private int getSloRCount(int r, int c, ChessType chessType) {
		int count = 1;
		int tr1 = r + 1;
		int tc1 = c + 1;
		int tr2 = r - 1;
		int tc2 = c - 1;
		for (int i = r + 1, j = c + 1; i < r + 5; i++, j++) {
			if (i >= GameView.ROWS || j >= GameView.COLS) {
				chessStatus[2] = ChessStatus.DIED;
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
				if (count >= 5)
					return count;
			} else {
				chessStatus[2] = (chessMap[i][j] == ChessType.NONE) ? ChessStatus.ALIVE
						: ChessStatus.DIED;
				tr1 = i;
				tc1 = j;
				break;
			}
		}
		for (int i = r - 1, j = c - 1; i > r - 5; i--, j--) {
			if (i < 0 || j < 0) {
				if (chessStatus[2] == ChessStatus.DIED && count < 5) {
					return 0;
				}
				chessStatus[2] = ChessStatus.DIED;
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
				if (count >= 5) {
					return count;
				}
			} else {
				if (chessStatus[2] == ChessStatus.DIED) {
					if (count < 5 && chessMap[i][j] != ChessType.NONE)
						return 0;
				} else {
					chessStatus[2] = chessMap[i][j] == ChessType.NONE ? ChessStatus.ALIVE
							: ChessStatus.DIED;
					tr2 = i;
					tc2 = j;
					// 两头都活 看是否可以延伸
					if (chessStatus[2] == ChessStatus.ALIVE) {
						int tempCount1 = count, tempCount2 = count;
						boolean isAlive1 = false, isAlive2 = false;
						for (int p = tr1 + 1, q = tc1 + 1; p < tr1 + 5; p++, q++) {
							if (p >= GameView.ROWS || q >= GameView.COLS) {
								break;
							}
							if (chessMap[p][q] == chessType) {
								tempCount1++;
							} else {
								isAlive1 = (chessMap[p][q] == ChessType.NONE) ? true
										: false;
								break;
							}
						}
						for (int p = tr2 - 1, q = tc2 - 1; p > tr2 - 5; p--, q--) {
							if (p < 0 || q < 0)
								break;
							if (chessMap[p][q] == chessType) {
								tempCount2++;
							} else {
								isAlive2 = (chessMap[p][q] == ChessType.NONE) ? true
										: false;
								break;
							}
						}
						if (tempCount1 == count && tempCount2 == count) {
							break;
						}
						if (tempCount1 == tempCount2) {
							count = tempCount1;
							chessStatus[2] = (isAlive1 && isAlive2) ? ChessStatus.HALFALIVE
									: ChessStatus.DIED;
						} else {
							count = (tempCount1 > tempCount2) ? tempCount1
									: tempCount2;
							if (count >= 5)
								return 0;
							if (tempCount1 > tempCount2) {
								chessStatus[2] = isAlive1 ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							} else {
								chessStatus[2] = isAlive2 ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							}
						}
					}
				}
				break;
			}
		}
		return count;
	}

	/**
	 * 斜向"/"
	 */
	private int getSloLCount(int r, int c, ChessType chessType) {
		int count = 1;
		int tr1 = r + 1;
		int tc1 = c + 1;
		int tr2 = r - 1;
		int tc2 = c - 1;
		for (int i = r + 1, j = c - 1; i < r + 5; i++, j--) {
			if (i >= GameView.ROWS || j < 0) {
				chessStatus[3] = ChessStatus.DIED;
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
				if (count >= 5)
					return count;
			} else {
				chessStatus[3] = (chessMap[i][j] == ChessType.NONE) ? ChessStatus.ALIVE
						: ChessStatus.DIED;
				tr1 = i;
				tc1 = j;
				break;
			}
		}

		for (int i = r - 1, j = c + 1; i > r - 5; i--, j++) {
			if (i < 0 || j >= GameView.COLS) {
				if (chessStatus[3] == ChessStatus.DIED && count < 5)
					return 0;
				chessStatus[3] = ChessStatus.DIED;
				break;
			}
			if (chessMap[i][j] == chessType) {
				count++;
				if (count >= 5)
					return count;
			} else {
				if (chessStatus[3] == ChessStatus.DIED) {
					if (count < 5 && chessMap[i][j] != ChessType.NONE) {
						return 0;
					}
				} else {
					chessStatus[3] = (chessMap[i][j] == ChessType.NONE) ? ChessStatus.ALIVE
							: ChessStatus.DIED;
					tr2 = i;
					tc2 = j;
					if (chessStatus[3] == ChessStatus.ALIVE) {
						int tempCount1 = count, tempCount2 = count;
						boolean isAlive1 = false, isAlive2 = false;
						for (int p = tr1 + 1, q = tc1 - 1; p < tr1 + 5; p++, q++) {
							if (p >= GameView.ROWS || q < 0) {
								break;
							}
							if (chessMap[p][q] == chessType) {
								tempCount1++;
							} else {
								isAlive1 = chessMap[p][q] == ChessType.NONE ? true
										: false;
								break;
							}
						}
						for (int p = tr2 - 1, q = tc1 + 1; p > tr2 - 5; p--, q++) {
							if (p < 0 || q >= GameView.COLS) {
								break;
							}
							if (chessMap[p][q] == chessType) {
								tempCount2++;
							} else {
								isAlive2 = chessMap[p][q] == ChessType.NONE ? true
										: false;
								break;
							}
						}
						if (tempCount1 == count && tempCount2 == count) {
							break;
						}
						if (tempCount1 == tempCount2) {
							count = tempCount1;
							chessStatus[3] = (isAlive1 && isAlive2) ? ChessStatus.HALFALIVE
									: ChessStatus.DIED;
						} else {
							count = (tempCount1 > tempCount2) ? tempCount1
									: tempCount2;
							if (count >= 5)
								return 0;
							if (tempCount1 > tempCount2) {
								chessStatus[3] = isAlive1 ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							} else {
								chessStatus[3] = isAlive2 ? ChessStatus.HALFALIVE
										: ChessStatus.DIED;
							}
						}
					}
				}
				break;
			}
		}
		return count;
	}
}
