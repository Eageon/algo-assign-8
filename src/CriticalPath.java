import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CriticalPath {
	private Task[] tasks;
	private Task start, finish;
	// private MyLinkedList<Task> eventNodes = new MyLinkedList<>();
	private MyListQueue<Task> Q = new MyListQueue<>();

	public CriticalPath(int num_tasks) {
		tasks = new Task[num_tasks];
		for (int i = 0; i < tasks.length; i++) {
			tasks[i] = new Task();
		}
	}

	public int findCriticalPath() {
		start = addStartNode();
		// addEventNodes();
		finish = addFinishNode();

		computeEC();

		return 0;
	}

	private Task addStartNode() {
		Task start = new Task();

		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i].indegree == 0) {
				start.succs.add(tasks[i]);
				tasks[i].indegree++;
			}
		}

		return start;
	}

	private Task addFinishNode() {
		Task finish = new Task();

		for (Task task : tasks) {
			if (task.succs.size() == 0) {
				finish.indegree++;
				task.succs.add(finish);
			}
		}

		return finish;
	}

	// private void addEventNodes() {
	// for (Task node : tasks) {
	// if (node.precs.size() > 1) {
	// Task nodePrime = new Task();
	// nodePrime.precs = node.precs;
	// nodePrime.succs.add(node);
	// node.precs = new MyLinkedList<>();
	// node.precs.add(nodePrime);
	// eventNodes.add(nodePrime);
	// }
	// }
	// }

	private void computeEC() {
		Q.enqueue(start);
		int next = 1;

		while (!Q.isEmpty()) {
			Task node = Q.dequeue();
			node.top = next++;
			Task succ;
			while ((succ = node.succs.remove(0)) != null) {
				succ.indegree--;

				if (succ.indegree == 0) {
					Q.enqueue(succ); // Each node enqueue only once
				}
				if (succ.EC <= node.EC + succ.duration) {
					succ.EC = node.EC + succ.duration;
				}
			}
		}

		// if(next != tasks.length +1)
		// try {
		// throw new Exception("G is NOT DAG");
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private void computeLC() {
		
	}

	public boolean isEveryTraversed() {
		boolean ret = true;
		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i].EC == 0) {
				System.out.println("Not traversed task " + i);
				ret = false;
			}
		}

		return ret;
	}

	public int findLongest() {

		return finish.EC;
	}

	public static void outputResult(BufferedWriter output) {
		// TODO Auto-generated method stub

	}

	private void taskAddIndegree(int index) {
		tasks[index].indegree++;
	}

	public void taskAddSucc(int index, int succ) {
		tasks[index].succs.add(tasks[succ]);
	}

	public void taskAddPrec(int index, int prec) {
		tasks[index].precs.add(tasks[prec]);
		taskAddIndegree(index);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String in50 = "pert.1000.5000.txt";
		String out = "out.pert.100.150.txt";
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
		System.out.println("Test : " + criticalPath.isEveryTraversed());

		BufferedWriter output = null;

		output = new BufferedWriter(new FileWriter(out));
		output.write("Jun Yu\n");
		output.write(titleLine + "\n");

		CriticalPath.outputResult(output);

		output.close();
	}

}
