package view;

import java.awt.BorderLayout;
//import java.awt.EventQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.ProgressMonitor;

import org.apache.commons.lang.ArrayUtils;

import logic.Logic.Observer;
import logic.Results;

public class ResultsPanel extends JPanel implements Observer {
	
	public ResultsPanel(FormPanel form) {
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		result = new JLabel("<html><body>Results: <br/><br/><br/><br/><br/></body></html>");
		Form = form;
		add(result);
	}

	@Override
	public void onGo() {
		result.setText("<html><body>Results: <br/> &nbsp;&nbsp;&nbsp;&nbsp; Computing...<br/><br/><br/><br/></body></html>");
	}
	
	@Override
	public void onResults(Results results) {
		if (Form.getMode().equals(Modes.ALPHA) || Form.getMode().equals(Modes.ALPHA_FILE)) {
			result.setText("<html><body>Results: <br/> &nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Depth = " + String.valueOf(results.getDepth()) +  "<br/> &nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Alpha = " + String.valueOf(results.getAs()[results.getDepth() - 1]) +  "<br/><br/><br/><br/></body></html>");
		} else if (Form.getMode().equals(Modes.SQUEEZINESS)) {
			result.setText("<html><body>Results: <br/> &nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Depth = " + String.valueOf(results.getDepth()) +  "<br/> &nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Alpha = " + String.valueOf(results.getAs()[results.getDepth() - 1]) +  "<br/> &nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Squeeziness = " + String.valueOf(results.getSqs()[results.getDepth() - 1]) + "<br/><br/><br/><br/></body></html>");
		} else if (Form.getMode().equals(Modes.SQUEEZINESS_FULL)) {
			printResults(results.getAs(), results.getSqs());
		}
	}
	
	@Override
	public void onReset() {
		result.setText("<html><body>Results: <br/><br/><br/><br/><br/></body></html>");
	}
	
	@Override
	public void onError(String error) {
		result.setText("<html><body>Results: <br/> &nbsp;&nbsp;&nbsp;&nbsp; Error<br/><br/><br/><br/></body></html>");
	}
	
	private void printResults(double[] As, double[] Sqs) {
		List<Double> list = Arrays.asList(ArrayUtils.toObject(Sqs));
		double max = (double) Collections.max(list);
		int index = list.indexOf(max);
		double maxA = As[index];
		double min = (double) Collections.min(list);
		index = list.indexOf(min);
		double minA = As[index];
		result.setText("<html><body>Results: <br/> &nbsp;&nbsp;&nbsp;&nbsp;"
				+ "Max Squeeziness = " + String.valueOf(max) + " <br/> &nbsp;&nbsp;&nbsp;&nbsp; (With alpha = "  + String.valueOf(maxA) + ")<br/> &nbsp;&nbsp;&nbsp;&nbsp;"
				+ "Min Squeeziness = " + String.valueOf(min) + " <br/> &nbsp;&nbsp;&nbsp;&nbsp; (With alpha = "  + String.valueOf(minA) + ")</body></html>");
	}

	private JLabel result;
	private FormPanel Form;
	private static final long serialVersionUID = 2767512105398842253L;
}
