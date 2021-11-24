package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {

	int currentSize = 0;
	int maxSize = 0;
	long entryNumber = 0;
	private Wrapper[] binaryHeap;

	protected class Wrapper<E> implements Comparable<Wrapper<E>> {

		long number;
		E data;

		public Wrapper(E d) {
			number = entryNumber++;
			data = d;
		}

		@Override
		public int compareTo(Wrapper<E> o) {
			if (((Comparable<E>) data).compareTo(o.data) == 0)
				return (int) (number - o.number);
			return ((Comparable<E>) data).compareTo(o.data);
		}
	}

	public BinaryHeapPriorityQueue() {
		this.binaryHeap = new Wrapper[DEFAULT_MAX_CAPACITY];
		this.maxSize = DEFAULT_MAX_CAPACITY;
	}

	public BinaryHeapPriorityQueue(int maximumSize) {
		this.binaryHeap = new Wrapper[maximumSize];
		this.maxSize = maximumSize;
	}

	/*
	 * Inserts a new object into the priority queue. Returns true if the insertion
	 * is successful. If the PQ is full, the insertion is aborted, and the method
	 * returns false.
	 */
	@Override
	public boolean insert(E object) {
		if (currentSize >= maxSize)
			return false;
		else {
			Wrapper obj = new Wrapper(object);
			currentSize++;
			int lastAvailableIndex = currentSize - 1;
			binaryHeap[lastAvailableIndex] = obj;
			trickleUp();
		}
		return true;
	}

	/*
	 * Removes the object of highest priority that has been in the PQ the longest,
	 * and returns it. Returns null if the PQ is empty.
	 */
	@Override
	public E remove() {
		if (currentSize == 0)
			return null;
		Wrapper obj = binaryHeap[0];
		E returnedObject = (E) obj.data;
		trickleDown(0);
		binaryHeap[currentSize - 1] = null;
		currentSize--;
		return returnedObject;
	}

	/*
	 * Deletes all instances of the parameter obj from the PQ if found, and returns
	 * true. Returns false if no match to the parameter obj is found.
	 */
	@Override
	public boolean delete(E obj) {

		if (currentSize == 0)
			return false;
		for (int i = 0; i < currentSize; i++) {
			// data is value type
			if (((Comparable) (binaryHeap[i].data)).compareTo(obj) == 0) {
				trickleDown(i);
				binaryHeap[currentSize - 1] = null;
				currentSize--;
				delete(obj);
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns the object of highest priority that has been in the PQ the longest,
	 * but does NOT remove it. Returns null if the PQ is empty.
	 */
	@Override
	public E peek() {
		if (currentSize == 0)
			return null;
		Wrapper obj = binaryHeap[0];
		E returnedObject = (E) obj.data;
		return returnedObject;
	}

	/*
	 * Returns true if the priority queue contains the specified element false
	 * otherwise.
	 */
	@Override
	public boolean contains(E obj) {
		if (currentSize == 0)
			return false;
		for (int i = 0; i < currentSize; i++) {
			if (binaryHeap[i].data.equals(obj))
				return true;
		}
		return false;
	}

	/* Returns the number of objects currently in the PQ. */
	@Override
	public int size() {
		return currentSize;
	}

	/* Returns the PQ to an empty state. */
	@Override
	public void clear() {
		if (currentSize == 0)
			return;
		for (int i = 0; i < currentSize; i++) {
			binaryHeap[i] = null;
		}
		currentSize = 0;
	}

	/* Returns true if the PQ is empty, otherwise false */
	@Override
	public boolean isEmpty() {
		if (currentSize == 0)
			return true;
		return false;
	}

	/* Returns true if the PQ is full, otherwise false. */
	@Override
	public boolean isFull() {
		if (currentSize == maxSize)
			return true;
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorCustom();
	}

	private class IteratorCustom implements Iterator<E> {
		int currentObjectCount;
		int ptr = 0;
		int lastReturn = -1;

		/*
		 * Returns true if the next element present while iterating over the binaryheap
		 * by validating currentObjectCount with currentSize, false otherwise.
		 */
		public boolean hasNext() {
			return currentObjectCount != currentSize;
		}

		/* Return the next object in iteration over the binary heap. */
		public E next() {
			int i = ptr;
			if (currentObjectCount >= currentSize)
				throw new NoSuchElementException();
			ptr = i + 1;
			lastReturn = i;
			currentObjectCount++;
			return (E) binaryHeap[lastReturn].data;
		}
	}

	/*
	 * Method restructures the heap to follow min heap property after adding an
	 * element to min heap.
	 */
	private void trickleUp() {
		int newIndex = currentSize - 1;
		int parentIndex = (newIndex - 1) >> 1;
		Wrapper<E> newValue = binaryHeap[newIndex];
		while (parentIndex >= 0 && newValue.compareTo(binaryHeap[parentIndex]) < 0) {
			binaryHeap[newIndex] = binaryHeap[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex - 1) >> 1;
		}
		binaryHeap[newIndex] = newValue;
	}

	/*
	 * Method restructures the heap to follow min heap property after removing an
	 * element from min heap.
	 */
	private void trickleDown(int index) {
		int current = index;
		int child = getNextChild(current);
		while (child != -1 && binaryHeap[current].compareTo(binaryHeap[child]) < 0
				&& binaryHeap[child].compareTo(binaryHeap[currentSize - 1]) < 0) {
			binaryHeap[current] = binaryHeap[child];
			current = child;
			child = getNextChild(current);
		}
		binaryHeap[current] = binaryHeap[currentSize - 1];
	}

	private int getNextChild(int current) {
		int left = (current << 1) + 1;
		int right = left + 1;
		if (right < currentSize) {
			if (binaryHeap[left].compareTo(binaryHeap[right]) < 0)
				return left;
			return right;
		}
		if (left < currentSize)
			return left;
		return -1;
	}
}
