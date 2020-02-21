package logic;

import java.util.HashMap;
import java.util.Iterator;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;

public class FSMExplorer extends Thread {

	public FSMExplorer(CompactMealy<String, String> fsm, int qid, int iter, int length, int[] inputs, HashMap<String, Integer>[] mapOtoI, String output) {
		this.fsm = fsm;
		this.initQid = qid;
		this.initIter = iter;
		this.length = length;
		this.mapOtoI = mapOtoI;
		this.initOutput = output;
		this.inps = inputs;
	}
	
	public void run() {

		ExploreFSM(initQid, initIter, initOutput);
		
		return;
	}


	private void ExploreFSM(int qid, int iter, String output) {

		String inp = "";
		
		if (iter == length || fsm.getLocalInputs(qid).size() <= 0) {
			if (iter != length) {
				for (int i = iter + 1; i < length; i++) {
					storeOutput(output, i);
				}
			}
		} else {
			for (Iterator<String> it = fsm.getLocalInputs(qid).iterator(); it.hasNext(); ) {
				inp = it.next();
				storeOutput(output, iter);
				ExploreFSM(fsm.getSuccessor(qid, inp), iter + 1, output + fsm.getOutput(qid, inp));
			}
		}
	}
	
	private void storeOutput(String output, int iter) {
		try {
			Operations.sem.acquire();
			inps[iter]++;
			if (mapOtoI[iter].containsKey(output)) {
				mapOtoI[iter].put(output, mapOtoI[iter].get(output) + 1);
			} else {
				mapOtoI[iter].put(output, 1);
			}
			Operations.sem.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private CompactMealy<String, String> fsm;
	private int initQid;
	private int initIter;
	private int length;
	private HashMap<String, Integer>[] mapOtoI;
	private String initOutput;
	private int[] inps;
}
