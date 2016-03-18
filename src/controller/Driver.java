package controller;

import java.io.FileNotFoundException;

public class Driver {

	
	public static void main(String[] args) {
		ControlUnit ctrlUnit = new ControlUnit();
		
		try {
			ctrlUnit.runInvestments();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
