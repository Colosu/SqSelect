package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import control.*;
import logic.*;
import view.*;

public class Main {

	public static void main(String[] args) {

		//Initialize core
		Core = new Logic();
		
		//Initialize controller
		Controller = new Control(Core);

		//Create view
		FormPanel form = new FormPanel(Controller);
		DoPanel does = new DoPanel(form, Controller);
		LPanel lPanel = new LPanel(form, does, Controller);
		GraphPanel graph = new GraphPanel(form);
		ResultsPanel results = new ResultsPanel(form);
		RPanel rPanel = new RPanel(graph, results);
		Window = new View("SqaSelector", lPanel, rPanel);
		
		//Add observers
		Core.addObserver(form);
		Core.addObserver(does);
		Core.addObserver(lPanel);
		Core.addObserver(graph);
		Core.addObserver(results);
		Core.addObserver(rPanel);
		Core.addObserver(Window);
		
		Window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Controller.orderExit();
			}
		});
		
		//End process
		return;
	}

	private static Logic Core;
	private static Control Controller;
	private static View Window;
}
