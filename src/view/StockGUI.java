package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.data.time.Day;

import controller.ControlUnit;

public class StockGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 500;
	
	private double currentStockPrice;
	
	private Timer timer = new Timer(100, this);
	private ControlUnit ctrl;
	private JPanel stockPanel, userPanel;
	private JLabel stockNameLabel, stockPriceLabel, userNameLabel;
	private final StockChart stockChart = new StockChart("Stock Prices");
	
	public StockGUI(ControlUnit ctrl) {
		super("View");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(SIZE,SIZE));
		this.setLayout(new BorderLayout());
		
		this.ctrl = ctrl;
		this.stockPanel = new JPanel();
		this.userPanel = new JPanel();
		
		stockPanel.setLayout(new BorderLayout());
		userPanel.setLayout(new GridLayout(ctrl.getInvestorCount(), 2, 0, 15));
		
		this.stockNameLabel = new JLabel("");
		this.stockPriceLabel = new JLabel("");
		this.userNameLabel = new JLabel("");
		
		
		stockPanel.add(stockChart.getChartPanel(), BorderLayout.PAGE_START);
		stockPanel.add(stockPriceLabel);
		stockPanel.add(stockNameLabel);
		
		stockPanel.setBackground(Color.BLACK);
		userPanel.setBackground(Color.BLUE);
		
		
		this.add(stockPanel, BorderLayout.NORTH);
		this.add(userPanel, BorderLayout.CENTER);
		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.stockChart.getDataSeries().add(new Day(), this.currentStockPrice);
		
	}
	
	public void setCurrentStockPrice(double price) {
		this.currentStockPrice  = price;
	}
	
}
