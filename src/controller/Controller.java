package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.Timer;

import actions.TradeAction;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import model.Model;
import view.View;

public class Controller implements ActionListener, MouseListener {

	private Model model;
	private View view;
	
	private Timer timer = new Timer(80, this);
	private double price = 0;
	private Scanner s;
	
	private DecimalFormat df = new DecimalFormat("#.#");
	private String currencySymbol = "\u20ac";
	
	private File soundFile = new File("sounds/darude.mp3");
	private MediaPlayer mediaPlayer;
	private boolean atEndOfMedia = false;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		view.registerListeners(this);
		
		try{
			setupInvestments();
			model.loadStocks();
			
			File file = model.getNextStockFile();
			this.s = new Scanner(file);
			view.getStockChart().setInitialStockYear(model.getCurrentStockYear());
			
			/* 
			 * TODO: make winnerPanel visible with a list of the loaded stock files.
			 * make the stock files "checkable" allowing for specified selection of stock files to run
			 * and not just running them all be default
			 */
			
			loadSound();
			view.setSoundIcon(false);
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
	public void actionPerformed(ActionEvent ae) {
		
		if (view.getWinnerPanel().isVisible()) {
			view.reset();
			setupInvestments();
			// initialize stockChart.stockDay with the new setup
			view.getStockChart().setInitialStockYear(model.getCurrentStockYear());

			// stop and await mouseClick to re-fire
			timer.stop();
			
		} else if (s.hasNextLine()) {
			double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, maxDiff = 0;
			
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
				view.getStockPriceLabel().setText(currencySymbol + " " + price);
				
				JLabel balanceLabel = view.getBalanceMap().get(investment);
				balanceLabel.setText("" + df.format(balance).replace(',', '.'));
				
				if (balance < min) min = balance;
				if (balance > max) max = balance;
				if (balance < 0 && max < 0) max = 0;
				else if (balance > 0 && min > 0) min = 0;
				maxDiff = Math.abs(max) + Math.abs(min);
				
			}	
			// set proper range +10 padding on the barChart
			view.getBarChart().setBalanceRange((maxDiff+10)*2);
			view.getStockNameLabel().setText(filterStockName(model.getCurrentStockFile().getName()));
			
		} else {
			try {
				Investment winner = model.getWinner();
				
				if (!model.isAllFilesRead()) {
					view.getWinnerLabel().setText(getWinTextHTML(winner));
					view.announceWinner();
					
				}
				else {
					view.getWinnerLabel().setText(getWinTextHTML(winner));
					view.announceWinner();
					timer.stop();
					this.s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getWinTextHTML(Investment winner) {
		// as for html text centering we use   <div style='text-align: center;'>
		// in place of the usual label.setHorizontalAlignment(SwingConstants.CENTER)
		return "<html><div style='text-align: center;'>"
				+ 	"<h1 style=font-size:26'>"
				+ 		"<u style='padding:10'>Congratulations!"
				+ 	"</h1>"
				+ 	"<body>"
				+		"<b style='font-size:22;'>~~~ Winner ~~~ </b><br>"
				+		"<i style='color:#669C2D; font-family:helvetica; font-size:24;'>" + winner.getInvestmentName() + "</i><br><br>"
				+ 		"<b style='font-size:22;'>Total balance(" + currencySymbol +"):</b><br>"
				+		"<i style='color:#669C2D; font-family:helvetica; font-size:24;'>" + df.format(model.getAlgorithms().get(winner).getBalance()).replace(',', '.') + "</i>"
				+ 	"</body>"
				+ "</html>";

	}

	private String filterStockName(String name) {		
		String[] substrings = name.substring(0,name.length()-4).split("_");
		String stockName = substrings[1];
		String stockStartYear = substrings[2];
		String stockEndYear = substrings[3];
		
		return "Stock: " + stockName + " from " + stockStartYear + " to " + stockEndYear;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getComponent().getName().equals("winnerPanel") && me.getClickCount() == 2) {
			me.getComponent().setVisible(false);
			/*
			 *  this if can be moved to above the 'if' to not
			 *  allow the last winnerPanel to be hidden
			 */
			if (!model.isAllFilesRead()){
				try {
					this.s = new Scanner(model.getNextStockFile());
					timer.start();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}				
			} else {
				/*
				 * scale the last barChart a bit nicer?
				 */
//				view.getBarChart().getRangeAxis().setAutoRange(true);
			}
		} else if (me.getComponent().getName().equals("soundLabel")) {
			Status status = mediaPlayer.getStatus();
			if (status == MediaPlayer.Status.PLAYING) 
				pauseSound();
			else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY)
				playSound();
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		
	}

	@Override
	public void mouseExited(MouseEvent me) {
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		
	}
	
	public void loadSound() {
		new javafx.embed.swing.JFXPanel(); // forces JavaFX init - needed for .mp3 playing
		
	    String uriString = soundFile.toURI().toString();
	    this.mediaPlayer = new MediaPlayer(new Media(uriString));
	    mediaPlayer.setOnEndOfMedia(new Runnable() {
	    	public void run() {
	    		atEndOfMedia = true;
	    	}
	    });
	}
	
	public void pauseSound() {
        mediaPlayer.pause();
        view.setSoundIcon(false);
        
    }

    public void playSound() {
    	if (!atEndOfMedia) {
	    	mediaPlayer.play();
	    	view.setSoundIcon(true);
    	}
    }
}
