import java.util.*;

/**
 * Represents a sparse numeric vector. Elements are comprised of a (long)
 * location index an a (double) value. The vector is maintained in increasing
 * order of location index, which facilitates numeric operations like inner
 * products (projections). Note that location indices can be any integer from 1
 * to Long.MAX_VALUE. The representation is based upon a singly-linked list. The
 * following methods are supported: iterator, getSize, getFirst, add, remove,
 * and dot, which takes the dot product of the with a second vector passed as a
 * parameter.
 * 
 * @author sarwat shaheen
 */
public class SparseNumericVector implements Iterable {

	protected SparseNumericNode head = null;
	protected SparseNumericNode tail = null;
	protected long size;

	/**
	 * Iterator
	 */
	@Override
	public Iterator<SparseNumericElement> iterator() { // iterator
		return new SparseNumericIterator(this);
	}

	/**
	 * @return number of non-zero elements in vector
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return the first node in the list.
	 */
	public SparseNumericNode getFirst() {
		return head;
	}

	/**
	 * Add the element to the vector. It is inserted to maintain the vector in
	 * increasing order of index. If the element has zero value, or if an element
	 * with the same index already exists, an UnsupportedOperationException is
	 * thrown.
	 * 
	 * @param e
	 *            element to add
	 */
	public void add(SparseNumericElement e) throws UnsupportedOperationException {
		// implement this method
		SparseNumericNode newNode = new SparseNumericNode(e, null);
		if (newNode.getElement().getValue() == 0) {
			throw new UnsupportedOperationException();
		}

		if (this.head == null && this.tail == null) {
			this.head = newNode;
			this.tail = this.head;
			this.size++;
		} else if (this.head == this.tail) {
			if (e.getIndex() == this.head.getElement().getIndex()) {
				throw new UnsupportedOperationException();
			}
			if (e.getIndex() < this.head.getElement().getIndex()) {
				newNode.setNext(this.head);
				this.head = newNode;
				this.size++;
			}
			if (e.getIndex() > this.head.getElement().getIndex()) {
				this.tail.setNext(newNode);
				this.tail = this.tail.getNext();
				this.size++;
			}
		} else {
			SparseNumericNode primary = this.head;
			SparseNumericNode secondary = primary.getNext();
			if (e.getIndex() < this.head.getElement().getIndex()) {
				newNode.setNext(this.head);
				this.head = newNode;
				this.size++;
				return;
			}
			long i, j;
			for (; secondary != null;) {
				i = primary.getElement().getIndex();
				j = secondary.getElement().getIndex();
				if (e.getIndex() == i || e.getIndex() == j) {
					throw new UnsupportedOperationException();
				}
				if (e.getIndex() < j) {
					newNode.setNext(secondary);
					primary.setNext(newNode);
					this.size++;
					return;
				}
				primary = primary.getNext();
				secondary = secondary.getNext();
			}
			this.tail.setNext(newNode);
			this.tail = this.tail.getNext();
			this.size++;
		}
	}

	/**
	 * If an element with the specified index exists, it is removed and the method
	 * returns true. If not, it returns false.
	 *
	 * @param index
	 *            of element to remove
	 * @return true if removed, false if does not exist
	 */
	public boolean remove(Long index) {
		// implement this method
		// this return statement is here to satisfy the compiler - replace it with your
		// code.
		if (this.head == this.tail) {
			if (this.head.getElement().getIndex() == index) {
				this.head = null;
				this.tail = null;
				this.size--;
				return true;
			} else {
				return false;
			}
		} else {
			SparseNumericNode primary = this.head;
			SparseNumericNode secondary = primary.getNext();
			if (primary.getElement().getIndex() == index) {
				this.head = secondary;
				primary = null;
				this.size--;
				return true;
			}
			for (; secondary != null;) {
				if (secondary.getElement().getIndex() == index) {
					primary.setNext(secondary.getNext());
					secondary = null;
					this.size--;
					return true;
				}
				primary = primary.getNext();
				secondary = secondary.getNext();
			}
			return false;
		}
	}

	/**
	 * Returns the inner product of the vector with a second vector passed as a
	 * parameter. The vectors are assumed to reside in the same space. Runs in
	 * O(m+n) time, where m and n are the number of non-zero elements in each
	 * vector.
	 * 
	 * @param Y
	 *            Second vector with which to take inner product
	 * @return result of inner product
	 */

	public double dot(SparseNumericVector Y) {
		// implement this method
		// this return statement is here to satisfy the compiler - replace it with your
		// code.
		SparseNumericNode myNode = this.head;
		SparseNumericNode givenNode = Y.head;
		double dotProduct = 0;
		long myIndex, givenIndex;
		double myValue, givenValue;
		long mySize = this.size, givenSize = Y.size;
		for (; myNode != null && givenNode != null;) {
			myIndex = myNode.getElement().getIndex();
			givenIndex = givenNode.getElement().getIndex();
			myValue = myNode.getElement().getValue();
			givenValue = givenNode.getElement().getValue();
			if (mySize == givenSize) {
				if (myIndex == givenIndex) {
					dotProduct += (myValue * givenValue);
					myNode = myNode.getNext();
					givenNode = givenNode.getNext();
				} else {
					if (myNode.getNext() != null) {
						myNode = myNode.getNext();
					} else {
						givenNode = givenNode.getNext();
					}
				}
			} else {
				if (myIndex == givenIndex) {
					dotProduct += (myValue * givenValue);
					if (mySize < givenSize) {
						myNode = myNode.getNext();
					} else {
						givenNode = givenNode.getNext();
					}
				} else {
					if (mySize > givenSize) {
						if (myNode.getNext() != null) {
							myNode = myNode.getNext();
						} else {
							givenNode = givenNode.getNext();
							myNode = this.head;
						}
					} else {
						if (givenNode.getNext() != null) {
							givenNode = givenNode.getNext();
						} else {
							myNode = myNode.getNext();
							givenNode = Y.head;
						}
					}
				}
			}
		}
		return dotProduct;
	}

	/**
	 * returns string representation of sparse vector
	 */

	@Override
	public String toString() {
		String sparseVectorString = "";
		Iterator<SparseNumericElement> it = iterator();
		SparseNumericElement x;
		while (it.hasNext()) {
			x = it.next();
			sparseVectorString += "(index " + x.getIndex() + ", value " + x.getValue() + ")\n";
		}
		return sparseVectorString;
	}
}
