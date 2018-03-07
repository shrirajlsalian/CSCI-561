
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Demo {

	public void takeInput(inferenceEngine inf) {
		//one way to take an input would be to use an array of strings and use the get index of method to find
		// i) The constant by getting index of ','
		// ii)The different predicates in a sentence by getting index of '|'
		//The second way is to use a char array and loop till you get the ',' for case (i) or the'|' for case (ii)
		// either ways the second approach can easily be derived from the first approach
		
		FileReader f = null;
		BufferedReader b = null;
		
		try {
			f = new FileReader("input");
			b = new BufferedReader(f);
			short i = 0, j = 0;
//			kB k = new kB(); // if required declare kb in psvm and call takeinput with the parameters as kB
			inf.no_of_queries = (short) Integer.parseInt(b.readLine());
			String queries[] = new String[inf.no_of_queries];
			
			for(i=0; i<inf.no_of_queries; i++)
			{
				queries[i] = b.readLine();
			}
//			System.out.println("The queries are \n"+Arrays.toString(queries));
			inf.setQueries(queries);
			inf.knowledgeBase.no_of_sentences =  (short) Integer.parseInt(b.readLine());
			String sentences[] = new String[inf.knowledgeBase.no_of_sentences];
			for(i=0; i<inf.knowledgeBase.no_of_sentences; i++)
			{
				sentences[i] = b.readLine();
				
			}
		//	System.out.println("In input main"+Arrays.toString(sentences));
			this.setKB(sentences, inf);

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
	}

	public void writeOutput(boolean output[])
	{
			StringBuilder b = new StringBuilder();
			// input-output done
			//need a better way to store output: first capitalise and then d
			for(int i=0;i<output.length;i++)
			{
				String a  = String.valueOf(output[i]);
				if(i==output.length-1)
					b.append(a.toUpperCase());
				else
					b.append(a.toUpperCase()+"\n");
			}
			FileWriter f;
			BufferedWriter w = null;
			try {
				f = new FileWriter("output");
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
	
	public void setKB(String sentences[], inferenceEngine inf)
	{
//		System.out.println("Forming predicates in setKB");

		for(int i=0;i<sentences.length;i++)
		{
			sentence s = new sentence();
//			System.out.println(sentences[i]);
			s.formPredicate(sentences[i], i);
			inf.putIntoHashMap(inf.predicateIntMapOriginal, s, i);
			inf.knowledgeBase.sentenceslist.add(s);
		//	System.out.println("This sentence has "+ s.toString());
		}	
//		System.out.println("My Hash Table\n"+inf.predicateIntMap);
		
	}
	
	public boolean[] runResolution(inferenceEngine inf)
	{
//	 	System.out.println(""+inf.predicateIntMap);
//		System.out.println("Start here");
		boolean result[] = new boolean[inf.no_of_queries];
//		System.out.println(inf.no_of_queries);
		for(int i=0; i< inf.no_of_queries;i++)
		{
			sentence s =new sentence();

//			System.out.println("Hey there"+inf.countUsed);
			s.predicatelist.add(inf.negateQuery(inf.queryList.get(i)));
			HashMap<String, String> substitutionMap = new HashMap();
			ArrayList<Integer> count = new ArrayList();
			
			inf.kcopy = kB.copyKB(inf.knowledgeBase);
			inf.kcopy.appendToKB(s);
//			System.out.println(inf.kcopy);
			inf.predicateIntMap = inf.copyHashTable(inf.predicateIntMapOriginal);
			inf.putIntoHashMap(inf.predicateIntMap, s, inf.knowledgeBase.sentenceslist.size());
//			System.out.println(inf.predicateIntMap);
			count = inf.setCountUsed(count);
			result[i] = inf.Resolution(s, count); //, substitutionMap);
//		s	System.out.println(result[i]);
//			break;
		}
//		System.out.println("My result is" +Arrays.toString(result));
		return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Demo d = new Demo();
		inferenceEngine inf = new inferenceEngine();
		d.takeInput(inf);
		// while writing the output check the number of input statements and the one's
		// left to prove and perform a sanity check to see if same number of statements
		// are written to output file
		
		//how to detect an infinite loop? refer pdf
		
		//
		
//		System.out.print("\nIn Inference engine\n"+ inf.toString());
		predicate q = new predicate();
//		ArrayList<String> arguments = new ArrayList();
		q.name="~P";
		q.arguments.add("Joe");
		q.arguments.add("Mary");
//		arguments.add("z");

//		q.arguments = arguments;
//		q.testRunSet("OmAmma", arguments);
		//System.out.println(inf.negateQuery(q).toString());
///*sc*/		sentence s3 = sentence.copySentence(inf.knowledgeBase.sentenceslist.get(2));

//sc		sentence s4 = sentence.copySentence(inf.knowledgeBase.sentenceslist.get(5));
//		HashMap<String, String> substitutionMap = new HashMap(); 
//		substitutionMap.put("x4", "Charlie");
//		substitutionMap.put("y4", "Billy");
//		substitutionMap.put("x5", "Charlie");
//		substitutionMap.put("y5", "Billy");
//		substitutionMap.put("z5", "Liz");
//		substitutionMap.put("z5", "x6");
//		System.out.println(substitutionMap);
//		System.out.println("My first predicate "+ inf.knowledgeBase.sentenceslist.get(0).predicatelist.get(0));
//		System.out.println("My second predicate "+ inf.knowledgeBase.sentenceslist.get(2).predicatelist.get(0));
//		predicate p1 = new predicate();
//		predicate p2 = new predicate();
//		p1.name = p2.name = "Amma" ;
//		p1.arguments.add("AB");
//		p1.arguments.add("y");
//		p1.arguments.add("s");
//		p2.arguments.add("z");
//		p2.arguments.add("Hello");
//		p2.arguments.add("t");
//		inf.checkPredicate(p1, p2, substitutionMap);
//		System.out.println("Original sentence is"+s.toString());
//		sentence copy =sentence.copySentence(s);
//		copy.predicatelist.get(0).arguments.add("x");
//		System.out.println("Copy sentence "+copy.toString());
//				System.out.println("The first  negated query is\n"+ s.toString());
	//	System.out.println("The value of 1st resolution is");
		//boolean set = inf.Resolution(s);
//		sentence ab = inf.merge(inf.knowledgeBase.sentenceslist.get(4), inf.knowledgeBase.sentenceslist.get(5), inf.knowledgeBase.sentenceslist.get(4).predicatelist.get(1));
//		System.out.println("The resolved sentence now is \n"+ab.toString());
//		System.out.println("The substitutioin map\n"+substitutionMap);
//		System.out.println("The sentence passed is\n"+s.toString());
//		inf.applySubstitution(s, substitutionMap);
//		System.out.println("Hi the substituted sentence \n"+ s.toString());
//		ArrayList<String> A = new ArrayList();
//		System.out.println(A.size());
//		System.out.println("The third sentence is \n"+s3);
//		System.out.println("The fourth sentence is \n"+s4);
//		inf.merge(s3, s4, substitutionMap);
		sentence s = new sentence();
		s.predicatelist.add(inf.queryList.get(0));
//		System.out.println(inf.queryList.get(0).toString());
//		HashMap<String, String> theta = inf.copyHashMap(substitutionMap);
//		theta.put("Om", "Amma");
//		System.out.println("Map = "+substitutionMap+"\nTheta = "+theta);
//		theta = inf.Unify(s, inf.knowledgeBase.sentenceslist.get(5), theta);
//		System.out.println("The Theta mapping is "+ theta);
//		System.out.println(q.toString());
//		System.out.println(s3.toString());
//		System.out.println(s3.contains(q));	
//		System.out.println("Start here!"+ inf.queryList.get(0));
//		System.out.println(inf.knowledgeBase.toString());
		boolean b[] = d.runResolution(inf);
//		System.out.println("My first Query "+ b);
		
		d.writeOutput(b);
		
	}
	
}
