package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import logic.Logic.Observer;
import logic.Results;

public class RPanel extends JPanel implements Observer {

	public RPanel(GraphPanel graph, ResultsPanel results) {
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		Graph = graph;
		Results = results;
		
		JPanel upPanel = new JPanel(new BorderLayout());
		JPanel downPanel = new JPanel(new BorderLayout());
		
		upPanel.add(Graph);
		downPanel.add(Results);
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.gridheight = 2;
		c.weighty = 1;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		add(upPanel, c);
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.gridheight = 1;
		c.weighty = 0.25;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(downPanel, c);
	}
	
	public GraphPanel getGraph() {
		return Graph;
	}
	public ResultsPanel getResults() {
		return Results;
	}

	@Override
	public void onGo() {
	}

	@Override
	public void onResults(Results results) {
	}

	@Override
	public void onReset() {
	}
	
	@Override
	public void onError(String error) {
	}

	private GraphPanel Graph;
	private ResultsPanel Results;
	private static final long serialVersionUID = 8340998460021581316L;
}
