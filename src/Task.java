public class Task {
	public int duration;
	public MyLinkedList<Task> precs;
	public MyLinkedList<Task> succs;
	public int EC;
	public int LC;
	//public boolean known;
	int top;
	int indegree;
	
	public Task() {
		duration = 0;
		precs = new MyLinkedList<>();
		succs = new MyLinkedList<>();
		EC = 0;
		LC = 0;
		//known = false;
		top = 0;
		indegree = 0;
	}
}
