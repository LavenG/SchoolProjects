
/**
 * This class extends the ArrayList<Record> class provided by the JavaAPI and overrides some of its 
 * methods by providing its own implementation. It provides two ways of sorting the ArrayList using either
 * Selection sort or Merge Sort and provides an isSorted method to check for correct sorting
 */

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
	public void sortQuadratic(String key) {
		// Check every possible key and pass call selection sort on the list
		// with the corresponding
		// comparator
		if (key.equalsIgnoreCase("CAMIS")) {
			this.selectionSort(this, new RecordComparatorByCamis());
		} else if (key.equalsIgnoreCase("DBA")) {
			this.selectionSort(this, new RecordComparatorByDBA());
		} else if (key.equalsIgnoreCase("CUISINE")) {
			this.selectionSort(this, new RecordComparatorByCuisine());
		} else if (key.equalsIgnoreCase("SCORE")) {
			this.selectionSort(this, new RecordComparatorByScore());
		} else if (key.equalsIgnoreCase("DATE")) {
			this.selectionSort(this, new RecordComparatorByDate());
		}
	}

	/**
	 * Takes a key as a parameter and calls mergeSort method that matches they
	 * key specified with the matching comparator
	 * 
	 * @param key
	 *            a String that specifies the data field which should be taken
	 *            in consideration when sorting
	 */
	void sort(String key) {
		// Check every possible key and call Merge Sort with the corresponding
		// comparator and index of first and last element of the list
		if (key.equalsIgnoreCase("CAMIS")) {
			this.mergeSort(this, 0, this.size() - 1, new RecordComparatorByCamis());
		} else if (key.equalsIgnoreCase("DBA")) {
			this.mergeSort(this, 0, this.size() - 1, new RecordComparatorByDBA());
		} else if (key.equalsIgnoreCase("CUISINE")) {
			this.mergeSort(this, 0, this.size() - 1, new RecordComparatorByCuisine());
		} else if (key.equalsIgnoreCase("SCORE")) {
			this.mergeSort(this, 0, this.size() - 1, new RecordComparatorByScore());
		} else if (key.equalsIgnoreCase("DATE")) {
			this.mergeSort(this, 0, this.size() - 1, new RecordComparatorByDate());
		}
	}

	/**
	 * Takes a ArrayList, it's starting index and the index of the last element
	 * and sorts it by recursively splitting it (it doesn't actually create two
	 * different arrays when splitting it just determines where each array
	 * starts and ends) and then merging them together in the correct order
	 * 
	 * @param list
	 *            the MyArrayList object that the sorting will be performed on
	 * @param start
	 *            an integer that specifies the index of the first element in
	 *            the MyArrayList object
	 * @param finish
	 *            an integer that specifies the index of the last element in the
	 *            MyArraylist object
	 * @param c
	 *            a comparator that determines which data field of the Record
	 *            objects should be taken in consideration when sorting
	 */
	void mergeSort(MyArrayList list, int start, int finish, Comparator<? super Record> c) {
		// if the index of the first element and the index of the last element
		// are the same
		// or if the list is empty then return
		if (start == finish || list.size() == 0) {
			return;
		}
		// find the midpoint of the list in order to split the list into two
		// parts
		int mid = (start + finish) / 2;
		// call mergeSort on the left sublist
		mergeSort(list, start, mid, c);
		// call mergeSort on the right sublist
		mergeSort(list, mid + 1, finish, c);
		// merge the left and right sublists
		merge(list, start, mid, mid + 1, finish, c);
	}

	/**
	 * Takes an ArrayList that's conceptually split into two lists, the starting
	 * index and the index of the last element of the first Array and the
	 * starting index and the index of the last element of the second Array and
	 * merge them together into one array that is sorted in ascending order
	 * 
	 * @param list
	 *            the MyArrayList object that the merging will be performed on
	 * @param leftFirst
	 *            an integer that specifies the index of the first element in
	 *            the first MyArrayList object
	 * @param leftLast
	 *            an integer that specifies the index of the last element in the
	 *            first MyArraylist object
	 * @param rightFirst
	 *            an integer that specifies the index of the first element in
	 *            the second MyArrayList object
	 * @param righttLast
	 *            an integer that specifies the index of the last element in the
	 *            second MyArraylist object
	 * @param c
	 *            a comparator that determines which data field of the Record
	 *            objects should be taken in consideration when sorting
	 */
	void merge(MyArrayList list, int leftFirst, int leftLast, int rightFirst, int rightLast,
			Comparator<? super Record> c) {
		// create a temporary array to store the elements in ascending order
		ArrayList<Record> temp = new ArrayList<Record>();
		// Create two variables to keep track of the index we're at in the left
		// and right sublists
		int indexLeft = leftFirst;
		int indexRight = rightFirst;
		// While we're not at the end of the left or right sublist keep on going
		while (indexLeft <= leftLast && indexRight <= rightLast) {
			// find the smallest element between the two sublists and add it to
			// the temporary list
			// so that it contains the elements in ascending order
			if ((c.compare(list.get(indexLeft), list.get(indexRight)) < 0)) {
				temp.add(list.get(indexLeft));
				indexLeft++;
			} else {
				temp.add(list.get(indexRight));
				indexRight++;
			}
		}
		// Check if there is anything left in the left sublist and add it to the
		// temporary list
		while (indexLeft <= leftLast) {
			temp.add(list.get(indexLeft));
			indexLeft++;
		}
		// Check if there is anything right in the left sublist and add it to
		// the temporary list
		while (indexRight <= rightLast) {
			temp.add(list.get(indexRight));
			indexRight++;
		}
		// Copy the contents of the temporary list to the actual list so that it
		// contains all elements in a sorted order
		int i = 0;
		int j = leftFirst;
		while (i < temp.size()) {
			list.set(j, temp.get(i++));
			j++;
		}

	}

	/**
	 * Takes a key as a parameter and calls isSorted method that matches they
	 * key specified with the matching comparator to determine if the list is
	 * sorted
	 * 
	 * @param key
	 *            a String that specifies the data field which should be taken
	 *            in consideration when sorting
	 * @return isSorted a Boolean that is true if the list sorted in ascending
	 *         order and false if it is not
	 */
	public boolean isSorted(String key) {
		// create a boolean variable to keep track of whether the list is sorted
		boolean isSorted = true;
		// depending on the key provided call the isSorted method with the
		// corresponding comparator
		// and assign its result to the boolean variable created above
		if (key.equalsIgnoreCase("CAMIS")) {
			isSorted = this.isSorted(this, new RecordComparatorByCamis());
		} else if (key.equalsIgnoreCase("DBA")) {
			isSorted = this.isSorted(this, new RecordComparatorByDBA());
		} else if (key.equalsIgnoreCase("CUISINE")) {
			isSorted = this.isSorted(this, new RecordComparatorByCuisine());
		} else if (key.equalsIgnoreCase("SCORE")) {
			isSorted = this.isSorted(this, new RecordComparatorByScore());
		} else if (key.equalsIgnoreCase("DATE")) {
			isSorted = this.isSorted(this, new RecordComparatorByDate());
		}
		// return true if the list is sorted and false if it isn't
		return isSorted;
	}

	/**
	 * Takes a list and a comparator as a parameter to loop through the list and
	 * check if the elements are in ascending order
	 * 
	 * @param list
	 *            the MyArrayList object that needs to be checked to see if it's
	 *            sorted in ascending order
	 * @param c
	 *            a comparator that determines which data field of the Record
	 *            objects should be taken in consideration when checking for the
	 *            sorting
	 * 
	 * @return isSorted a Boolean that is true if the list sorted in ascending
	 *         order and false if it is not
	 */
	public boolean isSorted(MyArrayList list, Comparator<? super Record> c) {
		// set a boolean variable to keep track of whether the list is sorted
		boolean sorted = true;
		// Loop through the list starting at index 1
		// and compare each element to the previous one to check whether they're
		// in the right order
		for (int i = 1; i < list.size(); i++) {
			if ((c.compare(list.get(i - 1), list.get(i)) > 0)) {
				// if an element is bigger than the following element than the
				// list has not been
				// correctly sorted
				sorted = false;
			}
		}
		// return true if the list is sorted and false if it isn't
		return sorted;
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
