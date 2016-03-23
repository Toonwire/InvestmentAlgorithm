package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.Timer;

import actions.TradeAction;
import model.Model;
import view.View;

public class Controller implements ActionListener {

	private Model model;
	private View view;
	
	private Timer timer = new Timer(20, this);
	private double price = 0;
	private Scanner s;
	
	private DecimalFormat df = new DecimalFormat("#.#");
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		
		try{
			
			setupInvestments();
			model.loadStocks();
			
			File file = model.getNextStockFile();
			this.s = new Scanner(file);
			view.getStockChart().setInitialStockYear(model.getCurrentStockYear());
			
			timer.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupInvestments() {
		try {
			model.getAlgorithms().clear();
			model.loadAlgorithms();
			view.getBalanceMap().clear();
			view.setBalancePanelGridRows(model.getAlgorithms().keySet().size());
			for (Investment investment : model.getAlgorithms().keySet()) {
				JLabel balanceLabel = view.generateBalanceLabel();
				view.getBalanceMap().put(investment, balanceLabel);
				view.getBalancePanel().add(balanceLabel);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (s.hasNextLine()) {
			double min = Double.MAX_VALUE, max = Double.MIN_VALUE, maxDiff = 0;
			
			double oldPrice = price;
			price = Double.parseDouble(s.nextLine());
			
			for (Investment investment : model.getAlgorithms().keySet()) {
				DataSet data = model.getAlgorithms().get(investment);
				
				TradeAction action = investment.tick(price);
				switch (action) {
				case BUY :
					data.setHasStock(true);
					break;
				case SELL :
					if (data.hasStock()) {
						data.setBalance(data.getBalance() + price - oldPrice);
						data.setHasStock(false);
					}
					break;
				default :
					if (data.hasStock())
						data.setBalance(data.getBalance() + price - oldPrice);
					break;
				}
				
				model.getAlgorithms().put(investment, data);
				
				double balance = data.getBalance();
				view.getStockChart().addStockPrice(price);
				view.getBarChart().updateBalance(investment.getInvestmentName(), balance);
				view.getStockPriceLabel().setText("€ " + price);
				
				view.getBalanceMap().get(investment).setText("" + df.format(balance).replace(',', '.'));
				
				if (balance < min) min = balance;
				if (balance > max) max = balance;
				if (balance < 0 && max < 0) max = 0;
				else if (balance > 0 && min > 0) min = 0;
				maxDiff = Math.abs(max) + Math.abs(min);
				
			}	
			// set proper range +10 padding on the barChart
			view.getBarChart().setBalanceRange((maxDiff+max+10)*2);
			view.getStockNameLabel().setText(filterStockName(model.getCurrentStockFile().getName()));
			
		} else {
			try {
				if (!model.isAllFilesRead()) {
					view.reset();
					setupInvestments();
					
					// initialize stockChart.stockDay with the new setup
					view.getStockChart().setInitialStockYear(model.getCurrentStockYear());
					
					Thread.sleep(2000);
					this.s = new Scanner(model.getNextStockFile());
					
				}
				else {
					timer.stop();
					this.s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String filterStockName(String name) {		
		String[] substrings = name.substring(0,name.length()-4).split("_");
		String stockName = substrings[1];
		String stockStartYear = substrings[2];
		String stockEndYear = substrings[3];
		
		return "Stock: " + stockName + " from " + stockStartYear + " to " + stockEndYear;
	}
}
