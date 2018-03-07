
import java.util.ArrayList;

public class query extends predicate{
//query can have only one argument
	
	query(String name, String val){
		
		super(name, 1, val);
	}
	
	query(){
		
	}
	
	public void copyQuery(String name, ArrayList<String> arguments)
	{
		query q = new query();
		q.name = name;
		System.out.println(q.name+"Hi query testrunset");
		q.arguments = arguments;
		
	}
}
