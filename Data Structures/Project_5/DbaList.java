import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a DbaList object that is a linked list used to store
 * records of a restaurants with the same DBA
 * 
 * @author Joanna Klukowska
 *
 */
public class DbaList implements Comparable<DbaList> {

	// This string is used as a tag to store the common dba
	// of the restaurants that the list contains
	private String dba;
	// This linked list is used to store the records of the restaurants
	// with the same dba
	private List<Record> list;

	/**
	 * Constructs a DbaList object given a record object by initializing the dba
	 * field to the dba of that record and storing the record in the list
	 * 
	 * @param r
	 *            a Record object
	 * @author Joanna Klukowska
	 *
	 */
	public DbaList(Record r) throws IllegalArgumentException {
		// If the record passed is null then we cannot create a DbaList
		// We throw an exception
		if (r == null)
			throw new IllegalArgumentException("Error: cannot create " + "DbaList with a null Record object.");
		// If the record is not null then we initialize the dba string to be
		// used as a tag
		// and store the passed record in the list
		dba = r.getDba();
		list = new LinkedList<Record>();
		list.add(0, r);
	}

	/**
	 * Constructs a DbaList object given a dba String by initializing the dba to
	 * that string and creating an empty list
	 * 
	 * @param dba
	 *            a String
	 * @author Joanna Klukowska
	 *
	 */
	public DbaList(String dba) {
		this.dba = dba;
		list = new LinkedList<Record>();
	}

	/**
	 * @return returns the dba of this DbaList
	 * 
	 * @author Joanna Klukowska
	 *
	 */
	public String getDba() {
		return dba;
	}

	/**
	 * Sets the dba of this DbaList to the given dba
	 * 
	 * @param dba
	 *            a String object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void setDba(String dba) {
		this.dba = dba;
	}

	/**
	 * @return returns the list of this DbaList
	 * 
	 * @author Joanna Klukowska
	 *
	 */
	public List<Record> getList() {
		return list;
	}

	/**
	 * Given a record object r it checks if it is equal to the dba of the
	 * current DbaList and adds it to the head of the the list
	 *
	 * @param r
	 *            a record object
	 * 
	 * @return boolean, returns true if the object is added and false if it
	 *         isn't
	 * 
	 * @author Joanna Klukowska
	 *
	 */
	public boolean add(Record r) {
		if (r.getDba().equalsIgnoreCase(this.dba)) {
			list.add(0, r);
			return true;
		} else
			return false;
	}

	/**
	 * This method is used to compare two dba lists by checking if their dbas
	 * are equal it doesn't compare the actual elements of the lists, just their
	 * dbas (not case sensitive)
	 * 
	 * @param other
	 *            A DbaList object
	 * @return int Returns 0 if equal, 1 is greater and -1 if smaller
	 * @author Alperen Karaoglu
	 *
	 */
	public int compareTo(DbaList other) {
		return this.dba.compareToIgnoreCase(other.dba);
	}

	/**
	 * Returns the size of the current list.
	 * 
	 * @return int
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public int size() {
		return this.getList().size();
	}

	/**
	 * Accesses the element in the list at the given index
	 * 
	 * @param index
	 *            integer that specifies desired index
	 * 
	 * @return Record record object at provided index
	 * @author Alperen Karaoglu
	 *
	 */
	public Record getElement(int index) {
		return this.getList().get(index);
	}

	/**
	 * Clears the elements of the list and sets its dba to null
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void clear() {
		this.setDba(null);
		this.getList().clear();
	}

	/**
	 * Transforms the List<Record> list object to a MyArrayList<Record> object
	 * Used to match the main method and make the contents printable using the
	 * toString() method of the MyArrayList class
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public MyArrayList toArrayList() {
		MyArrayList arraylist = new MyArrayList();

		if (this.list != null) {
			for (int i = 0; i < list.size(); i++) {
				arraylist.add(this.getElement(i));
			}
		}
		return arraylist;
	}

}