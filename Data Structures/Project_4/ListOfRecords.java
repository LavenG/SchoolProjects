
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ListOfRecords {

	private MyArrayList list;
	private bstOfRecordsByDBA bst;

	/**
	 * Constructs a ListOfRecords object by initializing a MyArraylist object
	 * called list and a bstOfRecordsByDBA object called bst that are used to
	 * store all the Records from the data files
	 */
	public ListOfRecords() {
		list = new MyArrayList();
		bst = new bstOfRecordsByDBA();
	}

	/**
	 * Adds Records to the list and bst objects from the data file
	 *
	 * @param words
	 *            an ArrayList of Strings
	 */
	public void add(ArrayList<String> words) {
		list.add(new Record(words));
		bst.add(new Record(words));
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
		for (int i = 0; i < this.size(); i++) {
			records += (this.get(i).toString()) + "\n";
		}
		return records;
	}

	/**
	 * Sorts list with the specified key by calling the sort method of
	 * MyArrayList on it
	 * 
	 * @param key
	 *            a String that specifies the desired sorting key
	 */
	public void sort(String key) {
		list.sort(key);
	}

	/**
	 * Looks for the given name in the bst that stores all the records and
	 * returns all records with the matching name. If no such records exist, it
	 * returns null
	 * 
	 * @param name
	 *            a String that specifies the desired Name
	 * @return a MyArrayList object that contains all Records with the matching
	 *         name (dba)
	 */
	public MyArrayList findByName(String name) {

		// if we found what we were looking for then we convert it into a
		// MyArrayList object to
		// match the main method and return it
		if (bst.get(name) != null)
			return bst.get(name).toArrayList();
		// if not we return null
		else
			return null;

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

		MyArrayList allResults = new MyArrayList();
		for (int i = 0; i < list.size(); i++) {
			if (dba.equals(list.get(i).getDba()) && address.equals(list.get(i).getAddress())) {
				allResults.add(list.get(i));
			}
		}
		allResults.sort("DATE");
		MyArrayList desiredResults = new MyArrayList();
		if (dateFlag.equals("first")) {
			desiredResults.add(allResults.get(0));
			return desiredResults;
		} else if (dateFlag.equals("last")) {
			desiredResults.add(allResults.get(allResults.size() - 1));
			return desiredResults;
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

		MyArrayList results = new MyArrayList();
		list.sort("DATE");

		for (int i = 0; i < numOfDates; i++) {
			int currentEarliestDateIndex = results.size();
			Date currentEarliestDate = list.get(currentEarliestDateIndex).getInspectionDate();
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getInspectionDate().equals(currentEarliestDate)) {
					results.add(list.get(j));
				}
			}
		}
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

		MyArrayList results = new MyArrayList();
		for (int i = 0; i < list.size(); i++) {
			if ((list.get(i).getScore() != -1) && (list.get(i).getScore() <= score)
					&& list.get(i).getZip().equals(zipcode)) {
				results.add(list.get(i));
			}
		}
		results.sort("SCORE");
		return results;
	}

}
