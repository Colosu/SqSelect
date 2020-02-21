package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.Control;
import logic.Logic.Observer;
import logic.Results;

public class LPanel extends JPanel implements Observer {

	public LPanel(FormPanel form, DoPanel does, Control controller) {
		super();
		setLayout(new BorderLayout());
		
		Form = form;
		Do = does;
		Controller = controller;

		JPanel upPanel = new JPanel(new BorderLayout());
		JPanel midPanel = new JPanel(new BorderLayout());
		JPanel exitPanel = new JPanel(new FlowLayout());
		
		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent event) {
				
				JOptionPane popup = new JOptionPane("¿Do you want to leave?");
				
				if (popup.showConfirmDialog(null, popup.getMessage(), "Warning", popup.YES_NO_OPTION) == 0) {

					Controller.orderExit();
				}
			}
		});
		
		exitPanel.add(exit);
		upPanel.add(Form);
		midPanel.add(Do);
		add(upPanel, BorderLayout.NORTH);
		add(midPanel, BorderLayout.CENTER);
		add(exitPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void onGo() {
		exit.setEnabled(false);
	}

	@Override
	public void onResults(Results results) {
		exit.setEnabled(true);
	}

	@Override
	public void onReset() {
		exit.setEnabled(true);
	}
	
	@Override
	public void onError(String error) {
		exit.setEnabled(false);
		JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
		Controller.orderReset();
	}

	private JButton exit;
	private FormPanel Form;
	private DoPanel Do;
	private Control Controller;
	private static final long serialVersionUID = 1257045596570586926L;
}
