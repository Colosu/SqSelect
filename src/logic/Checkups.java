package logic;

import java.util.ArrayList;
import java.util.Iterator;

import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.automata.transducers.impl.compact.CompactMealyTransition;
import net.automatalib.util.automata.equivalence.NearLinearEquivalenceTest;

public class Checkups {
	
	public Checkups() {
		
	}

	public boolean is_valid(CompactMealy<String,String> mm) {
		int state = mm.getInitialState();
		int succ = mm.getLocalInputs(state).size();
		if (succ > 0) {
			return true;
		}
		return false;
	}
	
	public boolean is_validMutation(CompactMealy<String,String> mmm, CompactMealy<String,String> mm) {
		return NearLinearEquivalenceTest.findSeparatingWord(mm, mmm, mm.getInputAlphabet()) != null;
	}

	public boolean checkMutation(Graph g, ArrayList<Node> TS) {
	
		boolean detected = false;
		Iterator<Node> iter = TS.iterator();
		while (!detected && iter.hasNext()) {
			detected = detected || checkMutations(g, iter.next());
		}
		return detected;
	}
	
	public boolean checkMutations(Graph g, Node T) {
		boolean detected = false;
		CompactMealy<String,String> mm = g.getMachine();
		int state = mm.getInitialState();
		Node iter = T;
		String in = iter.getInput();
		String out = iter.getOutput();
		String newout;
		CompactMealyTransition<String> tr = mm.getTransition(state, in);
		while (!detected && !(tr == null) && iter.getDepth() > 1) {
			newout = tr.getOutput();
			if (!newout.equals(out)) {
				detected = true;
			} else {
				state = tr.getSuccId();
				iter = iter.getNext();
				in = iter.getInput();
				out = iter.getOutput();
				tr = mm.getTransition(state, in);
			}
		}
		
		if (!detected && tr == null && iter.getDepth() > 1) {
			detected = true;
		} else if (!detected && !(tr == null)) {
			newout = tr.getOutput();
			if (!newout.equals(out)) {
				detected = true;
			}
		}
		
		return detected;
	}
}
