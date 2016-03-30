package controller;

import model.Model;
import view.View;

public class Driver {

	public static void main(String[] args) {
		Model model = new Model();
		View view = new View(model);
		new Controller(model, view);
		
	}
}
