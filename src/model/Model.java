package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.DataSet;
import controller.Investment;

public class Model {
	
	private Map<Investment, DataSet> algorithms;
	private List<File> stockFiles;
	private int stockCount = 0, currentStockYear = 0;
	private File currentStockFile;
	
	public Model() {
		this.algorithms = new HashMap<>();
		this.stockFiles = new ArrayList<>();
	}
	
	public void loadStocks() {
		for (File file : new File("stocks").listFiles()) {
			this.stockFiles.add(file);
		}
	}

	public void loadAlgorithms() throws Exception {
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

	public int getInvestorCount() {
		return this.algorithms.keySet().size();
	}

	public List<File> getStockFiles() {
		return this.stockFiles;
	}

	public Map<Investment, DataSet> getAlgorithms() {
		return this.algorithms;
	}
	
	public void runInvestments(double price) {
		
	}

	public File getNextStockFile() {
		this.currentStockFile = this.stockFiles.get(stockCount);
		
		// expecting that the stockPrice files contain a 4-digit long year in name
		// ex. "stockPrices_DanskeBank_2007_2008.txt"
		Pattern p = Pattern.compile("\\d{4}");
		Matcher m = p.matcher(currentStockFile.getName());
		if (m.find()) {
			this.currentStockYear = Integer.parseInt(m.group());
		}
		this.stockCount++;
		
		return currentStockFile;

	}

	public boolean isAllFilesRead() {
		return this.stockCount == this.stockFiles.size();
	}

	public int getCurrentStockYear() {
		return this.currentStockYear;
	}

	public File getCurrentStockFile() {
		return this.currentStockFile;
	}
}
