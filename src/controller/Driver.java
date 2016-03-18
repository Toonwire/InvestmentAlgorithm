package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import actions.TradeAction;
import algorithms.NruneInvestment;
import algorithms.ToonwireInvestment;

public class Driver {

	private final static File chosenStock = new File("stocks/stockPrices_DanskeBank_2007_2008.txt");
	
	public static void main(String[] args) {
		Map<Investment, DataSet> algorithms = new HashMap<>();
		double price = 0, oldPrice = 0;
		
		try {			
			for (Class<?> c : findClasses(new File("bin/algorithms"), "algorithms")){
				Class<?> clazz = Class.forName(c.getName());
				Object object = clazz.newInstance();
				algorithms.put((Investment) object, new DataSet());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			
			Scanner s = new Scanner(chosenStock);
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
			
			for (Investment investment : algorithms.keySet()) 
				System.out.println(investment.getInvestmentName() + ":   \t" + algorithms.get(investment).getBalance());			
			
			s.close();
			
		} catch(FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}
	}   
	
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		
		for (File file : directory.listFiles())
			if (file.getName().endsWith(".class"))
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
		
		return classes;
	}
}
