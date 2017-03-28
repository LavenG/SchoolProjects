package proj1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class MyArrayList extends ArrayList<Record> {

	/**
	 * Loops through a MyArrayList object and compares its elements by using a
	 * comparator to sort it in ascending order using selection sort
	 * 
	 * @param list
	 *            a MyArrayList object that needs to be sorted
	 * @param c
	 *            a comparator that determines which data field of the Record
	 *            objects should be taken in consideration when sorting
	 */
	public void selectionSort(MyArrayList list, Comparator<? super Record> c) {
		// If there are no elements in a list don't do the sorting
		if (list.size() == 0)
			return;
		for (int i = 0; i < this.size() - 1; i++) {
			// Set the current minimum at the beggining of the list
			Record currentMin = this.get(i);
			int currentMinIndex = i;
			// Loop through the list to determine if there is element smaller
			// than the current minimum and if so set the current minimum to
			// that element
			for (int j = i + 1; j < this.size(); j++) {
				if ((c.compare(this.get(j), currentMin)) < 0) {
					currentMinIndex = j;
					currentMin = this.get(j);
				}
			}
			// Put the currentMinimum at the lowest index of the unsorted part
			// of the list
			this.set(currentMinIndex, this.get(i));
			this.set(i, currentMin);
		}
	}

	/**
	 * Takes a key as a parameter and calls selectionSort method that matches
	 * they key specified with the matching comparator
	 * 
	 * @param key
	 *            a String that specifies the data field which should be taken
	 *            in consideration when sorting
	 */
	public void sort(String key) {
		// Check every possible key and pass call selection sort on the list
		// with the corresponding comparator
		if (key.equals("CAMIS")) {
			this.selectionSort(this, new RecordComparatorByCamis());
		} else if (key.equals("DBA")) {
			this.selectionSort(this, new RecordComparatorByDBA());
		} else if (key.equals("CUISINE")) {
			this.selectionSort(this, new RecordComparatorByCuisine());
		} else if (key.equals("SCORE")) {
			this.selectionSort(this, new RecordComparatorByScore());
		} else if (key.equals("DATE")) {
			this.selectionSort(this, new RecordComparatorByDate());
		}
	}

	/**
	 * Loops through a MyArrayList object and stores some of the data fields of
	 * each one in a String
	 * 
	 * @return a String object that contains a representation of all the Record
	 *         objects
	 */
	public String toString() {
		String records = new String();
		// Loop through the list and call toString on every single Record to get
		// the correctly
		// formatted output
		for (int i = 0; i < this.size(); i++) {
			records += (this.get(i).toString6()) + "\n";
		}
		return records;
	}

}
