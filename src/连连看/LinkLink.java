package 连连看;

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
	JLabel scoreLabel = new JLabel("0");//分数标签
	JButton firstClick, sectionClick;//记录先后点击的两个游戏按钮
	int data[][] = new int[6][5];//游戏按钮数据
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
	}
	
	/**
	 * 生成随机按钮值数据
	 */
	public void randomData() {
		int rand, c, r;//随机值,行标,列标
		for (int group = 1; group <= 15; group++) {
			rand = (int) (Math.random() * 30 + 1);//生成1-30的随机数
			for (int num = 1; num <= 2; num++) {
				c = (int) (Math.random() * 6);//随机列
				r = (int) (Math.random() * 5);//随机行
				while (data[c][r] != 0) {//已经赋值了的按钮,重新随机选择一个
					c = (int) (Math.random() * 6);//随机列
					r = (int) (Math.random() * 5);//随机行
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
				gameButton[i][j] = new JButton(String.valueOf(data[i][j]));//实例化按钮,并从data取值
				gameButton[i][j].addActionListener(this);
				cPanel.add(gameButton[i][j]);
			}
		}
		//实例化三个控制按钮并添加事件监听器
		newButton = new JButton("重新开始");
		newButton.addActionListener(this);
		resetButton = new JButton("重新排列");
		resetButton.addActionListener(this);
		exitButton = new JButton("退出游戏");
		exitButton.addActionListener(this);
		//添加三个控制按钮到sPanel控制面板
		sPanel.add(newButton);
		sPanel.add(resetButton);
		sPanel.add(exitButton);
		//修改分数标签内容并添加到nPanel头部面板
		scoreLabel.setText(String.valueOf(Integer.parseInt(scoreLabel.getText())));
		nPanel.add(scoreLabel);
		//修改主框架参数
		mainFrame.setLocationRelativeTo(null);//居中
		mainFrame.setSize(500, 450);//大小
		mainFrame.setVisible(true);//显示
	}
	
	/**
	 * 事件监听器
	 *
	 * @param e 事件对象
	 */
	public void actionPerformed(ActionEvent e) {
	
	}
}
