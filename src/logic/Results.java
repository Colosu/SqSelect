package logic;

public class Results {
	
	public double[] getAs() {
		return As;
	}
	public double[] getSqs() {
		return Sqs;
	}
	public int[] getDepths() {
		return depths;
	}
	public int getSize() {
		return size;
	}
	public int getDepth() {
		return depth;
	}
	public void setAs(double[] as) {
		As = as;
	}
	public void setSqs(double[] sqs) {
		Sqs = sqs;
	}
	public void setDepths(int[] depths) {
		this.depths = depths;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	private double[] As;
	private double[] Sqs;
	private int[] depths;
	private int depth = 0;
	private int size = 0;
}
