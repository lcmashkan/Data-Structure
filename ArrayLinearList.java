package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayLinearList<E> implements LinearListADT<E> {

	/* Points to first element of array */
	int front = -1;

	/* Points to last element of array */
	int rear = -1;

	/* Maximum size of array */
	int maxSize = 0;

	/* Number of objects array contains */
	int arraySize = 0;

	private Object[] arr;

	/* Constructs an empty array with the size specified in parameter */
	public ArrayLinearList(int maxCapacity) {
		arr = new Object[maxCapacity];
		maxSize = maxCapacity;
	}

	/* Constructs an empty array with the DEFAULT_MAX_CAPACITY */
	public ArrayLinearList() {
		arr = new Object[LinearListADT.DEFAULT_MAX_CAPACITY];
		maxSize = LinearListADT.DEFAULT_MAX_CAPACITY;
	}

	// Method will output the front and rear position
	@Override
	public void ends() {
		System.out.println("Front: " + front + " Rear: " + rear);
	}

	/*
	 * Add the object obj at the beginning of the array. If arraySize is 0 then set
	 * the front and rear to mid of array and add element, otherwise move the front
	 * to previous available position in circular order and add element.
	 * 
	 * returns false and aborts the insertion if the list is full, otherwise return
	 * true if the insertion is success.
	 */
	@Override
	public boolean addFirst(E obj) {
		if (arraySize == maxSize) {
			return false;
		} else {
			if (rear == -1 && front == -1) {
				front = maxSize / 2;
				rear = maxSize / 2;
			} else {
				front = (front - 1 + maxSize) % maxSize;
			}
		}
		arr[front] = obj;
		arraySize++;
		return true;
	}

	/*
	 * Add the object obj at the last of the array. If arraySize is 0 then set the
	 * rear and front to mid of array and add element, otherwise move the rear to
	 * next available position in circular order and add element.
	 * 
	 * returns false and aborts the insertion if the list is full, otherwise return
	 * true if insertion is success.
	 */
	@Override
	public boolean addLast(E obj) {
		if (arraySize == maxSize) {
			return false;
		} else {
			if (rear == -1 && front == -1) {
				front = maxSize / 2;
				rear = maxSize / 2;
			} else {
				rear = (rear + 1) % maxSize;
			}
		}
		arr[rear] = obj;
		arraySize++;
		return true;
	}

	/*
	 * Removes the first object, pointed by front in array and move front to next
	 * position in circular order and update the arraySize. If arraySize updates to
	 * 0 then set front and rear to -1.
	 * 
	 * Return the removed object obj if the array is not empty, null if the array is
	 * empty.
	 */
	@Override
	public E removeFirst() {
		if (arraySize == 0) {
			return null;
		}
		E obj = arr(front);
		arr[front] = null;
		arraySize--;
		if (arraySize == 0) {
			rear = -1;
			front = -1;
		} else
			front = (front + 1) % maxSize;
		return obj;
	}

	/*
	 * Removes the last object pointed by rear in array and move rear to previous
	 * available position in circular order and update the arraySize. If arraySize
	 * updates to 0 then set rear and front to -1.
	 * 
	 * Return the removed object obj if the array is not empty, null if the array is
	 * empty.
	 */
	@Override
	public E removeLast() {
		if (arraySize == 0) {
			return null;
		}
		E obj = arr(rear);
		arr[rear] = null;
		arraySize--;
		if (arraySize == 0) {
			rear = -1;
			front = -1;
		} else {
			rear = ((rear - 1) + maxSize) % maxSize;
		}
		return obj;
	}

	/*
	 * Removes and returns the parameter object obj from the array if the array
	 * contains it, null otherwise. The ordering of the array is preserved.
	 * 
	 * Method traverses the array from first position till matching element found,
	 * If element found then remove the element and shift all the subsequent
	 * elements to fill the slot. Decrease the arraySize by one, if the array size
	 * updates to 0 then set rear and front to -1 otherwise set rear to next
	 * previous available position in circular order.
	 */
	@Override
	public E remove(E obj) {
		if (arraySize == 0) {
			return null;
		} else {
			int i = front;
			for (int count = 1; count <= arraySize; count++) {
				if (obj.equals(arr[i])) {
					removeObject(i, count);
					return obj;
				}
				i = ((i + 1) % maxSize);
			}
		}
		return null;
	}

	private void removeObject(int i, int count) {
		while (count < arraySize) {
			arr[i] = arr[(i + 1) % maxSize];
			count++;
			i = (i + 1) % maxSize;
		}
		arraySize--;
		arr[i] = null;
		if (arraySize == 0) {
			rear = -1;
			front = -1;
		} else {
			rear = ((rear - 1) + maxSize) % maxSize;
		}
	}

	/*
	 * Returns the first element in the array pointed front, null if the array is
	 * empty.
	 * 
	 * The array is not modified.
	 * 
	 */
	@Override
	public E peekFirst() {
		if (arraySize == 0) {
			return null;
		}
		E obj = arr(front);
		return obj;
	}

	/*
	 * Returns the last element in array pointed by rear, null if array is empty.
	 * 
	 * The array is not modified.
	 * 
	 */
	@Override
	public E peekLast() {
		if (arraySize == 0) {
			return null;
		}
		E obj = arr(rear);
		return obj;
	}

	/*
	 * Method traverses thru the array and return true if array contains parameter
	 * object obj, false otherwise.
	 * 
	 * The array is not modified.
	 */
	@Override
	public boolean contains(E obj) {
		if (arraySize == 0) {
			return false;
		} else {
			int i = front;
			for (int count = 1; count <= arraySize; count++) {
				if (obj.equals(arr[i])) {
					return true;
				}
				i = ((i + 1) % maxSize);
			}
		}
		return false;
	}

	/*
	 * Method traverses thru the array and returns object matching with parameter
	 * object obj, null otherwise.
	 * 
	 * The array is not modified.
	 */
	@Override
	public E find(E obj) {
		if (arraySize == 0) {
			return null;
		} else {
			int i = front;
			for (int count = 1; count <= arraySize; count++) {
				if (obj.equals(arr[i])) {
					return arr(i);
				}
				i = ((i + 1) % maxSize);
			}
		}
		return null;
	}

	/* Method traverses thru the array and set array to empty state. */
	@Override
	public void clear() {
		if (arraySize == 0) {
			return;
		} else {
			int i = front;
			for (int count = 1; count <= arraySize; count++) {
				arr[i] = null;
				i = ((i + 1) % maxSize);
			}
		}
		arraySize = 0;
		front = -1;
		rear = -1;
	}

	/*
	 * Method checks if the array is empty by validating arraySize with 0. If the
	 * array is empty then return true, otherwise false.
	 */
	@Override
	public boolean isEmpty() {
		if (arraySize == 0) {
			return true;
		}
		return false;
	}

	/*
	 * Method checks if the array is full by validating arraySize with maxSize. If
	 * the array is full then return true, otherwise false.
	 */
	@Override
	public boolean isFull() {
		if (arraySize == maxSize) {
			return true;
		}
		return false;
	}

	/* Returns the number of objects currently in the array */
	@Override
	public int size() {
		return arraySize;
	}

	private E arr(int index) {
		return (E) arr[index];
	}

	/* Returns IteratorCustom object to iterate over the array in sequence. */
	@Override
	public Iterator<E> iterator() {
		return new IteratorCustom();
	}

	private class IteratorCustom implements Iterator<E> {
		int currentObjectCount;
		int ptr = front;
		int lastReturn = -1;

		/*
		 * Returns true if the next element present while iterating over the array by
		 * validating currentObjectCount with arraySize, false otherwise.
		 */
		public boolean hasNext() {
			return currentObjectCount != arraySize;
		}

		/* Return the next object in iteration over the array. */
		public E next() {
			int i = ptr;
			if (currentObjectCount >= arraySize)
				throw new NoSuchElementException();
			ptr = (i + 1) % maxSize;
			lastReturn = i;
			currentObjectCount++;
			return (E) arr[lastReturn];
		}
	}
}
