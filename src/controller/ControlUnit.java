package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import actions.TradeAction;

public class ControlUnit {

	private Map<Investment, DataSet> algorithms;
	private List<File> stockFiles;
	
	public ControlUnit() {
		this.algorithms = new HashMap<>();
		this.stockFiles = new ArrayList<>();
		
		try{
			this.loadAlgorithms();
			this.loadStocks();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runInvestments() throws FileNotFoundException {
		double price = 0, oldPrice = 0;
		
		for (File file : this.stockFiles) {
			
			Scanner s = new Scanner(file);
			while(s.hasNextLine()) {
				oldPrice = price;
				price = Double.parseDouble(s.nextLine());
				
				for (Investment investment : algorithms.keySet()) {
					DataSet data = algorithms.get(investment);
					
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
				}	
			}
			
			printBalances();
			s.close();
			
			try {
				reinstatiateAlgorithms();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
	}
	
	private void reinstatiateAlgorithms() throws Exception {
		this.algorithms.clear();
		loadAlgorithms();
		
	}

	private void printBalances() {
		for (Investment investment : algorithms.keySet()) 
			System.out.println(investment.getInvestmentName() + ":   \t" + algorithms.get(investment).getBalance());			
		
		
	}

	private void loadStocks() {
		for (File file : new File("stocks").listFiles()) {
			this.stockFiles.add(file);
		}
	}

	private void loadAlgorithms() throws Exception {
		for (Class<?> c : findInvestmentClasses(new File("bin/algorithms"), "algorithms")){
			Investment tradeAlgo = (Investment) Class.forName(c.getName()).newInstance();
			this.algorithms.put(tradeAlgo, new DataSet());
		}
	}
	
	private List<Class<?>> findInvestmentClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		
		for (File file : directory.listFiles())
			if (file.getName().endsWith(".class"))
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
		
		return classes;
	}
}
