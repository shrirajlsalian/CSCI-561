import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Gameplay {

	static int board[][];
	int n, p;
	float remaining_time;
	LinkedList<Move> finalmove;
	int finalscore = 0;
	int iterations, maxdepth;

	Gameplay() {
		finalmove = new LinkedList();
	}

	void initialexplore(int board[][], boolean explored[][]) {
		for (int k = 0; k < explored.length; k++) {
			for (int l = 0; l < explored.length; l++) {
				if (board[k][l] == 100) {
					explored[k][l] = true;
				}
			}
		}
	}
	
	public int getdepth(int possibleMoves)
	{
		int depth = 3, n = board.length;
		if (remaining_time < 1) {
			depth = 1;
		} else if (remaining_time < 2) {
			depth = 2;
		} else {
			if (n < 17) {
				if (possibleMoves <= 100) {
					depth = 5;
				} else if (n <= 200) {
					depth = 4;
				}
			} else if (n < 22) {
				if (possibleMoves < 60) {
					depth = 5;
				} else if (possibleMoves < 100) {
					depth = 4;
				}
			} else if (n < 27) {
				if (possibleMoves < 40) {
					depth = 5;
				} else if (possibleMoves < 80) {
					depth = 4;
				}
			}

		}
		return depth;
	
    }
/**
	public int getdepth(int possibleMoves)
	{
		int depth = 3;
	int boardSize = this.board.length;
	if(this.remaining_time<1)
		return 1;
	if(this.remaining_time<2)
		return 2;
	
    if (boardSize >= 22) {
        if (possibleMoves < 50) {
            return 4;
        } else if (possibleMoves < 100) {
            return 3;
        }

    } else if (boardSize >= 20) {
        if (possibleMoves < 60) {
            return 4;
        } else if (possibleMoves < 120) {
            return 3;
        }
    } else if (boardSize >= 17) {
        if (possibleMoves < 40) {
            return 5;
        } else if (possibleMoves < 80) {
            return 5;
        } else if (possibleMoves < 130) {
            return 3;
        }
    } else if (boardSize >= 15) {
        if (possibleMoves < 60) {
            return 5;
        } else if (possibleMoves < 100) {
            return 4;
        } else if (possibleMoves < 150) {
            return 3;
        }
    } else if (boardSize >= 13) {
        if (possibleMoves < 80) {
            return 5;
        } else if (possibleMoves < 130) {
            return 4;
        } else if (possibleMoves < 200) {
            return 3;
        }
    } else if (boardSize >= 9) {
        if (possibleMoves < 80) {
            return 5;
        } else if (possibleMoves < 160) {
            return 4;
        }
    } else if (boardSize > 0) {
        return 5;
    }
		return depth;
	}
	*/
	public LinkedList<fruits> traversal(fruits position, int board[][], boolean explored[][]) {
		int n = board.length;
		LinkedList<fruits> curr = new LinkedList();
		LinkedList<fruits> all = new LinkedList();

		// System.out.println("At course\n" + currpos.i + "\t" + currpos.j);

		initialexplore(board, explored);
		// for(int k=0;k<n;k++)
		// System.out.println(Arrays.toString(explored[k]));

		int i = position.i, j = position.j;
		int currfruit = board[i][j]; // get fruit type at position
//		node9.printboolean(explored);
		curr.add(position);
		all.add(position);
		explored[i][j] = true;
		
		int c = 0;
		// System.out.println(currfruit + "\n");
		while (!curr.isEmpty()) {

			fruits popf = curr.poll();
			i = popf.i;
			j = popf.j;
			explored[i][j] = true;
			if (board[i][j] == 100)
				continue; 
			if (i != 0 && board[i - 1][j] == currfruit && !explored[i - 1][j]) { // above
				curr.add(new fruits(i - 1, j));
				all.add(new fruits(i - 1, j));
				explored[i - 1][j] = true;
			}

			// System.out.println("above belowi ="+i+"j="+j);
			if (i != (n - 1) && board[i + 1][j] == currfruit && !explored[i + 1][j]) { // below
				// System.out.println("in above belowi ="+i+"j="+j);
				fruits temp = new fruits(i + 1, j);
				/// System.out.println("in here below i=" + temp.i + ", j=" + temp.j);
				curr.add(temp);
				all.add(temp);
				// System.out.println("Peek element\n" + curr.peek().i + "\t" + curr.peek().j);
				explored[i + 1][j] = true;
			}
			// System.out.println("After check below i= " + popf.i + " j=" + popf.j);

			if (j != 0 && board[i][j - 1] == currfruit && !explored[i][j - 1]) { // to the left
				
				curr.add(new fruits(i, j - 1));
				all.add(new fruits(i, j - 1));
				explored[i][j - 1] = true;
			}

			if (j != (n - 1) && board[i][j + 1] == currfruit && !explored[i][j + 1]) { // to the right
				
				curr.add( new fruits(i, j + 1));
				all.add( new fruits(i, j + 1));
				// System.out.println("to the right" + curr.peek().i + "\t" + curr.peek().j);
				explored[i][j + 1] = true;
			}

		}
		// printqueue(all);
		// System.out.println("\nSize of queue = " + all.size());
		return all;

	}

	public void starem(fruits position, int board[][]) {
		boolean explored[][] = new boolean[board.length][board.length];
		initialexplore(board, explored);
		LinkedList<fruits> movelist = traversal(position, board, explored);
		for (fruits ix : movelist) {
			int x = ix.i;
			int y = ix.j;
			board[x][y] = 100;
		}
	}

	public void rungravity(int boardrage[][]) {
		// 2 cases ntire board is *'d
		int n = boardrage.length;
		int k = n - 1;
		for (int j = 0; j < n; j++) {
			int count = n-1;
			for (int i = n - 1; i >= 0; i--)
				if (boardrage[i][j] != 100)
					boardrage[count--][j] = boardrage[i][j];
			
			while (count >= 0)
				boardrage[count--][j] = 100;
		}

	}

	
	public static void printboolean(boolean a[][])
	{
		for (int i=0;i<a.length;i++)
			System.out.println(Arrays.toString(a[i]));
	}
	public LinkedList<Move> generateallmoves(int board[][], Move m1, boolean maxplayer) {
		LinkedList<Move> allmoves = new LinkedList();
		int i, j, n = board.length, count = 0;
		boolean explored[][] = new boolean[n][n];
		initialexplore(board, explored); // make true for all
//		System.out.println("Hello");
//		printboolean(explored);
//		System.out.println("End");
		LinkedList<fruits> currentmove;
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				if (board[i][j] == 100 || explored[i][j])
					continue;
				currentmove = traversal(new fruits(i, j), board, explored);
				Move m2 = new Move();
				m2.mymove.addAll(m1.mymove);
				// System.out.println("\nCurrentmove is ");
				// printqueue(currentmove);
				m2.mymove.add(currentmove.get(0));
				if (maxplayer)
					m2.score = m1.score + (int) Math.pow(currentmove.size(), 2);
				else
					m2.score = m1.score - (int) Math.pow(currentmove.size(), 2);
				allmoves.add(m2);
				count++;
			}
		}
		// System.out.println("No.of moves possible "+count);
		// printallmoves(allmoves);
		return allmoves;
	}
	public LinkedList<Move> generateallmoves1(int board[][], Move m1, boolean maxplayer) {
		LinkedList<Move> allmoves = new LinkedList();
		int i, j, n = board.length, count = 0;
		boolean explored[][] = new boolean[n][n];
		initialexplore(board, explored); // make true for all
//		System.out.println("Hello");
//		printboolean(explored);
//		System.out.println("End");
		LinkedList<fruits> currentmove;
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				if (board[i][j] == 100 || explored[i][j])
					continue;
				currentmove = traversal(new fruits(i, j), board, explored);
				Move m2 = new Move();
				m2.mymove.addAll(m1.mymove);
				// System.out.println("\nCurrentmove is ");
				// printqueue(currentmove);
				m2.mymove.add(currentmove.get(0));
				if (maxplayer)
					m2.score = m1.score + (int) Math.pow(currentmove.size(), 2);
				else
					m2.score = m1.score - (int) Math.pow(currentmove.size(), 2);
				allmoves.add(m2);
				count++;
			}
		}
		System.out.println("No.of moves possible "+count);
//		 printallmoves(allmoves);
		return allmoves;
	}
	public void copyboard(int newboard[][]) {
		for (int i = 0; i < board.length; i++)
			newboard[i] = Arrays.copyOf(board[i], board.length);
	}

	public int[][] getcurrentmatrix(LinkedList<fruits> prevmoves) {
		int n = board.length;
		int newboard[][] = new int[n][n];
		copyboard(newboard);
		for (int i = 0; i < prevmoves.size(); i++) {
			this.starem(prevmoves.get(i), newboard);
			this.rungravity(newboard);
		}
		return newboard;
	}

	public void minimax(int maxdepth, int alpha, int beta) {
		Maxplayer(new Move(), maxdepth, alpha, beta);
		System.out.println(finalscore);
		System.out.println("Iterations = " + iterations);
//		printallmoves(finalmove);
//		 printmove(finalmove);
		writeinput(finalmove.getLast());
	}

	public int Maxplayer(Move m1, int depth, int alpha, int beta) {
		iterations++;
//		System.out.println(iterations);
		if (depth == 0)
			return m1.score;
		int v = Integer.MIN_VALUE;
		int newboard[][] = this.getcurrentmatrix(m1.mymove);
		LinkedList<Move> allmoves = generateallmoves(newboard, m1, true);
		if (allmoves.isEmpty())
			return m1.score;

		// sort allmoves
		Collections.sort(allmoves, new Comparator<Move>() {
			public int compare(Move n1, Move n2) {
				if (n1.score == n2.score) {
					return 0;
				} else if (n1.score > n2.score) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		// System.out.println("Maxplayer");
		// printallmoves(allmoves);
		for (int i = 0; i < allmoves.size(); i++) {
			Move m2 = allmoves.get(i);
			int currentscore = (int) Math.max(v, Minplayer(m2, depth - 1, alpha, beta));
			m2.score = currentscore;
			if (currentscore > v) {
				v = currentscore;
				if (depth == maxdepth) {
					if (!finalmove.isEmpty())
						finalmove.remove();
					finalmove.add(allmoves.get(i));
					finalscore = allmoves.get(i).score;
//					printmove(finalmove);
				}
				m2.score = v;
			}
			if (currentscore >= beta)
				return currentscore;
			alpha = Math.max(alpha, currentscore);
		}

		return v;
	}

	public int Minplayer(Move m1, int depth, int alpha, int beta) {
		iterations++;
//		System.out.println(iterations);
		if (depth == 0)
			return m1.score;
		int v = Integer.MAX_VALUE;
//		this.getcurrentmatrix(m1.mymove);
		int newboard[][] = this.getcurrentmatrix(m1.mymove);
		LinkedList<Move> allmoves = generateallmoves(newboard, m1, false);
		if (allmoves.isEmpty())
			return m1.score;
		// sort allmoves
		Collections.sort(allmoves, new Comparator<Move>() {
			public int compare(Move n1, Move n2) {
				if (n1.score == n2.score) {
					return 0;
				} else if (n1.score > n2.score) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		for (int i = 0; i < allmoves.size(); i++) {
			Move m2 = allmoves.get(i);
			v = (int) Math.min(v, Maxplayer(m2, depth - 1, alpha, beta));
			m2.score = v;
			if (v <= alpha)
				return v;
			beta = (int) Math.min(beta, v);
		}
		return v;
	}

	public static void printboard(int a[][]) {

		for (int i = 0; i < a.length; i++) {

			for (int j = 0; j < a.length; j++) {
				if (a[i][j] == 100) {
					System.out.print("*");
					continue;
				}
				System.out.print(a[i][j] + "");
			}
			System.out.println("");
		}
	}

	public static void printqueue(LinkedList<fruits> a) {
		for (fruits xy : a) {
			System.out.print("(" + xy.i + ", " + xy.j + ")");
		}
	}

	public static void printallmoves(LinkedList<Move> a) {
		System.out.println("In here");
		for (Move x : a) {
			System.out.println("\nmove score" + x.score);
			for (int i = 0; i < x.mymove.size(); i++)
				System.out.print("(" + x.mymove.get(i).i + ", " + x.mymove.get(i).j + ")");
			System.out.println();
		}
		System.out.println("Reched end");
	}

	public static void writeinput(Move move1) {
		if (move1.mymove.isEmpty())
			return;
		Gameplay n0 = new Gameplay();
		int size = board.length;
		int newboard[][] = new int[size][size];
		boolean explored[][] = new boolean[size][size];
		LinkedList<fruits> moves1 = move1.mymove;
		for (int i = 0; i < board.length; i++)
			newboard[i] = Arrays.copyOf(board[i], board.length);
		n0.initialexplore(newboard, explored);
		n0.starem((fruits) moves1.getLast(), newboard);
		n0.rungravity(newboard);
		StringBuilder b = new StringBuilder();
		fruits app = moves1.peek();
		System.out.println("(i, j) =" + app.i + ", " + app.j);
		b.append((char) (app.j + 'A'));
		b.append("" + (app.i + 1));
		b.append("\n");
		for (int i = 0; i < newboard.length; i++) {
			for (int j = 0; j < newboard[i].length; j++) {
				if (newboard[i][j] == 100) {
					b.append("*");
					continue;
				}
				b.append(newboard[i][j] + "");
			}
			if (i < newboard.length - 1)
				b.append("\n");
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
	
}
