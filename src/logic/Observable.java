package logic;

import java.util.LinkedList;

public class Observable<T> {
	
	//Creates the list of observers.
	public Observable() {
		
		observers = new LinkedList<T>();
	}
	
	//Adds an observer to the list.
	public void addObserver(T observer) {

		if (!observers.isEmpty()) {

			boolean existe = observers.contains(observer);
			
			if (!existe) {
				
				observers.add(observer);
			}
		} else {

			observers.add(observer);
		}
	}
	
	//Deletes the chosen observer from the list.
	public void removeObserver(T observer) {
		if (!observers.isEmpty()) {
			
			observers.remove(observer);
		}
	}
	
	protected java.util.List<T> observers;
}
