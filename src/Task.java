public class Task {
	public int duration;
	public MyLinkedList<Integer> precs;
	public MyLinkedList<Integer> succs;
	public int EC;
	public int LC;
	//public boolean known;
	int top;
	int reverseTop;
	int indegree;
	int outdegree;
	
	public Task() {
		duration = 0;
		precs = new MyLinkedList<>();
		succs = new MyLinkedList<>();
		EC = 0;
		LC = 0;
		//known = false;
		top = 0;
		reverseTop = 0;
		indegree = 0;
	}
}
