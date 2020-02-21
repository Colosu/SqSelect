package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileSystemView;

import control.Control;
import logic.Logic.Observer;
import logic.Results;

public class FormPanel extends JPanel implements Observer {
	
	public FormPanel(Control Controller) {
		super();
		setLayout(new BorderLayout());

		JPanel filePanel = new JPanel(new FlowLayout());
		JPanel fieldPanel = new JPanel(new GridLayout(1,2,0,0));
		JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 0, 0));
		JPanel inputsPanel = new JPanel(new FlowLayout());
		JPanel modePanel = new JPanel(new FlowLayout());
		JPanel checkPanel = new JPanel(new FlowLayout());

		FC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		final JLabel depth2 = new JLabel("Search Depth: ");
		Depth2 = new JFormattedTextField(5);
		Depth2.setValue(0);
		open = new JButton("Open File");
		open.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
				int returnValue = FC.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = FC.getSelectedFile();
					file = selectedFile;
				}
			}
		});
		
		final JLabel states = new JLabel("Nº of States: ");
		final JLabel maxTran = new JLabel("Max Nº of Transitions: ");
		final JLabel minTran = new JLabel("Min Nº of Transitions: ");
		final JLabel inputs = new JLabel("Input Alphabet Size: ");
		final JLabel outputs = new JLabel("Output Alphabet Size: ");
		final JLabel depth = new JLabel("Search Depth: ");
		States = new JFormattedTextField(5);
		MaxTran = new JFormattedTextField(5);
		MinTran = new JFormattedTextField(5);
		Inputs = new JFormattedTextField(5);
		Outputs = new JFormattedTextField(5);
		Depth = new JFormattedTextField(5);

		States.setValue(0);
		MaxTran.setValue(0);
		MinTran.setValue(0);
		Inputs.setValue(0);
		Outputs.setValue(0);
		Depth.setValue(0);

		String options [] = {Modes.ALPHA, Modes.ALPHA_FILE, Modes.SQUEEZINESS, Modes.SQUEEZINESS_FULL};
		final JLabel mode = new JLabel("Mode of computation: ");
		Mode = new JComboBox<String>(options);
		Mode.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				
				if (getMode().equals(Modes.ALPHA)) {
					filePanel.setVisible(false);
					fieldsPanel.setVisible(true);
				} else {
					fieldsPanel.setVisible(false);
					filePanel.setVisible(true);
				}
				if (getMode().equals(Modes.SQUEEZINESS_FULL)) {
					checkPanel.setVisible(false);
				} else {
					checkPanel.setVisible(true);
				}
				Controller.orderChangeMode();
			}
		});

		final JLabel corrs = new JLabel("Correlation: ");
		pearson = new JRadioButton("Pearson");
		pearson.setSelected(true);
	    spearman = new JRadioButton("Spearman");
	    
	    group = new ButtonGroup();
	    group.add(pearson);
	    group.add(spearman);
		
		fieldsPanel.add(states);
		fieldsPanel.add(States);
		fieldsPanel.add(minTran);
		fieldsPanel.add(MinTran);
		fieldsPanel.add(maxTran);
		fieldsPanel.add(MaxTran);
		fieldsPanel.add(inputs);
		fieldsPanel.add(Inputs);
		fieldsPanel.add(outputs);
		fieldsPanel.add(Outputs);
		fieldsPanel.add(depth);
		fieldsPanel.add(Depth);
		modePanel.add(mode);
		modePanel.add(Mode, BorderLayout.WEST);
		fieldPanel.add(depth2);
		fieldPanel.add(Depth2);
		filePanel.add(fieldPanel, BorderLayout.NORTH);
		filePanel.add(open, BorderLayout.SOUTH);
		filePanel.setVisible(false);
		inputsPanel.add(filePanel, BorderLayout.NORTH);
		inputsPanel.add(fieldsPanel, BorderLayout.SOUTH);
		checkPanel.add(corrs);
		checkPanel.add(pearson);
		checkPanel.add(spearman);
		add(modePanel, BorderLayout.NORTH);
		add(inputsPanel, BorderLayout.CENTER);
		add(checkPanel, BorderLayout.SOUTH);
		
	}
	
	@Override
	public void onGo() {
		open.setEnabled(false);
		States.setEnabled(false);
		MinTran.setEnabled(false);
		MaxTran.setEnabled(false);
		Inputs.setEnabled(false);
		Outputs.setEnabled(false);
		Depth.setEnabled(false);
		Depth2.setEnabled(false);
		Mode.setEnabled(false);
		spearman.setEnabled(false);
	}

	@Override
	public void onResults(Results results) {
		open.setEnabled(true);
		States.setEnabled(true);
		MinTran.setEnabled(true);
		MaxTran.setEnabled(true);
		Inputs.setEnabled(true);
		Outputs.setEnabled(true);
		Depth.setEnabled(true);
		Depth2.setEnabled(true);
		Mode.setEnabled(true);
		spearman.setEnabled(true);
	}

	@Override
	public void onReset() {
		open.setEnabled(true);
		States.setEnabled(true);
		MinTran.setEnabled(true);
		MaxTran.setEnabled(true);
		Inputs.setEnabled(true);
		Outputs.setEnabled(true);
		Depth.setEnabled(true);
		Depth2.setEnabled(true);
		Mode.setEnabled(true);
		spearman.setEnabled(true);
		States.setValue(0);
		MaxTran.setValue(0);
		MinTran.setValue(0);
		Inputs.setValue(0);
		Outputs.setValue(0);
		Depth.setValue(0);
		Depth2.setValue(0);
		file = null;
		pearson.setSelected(true);
	}
	
	@Override
	public void onError(String error) {
		open.setEnabled(false);
		States.setEnabled(false);
		MinTran.setEnabled(false);
		MaxTran.setEnabled(false);
		Inputs.setEnabled(false);
		Outputs.setEnabled(false);
		Depth.setEnabled(false);
		Depth2.setEnabled(false);
		Mode.setEnabled(false);
		spearman.setEnabled(false);
	}
	
	public String getMode() {
		return Mode.getSelectedItem().toString();
	}

	public int getStates() {
		return (int) States.getValue();
	}

	public int getMaxTran() {
		return (int) MaxTran.getValue();
	}

	public int getMinTran() {
		return (int) MinTran.getValue();
	}

	public int getInputs() {
		return (int) Inputs.getValue();
	}

	public int getOutputs() {
		return (int) Outputs.getValue();
	}

	public int getDepth() {
		return (int) Depth.getValue();
	}

	public int getDepth2() {
		return (int) Depth2.getValue();
	}

	public File getFile() {
		return file;
	}
	
	public boolean getSpearman() {
		return spearman.isSelected();
	}

	private File file;
	private JButton open;
	private JFileChooser FC;
	private JRadioButton pearson;
	private JRadioButton spearman;
	private ButtonGroup group;
	private JComboBox<String> Mode;
	private JFormattedTextField States;
	private JFormattedTextField MaxTran;
	private JFormattedTextField MinTran;
	private JFormattedTextField Inputs;
	private JFormattedTextField Outputs;
	private JFormattedTextField Depth;
	private JFormattedTextField Depth2;
	private static final long serialVersionUID = 6767225772800141581L;
}
