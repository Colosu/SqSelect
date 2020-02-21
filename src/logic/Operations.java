package logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;

public class Operations {
	
	@SuppressWarnings("unchecked")
	public double[] Squeeziness(Graph g, int length, double[] as) {

		if (length <= 0) {
			return null;
		}

		int[] inputs = new int[length];
		HashMap<String, Integer>[] mapOtoI = new HashMap[length];
		double[] TSq = new double[length];
		
		for (int i = 0; i < length; i++) {
			inputs[i] = 0;
			mapOtoI[i] = new HashMap<String, Integer>();
			TSq[i] = 0.0;
		}

		CompactMealy<String, String> fsm = g.getMachine();
		int qid = fsm.getInitialState();
		FSMExplorer[] explorer = new FSMExplorer[fsm.getLocalInputs(qid).size()];
		String inp = "";
		String output = "";
		int i = 0;
		for (Iterator<String> it = fsm.getLocalInputs(qid).iterator(); it.hasNext(); ) {
			inp = it.next();
			output = "" + fsm.getOutput(qid, inp);
			try {
				Operations.sem.acquire();
				inputs[0]++;
				if (mapOtoI[0].containsKey(output)) {
					mapOtoI[0].put(output, mapOtoI[0].get(output) + 1);
				} else {
					mapOtoI[0].put(output, 1);
				}
				Operations.sem.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			explorer[i] = new FSMExplorer(fsm, fsm.getSuccessor(qid, inp), 1, length, inputs, mapOtoI, "" + fsm.getOutput(qid, inp));
			explorer[i].start();
			i++;
		}
		for (int j = 0; j < i; j++) {
			try {
				if (explorer[j] != null) {
					explorer[j].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		inputs = ExploreFSM(fsm, qid, 0, length, mapOtoI, "");

		double max = 0.0;
		Integer val = 0;
		for (int k = 0; k < length; k++) {
			val = 0;
			max = 0.0;
			for (Iterator<Integer> it = mapOtoI[k].values().iterator(); it.hasNext(); ) {
				val = it.next();

				if (as[k] == 1) {
					TSq[k] += val * log2((double)val);
				} else {
					TSq[k] += Math.pow((double)val, as[k]);
				}

				if (val > max) {
					max = val;
				}
			}

			if (as[k] == 1) {
				TSq[k] = TSq[k]/((double)inputs[k]);
			} else if (as[k] == INFINITE) {
				TSq[k] = log2(max);
			} else {
				TSq[k] = (1.0/(1.0 - as[k]))*log2((double)inputs[k] / TSq[k]);
			}
		}
		
		return TSq;
	}
	
	@SuppressWarnings("unchecked")
	public void Squeeziness(Graph g, int length, Results vals, double[] as) {

		if (length <= 0) {
			return;
		}

		int[] inputs = new int[length];
		HashMap<String, Integer>[] mapOtoI = new HashMap[length];
		int size = as.length;
		double[] TSq = new double[size];
		for (int i = 0; i < size; i++) {
			TSq[i] = 0.0;
		}
		for (int i = 0; i < length; i++) {
			inputs[i] = 0;
			mapOtoI[i] = new HashMap<String, Integer>();
		}

		CompactMealy<String, String> fsm = g.getMachine();
		int qid = fsm.getInitialState();
		FSMExplorer[] explorer = new FSMExplorer[fsm.getLocalInputs(qid).size()];
		String inp = "";
		String output = "";
		int i = 0;
		for (Iterator<String> it = fsm.getLocalInputs(qid).iterator(); it.hasNext(); ) {
			inp = it.next();
			output = "" + fsm.getOutput(qid, inp);
			try {
				Operations.sem.acquire();
				inputs[0]++;
				if (mapOtoI[0].containsKey(output)) {
					mapOtoI[0].put(output, mapOtoI[0].get(output) + 1);
				} else {
					mapOtoI[0].put(output, 1);
				}
				Operations.sem.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			explorer[i] = new FSMExplorer(fsm, fsm.getSuccessor(qid, inp), 1, length, inputs, mapOtoI, "" + fsm.getOutput(qid, inp));
			explorer[i].start();
			i++;
		}
		for (int j = 0; j < i; j++) {
			try {
				if (explorer[j] != null) {
					explorer[j].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		inputs = ExploreFSM(fsm, qid, 0, length, mapOtoI, "");

		double max = 0.0;
		Integer val = 0;
		for (Iterator<Integer> it = mapOtoI[length - 1].values().iterator(); it.hasNext(); ) {
			val = it.next();

			for (int j = 0; j < size; j++) {
				if (as[j] == 1) {
					TSq[j] += val * log2((double)val);
				} else {
					TSq[j] += Math.pow((double)val, as[j]);
				}
			}

			if (val > max) {
				max = val;
			}
		}

		for (int j = 0; j < size; j++) {
			if (as[j] == 1) {
				TSq[j] = TSq[j]/((double)inputs[length - 1]);
			} else if (as[j] == INFINITE) {
				TSq[j] = log2(max);
			} else {
				TSq[j] = (1.0/(1.0 - as[j]))*log2((double)inputs[length - 1] / TSq[j]);
			}
		}
		
		vals.setAs(as);
		vals.setSqs(TSq);
		vals.setSize(size);
		vals.setDepth(length);
		
		return;
	}
	
//	public int ExploreFSM(CompactMealy<String, String> fsm, int qid, int iter, int length, HashMap<String, Integer> mapOtoI, String output) {
//
//		int inputs = 0;
//		int count = 0;
//		String inp = "";
//		
//		if (iter == length) {
//			try {
//				inputs++;
//				Operations.sem.acquire();
//				if (mapOtoI.containsKey(output)) {
//					mapOtoI.put(output, mapOtoI.get(output) + 1);
//				} else {
//					mapOtoI.put(output, 1);
//				}
//				Operations.sem.release();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		} else {
//			count = 0;
//			for (Iterator<String> it = fsm.getInputAlphabet().iterator(); it.hasNext(); ) {
//				inp = it.next();
//				if (fsm.getTransition(qid, inp) != null) {
//					inputs += ExploreFSM(fsm, fsm.getSuccessor(qid, inp), iter + 1, length, mapOtoI, output + fsm.getOutput(qid, inp));
//					count++;
//				}
//			}
//			if (count == 0) {
//				try {
//					inputs++;
//					Operations.sem.acquire();
//					if (mapOtoI.containsKey(output)) {
//						mapOtoI.put(output, mapOtoI.get(output) + 1);
//					} else {
//						mapOtoI.put(output, 1);
//					}
//					Operations.sem.release();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		return inputs;
//	}
	
	private double log2(double val) {
		return Math.log(val)/Math.log(2.0);
	}
	
	public static Semaphore sem = new Semaphore(1);
	public static int INFINITE = 1000;
}
