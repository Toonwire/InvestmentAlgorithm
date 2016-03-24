package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import controller.Investment;
import actions.TradeAction;

public class ToonwireInvestment extends Investment {
	
	LinkedList<Double> priceList = new LinkedList<>();
	private double lastPrice = 0;
	private int count = 0;
	private double min = 0;
	private double average = 0;
	private List<Double> averageList = new ArrayList<>();
	private boolean hasStock = false;
	private double maxDiff;
	private double diff;

	@Override
	public String getInvestmentName() {
		return "Toonwire";
	}
	
	@Override
	public TradeAction tick(double price) {
		TradeAction ta = TradeAction.DO_NOTHING;
	
		if (count < 50) {
			averageList.add(price);
		} else {
			averageList.remove(0);
			averageList.add(price);
		}
		
		int sum = 0;
		for (Double d : averageList) {
			sum += d;
		}
		
		average = sum / averageList.size();
		
		
		
		if (count != 0) {
			diff += price - lastPrice;
		} else {
			min = price;
		}
		
	   lastPrice = price;
		
		if (shouldBuy(price) && !hasStock) {
			ta = TradeAction.BUY;
			this.hasStock = true;
			
			
		} else if (shouldSell(price) && hasStock) {
			ta = TradeAction.SELL;
			this.hasStock = false;
		}
		
		if (price < min) {
			min = price;
			diff = 0;
		}
		count++;
	    return ta;
	}


	private boolean shouldSell(double value) {
		boolean sell = false;
		
		if (diff > maxDiff) {
			maxDiff = diff;
			sell = true;
		}
		
		return sell;
	}


	private boolean shouldBuy(double value) {
		return value < average*0.90;
	}


	
}
