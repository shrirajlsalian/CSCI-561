
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

	public class inferenceEngine {
		kB knowledgeBase, kcopy;
		int no_of_queries;
		int iterations = 0;
		ArrayList<predicate> queryList;
		Hashtable<String, ArrayList<Integer>> predicateIntMap;
		Hashtable<String, ArrayList<Integer>> predicateIntMapOriginal;
//		ArrayList<Integer> countUsed;
		// Hashtable<predicate, ArrayList<sentence>> h;
	
	inferenceEngine() {

		knowledgeBase = new kB();
		kcopy = new kB();
		predicateIntMap = new Hashtable();
		predicateIntMapOriginal = new Hashtable();
		queryList = new ArrayList();
	}
	
	public void putIntoHashMap(Hashtable<String, ArrayList<Integer>> predicateIntMapOriginal, sentence s, int row) {
		for (int i = 0; i < s.predicatelist.size(); i++) {
			if (predicateIntMapOriginal.containsKey(s.predicatelist.get(i).name))
				predicateIntMapOriginal.get(s.predicatelist.get(i).name).add(row);
			else {
				ArrayList<Integer> rowList = new ArrayList();
				rowList.add(row);
				predicateIntMapOriginal.put(s.predicatelist.get(i).name, rowList);
			}
			
		}
//	System.out.println(predicateIntMap);
	}
	
	public boolean isAConstant(String a) {
		if (a.charAt(0) <= 'Z')
			return true;
	
		return false;
	}
	
	public boolean isAVariable(String a) {
		if (a.charAt(0) >= 'a' && a.charAt(0) <= 'z')
			return true;
	
		return false;
	}
	
	public void setQueries(String queries[]) // change here
	{
		// System.out.println("Queries in InferenceEngine Class");
		for (int i = 0; i < queries.length; i++) {
			query q = new query();
			// p.getName(clause_parts[i]);
			q.no_of_arguments = 1;// no. of arguments
			q.getName(queries[i]);
			q.getArguments(queries[i], 0); // u can pass0 since it won't make a difference here as all queries will have
											// only constant arguments
			queryList.add(q);
			// System.out.println(q.toString());
		}
	}
	
	public ArrayList<Integer> setCountUsed(ArrayList<Integer> countUsed)
	{
		countUsed = new ArrayList();
		for(int i = 0; i<kcopy.sentenceslist.size(); i++)
		{
			countUsed.add(i, 0);
		}
		
		return countUsed;
	}
	
	public predicate negateQuery(predicate q1) {
		predicate q = predicate.copyPredicate(q1);
		if (q.name.charAt(0) == '~')
			q.name = q.name.substring(1);
		else
			q.name = "~" + q.name;
		return q;
	}
	
	public ArrayList<Integer> returnCountCopy(ArrayList<Integer> countUsed)
	{
		ArrayList<Integer> countCopy = new ArrayList();
		for(int i=0; i< countUsed.size(); i++)
		{
			countCopy.add(countUsed.get(i));
		}
		
		return countCopy;
	}
	public boolean Resolution(sentence resolvedSent, ArrayList<Integer> count) {//, HashMap<String, String> substitutionMap) { 
		// thte objective of this function is to take a sentence negate it and find the corresponding sentences in the Kb
//		System.out.println("\nI am now resolving " + resolvedSent.toString() );//+"\nsubstitution:- "+ substitutionMap +"\n");
//		System.out.println(predicateIntMap);
		
	  if(resolvedSent.predicatelist.size() == 0) // if resolved sentences.predicatelist == 0 sentence is resolved return true; 
		  return true; 
//	  kcopy = kB.copyKB(knowledgeBase);
	
	// The next step is to check all the sentences in the kB(or its clone ie // kbClone) that have the negated predicates of all predicates  in the resolvedSent ie till resolvedSent.predicatelist.size()==0 we loop // OR is it required? // while()
	  boolean  result = false;
	  for (int i = 0; i < resolvedSent.predicatelist.size(); i++) // for every predicate in resolvedSent find the sentence numbers in the kb
	  { 
		  // Now for each resolvedSent.predicatelist.get(i) search in the HashMap for the negated query So, take the 1st predicate negate it and check the sentence numbers
		  // Remember the resolvedSent should be added at the end
		  // As a backup create hashmap for <predicate, ArrayList<sentences> 
		  predicate currTemp = predicate.copyPredicate(resolvedSent.predicatelist.get(i)); //pickk the first predicate
		  currTemp = negateQuery(currTemp); //negate the curr predicate
		 
		  //n = no. of sentences in kb temp is in
		  if(predicateIntMap.get(currTemp.name) == null|| predicateIntMap.get(currTemp.name).size()==0)
			  return false; // if no sentences are present in kB then return false	  

		  // now check if all these sentences unify with resolvedSent 
		  
		  for (int j = 0; j <predicateIntMap.get(currTemp.name).size(); j++)
		  { // now check if resolvedSent and each of this unify against each other 
//			 System.out.println(predicateIntMap.get(currTemp.name)+"\t"+predicateIntMap.get(currTemp.name).get(j));
			  int index =predicateIntMap.get(currTemp.name).get(j); 
			  int value= count.get(index)+1;
			 count.set(index, value);
//			 System.out.println("Hi");
//			  System.out.println("count indices"+ countUsed);
//			 System.out.println(/*"My sentence in kb is"+*/j+" "+kcopy.sentenceslist.get(index)+" other sents"+predicateIntMap.get(currTemp.name));
			  if( count.get(index)>5)//100)
				  {
				//  count = this.setCountUsed(count);
				  continue;
				  }
//				  System.out.println(/*"The selected sentence from kB is is\n" +*/ knowledgeBase.sentenceslist.get(index).toString());
//				  System.out.println("Sentence to be resolved id"+sentence.copySentence(resolvedSent));
//			  System.out.println("In Resolution The map before is "+ substitutionMap); //unify working properly
			  HashMap<String, String> beta = new HashMap();
			  HashMap<String, String> theta = Unify(sentence.copySentence(resolvedSent), sentence.copySentence(kcopy.sentenceslist.get(index)),beta);// copyHashMap(substitutionMap)); //send copy
//			  System.out.println("In Resolution The map after is "+ theta); 
			  if(theta==null)
				  result= false;
			  else //if(!theta.isEmpty()) 
			  { // Resolve if a suitable substitution is found
				  
				  sentence newResolvedSent = merge(sentence.copySentence(resolvedSent), kcopy.sentenceslist.get(index), theta); // copyHashMap(theta)); 
				  
				  if(newResolvedSent.predicatelist.size() == 0) {
					  return true;
				  }
//				  System.out.println("-------------------------------------------New Call--------------------------------------------------------");//"the new rsolved sent is"+ newResolvedSent.toString());
				  result = Resolution(newResolvedSent, returnCountCopy(count));//, copyHashMap(theta));
				  
//				  System.out.println("BackTrack since result = "+ result);
//				  System.out.println("---------------------------------------------------------------------------------------------------");//"the new rsolved sent is"+ newResolvedSent.toString());
				  if(result==true)
					  return true;
				  
			  }

		  } 

	  }

	  return result; 
	}
	
	public HashMap<String, String> Unify(sentence prevResolvedSent1, sentence newSent1, HashMap<String, String> theta) {
		// you're using the prevResolvedSent and the newSent to return a substitution
		// that makes prevResolvedSent and newSent equal
		sentence prevResolvedSent = sentence.copySentence(prevResolvedSent1);
		sentence newSent = sentence.copySentence(newSent1);
		//
//		System.out.println("-------------------------------------------------------Hi there in Unify!---------------------------------------------------------------");
//		System.out.print(""+ prevResolvedSent);
//		System.out.println("\t&\n"+ newSent);
//		System.out.println("My substitution map is "+ theta);
		ArrayList<Boolean> flag = new ArrayList();
		for (int i = 0; i < prevResolvedSent.predicatelist.size(); i++) {
//			System.out.println("My predicate in prevResolvedSent is "+prevResolvedSent.predicatelist.get(i).toString());
			for (int j = 0; j < newSent.predicatelist.size(); j++) {
//				System.out.println("My predicate in newSent is "+newSent.predicatelist.get(j).toString());
				if(checkNegation(prevResolvedSent.predicatelist.get(i).name, newSent.predicatelist.get(j).name)) // CHECK IF THE TWO PREDICATES ARE NEGATIONS OF EACH OTHER
					{
					if(updateSubstitutionMap1(prevResolvedSent.predicatelist.get(i), newSent.predicatelist.get(j), theta)==false)//CHECK IF A SUBSTITUTION IS POSSIBLE
						{
//						return null;
//						System.out.println("Flag "+flag);
						flag.add(false);
						}
					else
						flag.add(true);
					}
					//error here
//				flag=0;
			}
//			flag = 0;
		}
//		System.out.println("Theta is "+ theta);
		if (!flag.contains(true))
			return null;
		
		return theta;
	}
	 
	public sentence merge(sentence prevResolvedSent, sentence newSent, HashMap<String, String> SubstitutionMap) {
		// U have two sentences, make a copy of them
		// take these examples:
		// A(x)|~B(x) and ~A(y)|B(y) conflicting different two pairs
		// A(x)|B(x) and C(x)|~B(x) these are the two easiest cases possible
		// A(x,y)|~A(z,y)|B(x,z) and ~A(x,B)|A(y,L)|B(x,z) Here if predicate contains
		// A(x) which one to unify first?

		sentence Resolved = new sentence();
		sentence prevResolvedSentCopy = sentence.copySentence(prevResolvedSent); // make copy of sentence 1
		sentence newSentCopy = sentence.copySentence(newSent); // make copy of sentence 2
		applySubstitution(prevResolvedSentCopy, SubstitutionMap);
//			System.out.println("The first substituted sentence is"+ prevResolvedSentCopy.toString());
//			System.out.println("The substitutiion map is "+ SubstitutionMap);
		applySubstitution(newSentCopy, SubstitutionMap);
//					System.out.println("The second substituted sentence is"+ newSentCopy.toString());
		if(prevResolvedSent.equals(newSent))
			return new sentence();
		int flag=0;
		for(int i=0; i< prevResolvedSentCopy.predicatelist.size();i++)
		{
			predicate negatedTemp = negateQuery(prevResolvedSentCopy.predicatelist.get(i));
			if(newSentCopy.contains(negatedTemp))
				continue;
//			else if(!Resolved.contains(prevResolvedSentCopy.predicatelist.get(i)))
				Resolved.predicatelist.add(prevResolvedSentCopy.predicatelist.get(i));
		}
		for(int i=0; i< newSentCopy.predicatelist.size();i++)
		{
			predicate negatedTemp = negateQuery(newSentCopy.predicatelist.get(i));
			if(prevResolvedSentCopy.contains(negatedTemp))
				continue;
//			else if(!Resolved.contains(newSentCopy.predicatelist.get(i)))
				Resolved.predicatelist.add(newSentCopy.predicatelist.get(i));
		}
	/**/
//		HashSet<Integer> Ai = new HashSet();
//		HashSet<Integer> Aj = new HashSet();
//		for(int i=0;i< prevResolvedSentCopy.predicatelist.size();i++)
//		{
//	//			System.out.println("Hi i here!");
//			for(int j=0;j<newSentCopy.predicatelist.size();j++)
//			{
//				//predicate names are not equal continue to next predicate in j
//	//				System.out.println("name of prevres "+ prevResolvedSentCopy.predicatelist.get(i).name);
//	//				System.out.println("name of newSentCopy "+newSentCopy.predicatelist.get(j).name);
//	//				System.out.println("Hi j here!");
//				//String newSentNeg = "~"+(newSentCopy.predicatelist.get(j).name);
//				if(!checkNegation(prevResolvedSentCopy.predicatelist.get(i).name, newSentCopy.predicatelist.get(j).name)) //of the sent its predicate and predicate's name equate a and b
//					continue;
//	//				System.out.println("Whoooo!");
//				int k;
//				for(k=0;k<newSentCopy.predicatelist.get(j).arguments.size();k++) //
//				{
//	//					System.out.println("Hi k here!");
//					String x = prevResolvedSentCopy.predicatelist.get(i).arguments.get(k);
//					String y = newSentCopy.predicatelist.get(j).arguments.get(k);
//					if(!x.equals(y))
//						break;
//				}
//				if(k==newSentCopy.predicatelist.get(j).arguments.size())
//				{
//					Ai.add(i); // if k == length thatmeans ith pred of prevSolvedCopy and jth pred of newSentCopy is the same
//					Aj.add(j);// store these in Ai and Aj
//					break;
//				}
//	//				else
//			}
//		}
//	//		System.out.println("Ai is\t"+ Ai.toString());
//	//		System.out.println("Aj is\t"+ Aj.toString());
//		for(int i=0;i< prevResolvedSentCopy.predicatelist.size();i++)//check this case
//		{
//			if (!Ai.contains(i)&&!Resolved.contains(prevResolvedSentCopy.predicatelist.get(i))) // if the ith pred is  not resolved add it to the resolved sentence
//				Resolved.predicatelist.add(prevResolvedSentCopy.predicatelist.get(i));
//		}
//		for(int j=0;j<newSentCopy.predicatelist.size();j++)
//		{//System.out.println("j = " + j);
//			if (!Aj.contains(j)&&!Resolved.contains(newSentCopy.predicatelist.get(j))) // if the jth pred is  not resolved add it to the resolved sentence
//				Resolved.predicatelist.add(newSentCopy.predicatelist.get(j));
//		}
		
//			System.out.println("In merge The resolved sentence is\n" + Resolved.toString());
		// deal with two cases here
		// the prevResolved sentence is what we're trying to unify so we only need to
		// check the predicate to be eliminated in the prevResolved
		// so we only check for the negated query in the newSent.
		// Thus, the substitution will be the constants from either sides
	//		System.out.println("Hello at the end!");
		return Resolved;
	
	}
	
	public sentence mergeSent(sentence prevResolvedSent, sentence newSent, HashMap<String, String> SubstitutionMap)
	{
		sentence Resolved = new sentence();
//		System.out.println("Mergeeee");
		sentence prevResolvedSentCopy = sentence.copySentence(prevResolvedSent); // make copy of sentence 1
		sentence newSentCopy = sentence.copySentence(newSent); // make copy of sentence 2
		applySubstitution(prevResolvedSentCopy, SubstitutionMap);
//			System.out.println("The first substituted sentence is b"+ prevResolvedSentCopy.toString()+"b");
//			System.out.println("The substitutiion map is "+ SubstitutionMap);
		applySubstitution(newSentCopy, SubstitutionMap);
//			System.out.println("The second substituted sentence is a"+ newSentCopy.toString()+"a");
		if(prevResolvedSentCopy.equals(newSentCopy))
			return new sentence();
		int flag=0;
		  Resolved.predicatelist.addAll(prevResolvedSentCopy.predicatelist);
		  Resolved.predicatelist.addAll(newSentCopy.predicatelist);
//		  System.out.println("Appended resolved "+ Resolved);
		  for(int i=0;i<prevResolvedSent.predicatelist.size(); i++)
		  {
			  predicate first = prevResolvedSentCopy.predicatelist.get(i);
			  System.out.println("First pred a"+ first+"a");
			  for(int j=0;j<newSent.predicatelist.size();j++)
			  {
				  predicate second = newSentCopy.predicatelist.get(j);
				  System.out.println("Second pred "+ second);
				  predicate negatedSec=negateQuery(second);
				  System.out.println("~Second pred b"+ negatedSec+"b");
				  if(first.equals(negatedSec))
				  {
//					  System.out.println("---------------------------Remove first and second"+ first+" "+ second);
					  if(Resolved.contains(first)&&Resolved.contains(second)){
						  Resolved.predicatelist.remove(Resolved.predicatelist.indexOf(first));
						  Resolved.predicatelist.remove(Resolved.predicatelist.indexOf(second));
						  break;
						  }
				  }
		
			  }
		  }
		  
		  return Resolved;
	}

	public boolean checkNegation(String x, String y)
	{
//		System.out.println("x = "+x+"\ty = "+y);
		if(x.charAt(0)=='~'&& !(y.charAt(0)== '~'))
		{
			if(x.substring(1).equals(y))
				return true;
			return false;
		}
		if(y.charAt(0)=='~'&&!(x.charAt(0)=='~'))
		{
			if(y.substring(1).equals(x))
				return true;
			return false;
		}
		
		return false;
	}
	
	public boolean updateSubstitutionMap(predicate a, predicate b, HashMap<String, String> substitutionMap) {
//		if(!a.name.equals(b.name))
//			return false;
//		if(checkNegation(a, b.name))
		//call with two predicates that are the negation values
//		System.out.println("Hi!");
		for(int i=0;i<a.arguments.size();i++)
		{
			
			String AsIthArg= a.arguments.get(i);
//			System.out.println("AsIharg is a"+ AsIthArg+"a");
			String BsIthArg = b.arguments.get(i);
//			System.out.println("BsIharg is b"+ BsIthArg+"b");
			if (isAConstant(AsIthArg) && isAConstant(BsIthArg)) // both are constants
			{
				
				if (AsIthArg.equals(BsIthArg))// both constants are equal, then return true
				{
					// update the substitution map ?
//					System.out.println("Im positively here");
					continue;
				} else {
//					System.out.println("Im here"+" 1st pred "+a+"\n2nd pred "+b);
					return false; // else return false
				}
			}
		
			if (isAVariable(AsIthArg) && isAConstant(BsIthArg)) // a has a variable and b has a constant
			{
				
				if (substitutionMap.containsKey(AsIthArg)) {
//					System.out.println("Hey there");
					if (substitutionMap.get(AsIthArg).equals(BsIthArg))// use a as the key in SubststitutionMap to get// value and equate to b.arguments
					{
//						System.out.println("Hello equals!");
						continue;
					}
					else {
//						System.out.println("var has "+predicateIntMap.get(b));
						return false;// return false as other substitution exists
					}
				}
				// update the substitution map if no key is found in hashmap
				substitutionMap.put(AsIthArg, BsIthArg);
				continue;
			}
	
			if (isAVariable(BsIthArg) && isAConstant(AsIthArg)) // a has a constant and b has a variable
			{
				if (substitutionMap.containsKey(BsIthArg)) {
//					System.out.println("Heythere");
					if (substitutionMap.get(BsIthArg).equals(AsIthArg))// use a as the key in SubststitutionMap to get
					{													// value and equate to b.arguments
//						System.out.println("Hi here in const-var!");
						continue;
					}
					else
						return false;// return false as other substitution exists
				}
				// update the substitution map
//				System.out.println("Hello war!");
				substitutionMap.put(BsIthArg, AsIthArg);
				continue;
			}
			
			if (isAVariable(AsIthArg) && isAVariable(BsIthArg)) // a and b both have variables
			{
				if (substitutionMap.containsKey(AsIthArg) && substitutionMap.containsKey(BsIthArg)) // both variables have a substitution
																									 
				{
					if (substitutionMap.get(AsIthArg).equals(substitutionMap.get(BsIthArg)))// both variables have same substitution return true
																							
						continue;
					else
					{
//						System.out.println("Hi here!");
						return false;//substitutionMap.put(BsIthArg, substitutionMap.get(AsIthArg));//; // else return false
					}
				}
	
				if (substitutionMap.containsKey(AsIthArg) && !substitutionMap.containsKey(BsIthArg)) // a exists but b
																										// does not
				{
					substitutionMap.put(BsIthArg, substitutionMap.get(AsIthArg)); // insert in b, the
					continue;																					// value of a
				}
	
				if (substitutionMap.containsKey(BsIthArg) && !substitutionMap.containsKey(AsIthArg)) // b exists but a
																										// does not
				{
					substitutionMap.put(AsIthArg, substitutionMap.get(BsIthArg)); // insert in a, the
					continue;																					// value of b
				}
				
				if (!substitutionMap.containsKey(AsIthArg) && !substitutionMap.containsKey(BsIthArg))
				{
					substitutionMap.put(AsIthArg, AsIthArg); 
					substitutionMap.put(BsIthArg, AsIthArg); // insert in b, the variable of a
					continue;																					
				}
			}
	
		}
					
		return true;			
	}
	
	public boolean updateSubstitutionMap1(predicate a, predicate b, HashMap<String, String> substitutionMap) {
//		System.out.println("Map here is "+ substitutionMap);
		for(int i=0;i<a.arguments.size();i++)
		{
//			System.out.println("Map=" +substitutionMap);
			String AsIthArg= a.arguments.get(i);
//			System.out.println("AsIharg is a"+ AsIthArg+"a");
			String BsIthArg = b.arguments.get(i);
//			System.out.println("BsIharg is b"+ BsIthArg+"b");
			
			if (isAConstant(AsIthArg) && isAConstant(BsIthArg)) // both are constants
			{
				
				if (AsIthArg.equals(BsIthArg))// both constants are equal, then return true
				{
					// update the substitution map ?
//					System.out.println("Im positively here");
					continue;
				} else {
//					System.out.println("Im here"+" 1st pred "+a+"\n2nd pred "+b);
					return false; // else return false
				}
			}
		
			if (isAVariable(AsIthArg) && isAConstant(BsIthArg)) // a has a variable and b has a constant
			{
				
				if (substitutionMap.containsKey(AsIthArg)) { //a has a value in map
//					System.out.println("Hey there");
						if (isAConstant(substitutionMap.get(AsIthArg)))//a's value is a constant// use a as the key in SubststitutionMap to get// value and equate to b.arguments
						{
							if(substitutionMap.get(AsIthArg).equals(BsIthArg))//a's value = bs argument
	//						System.out.println("Hello equals!");
								continue;
							else //a's value ! =  b's argumeny
								return false;
						}
						else {
//							if(substitutionMap.get(substitutionMap.containsKey(AsIthArg)))
//							substitutionMap.get(AsIthArg) is a variable get its key
							String first = substitutionMap.get(AsIthArg);
							/*if(substitutionMap.containsKey(first))
							{
								if(substitutionMap.get(first).equals(BsIthArg)) //recursive call if ur handling
									{
									substitutionMap.put(AsIthArg, BsIthArg);
									continue; 
									}
								else
									return false;
							}else {
								substitutionMap.put(AsIthArg, BsIthArg); // the current var and the var its equated to
								continue;
//								substitutionMap.put(first, BsIthArg);
							}*/
							
							substitutionMap.put(AsIthArg, BsIthArg); // the current var and the var its equated to
							substitutionMap.put(first, BsIthArg); 
							continue;
								
						}
						
					}
				else {// no mpping of variable in submap
//						System.out.println("var has "+predicateIntMap.get(b));
					substitutionMap.put(AsIthArg, BsIthArg);
					continue;// return false as other substitution exists
					}
				
				// update the substitution map if no key is found in hashmap

			}
	
			if (isAVariable(BsIthArg) && isAConstant(AsIthArg)) // a has a variable and b has a constant
			{
				
				if (substitutionMap.containsKey(BsIthArg)) { //a has a value in map

						if (isAConstant(substitutionMap.get(BsIthArg)))//a's value is a constant// use a as the key in SubststitutionMap to get// value and equate to b.arguments
						{
							if(substitutionMap.get(BsIthArg).equals(AsIthArg))//a's value = bs argument
	//						System.out.println("Hello equals!");
								continue;
							else //a's value ! =  b's argumeny
								return false;
						}
						else {
//							if(substitutionMap.get(substitutionMap.containsKey(AsIthArg)))
//							substitutionMap.get(AsIthArg) is a variable get its key
							
							String first = substitutionMap.get(BsIthArg);
							
							/*if(substitutionMap.containsKey(first))
							{System.out.println("Hey there");
								if(substitutionMap.get(first).equals(AsIthArg)) //recursive call if ur handling
									{
									substitutionMap.put(BsIthArg, AsIthArg);
									continue; 
									}
								else
									return false;
							}else {*/
							
								substitutionMap.put(BsIthArg, AsIthArg); // the current var and the var its equated to
								substitutionMap.put(first, AsIthArg); 
								continue;
//								substitutionMap.put(first, BsIthArg);
							}
								
						
					}
				else {// no mpping of variable in submap
//						System.out.println("var has "+predicateIntMap.get(b));
					substitutionMap.put(BsIthArg, AsIthArg);
					continue;// return false as other substitution exists
					}
				
				// update the substitution map if no key is found in hashmap

			}
			
			if (isAVariable(AsIthArg) && isAVariable(BsIthArg)) // a and b both have variables
			{
//				System.out.println("Hi here!");
				if (substitutionMap.containsKey(AsIthArg) && substitutionMap.containsKey(BsIthArg)) // both variables have a substitution
				{
					/*if (substitutionMap.get(AsIthArg).equals(substitutionMap.get(BsIthArg)))// both variables have same // substitution return true
						continue;
					else
					{//A 
*/						if(isAConstant(substitutionMap.get(AsIthArg))&&isAConstant(substitutionMap.get(BsIthArg)))
						{
							if(substitutionMap.get(AsIthArg).equals(substitutionMap.get(BsIthArg)))
								continue;
							else
								return false;
						}
						if(isAVariable(substitutionMap.get(AsIthArg))&&isAConstant(substitutionMap.get(BsIthArg)))
						{
								substitutionMap.put(AsIthArg, substitutionMap.get(BsIthArg));
								continue;
						}
						if(isAVariable(substitutionMap.get(BsIthArg))&&isAConstant(substitutionMap.get(AsIthArg)))
						{
							substitutionMap.put(BsIthArg, substitutionMap.get(AsIthArg));
							continue;
						}
						else {
							substitutionMap.put(BsIthArg, substitutionMap.get(AsIthArg)); //put var v1->v3 v2->v4 to v1->v3 && v2->v3  into bs arg
							continue;
						}
//						return false;////; // else return false
					//}
				}
	
				if (substitutionMap.containsKey(AsIthArg) && !substitutionMap.containsKey(BsIthArg)) // a exists but b
																										// does not
				{

//					System.out.println("A !B");
					substitutionMap.put(BsIthArg, substitutionMap.get(AsIthArg)); // insert in b, the
					continue;																					// value of a
				}
	
				if (substitutionMap.containsKey(BsIthArg) && !substitutionMap.containsKey(AsIthArg)) // b exists but a
																										// does not
				{
					substitutionMap.put(AsIthArg, substitutionMap.get(BsIthArg)); // insert in a, the
					continue;																					// value of b
				}
				
				if (!substitutionMap.containsKey(AsIthArg) && !substitutionMap.containsKey(BsIthArg))
				{
					substitutionMap.put(AsIthArg, AsIthArg); 
					substitutionMap.put(BsIthArg, AsIthArg); // insert in b, the variable of a
					continue;																					
				}
			}
	
		}
					
		return true;			
	}
	public void applySubstitution(sentence currSent, HashMap<String, String> substitution) {
	//		sentence currSent = sentence.copySentence(currSent1);
	
		for(int i=0;i<currSent.predicatelist.size(); i++)
		{
	//			System.out.println("hey i"+i+"\t"+currSent.predicatelist.get(i).arguments.size());
			for(int j=0; j<currSent.predicatelist.get(i).arguments.size();)
			{
//					System.out.println("Hi!");
				if(isAConstant(currSent.predicatelist.get(i).arguments.get(j))) 
				{//can this predicate have a constant and there is)
//					System.out.println("hey j"+j);
						j++;
						continue;
				}
				if(substitution.containsKey(currSent.predicatelist.get(i).arguments.get(j)))
				{
					String constant = new String(substitution.get(currSent.predicatelist.get(i).arguments.get(j)));
//					System.out.println(currSent.predicatelist.get(i).arguments.get(j));
//					System.out.println(constant);
					if(isAVariable(constant)&&!currSent.predicatelist.get(i).arguments.get(j).equals(constant))
						currSent.predicatelist.get(i).arguments.set(j, constant);
	//					System.out.println("Argument "+ constant);
					else {
					currSent.predicatelist.get(i).arguments.set(j, constant);
					j++;}
				}
				else 
					j++;
					
			}
		}
	//		System.out.println("My substituted sentence is\n"+currSent.toString());
	}
	
	public static HashMap<String,String> copyHashMap(HashMap<String, String> h)
	{
		HashMap<String, String> copy = new HashMap();
		
		for (String key: h.keySet()) {
			copy.put(key, h.get(key));
		}
		
		return copy;
	}
	
	public static Hashtable<String, ArrayList<Integer>> copyHashTable(Hashtable<String, ArrayList<Integer>> original)
	{
	Hashtable <String, ArrayList<Integer>> copy =new Hashtable();
	 Set<String> keys = original.keySet();
     for(String key: keys){
    	 copy.put(key, original.get(key));
     }
	return copy;
		
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("Queries are \n");
		for (int i = 0; i < queryList.size(); i++)
			sb.append(queryList.get(i) + "\n");
	
		return sb.toString() + "\nSentences in KB \n" + knowledgeBase.toString();
	}
	/**
	 * 		// function UNIFY(x , y, theta) returns a substitution to make x and y identical
		// inputs: x , a variable, constant, list, or compound expression
		// y, a variable, constant, list, or compound expression
		// theta, the substitution built up so far (optional, defaults to empty)
		// if theta = failure then return failure
		// else if x = y then return theta return the substitution forms base case for
		// termination
		// else if VARIABLE?(x ) then return UNIFY-VAR(x , y, theta) if x is a var call
		// unify variable
		// else if VARIABLE?(y) then return UNIFY-VAR(y, x , theta) if y is a variable
		// call unify variable with y as the variable. Remember Unify-var is called as
		// Unify-Var(var, cal, theta)
		// else if COMPOUND?(x ) and COMPOUND?(y) then if x is a compound sentence like
		// F(x,y) and y is a compound sentence like G(x, y)
		// return UNIFY(x.ARGS, y.ARGS, UNIFY(x.OP, y.OP, theta)) then pass the list of
		// arguments of x and y, unify the names of functions ie check if they are the
		// same name functions
		// else if LIST?(x ) and LIST?(y) then pass then take the first from both lists
		// and unify, and pass the rest of both lists as arguments
		// return UNIFY(x .REST, y.REST, UNIFY(x .FIRST, y.FIRST, theta))
		// else return failure if none of these cases are possible then return failure
	
		// function UNIFY-VAR(var, x , theta) returns a substitution
		// if {var/val} theta then return UNIFY(val , x , theta) if the var can be
		// unified with value val and this is present in theta return Unify val with x
		// else if {x/val} in theta then return UNIFY(var, val , theta) if x can be
		// unified with value val and this is present in theta return Unify var with val
		// else if OCCUR-CHECK?(var, x ) then return failure ?
		// else return add {var/x } to theta otherwise add the substitution var/x to
		// theta
	 * public HashMap<String, String> Unifyvar(String var, String x, HashMap theta)
	 * { if(!theta.get(var).equals("")) //I use "" string to denote that initially
	 * all substitutions are empty return Unify(theta.get(var), x, theta); else
	 * if(!theta.get(x).equals("")) return Unify(var, theta.get(x), theta); else {
	 * theta.put(var, x); } return theta; }
	 */
	//	public/ 
	/*	public HashMap Unify(sentence a, sentence b) {
			HashMap<String, String> substitution = new HashMap();
			// Check the first sentence
			//
			return substitution;
		}
	
		public HashMap UnifyVar() {
			HashMap<String, String> substitution = new HashMap();
	
			return substitution;
		}*/
	//merge old code
	/*		for (int i = 0; i < prevResolvedSent.predicatelist.size(); i++) {
		if (prevResolvedSent.predicatelist.get(i).name.equals(elim.name))
			continue;
		else {
			Resolved.predicatelist.add(prevResolvedSent.predicatelist.get(i));
		}
	}
	predicate negatedElim = negateQuery(elim);
	for (int j = 0; j < newSent.predicatelist.size(); j++) {
		if (newSent.predicatelist.get(j).name.equals(negatedElim.name))
			continue;
		else {
			Resolved.predicatelist.add(newSent.predicatelist.get(j));
		}
	}*/
	}
