package com.example.five;

public class ScoreTable {
	private ScoreTable() {}
	public final static int FIVE = 100;//成5
    public final static int DOUBLE_ALIVE_FOUR = 99;//双活4(分析对手用)
    public final static int ALIVE_FOUR_AND_DEAD_FOUR = 98;//活4死4(对手分析用)
    public final static int ALIVE_FOUR_AND_ALIVE_THREE = 96;//活4活3(分析对手用)
    public final static int ALIVE_FOUR_AND_DEAD_THREE = 95;//活4死3(分析对手用)
    public final static int ALIVE_FOUR_AND_ALIVE_TWO = 94;//活4活2
    public final static int ALIVE_FOUR = 93;//活4
    public final static int DOUBLE_DEAD_FOUR = 92;//双死4
    public final static int DEAD_FOUR_AND_ALIVE_THREE = 91;//死4活3
    public final static int DEAD_FOUR_AND_ALIVE_TWO = 90;//死4活2
    public final static int DOUBLE_ALIVE_THREE = 80;//双活3
    public final static int ALIVE_THREE_AND_DEAD_THREE = 70;//活死3
    public final static int HALF_ALIVE_FOUR = 65;//半活4(类似○○ ○形),优先级小于活4
    public final static int ALIVE_THREE = 60;//活3
    public final static int DEAD_FOUR = 50;//死4
    public final static int DOUBLE_ALIVE_TWO = 40;//双活2
    public final static int DEAD_THREE = 30;//死3
    public final static int ALIVE_TWO = 20;//活2
    public final static int DEAD_TWO = 10;//死2
    public final static int SINGLE = 0;//单个
}
