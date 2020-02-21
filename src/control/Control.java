package control;

import java.io.File;

import logic.*;

public class Control extends Thread {
	
	public Control(Logic log) {
		logic = log;
	}
	
	public void orderComputeAlpha(int states, int maxTrans, int minTrans, int inputs, int outputs, int depth, boolean spearman) {
		logic.computeAlpha(states, maxTrans, minTrans, inputs, outputs, depth, spearman);
	}
	
	public void orderComputeAlpha(File file, int depth, boolean spearman) {
		logic.computeAlpha(file, depth, spearman);
	}
	
	public void orderComputeSqueeziness(File file, int depth, boolean spearman) {
		logic.computeSqueeziness(file, depth, spearman);
	}
	
	public void orderComputeSqueezinessFull(File file, int depth) {
		logic.computeSqueezinessFull(file, depth);
	}
	
	public void orderChangeMode() {
		logic.reset();
	}
	
	public void orderReset() {
		logic.reset();
	}
	
	public void orderExit() {
		logic.close();
		System.exit(0);
	}
	
	private Logic logic;
}
