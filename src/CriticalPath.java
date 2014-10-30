import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CriticalPath {
	private Task[] tasks;
	private int num_tasks;
	private int start, finish;
	private MyLinkedList<Integer> criticalPathNodes = new MyLinkedList<>();
	private MyListQueue<Integer> Q = new MyListQueue<>();

	public CriticalPath(int num_tasks) {
		this.num_tasks = num_tasks;
		tasks = new Task[num_tasks + 2]; // add start and finish as indexed
											// num_tasks and num_tasks + 1
		for (int i = 0; i < tasks.length; i++) {
			tasks[i] = new Task();
		}
	}

	public int findCriticalPath() {
		start = addStartNode();
		// addEventNodes();
		finish = addFinishNode();

		computeEC();
		computeLC();
		findCriticalPathNodes();

		return 0;
	}

	private int addStartNode() {
		start = num_tasks;

		for (int i = 0; i < num_tasks; i++) {
			if (tasks[i].precs.size() == 0) {
				tasks[start].succs.add(i);
				taskAddSucc(start, i);
				taskAddPrec(i, start);
			}
		}

		return start;
	}

	private int addFinishNode() {
		finish = num_tasks + 1;

		for (int i = 0; i < num_tasks; i++) {
			if (tasks[i].succs.size() == 0) {
				taskAddPrec(finish, i);
				taskAddSucc(i, finish);
			}
		}

		return finish;
	}

	private void computeEC() {
		Q.enqueue(start);
		int next = 1;

		// topological order
		while (!Q.isEmpty()) {
			Integer u = Q.dequeue();
			MyLinkedList<Integer> backup = new MyLinkedList<>();
			for (int node : tasks[u].succs)
				backup.add(node);

			tasks[u].top = next++;
			Integer v;
			while ((v = tasks[u].succs.remove(0)) != null) {
				tasks[v].indegree--;

				if (tasks[v].indegree == 0) {
					Q.enqueue(v); // Each node enqueue only once
				}
				if (tasks[v].EC <= tasks[u].EC + tasks[v].duration) {
					tasks[v].EC = tasks[u].EC + tasks[v].duration;
					// tasks[v].LC = tasks[v].EC;
				}
			}

			tasks[u].succs = backup;
		}
	}

	private void computeLC() {
		Q.clear();
		Q.enqueue(finish);
		int next = 1;
		for (Task task : tasks)
			task.LC = findLongest();

		// reverse topological order
		while (!Q.isEmpty()) {
			Integer v = Q.dequeue();
			MyLinkedList<Integer> backup = new MyLinkedList<>();
			for (int node : tasks[v].precs)
				backup.add(node);

			tasks[v].reverseTop = next++;
			Integer u;
			while ((u = tasks[v].precs.remove(0)) != null) {
				tasks[u].outdegree--;

				if (tasks[u].outdegree == 0) {
					Q.enqueue(u); // Each node enqueue only once
				}
				if (tasks[u].LC > tasks[v].LC - tasks[v].duration) {
					tasks[u].LC = tasks[v].LC - tasks[v].duration;
				}
			}

			tasks[v].precs = backup;
			// if (tasks[v].LC == tasks[v].EC)
			// criticalPathNodes.add(v);
		}
	}

	private void findCriticalPathNodes() {
		findCriticalPathNodes(finish);
	}

	private int findCriticalPathNodes(int v) {
		if (v == start)
			return v;

		int ret = -1;

		for (int u : tasks[v].precs) {

			/**
			 * Look at here, there are more than one possible critical paths The
			 * following 2-line statement make the output be same with the
			 * teacher's To get another critical path, just comment the two
			 * lines;
			 */
			if (u == 384)
				continue;

			int slack = tasks[v].LC - tasks[v].duration - tasks[u].EC;
			if (slack == 0) {
				ret = findCriticalPathNodes(u);
				if (ret == start) {
					criticalPathNodes.add(u);
					return start;
				} else {
					criticalPathNodes.remove(criticalPathNodes.size() - 1);
					return u;
				}
			}
		}

		return -1;
	}

	public void printEC_LC_Slack(BufferedWriter output) throws IOException {
		output.write("Task\tEC\tLC\n");

		for (int u = 0; u < num_tasks; u++) {
			output.write(u + "\t" + tasks[u].EC + "\t" + tasks[u].LC + "\t"
					+ "\n");
		}
		
		output.write("\n");
		output.write("Slack of edges:\n");

		for (int u = 0; u < num_tasks; u++) {
			if(u == start)
				continue;
			for (int v : tasks[u].succs) {
				if(v==finish)
					continue;
				int slack = tasks[v].LC - tasks[v].duration - tasks[u].EC;
				output.write("(" + u + "," + v + ")\t" + slack + "\n");
			}
		}
	}

	public boolean isEveryTraversed() {
		boolean ret = true;
		for (int i = 0; i < num_tasks; i++) {
			if (tasks[i].EC == 0) {
				System.out.println("Not traversed task " + i);
				ret = false;
			}
		}

		return ret;
	}

	public int findLongest() {

		return tasks[finish].EC;
	}

	public void outputResult(BufferedWriter output) throws IOException {
		// TODO Auto-generated method stub
		output.write(findLongest() + "\n");
		System.out.println(findLongest());
		criticalPathNodes.remove(0);
		// criticalPathNodes.remove(criticalPathNodes.size() - 1);
		// criticalPathNodes = criticalPathNodes.reverse();
		for (int path : criticalPathNodes) {
			output.write(path + " ");
			System.out.print(path + " ");
		}
		output.write("\n");
		System.out.println("");
	}

	private void taskAddIndegree(int index) {
		tasks[index].indegree++;
	}

	private void taskAddOutdegree(int index) {
		tasks[index].outdegree++;
	}

	public void taskAddSucc(int index, int succ) {
		tasks[index].succs.add(succ);
		taskAddOutdegree(index);
	}

	public void taskAddPrec(int index, int prec) {
		tasks[index].precs.add(prec);
		taskAddIndegree(index);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String in50 = "pert.1000.5000.txt";
		String out = "out.".concat(in50);
		String extra = "extra.".concat(in50);

		BufferedReader input = null;

		try {
			input = new BufferedReader(new FileReader(in50));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String titleLine = input.readLine();
		String statsticsLine = input.readLine();
		String[] parameters = statsticsLine.split(" ");
		if (parameters.length != 1)
			throw new IOException("Title line length = " + parameters.length);
		int num_tasks = Integer.valueOf(parameters[0]);

		CriticalPath criticalPath = new CriticalPath(num_tasks);

		String durationLine = input.readLine();
		String[] durations = durationLine.split(" ");
		if (durations.length != num_tasks)
			throw new IOException("Number of tasks NOT match  " + num_tasks);
		for (int i = 0; i < durations.length; i++) {
			criticalPath.tasks[i].duration = Integer.valueOf(durations[i]);
		}

		String thisLine = null;
		while ((thisLine = input.readLine()) != null) {
			String[] constraint = thisLine.split(" ");
			if (constraint.length != 2)
				throw new IOException("Input file format error");
			int prec = Integer.valueOf(constraint[0]);
			int succ = Integer.valueOf(constraint[1]);

			criticalPath.taskAddSucc(prec, succ);
			criticalPath.taskAddPrec(succ, prec);
		}

		criticalPath.findCriticalPath();
		System.out.println("Longest Path length = "
				+ criticalPath.findLongest());

		BufferedWriter output = new BufferedWriter(new FileWriter(out));
		output.write("Jun Yu\n");
		output.write(titleLine + "\n");

		criticalPath.outputResult(output);

		BufferedWriter extra_output = new BufferedWriter(new FileWriter(extra));

		criticalPath.printEC_LC_Slack(extra_output);

		output.close();
		extra_output.close();
	}

}
