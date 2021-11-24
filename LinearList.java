package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinearList<E extends Comparable<E>> implements LinearListADT<E> {

	private Node<E> head; // Points to first node of the list
	private Node<E> tail; // Points to last node of the list
	int currentSize; // Maintains the size of list

	protected class Node<T> {
		T data;
		Node<T> next;
		Node<T> prev;

		public Node(T obj) {
			data = obj;
			next = null;
			prev = null;
		}
	}

	/*
	 * Adds the Object obj to the beginning of list and returns true once the obj
	 * gets added.
	 */
	@Override
	public boolean addFirst(E obj) {
		Node<E> newNode = new Node(obj);
		if (head == null)
			head = tail = newNode;
		else {
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
		}
		currentSize++;
		return true;
	}

	/*
	 * Adds the Object obj to the end of list and returns true once the obj gets
	 * added.
	 */
	@Override
	public boolean addLast(E obj) {
		Node<E> newNode = new Node(obj);
		if (head == null)
			head = tail = newNode;
		else {
			tail.next = newNode;
			newNode.prev = tail;
			tail = newNode;
		}
		currentSize++;
		return true;
	}

	/*
	 * Removes and returns the object obj at first position in list if the list is
	 * not empty, null if the list is empty.
	 */
	@Override
	public E removeFirst() {
		Node<E> firstNode = head;
		if (firstNode == null) {
			return null;
		}
		E obj = firstNode.data;
		head = head.next;
		firstNode.data = null;
		firstNode.next = null;
		if (head == null) {
			tail = null;
		} else {
			head.prev = null;
		}
		currentSize--;
		return obj;
	}

	/*
	 * Removes and returns the object obj at last position in list if the list is
	 * not empty, null if the list is empty.
	 */
	@Override
	public E removeLast() {
		Node<E> lastNode = tail;
		if (lastNode == null) {
			return null;
		}
		E obj = lastNode.data;
		tail = tail.prev;
		lastNode.data = null;
		lastNode.prev = null;
		if (tail == null) {
			head = null;
		} else {
			tail.next = null;
		}
		currentSize--;
		return obj;
	}

	/*
	 * Removes and returns the parameter object obj from the list if the list
	 * contains it, null otherwise. The ordering of the list is preserved.
	 */
	@Override
	public E remove(E obj) {
		if (head == null) {
			return null;
		} else {
			for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
				if (obj.equals(currentNode.data)) {
					Node<E> l_next = currentNode.next;
					Node<E> l_prev = currentNode.prev;
					if (l_prev == null) {
						head = l_next;
					} else {
						l_prev.next = l_next;
						currentNode.prev = null;
					}
					if (l_next == null) {
						tail = l_prev;
					} else {
						l_next.prev = l_prev;
						currentNode.next = null;
					}
					currentNode.data = null;
					currentSize--;
					return obj;
				}
			}
		}
		return null;
	}

	/*
	 * Returns the first element in the list, null if the list is empty. The list is
	 * not modified.
	 */
	@Override
	public E peekFirst() {
		Node<E> firstNode = head;
		if (firstNode == null) {
			return null;
		} else {
			return firstNode.data;
		}
	}

	/*
	 * Returns the last element in the list, null if the list is empty. The list is
	 * not modified.
	 */
	@Override
	public E peekLast() {
		Node<E> lastNode = tail;
		if (lastNode == null) {
			return null;
		} else {
			return lastNode.data;
		}
	}

	/*
	 * Returns true if the parameter object obj is in the list, false otherwise. The
	 * list is not modified.
	 */
	@Override
	public boolean contains(E obj) {
		if (head == null) {
			return false;
		} else {
			for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
				if (obj.equals(currentNode.data)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Returns the element matching parameter object obj if it is in the list, null
	 * otherwise. In the case of duplicates, this method returns the element closest
	 * to front. The list is not modified.
	 */
	@Override
	public E find(E obj) {
		if (head == null) {
			return null;
		} else {
			for (Node<E> currentNode = head; currentNode != null; currentNode = currentNode.next) {
				if (obj.equals(currentNode.data)) {
					return currentNode.data;
				}
			}
		}
		return null;
	}

	/*
	 * Method brings the list to an empty state.
	 */
	@Override
	public void clear() {
		for (Node<E> node = head; node != null;) {
			Node<E> nextNode = node.next;
			node.data = null;
			node.next = null;
			node.prev = null;
			node = nextNode;
		}
		head = null;
		tail = null;
		currentSize = 0;

	}

	/*
	 * Returns true if the list is empty, otherwise false
	 */
	@Override
	public boolean isEmpty() {
		if (currentSize == 0) {
			return true;
		}
		return false;
	}

	/*
	 * Returns false as list can't be full
	 */
	@Override
	public boolean isFull() {
		return false;
	}

	/*
	 * Returns the number of Objects currently in the list.
	 */
	@Override
	public int size() {
		return currentSize;
	}

	/*
	 * Returns an Iterator of the values in the list, presented in the same order as
	 * the underlying order of the list.
	 */
	@Override
	public Iterator<E> iterator() {
		return new IteratorCustom();
	}

	private class IteratorCustom implements Iterator<E> {
		int currentObjectCount;
		private Node<E> next = head;
		private Node<E> lastReturn;

		public boolean hasNext() {
			return currentObjectCount < currentSize;
		}

		public E next() {
			if (currentObjectCount >= currentSize)
				throw new NoSuchElementException();
			lastReturn = next;
			next = next.next;
			currentObjectCount++;
			return (E) lastReturn.data;
		}
	}
}
