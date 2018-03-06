import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.LinkedList;

class fruits {
	int i, j;

	fruits() {
	}

	fruits(int x, int y) {
		i = x;
		j = y;
	}
}

public class homework {

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
//		double ini = bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
//		long startTime = System.nanoTime();
		FileReader f = null;
		BufferedReader b = null;
		Gameplay n1 = new Gameplay();
		// int n, p;
		float time;
		boolean explored[][];
		try {
			f = new FileReader("input.txt");
			b = new BufferedReader(f);
			int i = 0, j = 0;

			n1.n = Integer.parseInt(b.readLine());
			n1.p = Integer.parseInt(b.readLine());
			n1.remaining_time = Float.parseFloat(b.readLine());
			n1.board = new int[n1.n][n1.n];
			String line;
			while ((line = b.readLine()) != null) {
				String t[] = line.trim().split("");
				for (j = 0; j < n1.n; j++) {
					if (t[j].equals("*")) {
						n1.board[i][j] = 100;
						continue;
					}
					n1.board[i][j] = Integer.parseInt(t[j]);
				}
				i++;
			}
			// Gameplay.printboard(n1.board);

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
		int len = n1.board.length;
		LinkedList<Move> rootmoves = n1.generateallmoves1(n1.board, new Move(), true);

//	 
//		for (int i = 1; i < 6; i++) {
			ThreadMXBean bean = ManagementFactory.getThreadMXBean();
			double ini = bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
			int a =n1.getdepth(rootmoves.size());
			System.out.println("depth"+a);
			n1.maxdepth = a;//a;
		n1.minimax(n1.maxdepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
//			System.out.println();
			
			double fin = bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
			System.out.println("time is : " + (fin - ini) / 1000000000);
		long endTime = System.nanoTime();
		// System.out.println("time taken = "+(endTime-startTime)/Math.pow(10, 9));
		// double duration = (endTime-startTime)/Math.pow(10, 9);

		// System.out.println(n1.iterations+"\t No. of iterations per second"+
		// (n1.iterations/duration));
		// n1.traversal(new fruits(1,0), n1.board);
		// n1.starem(new fruits(1,0), n1.board);
		// n1.rungravity(n1.board);
		// Gameplay.printboard(n1.board);
	}

}
