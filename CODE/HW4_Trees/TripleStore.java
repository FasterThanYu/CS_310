import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
// Three-column database that supports query, add, and remove in
// logarithmic time.
public class TripleStore{
	
	private String wild = "*";
	private TreeSet<Record> left,middle, right;
//	private TreeSet<Record> records;
	// Create an empty TripleStore. Initializes storage trees
	public TripleStore(){
		this.left = new TreeSet<Record>(Record.ERPCompare);
		this.middle = new TreeSet<Record>(Record.RPECompare);
		this.right = new TreeSet<Record>(Record.PERCompare);
	}
	
	// Access the current wild card string for this TripleStore which
	// may be used to match multiple records during a query() or
	// remove() calll
	public String getWild(){
		return wild;
	}
	
	// Set the current wild card string for this TripleStore
	public void setWild(String w){
		this.wild = w;
	}
	
	// Ensure that a record is present in the TripleStore by adding it
	// if necessary.  Returns true if the addition is made, false if the
	// Record was not added because it was a duplicate of an existing
	// entry. A Record with any fields may be added to the TripleStore
	// including a Record with fields that are equal to the
	// TripleStore's current wild card.
	// 
	// Target Complexity: O(log N)
	// N: number of records in the TripleStore
	public boolean add(String entity, String relation, String property){
		Record r = Record.makeRecord(entity, relation, property);
		boolean ret = left.add(r);
		if (!ret)
			return false;
		middle.add(r);
		right.add(r);
/*		if ((left.contains(entity))&&(middle.contains(relation))
				&&(right.contains(property)))
			return false;*/
		return ret;
	}
	
	// Return a List of the Records that match the given query. If no
	// Records match, the returned list should be empty. If a String
	// matching the TripleStore's current wild card is used for one of
	// the fields of the query, multiple Records may be returned in the
	// match.  An appropriate tree must be selected and searched
	// correctly in order to meet the target complexity.
	// 
	// TARGET COMPLEXITY: O(K + log N) 
	// K: the number of matching records 
	// N: the number of records in the triplestore.
	public List<Record> query(String entity, String relation, String property){
		//makes temp query
		Record r = Record.makeQuery(wild, entity, relation, property);
		ArrayList<Record> ret = new ArrayList<Record>();
		Iterator<Record> i = left.iterator();
		//empty tree
		if (!i.hasNext())
			return ret;
		Record t = (Record) i.next();
		
		//iterates and finds matches
		while(i.hasNext()){
			if (t.matches(r))
				ret.add(t);
			t = i.next();
		}
		if (t.matches(r))
			ret.add(t);
		return ret;
	}
	
	// Remove elements from the TripleStore that match the parameter
	// query. If no Records match, no Records are removed.  Any of the
	// fields given may be the TripleStore's current wild card which may
	// lead to multiple Records bein matched and removed. Return the
	// number of records that are removed from the TripleStore.
	// 
	// TARGET COMPLEXITY: O(K * log N)
	// K: the number of matching records 
	// N: the number of records in the triplestore.
	public int remove(String e, String r, String p){
		Record rr = Record.makeQuery(wild,e, r, p);
		int ctr = 0;
		//uses iterator to find and remove item
		Iterator<Record> i = left.iterator();
		while(i.hasNext()){
			Record t = (Record) i.next();
			if (t.matches(rr)){
				ctr++;
				i.remove();
			}
		}
		return ctr;
	}
	
	// Produce a String representation of the TripleStore. Each Record
	// is formatted with its toString() method on its own line. Records
	// must be shown sorted by Entity, Relation, Property in the
	// returned String. 
	// 
	// TARGET COMPLEXITY: O(N)
	// N: the number of records stored in the TripleStore
	public String toString(){
		String str = "";
		//uses iterator to append to string
		Iterator<Record> i = left.iterator();
		while(i.hasNext()){
			str += i.next().toString()+"\n";
		}
		return str;
	}

}