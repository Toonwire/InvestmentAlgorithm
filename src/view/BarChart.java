package view;

import java.awt.Color;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart {

	private static final Color CUSTOM_BLUE = new Color(90,155,212);
	private static final Color CUSTOM_RED = new Color(241,90,96);
	private static final Color CUSTOM_GREEN = new Color(122,195,106);
	private static final Color CUSTOM_DARK_GREEN = new Color(2,125,125);
	private static final Color CUSTOM_GRIDLINE_GRAY = new Color(56,52,67);
	private static final Color CUSTOM_DARK_BLUE = new Color(48,12,186);
	private static final Color CUSTOM_TICKS_COLOR = new Color(1,140,112);
	private static final Color CUSTOM_BALANCE_COLOR = new Color(33,108,42);
	private static final Color CUSTOM_BAR_COLOR = new Color(0,140,72);
	
	private final JFreeChart chart;
	private DefaultCategoryDataset dataset;
	private ChartPanel chartPanel;
	private String title;
	private CategoryAxis domainAxis;
	private NumberAxis rangeAxis;

	public BarChart(String title) {
		this.title = title;
		this.dataset = createDataset();
		this.chart = createChart(dataset);
		this.chartPanel = new ChartPanel(chart);
		
//		chartPanel.setPreferredSize(new Dimension(300, 200));
		chartPanel.setMouseZoomable(false, false); 
	}

	private DefaultCategoryDataset createDataset() { 

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        return dataset;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {

		final JFreeChart chart = ChartFactory.createBarChart(
				title,
				"Investor",
				"Balance (€)",
				dataset,
				PlotOrientation.HORIZONTAL,
				false,
				false,
				false);

			chart.setBackgroundPaint(new Color(0,85,85));
			chart.setBackgroundPaint(Color.BLACK);
			
	        final CategoryPlot plot = chart.getCategoryPlot();
	        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

	        plot.setBackgroundPaint(Color.BLACK);
			plot.setDomainGridlinesVisible(true);
			plot.setDomainGridlinePaint(CUSTOM_GRIDLINE_GRAY);
			plot.setRangeGridlinesVisible(true);
			plot.setRangeGridlinePaint(CUSTOM_GRIDLINE_GRAY);
			plot.getRenderer().setSeriesPaint(0, CUSTOM_BAR_COLOR);
			
			this.rangeAxis = (NumberAxis) plot.getRangeAxis();
//			rangeAxis.setAutoRange(true);
//			rangeAxis.setFixedAutoRange(100);
			rangeAxis.setRangeAboutValue(0,100);
//			rangeAxis.centerRange(0);
			rangeAxis.setLabelPaint(CUSTOM_BALANCE_COLOR);
			rangeAxis.setTickLabelPaint(CUSTOM_TICKS_COLOR);
			rangeAxis.setTickUnit(new NumberTickUnit(10));
			
			this.domainAxis = (CategoryAxis) plot.getDomainAxis();
			domainAxis.setLabelPaint(Color.BLACK);
			domainAxis.setTickLabelPaint(CUSTOM_TICKS_COLOR);
			domainAxis.setLabel(null);		// more horizontal space this way

			return chart;
  
	}

	public ChartPanel getChartPanel() {
		return this.chartPanel;
	}

	public void updateBalance(String investorName, double balance) {
		this.dataset.addValue(balance, "balance", investorName);
	}
	
	public void setBalanceRange(double range) {
		rangeAxis.setRangeAboutValue(0, range);
	}
}
