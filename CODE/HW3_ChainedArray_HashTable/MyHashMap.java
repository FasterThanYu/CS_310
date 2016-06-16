import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyHashMap<KeyType extends Comparable<KeyType>, ValueType> {
	
	// Table to hold key/val pairs 
	private MyHashTable<Entry<KeyType,ValueType>> items;
	
	// Constructor
	public MyHashMap(){
		items = new MyHashTable<Entry<KeyType, ValueType>>();
	}
	
	// Associates the specified value with the specified key in this map.
	// Throws IllegalArgumentException if key is null or val is null;
	public void put( KeyType key, ValueType val ){
		if ((key==null)||(val==null))
			throw new IllegalArgumentException();
		//calls MyHashTable insert
		Entry<KeyType, ValueType> ent = new Entry<KeyType, ValueType>(key, val);
		items.insert(ent);
	}
	
	// Returns the value to which the specified key is mapped, or null if 
	// this map contains no mapping for the key.
	// Throws IllegalArgumentException if key is null.
	public ValueType get(KeyType key ){
		if (key==null)
			throw new IllegalArgumentException();
		//calls contains for hashTable
		Entry<KeyType, ValueType> temp = makeEntry(key);
		if (!items.contains(temp))
			return null;
		return items.getMatch(temp).getValue();
	}
	
	// Returns true if there are no elements.
	// Target Complexity: O(1)
	public boolean isEmpty(){
		return items.isEmpty();
	}
	
	// Returns the number of elements
	// Target Complexity: O(1)
	public int size(){
		return items.size();
	}
	
	// Make the hash map logically empty.
	// Target Complexity: O(1)
	public void clear(){
		items.clear();
	}
	
	// Returns a Set of the mappings contained in this map.
	public Set<Entry<KeyType, ValueType>> entrySet(){
		//creates set
		Set<Entry<KeyType, ValueType>> ret = 
				new HashSet<Entry<KeyType, ValueType>>();
		//makes iterator
		Iterator<Entry<KeyType, ValueType>> it = items.iterator();
		//adds to set via iterator
		while (it.hasNext()){
			Entry<KeyType, ValueType> ent = it.next();
			ret.add(ent);
		}
		return ret;
	}
		
	// A helper method that returns an Entry created from the specified key and value
	protected MyHashMap.Entry<KeyType, ValueType> makeEntry( KeyType key, ValueType value ) {
		return new MyHashMap.Entry<KeyType, ValueType>( key, value );
	}
	
	// A convenience helper method for creating an Entry from a key value
	private MyHashMap.Entry<KeyType, ValueType> makeEntry( KeyType key ) {
		return makeEntry( (KeyType) key, null );
	}
	
	// Returns the hash table of items
	protected MyHashTable<Entry<KeyType,ValueType>> getItems(){
		return items;
	}
	
	//entry class, holds key and val that represent a pair
	public static class Entry<KeyType extends Comparable<KeyType>, ValueType> 
	implements Comparable<Entry<KeyType, ValueType>> {
		private KeyType key;
		private ValueType val;

		// Constructor
		// Target Complexity: O(1)		
		public Entry( KeyType k, ValueType v ){
			key = k;
			val = v;
		}

		// Returns the hash code value for this map entry. The hashCode of the Entry is the hashCode of
		// the Entry's key.
		public int hashCode(){
			return key.hashCode();
		}

		// Compares the specified object with this entry for equality. The equality of two Entries is 
		// based on the equality of their keys.
		public boolean equals( Object rhs ){
			@SuppressWarnings("unchecked")
			Entry<KeyType, ValueType> oth = (Entry<KeyType, ValueType>)rhs;
			return this.key.equals(oth.key);
		}

		// Returns the key corresponding to this entry.		
		public KeyType getKey(){
			return key;
		}

		// Returns the value corresponding to this entry.
		public ValueType getValue(){
			return val;
		}

		// toString() - create a pretty representation of the Entry.
		// Example: When key = "Carver" and value = "CS310-003": (Carver, CS310-003)
		public String toString(){
			return "("+key.toString()+", "+val.toString()+")";
		}

		// Compares this object with the specified object for order. Returns a negative integer, 
		// zero, or a positive integer if this object is less than, equal to, or greater than the 
		// other object.
		public int compareTo(Entry<KeyType, ValueType> other){
			return this.key.compareTo(other.key);
		}
	}
}