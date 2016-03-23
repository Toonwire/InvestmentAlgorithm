package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.Investment;
import model.Model;

public class View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Color CUSTOM_RED = new Color(241, 90, 96);
	private static final Color CUSTOM_GREEN = new Color(122, 195, 106);
	private static final Color CUSTOM_BLUE = new Color(90, 155, 212);
	private static final Color CUSTOM_DARK_GREEN = new Color(2, 125, 125);
	private static final Color CUSTOM_GRIDLINE_GRAY = new Color(56,52,67);
	
	private Model model;
	private StockChart stockChart;
	private BarChart barChart;
	private JPanel mergedPanel, stockPanel, investorPanel, stockGraphPanel, investorBarChartPanel, balancePanel;
	private JLabel stockNameLabel, stockPriceLabel;
	private Map<Investment, JLabel> balanceMap;
	
	public View(Model model){		
		super("Title");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(600,500));
		this.model = model;
		this.stockChart = new StockChart("Stock Prices");
		this.barChart = new BarChart("Top Investors");
		
		this.balanceMap = new HashMap<Investment, JLabel>();
		
		this.stockPanel = new JPanel(new BorderLayout());
		this.stockGraphPanel = stockChart.getChartPanel();
		this.stockNameLabel = new JLabel("Danske Bank @ 2007-2008");
		this.stockPriceLabel = new JLabel("€ 144.89");
		
		stockGraphPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		stockGraphPanel.setBackground(Color.BLACK); 
		
		stockNameLabel.setForeground(new Color(1,100,100));
		stockNameLabel.setFont(new Font("Helvetica", Font.ITALIC, 18));
		stockNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		stockPriceLabel.setForeground(new Color(8,225,225));
		stockPriceLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		stockPriceLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		
		stockPanel.setBackground(Color.BLACK);
		stockPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		stockPanel.add(stockGraphPanel, BorderLayout.CENTER);
		stockPanel.add(stockNameLabel, BorderLayout.NORTH);
		stockPanel.add(stockPriceLabel, BorderLayout.EAST);
	
		this.investorPanel = new JPanel(new BorderLayout());
		this.balancePanel = new JPanel();
		balancePanel.setBackground(Color.BLACK);
		balancePanel.setBorder(BorderFactory.createEmptyBorder(15,10,30,50));
		
		investorPanel.setBackground(Color.BLACK);
		investorPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CUSTOM_GRIDLINE_GRAY));	// top, left, bottom, right
		
		this.investorBarChartPanel = barChart.getChartPanel();
		
		investorPanel.add(investorBarChartPanel,BorderLayout.CENTER);
		investorPanel.add(balancePanel, BorderLayout.EAST);
		
		
		this.mergedPanel = new JPanel(new GridLayout(2,1));
		mergedPanel.setBackground(Color.BLACK);
		mergedPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		mergedPanel.add(stockPanel);
		mergedPanel.add(investorPanel);
		this.add(mergedPanel);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public JPanel getStockPanel() {
		return this.stockPanel;
	}
	
	public StockChart getStockChart() {
		return this.stockChart;
	}
	
	public JLabel getStockPriceLabel() {
		return this.stockPriceLabel;
	}
	
	public JLabel getStockNameLabel() {
		return this.stockNameLabel;
	}

	public BarChart getBarChart() {
		return this.barChart;
	}
	
	public JPanel getBalancePanel() {
		return this.balancePanel;
	}
	
	public Map<Investment, JLabel> getBalanceMap() {
		return this.balanceMap;
	}

	public JLabel generateBalanceLabel() {
		JLabel label = new JLabel("");
		label.setForeground(new Color(156,208,136));
		label.setFont(new Font("Helvetica", Font.PLAIN, 18));
		
		return label;
	}
	
	public void setBalancePanelGridRows(int rows) {
		this.balancePanel.setLayout(new GridLayout(rows,1));
	}
	
	public void reset() {
		stockPanel.remove(stockGraphPanel);
		this.stockChart = new StockChart("Stock Prices");
		this.stockGraphPanel = stockChart.getChartPanel();
		stockPanel.add(stockGraphPanel);
		balancePanel.removeAll();
		
		
		
	}
}