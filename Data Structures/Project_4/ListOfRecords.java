
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.PriorityQueue;

/**
 * The ListOfRecords class is used to store the list of all of the inspection
 * records in an object of type MYArrayList. It provides methods to sort this
 * data and search through it given different keys.
 */

public class ListOfRecords {

	private MyArrayList list;
	private bstOfRecordsByDBA bst;
	// A Hash table of priority queues that are minheaps sorted by the score of
	// the records
	private Hashtable<String, PriorityQueue<Record>> scoreTable;
	// A Hash table of linked Lists that contain Records with the same name and
	// address
	private Hashtable<String, DbaList> nameAddressTable;
	// A priority queue that is a minheap sorted by the date of the records
	private PriorityQueue<Record> dateMinHeap;

	/**
	 * Constructs a ListOfRecords object by initializing a MyArraylist object
	 * called list, a bstOfRecordsByDBA object called bst, a Hashtable object
	 * called scoreTable, a Hashtable object called nameAddressTable and a
	 * PriorityQueue object called dateMinHeap that are used to store all the
	 * Records from the data files
	 */
	public ListOfRecords() {
		list = new MyArrayList();
		bst = new bstOfRecordsByDBA();
		scoreTable = new Hashtable<String, PriorityQueue<Record>>();
		//We can use the DbaList class for this hashtable. The check for the keys is done before
		//assigning their corresponding value so the list itself does no need to check that all
		//of the records in it have matching dbas and addresses.
		nameAddressTable = new Hashtable<String, DbaList>();
		// defines the dateMinHeap data field and passes it the
		// RecordComparatorByDate
		// to make sure the Records are sorted by date inside the MinHeap
		dateMinHeap = new PriorityQueue<Record>(1, new RecordComparatorByDate());
	}

	/**
	 * Adds Records to the list, bst, scoreTable, nameAddressTable and
	 * dateMinHeap objects from the data file
	 *
	 * @param words
	 *            an ArrayList of Strings
	 */
	public void add(ArrayList<String> words) {

		// Creates a Record object called currentRecord from the ArrayList of
		// Strings passed
		Record currentRecord = new Record(words);
		// Adds the Record to the list
		list.add(currentRecord);
		// Adds the Record to the bst
		bst.add(currentRecord);

		// The zipcode of the Record is used as the key since it is unique.
		// If a record with the same zipcode has been added to the hashtable
		// before then we find
		// the minheap stored at that key,value pair in the hashtable and add
		// the record to the minheap
		// containing records with the same zipcode. Records with scores of -1
		// are not considered
		// because they represent Records that do not have a score in their data
		// entry
		if (scoreTable.get(currentRecord.getZip()) != null && currentRecord.getScore() != -1) {
			scoreTable.get(currentRecord.getZip()).add(currentRecord);
			// If no values exist at that zipcode then we create a new Priority
			// Queue with the key as the
			// zipcode of the current record and add the current record to that
			// Priority Queue
		} else if (currentRecord.getScore() != -1) {
			scoreTable.put(currentRecord.getZip(), new PriorityQueue<Record>(1, new RecordComparatorByScore()));
		}

		// For the nameAddressTable we use a concatenation of the dba and
		// address of the Record as keys since
		// the combination is unique. To ensure that all keys have the same
		// format and the user is able to get
		// the data he is looking for regardless of the way he enters the dba or
		// the address of the restaurant
		// we strip the string of white spaces and commas and set all its
		// characters to lowercase.
		String key = currentRecord.getDba() + currentRecord.getKeyFormatedAddress();
		key = key.replace(" ", "");
		key = key.replace(",", "");
		key = key.toLowerCase();

		// If a record with the same dba and address has been added to the
		// hashtable before we access
		// the DbaList to which it was added and add the currentRecord to it
		if (nameAddressTable.get(key) != null) {
			nameAddressTable.get(key).add(currentRecord);
			// If no records with the same dba and address as the Current Record
			// have been added to the hashtable
			// before then we create a new DbaList add the Record and store it
			// in the hashtable with the corresponding
			// key
		} else {
			nameAddressTable.put(key, new DbaList(currentRecord));
		}

		// add the record to the dateMinHeap object
		dateMinHeap.add(currentRecord);

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
	 * Accesses the record matching the record entered by the user using a concatenation 
	 * of dba and address and returns desired record
	 * 
	 * Accessing the elements in the hashtable is O(1) given we know the key. Sorting the elements
	 * by date is O(NlogN) and accessing the elements once they're sorted is O(1). The total efficiency
	 * of getting the elements is O(NlogN). The previous implementation had O(N) + O(NlogN) efficiency.
	 * Thus this implementation is much more efficient.
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
		
		//We format the user entered dba and address to match the format of the keys
		//we used when adding the records in the data to our hashtable
		String userEnteredKey = dba.toLowerCase() + address.toLowerCase();
		userEnteredKey = userEnteredKey.replace(" ", "");
		userEnteredKey = userEnteredKey.replace(",", "");

		//we make a copy of the list in the hashtable so that the original data is not modified
		MyArrayList sortedResults = nameAddressTable.get(userEnteredKey).toArrayList();
		sortedResults.sort("DATE");
		
		MyArrayList desiredResults = new MyArrayList();
		
		//if the dateFlag is "first" we return the first element in the arraylist
		if (dateFlag.equalsIgnoreCase("first")) {
			desiredResults.add(sortedResults.get(0));
			return desiredResults;
		//if the dateFlag is "last" we return the last element in the arraylist
		} else if (dateFlag.equalsIgnoreCase("last")) {
			desiredResults.add(sortedResults.get(sortedResults.size()-1));
			return desiredResults;
		} else {
			//otherwise we return the whole arraylist
			return sortedResults;
		}
	}

	/**
	 * Returns the desired number of dates by removing them from the Priority Queue which is
	 * a minheap.
	 * 
	 * The efficiency of this method is O(NlogN) since all we need to do is dequeue the deisred
	 * number of elements from the minheap. The efficieny of the previous implementation was
	 * O(NlogN) for sorting the date + O(N) for accessing it once it's sorted. Thus this implementation
	 * is more efficient.
	 * 
	 * @param numOfDates
	 *            an integer that specifies the number of dates desired
	 * @return a MyArrayList object that contains all the Records within the
	 *         desired range of dates
	 */
	public MyArrayList findByDate(int numOfDates) {

		// We set a counter to keep track of the number of different dates
		// we have encountered
		int counter = 0;

		// We copy the data in dateMinHeap in order to avoid modifying the
		// actual min heap
		// and be able to execute the same command consecutively and obtain
		// consistent results
		PriorityQueue<Record> dateMinHeapCopy = dateMinHeap;
		MyArrayList desiredResults = new MyArrayList();

		// While the number of different dates we added to the array list is
		// smaller than the number entered by the user we keep adding
		while (counter < numOfDates) {
			// we keep track of the record we're about to add
			Record currentRecord = dateMinHeapCopy.peek();
			// we add the record to the array list
			desiredResults.add(dateMinHeapCopy.poll());
			// if the record we're about to add has a different date than the
			// previous one
			// we increment the counter
			if (!(currentRecord.getInspectionDate().equals(dateMinHeapCopy.peek().getInspectionDate()))) {
				counter++;
			}
		}

		// return the desired results recorded in the arraylist
		return desiredResults;

	}

	/**
	 * Find the desired Priority Queue in the hashtable using zipcodes as keys and returns 
	 * the records with the desired scores (equal or smaller than the score entered by usre)
	 * 
	 * The efficiency of accessing the elements in the hashtable given that we know the key
	 * is O(1), the efficiency of removing elements from the priority queue is O(NlogN). The 
	 * efficiency of this implementation is thus O(NlogN). The previous implementation had O(N) for
	 * accessing the elements + O(NlogN) for sorting them. This implementation is thus more efficient.
	 * 
	 * @param score
	 *            an integer that specifies the desired score
	 * @param zipcode
	 *            a String that specifies the desired zipcode
	 * @return an Arraylist object that contains all the Records matching the
	 *         desired parameters
	 */
	public MyArrayList findByScore(int score, String zipcode) {

		// We make a copy of the priority queue we're going to use that we
		// access using the hashtable scoreTable and entering the key provided
		// by the user to avoid modifying the actual min heap
		// and be able to execute the same command consecutively and obtain
		// consistent results
		PriorityQueue<Record> desired = scoreTable.get(zipcode);
		MyArrayList results = new MyArrayList();
		// While the head of the PriorityQueue is smaller than or equal to the
		// user entered score
		// we keep taking it out and adding it to the arraylist
		while (desired.peek().getScore() <= score) {
			results.add(desired.poll());
		}
		// return the desired results recorded in the arraylist
		return results;
	}

}
