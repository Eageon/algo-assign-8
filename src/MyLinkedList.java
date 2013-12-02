/**
 * Algorithm-Assign-2
 * Author Jun Yu
 */

import java.util.Iterator;

/**
 * @author Jun Yu
 * 
 */
public class MyLinkedList<E> implements Iterable<E> {

	public MyLinkedList() { // Construct an empty LinkedList.
		doClear();
	}

	private static class Node<E> {
		public Node(E d, Node<E> p, Node<E> n) {
			data = d;
			prev = p;
			next = n;
		}

		@SuppressWarnings("unused")
		public Node(Node<E> e) {
			data = e.data;
			prev = e.prev;
			next = e.next;
		}

		public E data;
		public Node<E> prev;
		public Node<E> next;
	}

	private int theSize;
	private int modCount = 0;
	private Node<E> beginMarker;
	private Node<E> endMarker;

	public int size() {
		return theSize;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void clear() {
		doClear();
	}

	public void doClear() { // Change the size of this collection to zero
		beginMarker = new Node<>(null, null, null);
		endMarker = new Node<>(null, beginMarker, null);
		beginMarker.next = endMarker;
		theSize = 0;
		modCount++;
	}

	public E get(int idx) {
		return getNode(idx).data;
	}

	public E set(int idx, E newVal) {
		Node<E> p = getNode(idx);
		E oldVal = p.data;
		p.data = newVal;
		return oldVal;
	}

	private Node<E> getNode(int idx) {
		return getNode(idx, 0, size() - 1);
	}

	public E remove(int idx) {
		if(theSize == 0)
			return null;
		return remove(getNode(idx));
	}

	private E remove(Node<E> p) {
		p.next.prev = p.prev;
		p.prev.next = p.next;
		theSize--;
		modCount++;
		return p.data;
	}

	private Node<E> getNode(int idx, int lower, int upper) {
		Node<E> p;
		// if( idx < lower || idx > upper ) throw Exc
		if (idx < size() / 2) {
			p = beginMarker.next;
			for (int i = 0; i < idx; i++)
				p = p.next;
		} else {
			p = endMarker;
			for (int i = size(); i > idx; i--)
				p = p.prev;
		}
		return p;
	}

	public boolean add(E x) { // Add an item at the end
		add(size(), x);
		return true;
	}

	public void add(int idx, E x) { // Add item at specified location
		addBefore(getNode(idx, 0, size()), x);
	}

	private void addBefore(Node<E> p, E x) { // Add node at position p
		Node<E> newNode = new Node<>(x, p.prev, p);
		newNode.prev.next = newNode;
		p.prev = newNode;
		theSize++;
		modCount++;
	}

	private class LinkedListIterator implements java.util.Iterator<E> {
		private Node<E> current = beginMarker.next;
		private int expectedModCount = modCount;
		private boolean okToRemove = false;

		public boolean hasNext() {
			return current != endMarker;
		}

		public E next() { // if( modCount != expectedModCount ) throw exception
			if (!hasNext())
				throw new java.util.NoSuchElementException();
			E nextItem = current.data;
			current = current.next;
			okToRemove = true;
			return nextItem;
		}

		public void remove() { // if( modCount != expectedModCount ) throw
								// exception
			if (!okToRemove)
				throw new IllegalStateException();
			MyLinkedList.this.remove(current.prev);
			expectedModCount++;
			okToRemove = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new LinkedListIterator();
	}

	/************************************************************
	 * New method
	 ************************************************************/

	public boolean swap(int x, int y) {
		if (x > this.size() - 1 || y > this.size() - 1)
			return false;

		Node<E> xNode = this.getNode(x);
		Node<E> yNode = this.getNode(y);
		E swapValue = xNode.data;

		xNode.data = yNode.data;
		yNode.data = swapValue;

		return true;
	}

	public MyLinkedList<E> reverse() {
		MyLinkedList<E> reversedLinkedList = new MyLinkedList<E>();
		Node<E> node = this.endMarker.prev;
		while (node != this.beginMarker) {
			reversedLinkedList.add(node.data);
			node = node.prev;
		}

		return reversedLinkedList;
	}

	public boolean erase(int index, int num) {
		if ((index + num) > this.size())
			return false; // Not has enough nodes to remove

		Node<E> node = this.getNode(index).prev;
		for (int i = 0; i < num; i++) {
			this.remove(node.next);
		}

		return true;
	}

	public boolean insertList(int index, MyLinkedList<E> newList) {
		if (index > this.size() - 1)
			return false;

		Iterator<E> itor = newList.iterator();
		int i = index;
		while (itor.hasNext())
			this.add(i++, itor.next());

		return true;
	}

	public void print() {
		Iterator<E> itor = this.iterator();
		while (itor.hasNext())
			System.out.print(itor.next() + " ");
		System.out.println(" ");
	}

}
