package algorithms;

import java.util.ArrayList;
import java.util.List;

import actions.TradeAction;
import controller.Investment;

public class LalleInvestment extends Investment {

	private final int PRICESTOSTORE = 20;
	private final double MARGIN = 0.01;
	
	private boolean hasStuck = false;
	private double localMinPrice, localMaxPrice, buyPrice, minDiff = 0, maxDiff2, average;
	
	private List<Double> priceHistory = new ArrayList<Double>();
	
	@Override
	public String getInvestmentName() {
		return "Lallegoritme";
	}

	@Override
	public TradeAction tick(double price) {
		
		TradeAction ta = TradeAction.DO_NOTHING;
		
		this.priceHistory.add(price);
		if (this.priceHistory.size() > this.PRICESTOSTORE)
			this.priceHistory.remove(0);
		
		this.average = getAverage(this.priceHistory);
		
		if (!this.hasStuck && price < this.average && !stockIsFalling(this.priceHistory)) {
			ta = TradeAction.BUY;
			this.hasStuck = true;
			this.buyPrice = price;
		} else if ((this.hasStuck && price > this.buyPrice * (1+this.MARGIN)) || (this.hasStuck && stockIsFalling(this.priceHistory))) {
			ta = TradeAction.SELL;
			this.hasStuck = false;
			this.buyPrice = 0;
		}
		
		this.localMinPrice = getLocalMin(this.priceHistory);
		this.localMaxPrice = getLocalMax(this.priceHistory);

		this.minDiff = this.average - this.localMinPrice;
		
		return ta;
	}

	public double getAverage(List<Double> list) {
		double sum = 0;
		for (Double item : list)
			sum += item;
		
		return sum / list.size();
	}
	
	public double getLocalMin(List<Double> list) {
		double min = list.get(0);
		
		for (Double item : list)
			if (item < min) 
				min = item;
		
		return min;
	}
	
	public double getLocalMax(List<Double> list) {
		double max = list.get(0);
		
		for (Double item : list)
			if (item > max) 
				max = item;
		
		return max;
	}
	
	public boolean stockIsFalling(List<Double> list) {
		boolean falling = true;
		double previousPrice = list.get(0);
		int count = 0;
		
		for (Double item : list) {
			if (item <= previousPrice) {
				previousPrice = item;
				count = 0;
			} else {
				count ++;
				previousPrice = item;
				if (count > 1) {
					falling = false;
					break;
				}
			}
		}
		
		
		return falling;
	}
	
}
