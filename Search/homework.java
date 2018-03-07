import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class homework implements Serializable {
	short[][] nursery;
	short n, p, liz_placed;
	Stack<lizard> lposition = new Stack();
	static short[][] anneal;

	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isSafe(short r, short c) {
		// System.out.println("Can Lizard be placed in " + r + ", " + c + " check");
		short l, m, size = (short) this.nursery.length;
		if (r >= size || c >= size || this.nursery[r][c] == 2)
			return false;
		// System.out.println("I dont fail column up");
		for (l = r; l >= 0 && this.nursery[l][c] != 2; l--) // check for each column going up
		{
			if (this.nursery[l][c] == 1)
				return false;
		}
		// System.out.println("I dont fail column down");
		for (l = r; l < size && this.nursery[l][c] != 2; l++) // check for each column going down
		{
			if (this.nursery[l][c] == 1)
				return false;
		}
		// System.out.println("I dont fail right");
		for (m = c; m < size && this.nursery[r][m] != 2; m++) // check for each row towards right
		{
			if (this.nursery[r][m] == 1)
				return false;
		}

		for (m = c; m >= 0 && this.nursery[r][m] != 2; m--) // check for each row towards right
		{
			if (this.nursery[r][m] == 1)
				return false;
		}

		for (l = r, m = c; l >= 0 && m >= 0 && this.nursery[l][m] != 2; l--, m--)// upper left dia 1
		{
			if (this.nursery[l][m] == 1)
				return false;
		}

		for (l = r, m = c; l < size && m < size && this.nursery[l][m] != 2; l++, m++)// lower right dia 1
		{
			if (this.nursery[l][m] == 1)
				return false;
		}

		for (l = r, m = c; l >= 0 && m < size && this.nursery[l][m] != 2; l--, m++)// upper right dia 2
		{
			if (this.nursery[l][m] == 1)
				return false;
		}

		for (l = r, m = c; l < size && m >= 0 && this.nursery[l][m] != 2; l++, m--)// lower left dia 2
		{
			if (this.nursery[l][m] == 1)
				return false;
		}

		return true;
	}

	public void printnode() {
		System.out.println("Nursery");
		for (short i = 0; i < nursery.length; i++)
			System.out.println(Arrays.toString(nursery[i]));
	}

	private void copynursery(short source[][]) {
		this.nursery = new short[this.n][this.n];
		for (short i = 0; i < source.length; i++) {
			for (short j = 0; j < source.length; j++) {
				nursery[i][j] = source[i][j];
			} // System.out.println();
		} // this.nursery[i][j]=source[i][j];
	}

	private static void DFS(homework initial) {
		Stack<homework> DFStack = new Stack();
		boolean success = true;
		short i = 0;
		short n = (short) initial.nursery.length, p = initial.p;
		Iterator<homework> iter = DFStack.iterator();
		int tempcount = 0;
		if (initial.liz_placed == initial.p) {
			initial.writeinput(success);
			return;
		} // new added
			// System.out.println("my n is" + n);
		for (short z = (short) (n - 1); z >= 0; z--) {// loop untill there is at least something
			// System.out.println("start with z = " + z + "& tempcount=" + tempcount); // in
			// the stack use
			// &&stack.size()!=0
			if (tempcount == n) {
				initial.writeinput(false);
				return;
			}
			if (initial.nursery[tempcount][z] == 2) // in that case mentioned above instead of zero use a tempcount var
			{

				if (DFStack.isEmpty() && z == 0) {
					z = (short) (n);
					tempcount++;
				}
				continue; // place nested if below here otherwise it is unreachable
			}
			homework temp = new homework();
			temp.liz_placed = 1;
			temp.lposition.push(new lizard((short) 0, z));
			temp.n = n;
			temp.p = initial.p;
			temp.copynursery(initial.nursery);
			if (temp.nursery[tempcount][z] != (short) 2)
				temp.nursery[tempcount][z] = 1;
			DFStack.push(temp);
			if (temp.liz_placed == temp.p) {
				// temp.printnode();
				temp.writeinput(true);
				return;
			}

			// here take an if condition if stack size is 0 then re initialise z to
			// (short)(n-1) and tempcount++
			// temp.printnode();
		} // how to handle if all cells in a row are trees

		while (!DFStack.isEmpty()) {
			// System.out.println("\nNode popped from stack of size: " + DFStack.size());
			homework popstate = DFStack.pop();// if no queen placed in a row state will be discarded need to re insert
											// state in stack
			// System.out.println("Last Lizard was placed at (i, j)=" +
			// popstate.lposition.peek().i + ", "
			// + popstate.lposition.peek().j);
			boolean flag = true;
			short row = popstate.lposition.peek().i;
			short col = popstate.lposition.peek().j;
			while (flag) {
				if (row == popstate.lposition.peek().i)
					col = (short) (popstate.lposition.peek().j + 1);
				else
					col = 0;
				for (short j = (short) (n - 1); j >= col; j--) {// change j>
					// lizard l1 = popstate.lposition.peek();
					// System.out.println("Can lizard be placed at " + (row) + ", " + j);
					if (row >= n) { // this was row+1
						flag = false;
						break;
					}
					if (popstate.isSafe(row, (short) j)) // pass popped st ate for tree logic use l.j+1 ie start
															// searching
					// from next position
					{
						// System.out.println("Hi newstate! Lizard is placed at (i, j) =" + (row + 1) +
						// ", " + j);
						// System.out.println("Last Lizard was placed at (i, j)="+l1.i+", "+l1.j);

						homework newstate = (homework) deepClone(popstate);
						newstate.nursery[row][j] = 1;// it was row +1 here // for boolean array set this to true for
														// tree logic use
														// l.j+1
						newstate.lposition.push(new lizard((short) (row), j));// it was row +1 here too!
						newstate.liz_placed += 1;
						if (newstate.liz_placed == newstate.p) {
							// System.out.println("OK");
							// newstate.printnode();
							newstate.writeinput(true);
							return;
						}
						// newstate.printnode();
						DFStack.push(newstate);
						// System.out.println("Stack size=" + DFStack.size());
						flag = false;
						// lizard tem = newstate.lposition.peek();
						// System.out.println("lizard here"+tem.i+tem.j);
					}
				}

				row++;

			}

			// System.out.println("FAIL");
		}
		success = false;
		initial.writeinput(success);

	}

	private static void BFS(homework initial) {
		Queue<homework> BFStack = new LinkedList();
		boolean success = true;
		short i = 0;
		short n = (short) initial.nursery.length, p = initial.p;
		Iterator<homework> iter = BFStack.iterator();
		int tempcount = 0;
		// System.out.println("my n is" + n);
		for (short z = (short) (n - 1); z >= 0; z--) {// loop untill there is at least something
			// System.out.println("start with z = " + z + "& tempcount=" + tempcount); // in
			// the stack use
			// &&stack.size()!=0
			if (initial.nursery[tempcount][z] == 2) // in that case mentioned above instead of zero use a tempcount var
			{

				if (BFStack.isEmpty() && z == 0) {
					z = (short) (n);
					tempcount++;
				}
				continue; // place nested if below here otherwise it is unreachable
			}
			homework temp = new homework();
			temp.liz_placed = 1;
			temp.lposition.push(new lizard((short) 0, z));
			temp.n = n;
			temp.p = initial.p;
			temp.copynursery(initial.nursery);
			if (temp.nursery[tempcount][z] != (short) 2)
				temp.nursery[tempcount][z] = 1;
			BFStack.add(temp);
			if (temp.liz_placed == temp.p) {
				// temp.printnode();
				temp.writeinput(true);
				return;
			}
			// here take an if condition if stack size is 0 then re initialise z to
			// (short)(n-1) and tempcount++
			// temp.printnode();
		} // how to handle if all cells in a row are trees

		while (!BFStack.isEmpty()) {
			// System.out.println("\nNode popped from stack of size: " + BFStack.size());
			homework popstate = BFStack.remove();// if no queen placed in a row state will be discarded need to re insert
												// state in stack
			// System.out.println("Last Lizard was placed at (i, j)=" +
			// popstate.lposition.peek().i + ", "
			// + popstate.lposition.peek().j);
			boolean flag = true;
			short row = popstate.lposition.peek().i;
			short col = popstate.lposition.peek().j;
			while (flag) {
				if (row == popstate.lposition.peek().i)
					col = (short) (popstate.lposition.peek().j + 1);
				else
					col = 0;
				for (short j = (short) (n - 1); j >= col; j--) {// change j>
					// lizard l1 = popstate.lposition.peek();
					// System.out.println("Can lizard be placed at " + (row) + ", " + j);
					if (row >= n) { // this was row+1
						flag = false;
						break;
					}
					if (popstate.isSafe(row, (short) j)) // pass popped st ate for tree logic use l.j+1 ie start
															// searching
					// from next position
					{
						// System.out.println("Hi newstate! Lizard is placed at (i, j) =" + (row + 1) +
						// ", " + j);
						// System.out.println("Last Lizard was placed at (i, j)="+l1.i+", "+l1.j);

						homework newstate = (homework) deepClone(popstate);
						newstate.nursery[row][j] = 1;// it was row +1 here // for boolean array set this to true for
														// tree logic use
														// l.j+1
						newstate.lposition.push(new lizard((short) (row), j));// it was row +1 here too!
						newstate.liz_placed += 1;
						if (newstate.liz_placed == newstate.p) {
							// System.out.println("OK");
							// newstate.printnode();
							success = true;
							newstate.writeinput(success);
							return;
						}
						// newstate.printnode();
						BFStack.add(newstate);
						// System.out.println("Stack size=" + DFStack.size());
						flag = false;
						// lizard tem = newstate.lposition.peek();
						// System.out.println("lizard here"+tem.i+tem.j);
					}
				}

				row++;

			}

		}
		success = false;
		initial.writeinput(success);
		// System.out.println("FAIL");
	}

	public lizard safepos() {
		Random r = new Random();
		while (true) {
			// System.out.println("safepos n=" + this.n);
			short r1 = (short) r.nextInt(this.n);
			short c1 = (short) r.nextInt(this.n);
			if (this.nursery[r1][c1] != 1 && this.nursery[r1][c1] != 2) {
				return new lizard(r1, c1);
			}
		}
	}

	private boolean probaccept(double probability) {
		double rand = 0;
		Random r = new Random();
		rand = r.nextDouble();
		if (rand <= probability)
			return true;
		return false;

	}

	private int calEnrgy(ArrayList q) {
		// System.out.println("Calculating energyy from cal energy");
		int Energy = 0, size = this.p, conflicts = 0;
		// System.out.println("size of lizards"+p);
		for (short l = 0; l < size; l++) // for all queens calculate conflicts
		{
			lizard temp = (lizard) q.get(l);
			conflicts += issafeconflict_count((short) temp.i, (short) temp.j);
		}
		// System.out.println("calEnrgy "+conflicts);
		// System.exit(0);
		return conflicts; // I removed divide by 2
	}

	public int issafeconflict_count(short r, short c) {
		// System.out.println("calculating energy for queen" + r + "," + c);
		int conflict = 0;
		// System.out.println("Can Lizard be placed in " + r + ", " + c + " check");
		short l, m, size = (short) this.nursery.length;
		// System.out.println("CHeck for " + r + " ," + c);
		// this.printnode();
		for (l = (short) (r - 1); l >= 0 && this.nursery[l][c] != 2; l--) // check for each column going up
		{
			if (this.nursery[l][c] == 1) {
				conflict++;
			}
			// else {
			// System.out.println(l + "," + c + "is safe");
			// }
		}
		// System.out.println("conflict = "+ conflict);
		for (l = (short) (r + 1); l < size && this.nursery[l][c] != 2; l++) // check for each column going down
		{
			if (this.nursery[l][c] == 1)
				conflict++;
		}

		for (m = (short) (c + 1); m < size && this.nursery[r][m] != 2; m++) // check for each row towards right
		{
			if (this.nursery[r][m] == 1)
				conflict++;
		}

		for (m = (short) (c - 1); m >= 0 && this.nursery[r][m] != 2; m--) // check for each row towards left
		{
			if (this.nursery[r][m] == 1)
				conflict++;
		}

		for (l = (short) (r - 1), m = (short) (c - 1); l >= 0 && m >= 0 && this.nursery[l][m] != 2; l--, m--)// upper
																												// left
																												// dia 1
		{
			if (this.nursery[l][m] == 1)
				conflict++;
		}

		for (l = (short) (r + 1), m = (short) (c + 1); l < size && m < size && this.nursery[l][m] != 2; l++, m++)// lower
																													// right
																													// dia
																													// 1
		{
			if (this.nursery[l][m] == 1)
				conflict++;

		}

		for (l = (short) (r - 1), m = (short) (c + 1); l >= 0 && m < size && this.nursery[l][m] != 2; l--, m++)// upper
																												// right
																												// dia 2
		{
			if (this.nursery[l][m] == 1)
				conflict++;
		}

		for (l = (short) (r + 1), m = (short) (c - 1); l < size && m >= 0 && this.nursery[l][m] != 2; l++, m--)// lower
																												// left
																												// dia 2
		{
			if (this.nursery[l][m] == 1)
				conflict++;
		}
		// System.out.println("conflict" + conflict+"");
		// System.exit(0);
		return conflict;
	}

	public static void printArrayList(ArrayList lizqueue) {
		for (int i = 0; i < lizqueue.size(); i++) {
			System.out.print(lizqueue.get(i));
		}
	}

	public static void SimmulatedAnnealing(homework initial) {
		double T = 0.99;// T= TEMPERATURE
		// get current time
		ArrayList<lizard> q = new ArrayList();
		ArrayList<lizard> qnext = new ArrayList();
		int iterations = 0;
		double prob = 0;
		Random r = new Random();
		short size = initial.n;
		for (int k = 0; k < initial.p; k++) {
			lizard temp = initial.safepos();
			initial.nursery[temp.i][temp.j] = 1;
			q.add(new lizard(temp.i, temp.j));
			// break;
		}

		int E1 = 0, E2 = 0;
		short liz = initial.p;
		//System.out.println("Hello Initial node");
		//initial.printnode();
		int randq, Delta = 0;
		//System.out.println("");
		long start = System.currentTimeMillis();
		long end = start + 270*1000;
		while (T > 0&&System.currentTimeMillis()<end)  { // or my time is less that 4:45
			E1 = initial.calEnrgy(q);// issafeconflict_count();
			// System.out.println("Hi my Energy E1 isssss " + E1);
			if (E1 == 0) {
				// System.out.println("\nThis is");
				//initial.printnode();
				initial.writeinput(true);
				return;
			}
			// System.out.println("Queue size"+ q.size());
			randq = r.nextInt(q.size()); // select a random queen
			// System.out.println("eandm liz " + randq);
			lizard tempo = q.get(randq); // make a random move
			// System.out.println("The old queue");
			// printArrayList(q);
			homework newstate = new homework();
			newstate.nursery = new short[size][size];
			newstate.n = size;
			newstate.p = liz;
			newstate.copynursery(initial.nursery);
			// System.out.println("Old state =");
			//initial.printnode();

			newstate.nursery[tempo.i][tempo.j] = 0;
			lizard ab = newstate.safepos();
			newstate.nursery[ab.i][ab.j] = 1;
			qnext.clear();
			qnext.addAll(q);
			qnext.set(randq, ab); // replace old lizard by new one
			// System.out.println("The new Queue");
			// printArrayList(qnext);
			E2 = newstate.calEnrgy(qnext);
			// System.out.println("New state is = \n");
			//newstate.printnode();
			Delta = E2 - E1;
			prob = Math.exp(-1 * Delta / T);
			if (Delta < 0) {
				initial = (homework) deepClone(newstate);
				q.clear();
				// System.out.println("Queue size now"+ q.size());
				q.addAll(qnext);
		//T -= 0.0005;
			} else {
				if (newstate.probaccept(prob)) {
					initial = (homework) deepClone(newstate);
					q.clear();
					//System.out.println("Queue size now" + q.size());
					q.addAll(qnext);
			//T -= 0.0005;
				}
			}
			iterations++;
       	T = 1/Math.log(iterations);
			// 1 / (Math.log(iterations));
			// System.out.println("Ending enercy calculation start");
			// E1 = initial.calEnrgy(q);
			// System.out.println("Ending enerrgy="+E1);
			// break;
			// q.clear();
		}
		initial.writeinput(false);
	}

	public void writeinput(boolean success) {
		StringBuilder b = new StringBuilder();
		short size = this.n;
		if (success == false) {
			b.append("FAIL");
		} else {
			b.append("OK\n");
			// for(int i = 0; i<pa.length; i++)
			// {
			// nursery[pa[i].i][pa[i].j] = 1;
			// }
			for (int i = 0; i < nursery.length; i++) {
				for (int j = 0; j < nursery[i].length; j++)
					b.append(nursery[i][j] + "");
				if (i < nursery.length - 1)
					b.append("\n");
			}
		}
		FileWriter f;
		BufferedWriter w = null;
		try {
			f = new FileWriter("output.txt");
			w = new BufferedWriter(f);
			w.write(b.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String args[]) {
		FileReader f = null;
		BufferedReader b = null;
		homework n1 = new homework();
		String method = "";
		try {
			f = new FileReader("input.txt");
			b = new BufferedReader(f);

			short i = 0, j = 0, c = 0;
			String line;
			method = b.readLine();
			n1.n = (short) Integer.parseInt(b.readLine());
			n1.p = (short) Integer.parseInt(b.readLine());
			n1.liz_placed = 0;
			n1.nursery = new short[n1.n][n1.n];
			// System.out.println(method + "\n" + n1.n + " \n" + n1.p);
			while ((line = b.readLine()) != null) {
				String t[] = line.trim().split("");
				for (j = 0; j < n1.nursery[i].length; j++) {
					// System.out.println(i+" ," + j);
					n1.nursery[i][j] = (short) Integer.parseInt(t[j]);
					// if(board[i][j]==2)
					// tree[c++] = new posn(i,j);
				}
				i++;
			}
			// for (i = 0; i < n1.nursery.length; i++)
			// System.out.println(Arrays.toString((n1.nursery[i])));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (f != null)
					f.close();
				if (b != null)
					b.close();
			} catch (IOException ex) {

			}
		}
			if(method.equals("BFS"))
				BFS(n1);
			else if(method.equals("DFS"))
				DFS(n1);
			else
			 SimmulatedAnnealing(n1);
	}

}
