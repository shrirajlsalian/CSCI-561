
import java.util.ArrayList;
import java.util.Arrays;

public class predicate {
//predicate can have more than one arguments
	String name;
	int no_of_arguments;
	ArrayList<String> constants;
	ArrayList<String> arguments; // those multiple arguments are stored in an arraylist

	
// how to define the members? as string? or StringBuilder?
	
	predicate(String name, int no_arguments, String val)
	{
		this.name = name;
		this.no_of_arguments = no_arguments;
		constants = new ArrayList();
		arguments = new ArrayList();
		//convar =val;
	}
	
	predicate(){
		constants = new ArrayList();
		arguments = new ArrayList();
	}
	public void getName(String fullPredicate) // get name of the predicate
	{
		//if there is a tilde (~) symbol ignore it and pick the remaining substring
//1d		if(fullPredicate.contains("~"))
//1d			this.name = fullPredicate.substring(1, fullPredicate.indexOf('(')); // start from index 1 and go till (
//1d		else
			this.name = fullPredicate.substring(0, fullPredicate.indexOf('(')).trim(); //1 start from index 0 and go till (
//			System.out.println("Hello a"+name+"a");
	}
	
	public void getArguments(String fullPredicate, int row) // get arguments of predicate
	{
		//take a substring from ( to ) from fullPredicate and then run a splir to get the arguments
		String argumentSubstring = fullPredicate.substring( fullPredicate.indexOf('(')+1,  fullPredicate.indexOf(')')).trim(); //2
//		System.out.println("Hi here "+argumentSubstring);
		String argumentArray[] = argumentSubstring.split(",");
		
		for(String x: argumentArray)
		{
//			System.out.println(x.trim());
//s			arguments.add(x);
			if(x.trim().charAt(0)<'a')
				constants.add(x.trim());
			else {
				x = x+row;
//				System.out.println("Row "+ x);
			}
			arguments.add(x.trim());
		}	
//		System.out.println("My constants"+ constants);
	}
	
	public static predicate copyPredicate(predicate p)
	{
		predicate copy = new predicate();
		copy.name = p.name;
		copy.copyArguments(p);
		
		return copy;
	}
	
	public void copyArguments(predicate p)
	{
		int i=0;
		for(String x: p.arguments)
			this.arguments.add(i++, x);
	}
	
	public boolean equals(predicate p)
	{
//			System.out.println("The predicate in sentence is "+ predicatelist.get(i));
			if(!this.name.equals(p.name))
				return false;
			int j;
			for(j=0; j < p.arguments.size()&&j<this.arguments.size(); j++)
			{
				if(p.arguments.get(j).equals(this.arguments.get(j)))
					continue;
				else break;
			}
			if(j==p.arguments.size()) //what if no arguments
				return true;
		
		
		return false;
	
	}
	public String toString() // returns the name of the predicate, its arguments and the no. of arguments it has
	{
		StringBuilder sb = new StringBuilder("");
		for(int i=0; i<arguments.size(); i++)
		{
			sb.append(arguments.get(i));
			if(i!=(arguments.size()-1))
				sb.append(", ");
		}
		return  name+""+ arguments;//"\n"+name+"\t Arguments = "+ arguments+ " \t no_of_arguments = "+no_of_arguments+"\t constants"+" = "+constants;
	}
	
	
}
