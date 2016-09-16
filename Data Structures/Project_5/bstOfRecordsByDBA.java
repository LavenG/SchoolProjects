/**
 * This class represents a bstOfRecordsByDba object that a Binary Search Tree
 * composed of DbaNodes. It is used to store Records, specifically taking into
 * consideration their dba when making decisions considering order (when adding
 * to the tree).
 * 
 * @author Alperen Karaoglu
 *
 */
public class bstOfRecordsByDBA {

	// Refence to the root of the tree
	private DbaNode root;

	/**
	 * Constructs a bstOfRecordsByDBA object and sets it root to null
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public bstOfRecordsByDBA() {
		this.root = null;
	}

	/**
	 * Returns a reference to the DbaList containing all records in the tree
	 * that match the given dba, or null if no such records exist in the tree
	 * 
	 * @param dba
	 *            a String object
	 * 
	 * @return a DbaList object that contains the desired records
	 *
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaList get(String dba) {

		// If the root of the tree is null then there is nothing to look for
		if (this.root == null) {
			System.out.println("not tree found");
			return null;

		} else {
			// Otherwise get a reference to the root root of the tree and call
			// the getRecursive()
			// method passing to it the root and the given dba to find the
			// desired DbaList and return it
			DbaNode current = this.root;
			return getRecursive(dba, current);
		}
	}

	/**
	 * Returns a reference to the DbaList containing all records in the tree
	 * that match the given dba, or null if no such records exist in the tree
	 * 
	 * @param dba
	 *            a String object
	 * 
	 * @param current
	 *            a DbaNode object that reference the root of the tree
	 * 
	 * @return a DbaList object that contains the desired records
	 *
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaList getRecursive(String dba, DbaNode current) {

		// If the current node is null then we haven't found the DbaList we're
		// looking for
		// we return null
		if (current == null) {
			return null;
			// If the given dba is less than (not case sensitive) than the dba
			// of the current node
			// we go to the left child
		} else if (dba.compareToIgnoreCase(current.getDba()) < 0) {
			return getRecursive(dba, current.getLeftChild());
			// If the given dba is greater than (not case sensitive) than the
			// dba of the current node
			// we go to the right child
		} else if (dba.compareToIgnoreCase(current.getDba()) > 0) {
			return getRecursive(dba, current.getRightChild());
		} else {
			// Otherwise we have found what we're looking for so we return the
			// DbaList of the current node
			return current.getDbaList();
		}
	}

	/**
	 * Given a Record object R, adds the record to the DbaList with the matching
	 * name, if no such node exists, it creates a new node with the given record
	 * and adds it to the tree
	 * 
	 * @param r
	 *            a Record object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public void add(Record r) {
		// Set the root to reference the new (modified) tree
		this.root = addRecursive(this.root, r);
	}

	/**
	 * Given a Record object r and a DbaNode that references the root, adds the
	 * record to the DbaList with the matching name, if no such node exists, it
	 * creates a new node with the given record and adds it to the tree
	 * 
	 * @param node
	 *            a DbaNode object
	 * @param r
	 *            a Record object
	 * 
	 * @return a DbaNode that is a reference to the new (modified) tree
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	private DbaNode addRecursive(DbaNode node, Record record) {
		// if the root is null we make a new tree and initialize
		// the root with give record
		if (node == null) {
			DbaNode newNode = new DbaNode(record);
			return newNode;

			// if the root is not null we start searching
			// for the location where we want to add the record
		} else {
			// if the current node is where the record should be added then add
			// it here
			if (record.getDba().equalsIgnoreCase(node.getDba())) {
				node.add(record);
				// If the given dba is less than (not case sensitive) than the
				// dba
				// of the current node
				// we go to the left child
			} else if (record.getDba().compareToIgnoreCase(node.getDba()) < 0) {

				node.setLeftChild(addRecursive(node.getLeftChild(), record));
				// If the given dba is greater than (not case sensitive) than
				// the
				// dba of the current node
				// we go to the right child
			} else {
				node.setRightChild(addRecursive(node.getRightChild(), record));
			}
		}
		// return a reference to the modified tree
		return node;
	}

	/**
	 * Given a String object dba, removes and returns a DbList object that
	 * contains all of the record matching. Returns null if no such record
	 * exists
	 * 
	 * @param dba
	 *            a String object
	 * @return a DbaList object
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaList remove(String dba) {

		// If the DbaList was not found then return null
		if (this.get(dba) == null) {
			return null;
		}
		// Otherwise, first store the dbaList of the node to be removed
		DbaList nodeToBeRemovedList = this.get(dba);
		// Set the root to reference the new (possibly modified) tree
		this.root = recRemove(this.root, nodeToBeRemovedList);
		// Return the DbaList of the node that was removed
		return nodeToBeRemovedList;
	}

	/**
	 * Takes a reference to the tree (the root), locates the node to be removed,
	 * removes it and returns a reference to the new (possibly modified tree)
	 * 
	 * @param dba
	 *            a String object
	 * @return a DbaList object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode recRemove(DbaNode node, DbaList list) {
		// If the node is null then there is no tree and nothing to look for
		// we return null
		if (node == null) {
			return null;
			// If the given dba is less than (not case sensitive) than the dba
			// of the current node
			// we go to the left child
		} else if (list.compareTo(node.getDbaList()) < 0) {
			node.setLeftChild(recRemove(node.getLeftChild(), list));
			// If the given dba is greater than (not case sensitive) than the
			// dba of the current node
			// we go to the right child
		} else if (list.compareTo(node.getDbaList()) > 0) {
			node.setRightChild(recRemove(node.getRightChild(), list));
			// Otherwise we found what we were looking for so we remove it
		} else {
			node = remove(node);
		}
		// return a reference to the new (possibly modified) node
		return node;
	}

	/**
	 * Takes a node and removes it by considering every possible scenario,
	 * returns a reference to the new (possibly modified) node
	 * 
	 * @param node
	 *            a DbaNode object
	 * 
	 * @return a DbaNode object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaNode remove(DbaNode node) {
		// If the node only has a right child then we return it
		if (node.getLeftChild() == null) {
			return node.getRightChild();
		}
		// If the node only has a left child then we return it
		if (node.getRightChild() == null) {
			return node.getLeftChild();
		}
		// Otherwise the has two children
		// We get the predecessor of the node
		DbaList list = getPredecessor(node);
		// We replace the DbaList of the node by the dbalist of its predecessor
		node.setDbaList(list);
		// We update the references of the nodes children
		node.setLeftChild(recRemove(node.getLeftChild(), list));
		// We return the modified node
		return node;
	}

	/**
	 * Find the rightmost node in a left subtree of a given node and returns its
	 * DbaList
	 * 
	 * @param n
	 *            a DbaNode object
	 * 
	 * @return a DbaList object
	 * 
	 * @author Alperen Karaoglu
	 *
	 */
	public DbaList getPredecessor(DbaNode n) {

		// The left child of the node should not be null, we return an error in
		// this case
		if (n.getLeftChild() == null) {
			System.out.print("error");
			return null;
		} else {
			// Store a reference to the left child of the node
			DbaNode current = n.getLeftChild();
			// set the reference to its right child as long as it has one
			while (current.getRightChild() != null) {

				current = current.getRightChild();

			}
			// Return the DbaList of the rightmost node in the left subtree
			return current.getDbaList();
		}

	}
}
