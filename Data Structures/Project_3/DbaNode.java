/**
 * This class represents a DbaNode object that is a node used to build a Binary
 * Search Tree It's data field is a DbaList and it has a reference to its left
 * and right children.
 * 
 * @author Alperen Karaoglu
 *
 */
public class DbaNode {

	// The data that the Node stores
	private DbaList dbalist;
	// Refereces to left and right children
	private DbaNode left;
	private DbaNode right;

	/**
	 * Constructs a DbaNode object given a String by initializing its dbalist
	 * and setting its children to null
	 * 
	 * @param dba
	 *            a String
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode(String dba) {
		this.dbalist = new DbaList(dba);
		this.left = null;
		this.right = null;
	}

	/**
	 * Constructs a DbaNode object given a Record object by initializing its
	 * dbalist's dba to the dba of the given record and adding the record to the
	 * dbalist's list and setting its children to null
	 * 
	 * @param r
	 *            a Record object
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode(Record r) {
		this.dbalist = new DbaList(r);
		this.left = null;
		this.right = null;
	}

	/**
	 * Constructs a DbaNode object given a Record object by initializing its
	 * dbalist's dba to the dba of the given record and adding the record to the
	 * dbalist's list and setting its children to the given nodes
	 * 
	 * @param r
	 *            a Record object
	 * @param left
	 *            a DbaNode object
	 * @param right
	 *            a DbaNode object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode(DbaList list, DbaNode left, DbaNode right) {
		this.dbalist = list;
		this.left = left;
		this.right = right;
	}

	/**
	 * @return a list that is the DbaList of this node
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaList getDbaList() {
		return this.dbalist;
	}

	/**
	 * @return a DbaNode that is the left child of this node
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode getLeftChild() {
		return this.left;
	}

	/**
	 * @return a DbaNode that is the right child of this node
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode getRightChild() {
		return this.right;
	}

	/**
	 * @return a String that is the dba of the DbaList of this node
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public String getDba() {
		return this.getDbaList().getDba();
	}

	/**
	 * Sets the left child of this node to the given node
	 * 
	 * @param n
	 *            a DbaNode object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void setLeftChild(DbaNode n) {
		this.left = n;
	}

	/**
	 * Sets the right child of this node to the given node
	 * 
	 * @param n
	 *            a DbaNode object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void setRightChild(DbaNode n) {
		this.right = n;
	}

	/**
	 * Adds a given record object to the dbalist of this node by calling the
	 * add() method of the DbaList class
	 * 
	 * @param r
	 *            a Record object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void add(Record r) {
		this.getDbaList().add(r);
	}

	/**
	 * Given a DbaList object sets the dbalist of this node to that parameter
	 * 
	 * @param list
	 *            a DbaList object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void setDbaList(DbaList list) {

		// if the given list is empty then we empty the DbaList of this node
		// and set its dba to null
		if (list.size() == 0) {
			this.getDbaList().clear();
		} else {
			// empty the DbaList of this node and set its dba to null
			this.getDbaList().clear();
			// Set the dba of this DbaList to the dba of the given list
			this.getDbaList().setDba(list.getDba());
			// Loop through the given list and copy all of its elements to the
			// DbaList of this node
			for (int i = 0; i < list.size(); i++) {

				this.dbalist.add(list.getElement(i));

			}
		}

	}

}
