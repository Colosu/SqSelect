package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import logic.Logic.Observer;
import logic.Results;

public class GraphPanel extends JPanel implements Observer {
	
	public GraphPanel(FormPanel form) {
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		
		size = new Dimension(400,300);
		
		Form = form;

		Dataset = new XYSeriesCollection();
		createChart("Renyi's Squeeziness", "", "", Color.GRAY, true);
		createCanvas();

		FC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		
		save = new JButton("Save File");
		save.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
				int returnValue = FC.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = FC.getSelectedFile();
					try {
						ChartUtils.saveChartAsPNG(selectedFile, Chart, 450, 400);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		add(Canvas, BorderLayout.NORTH);
		add(save, BorderLayout.SOUTH);
	}

	@Override
	public void onGo() {
		remove(Canvas);
		Dataset = new XYSeriesCollection();
		createChart("Renyi's Squeeziness", "", "", Color.GRAY, true);
		createCanvas();
		add(Canvas, BorderLayout.NORTH);
	}

	@Override
	public void onResults(Results results) {
		
		if (Form.getMode().equals(Modes.ALPHA) || Form.getMode().equals(Modes.ALPHA_FILE)) {
			remove(Canvas);
			createDataset(results.getDepths(), results.getAs(), results.getDepth());
			createChart("Renyi's Squeeziness", "Search Depth", "Alpha", Color.WHITE, true);
			createCanvas();
			add(Canvas, BorderLayout.NORTH);
		} else if (Form.getMode().equals(Modes.SQUEEZINESS)) {
			remove(Canvas);
			createDataset(results.getDepths(), results.getSqs(), results.getDepth());
			createChart("Renyi's Squeeziness", "Search Depth", "Squeeziness", Color.WHITE, true);
			createCanvas();
			add(Canvas, BorderLayout.NORTH);
		} else if (Form.getMode().equals(Modes.SQUEEZINESS_FULL)) {
			remove(Canvas);
			createDataset(results.getAs(), results.getSqs(), results.getSize());
			createChart("Renyi's Squeeziness", "Alpha", "Squeeziness", Color.WHITE, false);
			createCanvas();
			add(Canvas, BorderLayout.NORTH);
		}
		
	}

	@Override
	public void onReset() {
		remove(Canvas);
		Dataset = new XYSeriesCollection();
		createChart("Renyi's Squeeziness", "", "", Color.GRAY, true);
		createCanvas();
		add(Canvas, BorderLayout.NORTH);
	}
	
	@Override
	public void onError(String error) {
		remove(Canvas);
		Dataset = new XYSeriesCollection();
		createChart("Renyi's Squeeziness", "", "", Color.GRAY, true);
		createCanvas();
		add(Canvas, BorderLayout.NORTH);
	}
	
	private void createDataset(int[] Depths, double[] Sqs, int size) {
		
		XYSeries series = new XYSeries("Depths VS Squeeziness");
		for (int i = 0; i < size; i++) {
			series.add(Depths[i], Sqs[i]);
		}
		
		XYSeriesCollection seriesColl = new XYSeriesCollection(series);
		Dataset = seriesColl;
	}
	
	private void createDataset(double[] As, double[] Sqs, int size) {
		
		XYSeries series = new XYSeries("Alpha VS Squeeziness");
		for (int i = 0; i < size; i++) {
			series.add(As[i], Sqs[i]);
		}
		
		XYSeriesCollection seriesColl = new XYSeriesCollection(series);
		Dataset = seriesColl;
	}
	
	private void createChart(String name, String X, String Y, Paint color, boolean depth) {
		
		Chart = ChartFactory.createXYLineChart(name, X, Y, Dataset, PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = Chart.getXYPlot();
		
		XYSplineRenderer renderer = new XYSplineRenderer(9);
		renderer.setSeriesPaint(0, Color.BLUE);
		renderer.setSeriesStroke(0, new BasicStroke(1.0f));
		renderer.setDefaultShapesVisible(false);
		
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(color);

		NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
		if (depth) {
			xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		}
		
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		
//		Chart.getLegend().setFrame(BlockBorder.NONE);
		
		Chart.setTitle(new TextTitle(name, new Font("Serif", Font.BOLD, 18)));
	}
	
	@SuppressWarnings("serial")
	private void createCanvas() {
		
		Canvas = new ChartPanel(Chart) {
            @Override
            public Dimension getPreferredSize() {
                return size;
            }
        };
        
//		Canvas.setPreferredSize(size);
//		Canvas.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		Canvas.setBackground(Color.WHITE);
	}

	private FormPanel Form;
	private XYDataset Dataset;
	private JFreeChart Chart;
	private ChartPanel Canvas;
	private JFileChooser FC;
	private JButton save;
	private Dimension size;
	private static final long serialVersionUID = -4592387392090364706L;
}
