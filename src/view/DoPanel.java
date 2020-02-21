package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.Control;
import logic.Logic.Observer;
import logic.Results;

public class DoPanel extends JPanel implements Observer {

	public DoPanel(FormPanel form, Control Controller) {
		super();
		setLayout(new FlowLayout());
		
		go = new JButton("Compute");
		go.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent event) {
				
				JOptionPane popup = new JOptionPane();
				
				if (popup.showConfirmDialog(null, "¿Do you want to run?", "Warning", popup.YES_NO_OPTION) == 0) {

					if (form.getMode().equals(Modes.ALPHA)) {
						if (form.getStates() > 0 && form.getMaxTran() > 0
								&& form.getMinTran() >= 0 && form.getInputs() > 0
								&& form.getOutputs() > 0 && form.getDepth() > 0) {

							Controller.orderComputeAlpha(form.getStates(), form.getMaxTran(), form.getMinTran(),
									form.getInputs(), form.getOutputs(), form.getDepth(), form.getSpearman());
						} else {
							popup.showMessageDialog(null, "One of the parameters is not valid.", "Warning", popup.OK_OPTION);
						}
					} else if (form.getMode().equals(Modes.ALPHA_FILE)) {
						if (form.getFile() != null && form.getDepth2() > 0) {
							Controller.orderComputeAlpha(form.getFile(), form.getDepth2(), form.getSpearman());
						} else {
							popup.showMessageDialog(null, "One of the parameters is not valid.", "Warning", popup.OK_OPTION);
						}
					} else if (form.getMode().equals(Modes.SQUEEZINESS)) {
						if (form.getFile() != null && form.getDepth2() > 0) {
							Controller.orderComputeSqueeziness(form.getFile(), form.getDepth2(), form.getSpearman());
						} else {
							popup.showMessageDialog(null, "One of the parameters is not valid.", "Warning", popup.OK_OPTION);
						}
					} else if (form.getMode().equals(Modes.SQUEEZINESS_FULL)) {
						if (form.getFile() != null && form.getDepth2() > 0) {
							Controller.orderComputeSqueezinessFull(form.getFile(), form.getDepth2());
						} else {
							popup.showMessageDialog(null, "One of the parameters is not valid.", "Warning", popup.OK_OPTION);
						}
					} else {
						popup.showMessageDialog(null, "You have no mode selected.", "Warning", popup.OK_OPTION);
					}
				}
			}
		});

		reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent event) {
				
				JOptionPane popup = new JOptionPane();
				
				if (popup.showConfirmDialog(null, "¿Do you want to reset?", "Warning", popup.YES_NO_OPTION) == 0) {

					Controller.orderReset();
				}
			}
		});
		
		add(go);
		add(reset);
	}
	
	@Override
	public void onGo() {
		go.setEnabled(false);
		reset.setEnabled(false);
	}
	
	@Override
	public void onResults(Results results) {
		go.setEnabled(true);
		reset.setEnabled(true);
	}
	
	@Override
	public void onReset() {
		go.setEnabled(true);
		reset.setEnabled(true);
	}
	
	@Override
	public void onError(String error) {
		go.setEnabled(false);
		reset.setEnabled(false);
	}

	private JButton go;
	private JButton reset;
	private static final long serialVersionUID = -3457829488762740658L;
}
