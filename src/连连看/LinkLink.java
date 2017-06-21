package 连连看;

import com.sun.xml.internal.bind.v2.TODO;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LinkLink extends JFrame implements ActionListener {
	JFrame mainFrame;//游戏主框架
	Container mainContainer;//框架容器
	JPanel nPanel, cPanel, sPanel;//上中下三个panel
	JButton gameButton[][] = new JButton[6][5];//游戏按钮组
	JButton newButton, resetButton, exitButton;//新游戏|重排|退出游戏
	JLabel scoreTipLabel = new JLabel("当前分数：");
	JLabel scoreLabel = new JLabel("0");//分数标签
	JLabel ruleTipLabel = new JLabel("游戏规则：" +
			"行内消除加分100；" +
			"一次拐角消除加分100；" +
			"两次拐角消除加分100；" +
			"重新排列扣分1000" +
			"");
	JButton firstClick, secondClick;//记录先后点击的两个游戏按钮
	int data[][] = new int[8][7];//游戏按钮数据
	static Boolean isSelect = false;//是否选中了游戏按钮
	int x1 = 0, y1 = 0, x2 = 0, y2 = 0, firstValue = 0, secondValue = 0;//先后点击的两个按钮的坐标和值
	
	
	/**
	 * 游戏启动入口
	 *
	 * @param args 命令行运行参数
	 */
	public static void main(String[] args) {
		LinkLink llk = new LinkLink();
		llk.randomData();//初始化数据
		llk.buildFrame();//初始化面板
		llk.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	/**
	 * 生成随机按钮值数据
	 */
	public void randomData() {
		int rand, c, r;//随机值,行标,列标
		for (int group = 1; group <= 15; group++) {
			rand = (int) (Math.random() * 30 + 1);//生成1-30的随机数
			for (int num = 1; num <= 2; num++) {
				c = (int) (Math.random() * 6 + 1);//随机列
				r = (int) (Math.random() * 5 + 1);//随机行
				while (data[c][r] != 0) {//已经赋值了的按钮,重新随机选择一个
					c = (int) (Math.random() * 6 + 1);//随机列
					r = (int) (Math.random() * 5 + 1);//随机行
				}
				data[c][r] = rand;//对这个空按钮位置赋值
			}
		}
	}
	
	/**
	 * 搭建JFrame框架,填充分数标签,控制按钮,游戏按钮到面板,并显示面板
	 */
	public void buildFrame() {
		mainFrame = new JFrame("连连看");//实例化游戏总框架
		mainContainer = mainFrame.getContentPane();//获取JFrame的内容区域
		mainContainer.setLayout(new BorderLayout());//设置为BorderLayout布局
		nPanel = new JPanel();//上方分数面板
		cPanel = new JPanel();//中间游戏面板
		sPanel = new JPanel();//下方控制面板
		mainContainer.add(nPanel, "North");//添加上面板到内容区域
		mainContainer.add(cPanel, "Center");//添加中面板到内容区域
		mainContainer.add(sPanel, "South");//添加下面板到内容区域
		cPanel.setLayout(new GridLayout(6, 5));//设置游戏面板为网格布局
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				gameButton[i][j] = new JButton(String.valueOf(data[i + 1][j + 1]));//实例化按钮,并从data取值
				gameButton[i][j].addActionListener(this);
				cPanel.add(gameButton[i][j]);
			}
		}
		//实例化三个控制按钮并添加事件监听器
		newButton = new JButton("重新开始");
		newButton.addActionListener(this);
		resetButton = new JButton("重新排列(-1000分)");
		resetButton.addActionListener(this);
		exitButton = new JButton("退出游戏");
		exitButton.addActionListener(this);
		//添加三个控制按钮到sPanel控制面板
		sPanel.add(newButton);
		sPanel.add(resetButton);
		sPanel.add(exitButton);
		//修改分数标签内容并添加到nPanel头部面板
		scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText())));
		nPanel.add(scoreTipLabel);
		nPanel.add(scoreLabel);
		nPanel.add(ruleTipLabel);
		//修改主框架参数
		mainFrame.setLocationRelativeTo(null);//居中
		mainFrame.setSize(900, 800);//大小
		mainFrame.setVisible(true);//显示
	}
	
	/**
	 * 重排游戏按钮
	 */
	public void reset() {
		int oldData[] = new int[30];//用来存储原先游戏数据
		int newData[][] = new int[8][7];//全部置空
		int n = 0, c, r;
		for (int i = 1; i <= 6; i++) {
			for (int j = 1; j <= 5; j++) {
				if (data[i][j] != 0) {
					oldData[n] = data[i][j];
					n++;
				}
			}
		}
		n -= 1;
		data = newData;//重置实例变量data
		while (n >= 0) {
			c = (int) (Math.random() * 6 + 1);
			r = (int) (Math.random() * 5 + 1);
			while (data[c][r] != 0) {
				c = (int) (Math.random() * 6 + 1);
				r = (int) (Math.random() * 5 + 1);
			}
			data[c][r] = oldData[n];
			n--;
		}
		mainFrame.setVisible(false);
		isSelect = false;
		buildFrame();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				if (data[i + 1][j + 1] == 0) {
					gameButton[i][j].setVisible(false);
				}
			}
		}
		cutScore(1000);
	}
	
	/**
	 * 事件监听器
	 *
	 * @param e 事件对象
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newButton) {
			data = new int[8][7];//重置数据
			randomData();//随机新数据
			mainFrame.setVisible(false);//隐藏窗体
			isSelect = false;//清除点击
			scoreLabel.setText("0");
			buildFrame();//重新建立窗体
		}
		if (e.getSource() == resetButton) {
			reset();
		}
		if (e.getSource() == exitButton) {
			System.exit(0);
		}
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				if (e.getSource() == gameButton[i][j]) {
					clickAction(i, j, gameButton[i][j]);
				}
			}
		}
	}
	
	
	/**
	 * 点击一个按钮之后所做的事
	 *
	 * @param col   列
	 * @param row   行
	 * @param click 点击的按钮对象
	 */
	public void clickAction(int col, int row, JButton click) {
		System.out.println("(" + (col + 1) + "," + (row + 1) + ")");
		if (!isSelect) {//如果是游戏刚开始,还没有点击按钮的状态
			x2 = col + 1;
			y2 = row + 1;
			secondClick = click;
			secondValue = data[x2][y2];
			isSelect = true;
		} else {//如果不是未点击按钮状态,按钮记录信息向前推
			x1 = x2;
			y1 = y2;
			x2 = col + 1;
			y2 = row + 1;
			firstClick = secondClick;
			firstValue = secondValue;
			secondClick = click;
			secondValue = data[x2][y2];
			if (firstValue == secondValue && secondClick != firstClick) {//点击了两个按钮之后,进行判断
				judgeEliminate();//执行消除判断函数
			}
		}
	}
	
	/**
	 * 根据当前实例变量中的两个按钮信息进行判断
	 * 如果能消除,执行hideButton函数
	 * 如果不能消除,不做任何事情
	 */
	public void judgeEliminate() {
		if (canLineEliminate(x1, y1, x2, y2)) {//能行消
			addScore(100);
			hideButton();
		} else {//不能行消
			if (canOneAngleEliminate(x1, y1, x2, y2)) {//能一个拐角消除
				addScore(200);
				hideButton();
			} else {//不能一个拐角消除
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 7; j++) {
						if (data[i][j] == 0 && ((canLineEliminate(i, j, x1, y1) && canOneAngleEliminate(i, j, x2, y2)) || (canLineEliminate(i, j, x2, y2) && canOneAngleEliminate(i, j, x1, y1)))) {
							addScore(300);
							hideButton();
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * 判断在同一行内的(a,b)和(c,d)是否能进行消除
	 * 行消
	 *
	 * @param a x1
	 * @param b y1
	 * @param c x2
	 * @param d y2
	 * @return boolean can?
	 */
	public Boolean canLineEliminate(int a, int b, int c, int d) {
		if ((a == c && (b == d + 1 || b == d - 1)) || (b == d && (a == c + 1 || a == c - 1))) {//判断点击的两个格子是否相邻
			return true;
		}
		Boolean can = true;
		if (a == c) {//在同一列
			int i, j;
			if (b > d) {
				i = d + 1;
				j = b;
			} else {
				i = b + 1;
				j = d;
			}
			for (; i < j; i++) {
				if (data[a][i] != 0) {
					can = false;
				}
			}
		} else if (b == d) {//在同一行
			int i, j;
			if (a > c) {
				i = c + 1;
				j = a;
			} else {
				i = a + 1;
				j = c;
			}
			for (; i < j; i++) {
				if (data[i][b] != 0) {
					can = false;
				}
			}
		} else {
			can = false;
		}
		return can;
	}
	
	/**
	 * 判断是否可以一个拐角消除
	 *
	 * @param a x1
	 * @param b y1
	 * @param c x2
	 * @param d y2
	 * @return can?
	 */
	public Boolean canOneAngleEliminate(int a, int b, int c, int d) {
		if (canLineEliminate(a, b, a, d) && canLineEliminate(c, d, a, d)) {
			return data[a][d] == 0;
		} else if (canLineEliminate(a, b, c, b) && canLineEliminate(c, d, c, b)) {
			return data[c][b] == 0;
		} else {
			return false;
		}
	}
	
	/**
	 * 执行消除两个已选格子的操作
	 */
	
	public void hideButton() {
		//隐藏两个可消除按钮
		firstClick.setVisible(false);
		secondClick.setVisible(false);
		//重置选定状态
		isSelect = false;
		//清除消除格子的数据
		data[x1][y1] = 0;
		data[x2][y2] = 0;
	}
	
	public void addScore(int num) {
		scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText()) + num));
	}
	
	public void cutScore(int num) {
		scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText()) - num));
	}
}
