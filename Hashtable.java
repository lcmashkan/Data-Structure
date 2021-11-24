package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashtable<K extends Comparable<K>, V> implements DictionaryADT<K, V> {

	private LinkedListDS<DictionaryNode<K, V>>[] dictionaryList;
	int tableSize; // HashTableMaximumSize
	int currentNodesCount; // Current nodes in hashtable

	// Constructor to initialize hash table
	public Hashtable(int maximumSize) {
		tableSize = maximumSize;
		dictionaryList = new LinkedListDS[maximumSize];
		for (int i = 0; i < maximumSize; i++) {
			dictionaryList[i] = new LinkedListDS<DictionaryNode<K, V>>();
		}
	}

	/*
	 * Returns true if the dictionary has an object identified by key in it,
	 * otherwise false.
	 */
	@Override
	public boolean contains(K key) {
		boolean result = false;
		DictionaryNode<K, V> newNode = new DictionaryNode<K, V>(key, null);
		int index = getHashCodeMod(key);
		result = dictionaryList[index].contains(newNode);
		return result;
	}

	/*
	 * Adds the given key/value pair to the dictionary. Returns false if the
	 * dictionary is full, or if the key is a duplicate. Returns true if addition
	 * succeeded.
	 */
	@Override
	public boolean add(K key, V value) {
		if (isFull() || contains(key)) {
			return false;
		}
		DictionaryNode<K, V> newNode = new DictionaryNode<K, V>(key, value);
		int index = getHashCodeMod(key);
		dictionaryList[index].addFirst(newNode);
		currentNodesCount++;
		return true;
	}

	/*
	 * Deletes the key/value pair identified by the key parameter. Returns true if
	 * the key/value pair was found and removed, otherwise false.
	 */
	@Override
	public boolean delete(K key) {
		if (currentNodesCount == 0) {
			return false;
		}
		DictionaryNode<K, V> newNode = new DictionaryNode<K, V>(key, null);
		int index = getHashCodeMod(key);
		boolean result = dictionaryList[index].remove(newNode);
		if (result == false) {
			return false;
		}
		currentNodesCount--;
		return true;
	}

	/*
	 * Returns the value associated with the parameter key. Returns null if the key
	 * is not found or the dictionary is empty.
	 */
	@Override
	public V getValue(K key) {
		if (currentNodesCount == 0) {
			return null;
		}
		DictionaryNode<K, V> newNode = new DictionaryNode<K, V>(key, null);
		int index = getHashCodeMod(key);
		DictionaryNode<K, V> resultNode = dictionaryList[index].find(newNode);
		if (resultNode == null) {
			return null;
		}
		return resultNode.value;
	}

	/*
	 * Returns the key associated with the parameter value. Returns null if the
	 * value is not found in the dictionary. If more than one key exists that
	 * matches the given value, returns the first one found.
	 */
	@Override
	public K getKey(V value) {
		if (currentNodesCount == 0) {
			return null;
		}
		int dictionarySize = dictionaryList.length;
		for (int i = 0; i < dictionarySize; i++) {
			Iterator<DictionaryNode<K, V>> itr = dictionaryList[i].iterator();
			while (itr.hasNext()) {
				DictionaryNode<K, V> node = itr.next();
				if (((Comparable<V>) node.value).compareTo(value) == 0) {
					return node.key;
				}
			}
		}
		return null;
	}

	/*
	 * Returns the number of key/value pairs currently stored in the dictionary
	 */
	@Override
	public int size() {
		return currentNodesCount;
	}

	/* Returns true if the dictionary is at maximum capacity */
	@Override
	public boolean isFull() {
		boolean result = false;
		if (currentNodesCount == tableSize) {
			result = true;
		}
		return result;
	}

	/* Returns true if the dictionary is empty */
	@Override
	public boolean isEmpty() {
		if (currentNodesCount == 0) {
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	@Override
	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}

	/* Method returns index of the key in hashtable basis on hashcode. */
	private int getHashCodeMod(K key) {
		int index = (key.hashCode() & 0x7FFFFFFF) % tableSize;
		return index;
	}

	private class DictionaryNode<K, V> implements Comparable<DictionaryNode<K, V>> {
		K key;
		V value;

		/* Constructor to initialize Dictionary Node. */
		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public int compareTo(DictionaryNode<K, V> node) {
			return ((Comparable<K>) key).compareTo((K) node.key);
		}
	}

	/*
	 * KeyIteratorHelper class implements the method to iterate on dictionary and
	 * retrieve keys in sorted order.
	 */
	protected class KeyIteratorHelper implements Iterator<K> {
		private DictionaryNode[] dictionaryNodes;
		private int index;

		public KeyIteratorHelper() {
			/*
			 * Create dictionaryNodes array of size equal to nodes in dictionary.
			 */
			dictionaryNodes = new DictionaryNode[currentNodesCount];
			index = 0;
			int j = 0;
			for (int i = 0; i < tableSize; i++) {
				for (DictionaryNode node : dictionaryList[i]) {
					dictionaryNodes[j] = node;
					j++;
				}
			}
			/* Sort dictionaryNodes array to retrieve keys in sorted order. */
			quickSort(0, (dictionaryNodes.length - 1));
		}

		private void quickSort(int left, int right) {
			if (right - left <= 0) {
				return;
			}
			DictionaryNode<K, V> pivot = dictionaryNodes[right];
			int partition = getPartition(left, right, pivot);
			quickSort(left, partition - 1);
			quickSort(partition + 1, right);
		}

		private int getPartition(int left, int right, DictionaryNode<K, V> pivot) {
			int lptr = left - 1;
			int rptr = right;
			for (;;) {
				while (dictionaryNodes[++lptr].compareTo(pivot) < 0)
					;
				while (rptr > 0 && (dictionaryNodes[--rptr].compareTo(pivot) > 0))
					;
				if (lptr >= rptr) {
					break;
				} else {
					swap(lptr, rptr);
				}
			}
			swap(lptr, right);
			return lptr;
		}

		private void swap(int lptr, int rptr) {
			DictionaryNode<K, V> temp = dictionaryNodes[lptr];
			dictionaryNodes[lptr] = dictionaryNodes[rptr];
			dictionaryNodes[rptr] = temp;
		}

		@Override
		public boolean hasNext() {
			return (index < currentNodesCount);
		}

		@Override
		public K next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			K currentKey = (K) dictionaryNodes[index].key;
			index++;
			return currentKey;
		}
	}

	/*
	 * ValueIteratorHelper class implements the methods to iterate on dictionary and
	 * retrieve values. Values will be retrieved in sorted order of keys.
	 */
	protected class ValueIteratorHelper implements Iterator<V> {
		Iterator<K> itr;

		public ValueIteratorHelper() {
			// get the iterator on keys
			itr = keys();
		}

		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		@Override
		public V next() {
			return getValue(itr.next());
		}
	}

	protected class LinkedListDS<E> implements ListADT<E> {
		/////////////////////////////////////////////////////////////////
		class Node<T> {
			T data;
			Node<T> next;

			public Node(T obj) {
				data = obj;
				next = null;
			}
		}
		// END CLASS NODE ///////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////
		class ListIteratorHelper implements Iterator<E> {
			Node<E> index;

			public ListIteratorHelper() {
				index = head;
			}

			public boolean hasNext() {
				return index != null;
			}

			public E next() {
				if (!hasNext())
					throw new NoSuchElementException();
				E tmp = index.data;
				index = index.next;
				return tmp;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		}
		// END CLASS LIST_ITERATOR_HELPER //////////////////////////////

		private Node<E> head, tail;
		private int currentSize;

		public LinkedListDS() {
			head = tail = null;
			currentSize = 0;
		}

		public void addFirst(E obj) {
			Node<E> newNode = new Node<E>(obj);
			if (isEmpty())
				head = tail = newNode;
			else {
				newNode.next = head;
				head = newNode;
			}
			currentSize++;
		}

		public void addLast(E obj) {
			Node<E> newNode = new Node<E>(obj);
			if (isEmpty())
				head = tail = newNode;
			else {
				tail.next = newNode;
				tail = newNode;
			}
			currentSize++;
		}

		public E removeFirst() {
			if (isEmpty())
				return null;
			E tmp = head.data;
			head = head.next;
			if (head == null)
				head = tail = null;
			currentSize--;
			return tmp;
		}

		public E removeLast() {
			if (isEmpty())
				return null;
			E tmp = tail.data;
			if (head == tail) // only one element in the list
				head = tail = null;
			else {
				Node<E> previous = null, current = head;
				while (current != tail) {
					previous = current;
					current = current.next;
				}
				previous.next = null;
				tail = previous;
			}

			currentSize--;
			return tmp;
		}

		public E peekFirst() {
			if (head == null)
				return null;
			return head.data;
		}

		public E peekLast() {
			if (tail == null)
				return null;
			return tail.data;
		}

		public E find(E obj) {
			if (head == null)
				return null;
			Node<E> tmp = head;
			while (tmp != null) {
				if (((Comparable<E>) obj).compareTo(tmp.data) == 0)
					return tmp.data;
				tmp = tmp.next;
			}
			return null;
		}

		public boolean remove(E obj) {
			if (isEmpty())
				return false;
			Node<E> previous = null, current = head;
			while (current != null) {
				if (((Comparable<E>) current.data).compareTo(obj) == 0) {
					if (current == head)
						removeFirst();
					else if (current == tail)
						removeLast();
					else {
						previous.next = current.next;
						currentSize--;
					}
					return true;
				}
				previous = current;
				current = current.next;
			}
			return false;
		}

		// not in the interface. Removes all instances of the key obj
		public boolean removeAllInstances(E obj) {
			Node<E> previous = null, current = head;
			boolean found = false;
			while (current != null) {
				if (((Comparable<E>) obj).compareTo(current.data) == 0) {
					if (previous == null) { // node to remove is head
						head = head.next;
						if (head == null)
							tail = null;
					} else if (current == tail) {
						previous.next = null;
						tail = previous;
					} else
						previous.next = current.next;
					found = true;
					currentSize--;
					current = current.next;
				} else {
					previous = current;
					current = current.next;
				}
			} // end while
			return found;
		}

		public void makeEmpty() {
			head = tail = null;
			currentSize = 0;
		}

		public boolean contains(E obj) {
			Node current = head;
			while (current != null) {
				if (((Comparable<E>) current.data).compareTo(obj) == 0)
					return true;
				current = current.next;
			}
			return false;
		}

		public boolean isEmpty() {
			return head == null;
		}

		public boolean isFull() {
			return false;
		}

		public int size() {
			return currentSize;
		}

		public Iterator<E> iterator() {
			return new ListIteratorHelper();
		}
	}

	protected interface ListADT<E> extends Iterable<E> {

		// Adds the Object obj to the beginning of the list
		public void addFirst(E obj);

		// Adds the Object obj to the end of the list
		public void addLast(E o);

		// Removes the first Object in the list and returns it.
		// Returns null if the list is empty.
		public E removeFirst();

		// Removes the last Object in the list and returns it.
		// Returns null if the list is empty.
		public E removeLast();

		// Returns the first Object in the list, but does not remove it.
		// Returns null if the list is empty.
		public E peekFirst();

		// Returns the last Object in the list, but does not remove it.
		// Returns null if the list is empty.
		public E peekLast();

		// Finds and returns the Object obj if it is in the list, otherwise
		// returns null. Does not modify the list in any way
		public E find(E obj);

		// Removes the first instance of thespecific Object obj from the list, if it
		// exists.
		// Returns true if the Object obj was found and removed, otherwise false
		public boolean remove(E obj);

		// The list is returned to an empty state.
		public void makeEmpty();

		// Returns true if the list contains the Object obj, otherwise false
		public boolean contains(E obj);

		// Returns true if the list is empty, otherwise false
		public boolean isEmpty();

		// Returns true if the list is full, otherwise false
		public boolean isFull();

		// Returns the number of Objects currently in the list.
		public int size();

		// Returns an Iterator of the values in the list, presented in
		// the same order as the list.
		public Iterator<E> iterator();

	}
}