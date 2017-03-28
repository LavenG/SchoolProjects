package proj1;

/**
 * The ListOfRecords class is used to store the list of all of the inspection records in an
 * object of type MYArrayList. It provides methods to sort this data and search through it 
 * given different keys.
 */

import java.util.ArrayList;
import java.util.Date;

public class ListOfRecords {

	private MyArrayList list;

	/**
	 * Constructs a ListOfRecords object by initializing a MyArraylist object
	 * called list that is used to store all the Records from the data files
	 */
	public ListOfRecords() {
		list = new MyArrayList();
	}

	/**
	 * Adds Records to the list object from the data file
	 *
	 * @param words
	 *            an ArrayList of Strings
	 */
	public void add(ArrayList<String> words) {
		list.add(new Record(words));
	}

	/**
	 * Takes in an integer as an argument that is the desired index and returns
	 * the Record object that is stored at that index
	 * 
	 * @param i
	 *            an integer
	 * @return Record object that is at desired position in the list
	 */
	public Record get(int index) {
		return list.get(index);
	}

	/**
	 * @return an integer that specifies the current size of the list
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Loops through the whole list and prints out every single Record object as
	 * a string
	 * 
	 * @return a String object that specifies all data fields of the Record
	 *         objects
	 */
	public String toString() {
		String records = new String();
		// loop through every element of the list and print them in the correct
		// format
		for (int i = 0; i < this.size(); i++) {
			records += (this.get(i).toString()) + "\n";
		}
		return records;
	}

	/**
	 * Sorts list with the specified key by calling the quadratic sort method of
	 * MyArrayList on it
	 * 
	 * @param key
	 *            a String that specifies the desired sorting key
	 */
	public void sort(String key) {
		// call the Quadratic sort method on the list
		list.sort(key);
	}

	/**
	 * Loops through all of the Records in the data file and stores the ones
	 * that match the key in a MyArrayList object called results
	 * 
	 * @param key
	 *            a String that specifies the desired Name
	 * @return a MyArrayList object that contains all Records with the matching
	 *         key
	 */
	public MyArrayList findByName(String name) {
		// Create a my Arraylist object to store the results
		MyArrayList results = new MyArrayList();
		// loop through the list that contains the records from the input file
		// and add
		// every element with the matching name to the results list
		for (int i = 0; i < list.size(); i++) {
			if ((list.get(i).getDba()).equalsIgnoreCase(name)) {
				results.add(list.get(i));
			}
		}
		// return results
		return results;
	}

	/**
	 * Loops through all the Records in the data file and stores the ones that
	 * match the desired Dba, Address and in
	 * 
	 * @param dba
	 *            a String that specifies the desired dba
	 * @param address
	 *            a String that specifies the desired address
	 * @param dateFlag
	 *            a String that specifies the kind of output desired
	 * 
	 * @return a MyArrayList object containing the Records with the desired Dba,
	 *         Address, and dateFlag
	 */
	public MyArrayList findByNameAddress(String dba, String address, String dateFlag) {
		// Create a MyArrayList object called allResults and add all the
		// elements with the matching dba and address to it
		MyArrayList allResults = new MyArrayList();
		MyArrayList sortedResults = new MyArrayList();
		for (int i = 0; i < list.size(); i++) {
			if (dba.equalsIgnoreCase(list.get(i).getDba()) && address.equalsIgnoreCase(list.get(i).getAddress())) {
				allResults.add(list.get(i));
				sortedResults.add(list.get(i));
			}
		}
		// Sort desiredResults by date
		sortedResults.sort("DATE");
		// Create a MyArrayList object called desiredResults to only store the
		// desired Records depending on the date flag
		MyArrayList desiredResults = new MyArrayList();

		// if the date flag is "first" then the record of the first ever
		// inspection is stored in desiredResults
		if (dateFlag.equalsIgnoreCase("first")) {
			desiredResults.add(sortedResults.get(0));
			return desiredResults;
			// if the date flag is "last" then the record of the last ever
			// inspection is stored in desiredResults
		} else if (dateFlag.equalsIgnoreCase("last")) {
			desiredResults.add(sortedResults.get(sortedResults.size() - 1));
			return desiredResults;
			// Otherwise all the results are returned
		} else {
			return allResults;
		}

	}

	/**
	 * Sorts all the records by date and then loops through them to select the
	 * earliest dates depending on the number of dates desired and stores them
	 * in another MyArrayList object.
	 * 
	 * @param numOfDates
	 *            an integer that specifies the number of dates desired
	 * @return a MyArrayList object that contains all the Records within the
	 *         desired range of dates
	 */
	public MyArrayList findByDate(int numOfDates) {

		// Create a new MyArrayList object called results to store the desired
		// records
		MyArrayList results = new MyArrayList();
		// Sort the data we have by date
		list.sort("DATE");
		// Keeps track of the current earliest date index

		int currentEarliestDateIndex = results.size();

		// loop through all the Data for the first numOfDates provided
		for (int i = 0; i < numOfDates; i++) {
			// get the inspection date at the current earliest date index
			if (currentEarliestDateIndex < list.size()) {
				Date currentEarliestDate = list.get(currentEarliestDateIndex).getInspectionDate();
				// loop through the data and add the records with the earliest
				// "numOfDates" to the results list
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getInspectionDate().equals(currentEarliestDate)) {
						results.add(list.get(j));
					}
				}
				currentEarliestDateIndex = results.size();
			}
		}
		// return the results
		return results;
	}

	/**
	 * Loops through a MyArraylist object and stores the Record objects that
	 * match the desired score and zipcode in another MyArrayList that is
	 * returned. Records that have a null score are not included.
	 * 
	 * @param score
	 *            an integer that specifies the desired score
	 * @param zipcode
	 *            a String that specifies the desired zipcode
	 * @return an Arraylist object that contains all the Records matching the
	 *         desired parameters
	 */
	public MyArrayList findByScore(int score, String zipcode) {
		// Create a new MyArrayList object called results to store the desired
		// records
		MyArrayList results = new MyArrayList();
		// loop through the data
		for (int i = 0; i < list.size(); i++) {
			// if there is a score and it is less than or equal to the one
			// provided and the zipcode of the Record that contains it is the
			// same as the one provided
			// add it to the results
			if ((list.get(i).getScore() != -1) && (list.get(i).getScore() <= score)
					&& list.get(i).getZip().equals(zipcode)) {
				results.add(list.get(i));
			}
		}
		// Sort the results by the score and return them
		results.sort("SCORE");
		return results;
	}

}
