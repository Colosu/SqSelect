package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.*;

import logic.Logic.Observer;
import logic.Results;

public class View extends JFrame implements Observer {

	public View(String name, LPanel lPanel, RPanel rPanel) {
		super(name);
		initGUI(lPanel, rPanel);
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				
				setVisible(true);
			}
		});
	}
	
	private void initGUI(LPanel lPanel, RPanel rPanel) {
		
		//The window is started
		this.setSize(800, 600);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1, 2, 20, 10));

		//The components are added to the window
		this.add(lPanel, BorderLayout.WEST);
		this.add(rPanel, BorderLayout.EAST);
	}
	
	public LPanel getLPanel() {
		return lPanel;
	}

	public RPanel getRPanel() {
		return rPanel;
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

	private LPanel lPanel;
	private RPanel rPanel;
	private static final long serialVersionUID = 4196456464139782421L;
}
