package controller;

import actions.TradeAction;

public abstract class Investment {
	
	public abstract String getInvestmentName();
	public abstract TradeAction tick(double price);
}
