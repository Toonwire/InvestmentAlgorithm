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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.Investment;
import model.Model;

public class View extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Color CUSTOM_GRIDLINE_GRAY = new Color(56,52,67);
	private static final Color CUSTOM_DAMPENED_CYAN = new Color(109,186,197);
	private static final Color CUSTOM_DAMPENED_CYAN2 = new Color(20,197,216);
	
	private StockChart stockChart;
	private BarChart barChart;
	private JPanel mergedPanel, stockPanel, investorPanel, stockGraphPanel,
					investorBarChartPanel, balancePanel;
	
	private WinnerPanel winnerPanel;
	private JLabel stockNameLabel, stockPriceLabel, winnerLabel;
	private JLayeredPane layerPane;
	private Map<Investment, JLabel> balanceMap;
	

	
	public View(Model model){		
		super("Title");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(600,500));
		this.setLayout(new BorderLayout());
		
		this.stockChart = new StockChart("Stock Prices");
		this.barChart = new BarChart("Top Investors");
		
		this.balanceMap = new HashMap<Investment, JLabel>();
		
		this.stockPanel = new JPanel(new BorderLayout());
		this.stockGraphPanel = stockChart.getChartPanel();
		this.stockNameLabel = new JLabel("");
		this.stockPriceLabel = new JLabel("");
		
		stockGraphPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		stockGraphPanel.setBackground(Color.BLACK); 
		
		stockNameLabel.setForeground(new Color(1,100,100));
		stockNameLabel.setFont(new Font("Helvetica", Font.ITALIC, 18));
		stockNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		stockPriceLabel.setForeground(new Color(28,166,196));
		stockPriceLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		stockPriceLabel.setFont(new Font("Helvetica", Font.BOLD, 28));
		
		stockPanel.setBackground(Color.BLACK);
		stockPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		stockPanel.add(stockGraphPanel, BorderLayout.CENTER);
		stockPanel.add(stockNameLabel, BorderLayout.NORTH);
		stockPanel.add(stockPriceLabel, BorderLayout.EAST);
	
		this.investorPanel = new JPanel(new BorderLayout());
		this.balancePanel = new JPanel();
		balancePanel.setBackground(Color.BLACK);
		balancePanel.setBorder(BorderFactory.createEmptyBorder(24,10,42,50));
		
		investorPanel.setBackground(Color.BLACK);
		investorPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CUSTOM_GRIDLINE_GRAY));	// top, left, bottom, right
		
		this.investorBarChartPanel = barChart.getChartPanel();
		
		investorPanel.add(investorBarChartPanel,BorderLayout.CENTER);
		investorPanel.add(balancePanel, BorderLayout.EAST);
		
		
		GridLayout gridLayout = new GridLayout(2,1);
		gridLayout.setVgap(15);
		this.mergedPanel = new JPanel(gridLayout);
		mergedPanel.setBackground(Color.BLACK);
		mergedPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		mergedPanel.add(stockPanel);
		mergedPanel.add(investorPanel);
		
		
		this.layerPane = new JLayeredPane();
		layerPane.setBounds(0,0,600,500);
		mergedPanel.setBounds(0, 0, 600, 471);	// 471 in y-direction 'cuz reasons..
		
		this.winnerLabel = new JLabel("");
		winnerLabel.setForeground(CUSTOM_DAMPENED_CYAN2);
		winnerLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		winnerLabel.setVisible(true);
		
		this.winnerPanel = new WinnerPanel();
		winnerPanel.setBounds(180,130,240,220);
		winnerPanel.setVisible(false);
		winnerPanel.add(winnerLabel);
		
		layerPane.add(mergedPanel, new Integer(0),0);
		layerPane.add(winnerPanel, new Integer(1),0);
		
		this.add(layerPane, BorderLayout.CENTER);
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
		label.setForeground(new Color(28,166,196));
		label.setForeground(CUSTOM_DAMPENED_CYAN);
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
		stockPanel.add(stockGraphPanel, BorderLayout.CENTER);
		
		investorPanel.remove(investorBarChartPanel);
		this.barChart = new BarChart("Top Investors");
		this.investorBarChartPanel = barChart.getChartPanel();
		investorPanel.add(investorBarChartPanel, BorderLayout.CENTER);
		
		balancePanel.removeAll();
		
	}

	public void announceWinner() {
		winnerPanel.setVisible(true);
	}

	public JLabel getWinnerLabel() {
		return this.winnerLabel;
	}

	public WinnerPanel getWinnerPanel() {
		return this.winnerPanel;
	}
}