public class MyListQueue<E> {
	private MyLinkedList<E> items;
	
	public MyListQueue() {
		items = new MyLinkedList<>();
	}

	public void enqueue(E ob) {
		items.add(ob);
	}

	public E dequeue() {
		return items.remove(0);
	}

	public boolean isEmpty() {
		return items.size() == 0 ? true : false;
	}
}
