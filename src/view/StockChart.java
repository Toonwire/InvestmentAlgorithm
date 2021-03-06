package view;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class StockChart {
	
	private static final Color CUSTOM_GRIDLINE_GRAY = new Color(56,52,67);
	private static final int MAXIMUM_POINTS = 300;
	private static final Color CUSTOM_TICKS_COLOR = new Color(1,140,112);
	private static final Color CUSTOM_GREEN = new Color(122,195,106);
	

	private JFreeChart chart;
	private XYDataset dataset;
	private TimeSeries series;
	private ChartPanel chartPanel;
	private Day stockDay;
	private String title;
	private DateAxis xAxis;
	private ValueAxis yAxis;
	
	public StockChart(String title) {
		this.title = title;
		this.dataset = createDataset();
		this.chart = createChart(dataset);
		this.chartPanel = new ChartPanel(chart);

		// let the layout managers handle sizing
//		chartPanel.setPreferredSize(new Dimension(300, 200));
		chartPanel.setMouseZoomable(false, false); 
		
	}
	
	private XYDataset createDataset() {
		this.series = new TimeSeries("Stock Prices");      
		this.series.setMaximumItemAge(MAXIMUM_POINTS);
		return new TimeSeriesCollection(series);
	}     

	private JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(             
		title, 
		"Day",              
		"Value",              
		dataset,             
		false,              
		false,              
		false);
		
		final XYPlot plot = chart.getXYPlot();
		
		plot.setBackgroundPaint(Color.BLACK);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(CUSTOM_GRIDLINE_GRAY);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(CUSTOM_GRIDLINE_GRAY);
		plot.getRenderer().setSeriesPaint(0, CUSTOM_GREEN);
		
		chart.setBackgroundPaint(Color.BLACK);
		chart.getTitle().setVisible(false);
		
		/*
		 * Should we show the x-axis?
		 */
		plot.getDomainAxis().setVisible(false);
		//-------------

		this.xAxis = (DateAxis)	plot.getDomainAxis();
		this.yAxis = plot.getRangeAxis();
		
		xAxis.setAutoRange(true);
		xAxis.setTickLabelPaint(CUSTOM_TICKS_COLOR);
		xAxis.setLabel(null);
		
		yAxis.setAutoRange(true);		
		yAxis.setTickLabelPaint(CUSTOM_TICKS_COLOR);
		yAxis.setLabel(null);
		
		return chart;
	}

	public ChartPanel getChartPanel() {
		return this.chartPanel;
	}

	public void addStockPrice(double price) {
		this.series.add(stockDay, price);
		this.stockDay = (Day) stockDay.next(); 
	}
	
	public TimeSeries getDataSeries() {
		return this.series;
	}

	public Day getNextDay() {
		return (Day) stockDay.next();
	}
   
	public void setInitialStockYear(int year) {
		this.stockDay = new Day(1, 1, year);
	}

}