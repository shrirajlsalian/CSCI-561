
import java.util.ArrayList;
import java.util.Arrays;

public class sentence {
	ArrayList<predicate> predicatelist;
	ArrayList<String> predicateNames;
	
	sentence(){
		predicatelist = new ArrayList();
		predicateNames = new ArrayList();
	}
	
	sentence(String sent)
	{
		
	}
	
	public static int countOccurenceOf(String sent, char c)
	{
		int count=0;
		for(int i=0;i<sent.length();i++)
			if(sent.charAt(i)==c)
				count++;
		return count;
	}
	
	public void formPredicate(String sent, int row) { // can be used to form predicates and queries as well
	
		String clause_parts[] = sent.split("\\|"); // holds all predicates
//		System.out.println("Forming predicates in sentence class "+Arrays.toString(clause_parts));
		for(int i=0;i<clause_parts.length;i++)
		{
			predicate p = new predicate();
//			p.getName(clause_parts[i]);
			p.no_of_arguments = countOccurenceOf(clause_parts[i], ',') + 1;// no. of arguments
			p.getName(clause_parts[i]);
			p.getArguments(clause_parts[i], row);
//			System.out.println(p.arguments);
			predicatelist.add(p);
			predicateNames.add(p.name);
//			System.out.println(p.toString());
		}
		
	}
	
	public static sentence copySentence(sentence s)
	{
		sentence copy = new sentence();
		for(int i=0; i<s.predicatelist.size();i++)
		{
			copy.predicatelist.add(predicate.copyPredicate(s.predicatelist.get(i)));
		}
		
		return copy;
	}
	
	public boolean contains(predicate p)
	{
//		System.out.println("-------------------------------In contains----------------------------------------------\nPredicate to find " + p.toString());
		for(int i=0; i < predicatelist.size(); i++)
		{
//			System.out.println("The predicate in sentence is "+ predicatelist.get(i));
			if(!predicatelist.get(i).name.equals(p.name))
				continue;
			int j;
			for(j=0;j < p.arguments.size(); j++)
			{
				if(p.arguments.get(j).equals(predicatelist.get(i).arguments.get(j)))
					continue;
				else break;
			}
			if(j==p.arguments.size()) //what if no arguments
				return true;
		}
		
		return false;
	}
	
	public String toString() //returns the predicates in a sentence
	{
		StringBuilder asb = new StringBuilder("");
		for(int i=0; i<predicatelist.size(); i++)
		{
			asb.append(predicatelist.get(i).toString());
			if(i!=(predicatelist.size()-1))
				asb.append(" | ");
		}
		
		return asb.toString();
	}
	
}
