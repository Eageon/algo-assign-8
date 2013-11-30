

public class Heap {

	public static final int DEFAULT_HEAP_SIZE = 17;

	private int[] array;
	private int heapSize = 0;

	public Heap(int size) {
		array = new int[size + 1];
	}

	public Heap() {

	}

	public void buildHeap(int[] rawArray) {
		heapSize = rawArray.length;
		array = new int[heapSize * 2 + 1];

		for (int i = 0; i < rawArray.length; i++) {
			array[i + 1] = rawArray[i];
		}

		for (int i = heapSize / 2; i > 0; i--) {
			percolateDown(i);
		}
	}

	private void percolateDown(int hole) {
		int child;
		int tmp = array[hole];

		for (; hole * 2 <= heapSize; hole = child) {
			child = hole * 2;
			if (child != heapSize && array[child + 1] < array[child])
				child++;
			if (array[child] < tmp)
				array[hole] = array[child];
			else
				break;
		}
		array[hole] = tmp;
	}

	public int deleteMin() {
		int minItem = array[1];
		array[1] = array[heapSize--];
		percolateDown(1);

		return minItem;
	}

	/**
	 * 
	 * @param value
	 * @return hole
	 */
	public int insert(int value) {
		// if(heapSize == array.length -1)

		int hole = ++heapSize;
		array[hole] = value;
		hole = percolateUp(hole);
		return hole;
	}

	public void decreaseElement(int index, int newValue) {
		int oldValue = array[index];
		if (oldValue <= newValue)
			return;

		array[index] = newValue;
		percolateUp(index);
	}

	private int percolateUp(int hole) {
		int value = array[hole];
		for (array[0] = value; value < array[hole / 2]; hole /= 2) {
			array[hole] = array[hole / 2];
		}
		array[hole] = value;
		return hole;
	}

	public boolean isEmpty() {
		return (heapSize == 0) ? true : false;
	}

	public void showHeap() {
		for (int i = 1; i <= heapSize; i++)
			System.out.print(array[i] + ", ");
		System.out.println("");
	}
}
