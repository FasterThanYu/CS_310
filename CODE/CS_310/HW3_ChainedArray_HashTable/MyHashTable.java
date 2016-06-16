import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class MyHashTable<T extends Comparable<T>> implements Iterable<T> {
	
	public static class MyListNode<U extends Comparable<U>> implements Comparable<MyListNode<U>> {
		
		public U data;
		public MyListNode<U> prev, next;
		// Constructors
		// Target Complexity: O(1)
		public MyListNode(U data, MyListNode<U> prev, MyListNode<U> next){
			this.data= data;
			this.prev = prev;
			this.next = next;
		}
		public MyListNode(MyListNode<U> prev, MyListNode<U> next){
			this.prev = prev;
			this.next = next;
		}
		
		//Returns the hashCode() value of data.
		public int hashCode(){
			return data.hashCode();
		}
		
		// Return true if the two objects are equal.
		// Two MyListNode objects are equal if their data values are equal 
		public boolean equals(Object other){
			if (!(other instanceof MyListNode))
				 return false;
			@SuppressWarnings("unchecked")
			MyListNode<U> oth = (MyListNode<U>) other;
			return this.data.equals(oth.data);
		}
		
		// Return true if two objects are equal; works if objects can be
		// null.  Used internally for implementation of equals(other).
		protected boolean isEqual( Object lhs, Object rhs ){
			boolean x = lhs==null;
			boolean y = rhs == null;
			//XOR OPERATOR, only one is null
			if ((x || y) && !(x && y))
				return false;
			if (lhs.equals(rhs))
				return true;
			return false;
		}
		
		// Compares this object with the specified other object for order. 
		public int compareTo(MyListNode<U> other){
			return this.data.compareTo(other.data);
		}
		
		// toString() - create a pretty representation of the MyListNode.
		// Example: for an integer: 3
		public String toString(){
			return data.toString();
		}
	}
	
	final int DEFAULT_TABLE_SIZE = 101;
	Object[] table;
	int size, numAdded=0;
	int tableSize, modCount;
	MyListNode<T> beginMarker, endMarker;
	// Constructor. DEFAULT_TABLE_SIZE is 101
	public MyHashTable( ){
		tableSize = DEFAULT_TABLE_SIZE;
		table = new Object[DEFAULT_TABLE_SIZE];
		beginMarker = new MyListNode<T>(null, null);
		endMarker = new MyListNode<T>(beginMarker, null);
		beginMarker.next = endMarker;
	}

	// Construct an instance; the internal table size is is initially the
	// parameter size if size is prime or the next prime number which is
	// greater than size if it is not prime
	public MyHashTable( int size ){
		table= new Object[size];
		tableSize = size;
		beginMarker = new MyListNode<T>(null, null);
		endMarker = new MyListNode<T>(beginMarker, null);
		beginMarker.next = endMarker;
	}

	// Make the hash table logically empty.
	public void clear( ){
		table = new Object[tableSize];
		numAdded=0;
		beginMarker.next = endMarker;
		endMarker.prev = beginMarker;
		modCount++;
	}

	// Helper method for the linked list. Insert a new node after the given node.
	// Returns a reference to the new node
	protected MyListNode<T> insertListNodeAfter(MyListNode<T> pos, T data){
		MyListNode<T> newNode = new MyListNode<T>(data, pos, pos.next);
		//just does pointer stuff
		newNode.prev.next = newNode;
		newNode.next.prev = newNode;
		return newNode;
	}

	// Helper method for the linked list. Insert a new node before the given node 
	// Returns a reference to the new node
	protected MyListNode<T> insertListNodeBefore(MyListNode<T> pos, T data){
		MyListNode<T> newNode = new MyListNode<T>(data, pos.prev, pos);
		//just does pointer stuff
		newNode.prev.next = newNode;
		newNode.next.prev = newNode;
		return newNode;
	}

	// Helper method for the linked list. Remove the selected node.
	// Returns a reference to the removed node
	protected MyListNode<T> removeListNode(MyListNode<T> pos){
		//just does pointer stuff
		pos.next.prev = pos.prev;
		pos.prev.next = pos.next;
		return pos;
	}

	// Return a String representation of the linked list, 
	// Example: 2, 1, 3, 5, 4 
	protected String listNodesToString(){
		String str= "";
		//iterates through, adds to string
		MyListNode<T> temp = beginMarker.next;
		while (temp != endMarker){
			str += temp.toString();
			if(!(temp.next == endMarker)){
				str+=", ";
			}
			temp = temp.next;
		}
		return str;
	}

	// Insert x into the hash table. If x is already present, then do nothing.
	// Throws IllegalArgumentException if x is null.
	public void insert(T x){
		if (x==null)
			throw new IllegalArgumentException();
		
		//create new node and temp for inserting to linked list
		MyListNode<T> node = new MyListNode<T>(x,endMarker.prev, endMarker);
		//pointers insert node
		node.prev.next = node;
		node.next.prev = node;

		int hashVal = myhash(node);
		
		@SuppressWarnings("unchecked")
		//gets chainedArrays from table
		ChainedArrays<MyListNode<T>> c = (ChainedArrays<MyListNode<T>>) table[hashVal];
		//c has not been initialized yet
		if (c== null)
			c = new ChainedArrays<MyListNode<T>>();
		//adds node to c
		table[hashVal] = c;
		c.add(node);
		numAdded++;
		modCount++;
		
		//rehash causing errors, todo: redo
		/*if (numAdded/tableSize>=0.7)
			rehash();*/
	}

	// Remove x from the hash table.
	// Throws IllegalArgumentException if x is null.
	public void remove( T x ){
		if (x==null){
			throw new IllegalArgumentException();
		}
		//removes from linked list
		MyListNode<T> temp = beginMarker.next;
		MyListNode<T> node = new MyListNode<T> (x, null, null);
		while(temp != endMarker){
			if (temp.compareTo(node)==0){
				node = removeListNode(temp);
				break;
			}
			else
				temp = temp.next;
		}
		//hashes
		int hashVal = myhash(node);
		//so long as hash address isn't null, remove() is called
		if (table[hashVal]!= null){
			@SuppressWarnings("unchecked")
			ChainedArrays<MyListNode<T>> c = 
				(ChainedArrays<MyListNode<T>>) table[hashVal];
			c.remove(node);
		}
		modCount++;
		numAdded--;
	}

	// Return true if x is in the hash table
	// Throws IllegalArgumentException if x is null.
	public boolean contains( T x ){
		if (x==null)
			throw new IllegalArgumentException();
		//made for hashing
		MyListNode<T> node = new MyListNode<T> (x, null, null);
		int hashVal = myhash(node);
		@SuppressWarnings("unchecked")
		ChainedArrays<MyListNode<T>> c = (ChainedArrays<MyListNode<T>>) table[hashVal];
		//same as above, just different return
		if (c==null)
			return false;
		if (c.contains(node))
			return true;
		return false;
	}

	// Return an element in the list that equals x, or null if there is no such element.
	// Throws IllegalArgumentException if x is null.
	@SuppressWarnings("unchecked")
	public T getMatch(T x){
		if (x==null){
			throw new IllegalArgumentException();
		}
		//looks through hashtable to find it
		MyListNode<T> node = new MyListNode<T> (x, null, null);
		int hashVal = myhash(node);
		
		ChainedArrays<MyListNode<T>> c = (ChainedArrays<MyListNode<T>>) table[hashVal];
		if (c.contains(node))
			return c.getMatch(node).data;
		return null;
	}

	// Create a pretty representation of the hash table. Does not include the linked list.
	// See the representation of the Table in toString()
	public String tableToString(){
		String str = "Table:\n";
		for(int i=0; i<tableSize; i++){
			str+= i+":";
			if (table[i]!= null){
				str+= " ";
				str+= table[i].toString();
				//str+= "|";
			}
			str+= "\n";
		}
		return str;
	}

	// Create a pretty representation of the hash table and linked list.
	// Uses toString() of ChainedArrays.
	// May call tableToString() and listNodesToString
	// Example: table size is 3, insertion order: one, three, two
	// Table:
	// 0: | two |
	// 1: | one, three |
	// 2: 
	// Linked List: 
	// one, three, two
	public String toString(){
		String str = "";
		str+= tableToString();
		str+="\nLinked List:\n";
		str+= listNodesToString();
		return str;
	}

	// Increases the size of the table by finding a prime number (nextPrime) at least as large as 
	// twice the table size. Rehashes the elements in the linked list of the hash table.
	private void rehash( ){
		//iterator goes through linked list and 
		tableSize = nextPrime(tableSize*2);
		Object[] newTable = new Object[tableSize];
		Iterator<T> it = iterator();
		while(it.hasNext()){
			T x = it.next();
			MyListNode<T> node = new MyListNode<T>(x, null, null);
			int hashVal = myhash(node);
			newTable[hashVal] = node;
		}
		table = newTable;
		/*for(int i=0; i< oldSize; i++){
			if (table[i] != null){
				int hashVal = myhash((MyListNode<T>) table[i]);
				while(newTable[hashVal]!= null){
					hashVal++; 
				}
				newTable[hashVal] = table[i];
			}
		}*/
	}

	// internal method for computing the hash value from the hashCode of x.
	private int myhash( MyListNode<T> x ) {
	int hashVal = x.hashCode( );
	hashVal %= tableSize;
	if( hashVal < 0 )
	  hashVal += tableSize;
	return hashVal;
	}

	// Internal method to find a prime number at least as large as n. 
	private static int nextPrime( int n ){
	if( n % 2 == 0 )
	  n++;
	for( ; !isPrime( n ); n += 2 )
	  ;
	return n;
	}

	// Internal method to test if a number is prime. Not an efficient algorithm. 
	private static boolean isPrime( int n ) {
	if( n == 2 || n == 3 )
	  return true;
	if( n == 1 || n % 2 == 0 )
	  return false;
	for( int i = 3; i * i <= n; i += 2 )
	  if( n % i == 0 )
		return false;
	return true;
	}

	// Returns true if there are no elements.
	// Target Complexity: O(1)
	public boolean isEmpty( ){
		if (beginMarker.next==endMarker)
			return true;
		return false;
	}

	// Returns the number of elements
	//Target Complexity: O(1)
	public int size(){
		return numAdded;
	}

	// Returns an iterator over the elements in the proper sequence.
	public Iterator<T> iterator(){
		return new MyHashTableIterator();
	}

	// Returns the hash table array of Objects
	protected Object[] getTable(){
		return table;
	}

	// Internal class for iteration; see documentation elsewhere
	//iterates through linked list
	private class MyHashTableIterator implements Iterator<T> {
		// Constructor
		// Target Complexity: O(1)
		MyListNode<T> current;
		int expectedModCount = modCount;
		public MyHashTableIterator (){
			current = beginMarker.next;
		}

		// Returns true if the iterator can be moved to the next() element.
		// Target Complexity: O(1)
		public boolean hasNext(){
			if (expectedModCount != modCount)
				throw new ConcurrentModificationException();
			if (current != endMarker)
				return true;
			return false;
		}

		// Move the iterator forward and return the passed-over element
		// Target Complexity: O(1)
		public T next(){
			if (expectedModCount != modCount)
				throw new ConcurrentModificationException();
			T passed = current.data;
			current = current.next;
			return passed;
		}

		// The following operation is part of the Iterator interface
		// but are not supported by the iterator.
		// Throws UnsupportedOperationException exceptions if invoked.
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
}