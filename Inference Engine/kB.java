
import java.util.ArrayList;

public class kB {
	int no_of_sentences;
	ArrayList<sentence> sentenceslist;
//	int no_of_queries;
//	ArrayList<query> queries;
	
	kB()
	{
		sentenceslist = new ArrayList();
	}
	
	public void appendToKB(sentence s)
	{
		sentenceslist.add(s); // when appending to the kb 
		this.no_of_sentences++;
	}
	
/*
	public sentence unify(sentence a, sentence b, predicate p)
	{
		sentence unified = new sentence();
		//The algorithm is as follows
		//Check if sentences can be unified
		//if no, then return null
		// else unify them and return the new sentence
		
		//sentence
		appendToKB(unified);// addd unified sentence to KB
		return unified;//Returnn Unified Sentence
	}
	*/
	
public static kB copyKB(kB k)
	{
		kB copy = new kB();
		for(int i=0;i<k.sentenceslist.size();i++)
		{
			copy.sentenceslist.add(sentence.copySentence(k.sentenceslist.get(i)));
		}
		
		return copy;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder("");
		for(int i =0;i<sentenceslist.size();i++)
		{
			sb.append(sentenceslist.get(i).toString()+"\n");
		}
		return sb.toString();
	}
}
