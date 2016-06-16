import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainedArrays<T extends Comparable<T>> implements Iterable<T> {

	protected static class ArrayNode<U extends Comparable<U>> {
		public ArrayNode<U> next, prev;
		private int arraySize,numElems;
		public final int DEFAULTCAPACITYOFARRAYS = 16;
		private Object[] array;
		
	    // Workhorse constructor. Initialize prev and next and the size of the array, and 
	    // create an array of Objects with the specified capacity.
	    // Throws IllegalArgumentException if capacityOfArray < 0.
	    public ArrayNode(ArrayNode<U> prev, ArrayNode<U> next, int capacityOfArray){
	    	if (capacityOfArray<0){
	    		throw new IllegalArgumentException("capacity cannot be negative");
	    	}
	    	this.prev = prev;
	    	this.next = next;
	    	numElems=0;
	    	this.arraySize = capacityOfArray;
	    	array = new Object[capacityOfArray];
	    }

	    // Convenience constructor.
	    public ArrayNode(ArrayNode<U> prev, ArrayNode<U> next){
	    	this.prev = prev;
	    	this.next = next;
	    	numElems=0;
	    	this.arraySize = DEFAULTCAPACITYOFARRAYS;
	    	array = new Object[DEFAULTCAPACITYOFARRAYS];
	    }

	    // Insert in ascending sorted order using binarySearch(). This may require elements 
	    // to be shifted.
	    // Throws IllegalArgumentException if x is null.
	    // Target Complexity: O(n)
	    public void insertSorted(U x){
	    	if (x==null)
	    		throw new IllegalArgumentException("X cannot be null");
	    	
	    	//performs binary search
	    	int i = Arrays.binarySearch(array,0, getArraySize(), x);
	    	if (i < 0){
		    	//binarySearch() returns -(insertion point)-1
		    	i *= -1;
		    	i--;
	    	}
	    	//store last element
	    	int j = numElems;
	    	//enter loop(condition: > i)
	    	while(j>i){
	    		//move element right
	    		array[j]= array[j-1];//temp;
	    		j--;
	    	}
	    	//Set element at i
	    	array[i] = x;
	    	numElems++;
	    }

	    // Locate element x using binarySearch() and remove x. This may require elements 
	    // to be shifted.
	    // Returns a reference to the removed element, or null if the element was not removed.
	    // Throws IllegalArgumentException if x is null.
	    // Target Complexity: O(n)
	    public U remove(U x){
	    	if (x==null)
	    		throw new IllegalArgumentException("x cannot be null");
	    	//find index
	    	int i = Arrays.binarySearch(array,0, getArraySize(), x);
	    	//x not found in array
	    	if (i<0)
	    		return null;
	    	//store current element
	    	@SuppressWarnings("unchecked")
			U data = (U) array[i];
	    	//enter loop
	    	while(i<numElems-1){
	    		//move element left
	    		array[i]= array[i+1];
	    		i++;
	    	}

	    	//Not sure if needed: set last element to null after shift
	    	array[numElems-1] = null;
	    	numElems--;
	    	return data;
	    	
	    }

	    // Remove the element at index idx. This may require elements to be shifted.
	    // Returns a reference to the removed element.
	    // Throws IndexOutOfBoundsException if idx is out of bounds (less than 0 
	    // or greater than array size -1)
	    // Target Complexity: O(n)
	    public U remove(int idx){
	    	if ((idx < 0)|| (idx > numElems-1))
	    		throw new IndexOutOfBoundsException();
	    	int i = idx;
	    	@SuppressWarnings("unchecked")
			U data = (U) array[i];
	    	//Same shift algorithm as other remove method
	    	while(i<numElems-1){
	    		array[i]= array[i+1];
	    		i++;
	    	}
	    	//Not sure if needed: set last element to null after shift
	    	array[numElems-1] = null;
	    	numElems--;
	    	return data;
	    }

	    // Uses binarySearch() to return the index of x, if x is contained in the array; 
	    // otherwise, return (-(insertion point) - 1). The insertion point is defined as the point 
	    // at which x would be inserted into the array: the index of the first element greater 
	    // than x, or array.length if all elements in the array are less than x. Note that this 
	    // guarantees that the return value will be >= 0 if and only if x is found.
	    // Throws IllegalArgumentException if x is null.
	    // Target Complexity: O(lg n)
	    public int indexOf(U x){
	    	if (x==null)
	    		throw new IllegalArgumentException("x cannot be null");
	    	return Arrays.binarySearch(array,x);
	    }

	    //Returns the element at the specified index. Throws IndexOutOfBoundsException if idx 
	    // is out of bounds (less than 0 or greater than array size -1)
	    //Target Complexity: O(1)
		@SuppressWarnings("unchecked")
		public U get(int idx){
	    	if ((idx < 0)|| (idx > numElems-1))
	    		throw new IndexOutOfBoundsException();
	    	//Uses cast to take U object out of generic-less array
	    	return (U) array[idx];
	    }

	    //Returns the first element. Throws IndexOutOfBoundsException if the number 
	    //of elements is 0. May call get(int idx).
	    // Target Complexity: O(1)
	    public U getFirst(){
	    	if (getArraySize()==0)
	    		throw new IndexOutOfBoundsException();
	    	//calls get
	    	return get(0);
	    }

	    // Returns the last element. Throws IndexOutOfBoundsException if the number 
	    // of elements is 0. May call get(int idx).
	    // Target Complexity: O(1)
	    public U getLast(){
	    	if (getArraySize()==0)
	    		throw new IndexOutOfBoundsException();
	    	//calls get
	    	return get(numElems-1);
	    }

	    // Remove the element at index 0. This may require elements to be shifted.
	    // Returns a reference to the removed element.
	    // Throws IndexOutOfBoundsException if the number of elements is 0.
	    // Target Complexity: O(n)
	    public U removeFirst(){
	    	if (getArraySize()==0)
	    		throw new IndexOutOfBoundsException();
	    	//calls remove on first
	    	return remove(0);
	    }

	    // Remove the last element.
	    // Returns a reference to the removed element.
	    // Throws IndexOutOfBoundsException if the number of elements is 0.
	    // Target Complexity: O(1)
	    public U removeLast(){
	    	if (getArraySize()==0)
	    		throw new IndexOutOfBoundsException();
	    	//calls remove on last
	    	return remove(numElems-1);
	    }

	    // toString() - create a pretty representation of the ArrayNode by showing all of the elements in the array.
	    // Target Complexity: O(n)
	    // Example: four elements in an array of capacity five:1, 2, 4, 5
	    public String toString(){
	    	String str = "";
	    	int i=0;
	    	for(; i < numElems-1; i++){
	    		str += array[i].toString();
	    		str += ", ";
	    	}
	    	str += array[numElems-1];
	    	return str;
	    }

	    // Return array of Objects
	    protected Object[] getArray(){
	    	return array;
	    }

	    // Set array of Objects
	    protected void setArray(Object[] array){
	    	this.array = array;
	    }

	    // Return size of array (not length)
	    protected int getArraySize(){
	    	return numElems;
	    	//return arraySize;
	    }

	    // Set size of array
	    protected void setArraySize(int arraySize){
	    	this.arraySize = arraySize;
	    }
	    //Helper method made by me to allow for adhering to target 
	    //complexities.
	    public int getLength(){
	    	return arraySize;
	    }

	  }

	
	

	int size, numAdded=0;
	int capacityOfArrays, modCount;
	public ArrayNode<T> beginMarker, endMarker;
	// Workhorse constructor.
	ChainedArrays(int capacityOfArrays){
		this.capacityOfArrays = capacityOfArrays;
		size = 0;
		beginMarker = new ArrayNode<T>(null, null, 0);
		endMarker = new ArrayNode<T> (beginMarker, null, 0);
		beginMarker.next = endMarker;
		modCount = 0;
	}

	// Convenience constructor  
	ChainedArrays(){
		size = 0;
		beginMarker = new ArrayNode<T>(null, null, 0);
		endMarker = new ArrayNode<T> (beginMarker, null, 0);
		beginMarker.next = endMarker;
		this.capacityOfArrays = 16;
		modCount = 0;
	}


	// Make the ChainedArrays logically empty.
	// Target Complexity: O(1)
	// Implementation note: It is not necessary to remove() all the elements; instead, some data
	// members can be reinitialized.
	public void clear(){
		beginMarker.next = endMarker;
		endMarker.prev = beginMarker;
		numAdded=0;
		size=0;
    	modCount++;
	}
	
	// Returns the number of elements in the ChainedArrays
	// Target Complexity: O(1)
	public int size(){
		return numAdded;
	}
	
	// returns the number of ArrayNodes
	// Target Complexity: O(1)
	public int nodeCount(){
		return size;
	}
	
	// Returns true if there are no elements in the list
	// Target Complexity: O(1)
	public boolean isEmpty( ){
		return beginMarker.next==endMarker;
	}
	
	// Return the first element in the list that equals data, or null 
	// if there is no such element.
	// Throws IllegalArgumentException if x is null.
	// Target Complexity: O(n)
	public T getMatch(T data){
		if (data == null)
			throw new IllegalArgumentException("data cannot be null"); 
		if (isEmpty())
			return null;
		ArrayNode<T> temp = beginMarker.next;
		
		//same as contains (below), except returns pointer, not boolean
		while (temp != endMarker){
			if (temp.getLast().compareTo(data) < 0){
				temp = temp.next;
			}
			else{
				for (int i=0; i< temp.getArraySize();i++){
					if (temp.get(i).equals(data))
						return temp.get(i);
				}
			}
		}
		return null;
	}
		
	
	
	// Returns true if this ChainedArray contains the specified element.
	// Throws IllegalArgumentException if data is null.
	public boolean contains (T data){
		if (data == null)
			throw new IllegalArgumentException("data cannot be null"); 
		//can't be in empty node
		if (isEmpty())
			return false;
		ArrayNode<T> temp = beginMarker.next;
		//loop through nodes
		while (temp != endMarker){
			//past this node
			if (temp.getLast().compareTo(data) < 0){
				temp = temp.next;
			}
			else{
				//loop through possible node
				for (int i=0; i< temp.getArraySize();i++){
					//found
					if (temp.get(i).equals(data))
						return true;
				}
				//iterate
				temp = temp.next;
			}
		}
		return false;
	}
	
	// Inserts data into the list. Parameter data will be inserted into the node 
	// referenced by current, or node current will be split into two nodes, 
	// and data will be inserted into one of these nodes.
	// The rules for splitting a node are described in the implementation section.
	// Implementation Note: Called by add().
	protected void insertWithPossibleSplit(ArrayNode<T> current, T data){
		//no split
		if (current.getLength() != current.getArraySize()){
			current.insertSorted(data);
		}
		//split
		else{
			//Special case: size of 1
			if (current.getLength()==1){
				//inserting after current
				if (current.getFirst().compareTo(data)<0){
					//make newnode
					ArrayNode<T> newNode = new ArrayNode<T>(current, current.next, 1);
					//data in newnode
					newNode.insertSorted(data);
					//pointer stuff
					current.next.prev = newNode;
					current.next = newNode;
					size++;
				}
				//Inserting before current
				else{
					//samesort of algorithm as above
					ArrayNode<T> newNode = new ArrayNode<T>(current, current.next, 1);
					newNode.insertSorted(current.getFirst());
					current.next.prev = newNode;
					current.next = newNode;
					Object[] tt = {data};
					current.setArray(tt);
					size++;
				}
				numAdded++;
			}
			
			//Not special case
			else{
				//make newnode
				ArrayNode<T> newNode = new ArrayNode<T>
					(current, current.next, capacityOfArrays);
				//pointers
				current.next = newNode;
				newNode.next.prev = newNode;
				//do some sort of loop with condition from the specs which
				//moves 2nd half of current to newNode
				int startIndex = (current.getArraySize())/2;
				
				//take second half of current and put in newnode
				for (; startIndex < current.getArraySize();){
					T temp = current.remove(startIndex);
					newNode.insertSorted(temp);
				}
				
				//decide which node to put into
				if (newNode.getFirst().compareTo(data)>0){
					current.insertSorted(data);
				}
				else
					newNode.insertSorted(data);
					
			size++;
			}
		}
	}
	
	// Insert data into the list. Returns true if data was added.
	// The rules for finding the node in which to insert data are described 
	// in the implementation section.
	// Throws IllegalArgumentException if data is null.
	public boolean add(T data){
		if (data == null){
			throw new IllegalArgumentException("data cannot be null");
		}
		numAdded++;
    	modCount++;
		ArrayNode<T> temp = beginMarker.next;
		
		//Make first node and insert
		if (temp == endMarker){
			ArrayNode<T> first = new ArrayNode<T>(
					beginMarker,endMarker,capacityOfArrays);
			size++;
			beginMarker.next = first;
			endMarker.prev = first;
			first.insertSorted(data);
			return true;
		}
		//find node to place
		while (temp != endMarker){
			//last node
			if ((temp.next==endMarker)){
				insertWithPossibleSplit(temp, data);
				break;
			}
			//first element in the chainedarrays
			if ((temp==beginMarker.next)&&(temp.getFirst().compareTo(data)>0)){
				insertWithPossibleSplit(temp, data);
				break;
			}
			//in between nodes
			if ((temp.getLast().compareTo(data)<0)&&
					(temp.next.getFirst().compareTo(data)>0)){
				//checks which is longer
				if ((temp.getArraySize()>=temp.next.getArraySize())&&
						temp.getArraySize()==temp.getLength())
					temp = temp.next;
				
				insertWithPossibleSplit(temp, data);
				break;
			}
			//past this node
			if ((temp.getLast().compareTo(data) < 0)){
				temp = temp.next;
			}
			//otherwise this one
			else{
				insertWithPossibleSplit(temp, data);
				return true;
			}
		}
		return false;
	}
	
	
	// Inserts all of the elements in the specified collection in the order 
	// that they are returned by the specified collection's Iterator. 
	// Returns true if at least one element was added.
	// Implementation note: May repeatedly call add().
	public boolean addAll(Iterable<T> c){
		//same addAll from HW2 denseboard
		boolean added = false;
		for(T thing : c){
			added |= this.add(thing);
		}
		return added;
	}
	
	// Removes the first occurrence of the specified element if it is present. 
	// Return a reference to the removed element if it is removed. 
	// Compress() the list, if necessary.
	protected T remove(T data){
		//data not in it, saves time
		if(!contains(data))
			return null;

    	modCount++;
		ArrayNode<T> current = beginMarker.next;
		while (current != endMarker){
			//uses node remove
			T x = current.remove(data);
			//data not in this node
			if (x== null)
				current = current.next;
			//data removed
			else{
				numAdded--;
				//remove empty node
				if (current.getArraySize()==0){
					current.next.prev = current.prev;
					current.prev.next = current.next;
					size--;
				}
				//Check if compression is needed
				if (size() < (nodeCount()*capacityOfArrays*0.5)){
					compress();
				}
				
				return x;
			}
		}
		return null;
	}
		//check (the number of nodes * the capacity of the arrays * 0.5)
		//Use chainedArray remove and shift
	
	
	// Reduce the amount of allocated space by shifting elements and possibly 
	// removing nodes. No new nodes can be created during compression.
	// The compression procedure is described in the implementation section.
	protected void compress(){
		
    	modCount++;
		ArrayNode<T> current = beginMarker.next;
		
		//find non-full node
		while (current != endMarker){
			if (current.getLength()== current.getArraySize()){
				current = current.next;
			}
			else{
				ArrayNode<T> removing = current.next;
				//compression done
				if (removing==endMarker){
					//remove empty nodes
					break;
				}
				//empty node
				while (removing.getArraySize()==0){
					removing = removing.next;
					if (removing==endMarker)
						break;
				}
				//not sure why this is needed
				if (removing==endMarker){
					//remove empty nodes
					break;
				}
				//move elements from "removing" node to "current"
				T temp = removing.removeFirst();
				current.insertSorted(temp);
				
				//check current length, go to next if full, otherwise redo loop
				if (current.getLength()==current.getArraySize()){
					current = current.next;
				}
			}
		}
		//do another sweep at end to remove empty nodes
		current = beginMarker.next;
		while (current != endMarker){
			if (current.getArraySize()==0){
				current.prev.next = current.next;
				current.next.prev = current.prev;
				current = current.next;
				size--;
			}
			else{
				current = current.next;
			}
		}
	}
	
	// Returns the first item.
	// Throws NoSuchElementException if the ChainedArrays are empty.
	public T getFirst( ){
		if (isEmpty())
			throw new NoSuchElementException("ChainedArrays are empty");
		//first node
		ArrayNode<T> first = beginMarker.next;
		try{
			//first elem
			return first.getFirst();
		}catch( IndexOutOfBoundsException e){
			throw new NoSuchElementException("ChainedArrays are empty");
		}
		
	}
	
	// Returns the last item.
	// Throws NoSuchElementException if the ChainedArrays are empty.
	public T getLast( ){
		if (isEmpty())
			throw new NoSuchElementException("ChainedArrays are empty");
		//last node
		ArrayNode<T> last = endMarker.prev;
		try{
			//last elem
			return last.getLast();
		}catch( IndexOutOfBoundsException e){
			throw new NoSuchElementException("ChainedArrays are empty");
		}
	}
	
	// Removes the first item.
	// Return a reference to the removed element if it is removed. 
	// Compress() the list, if necessary.
	// Throws NoSuchElementException if the ChainedArrays are empty.
	public T removeFirst( ){
		//calls remove onfirst
		T toRemove = getFirst();
		return remove(toRemove);
	}
	
	// Returns the last item.
	// Return a reference to the removed element if it is removed. 
	// Compress() the list, if necessary.
	// Throws NoSuchElementException if the ChainedArrays are empty.
	public T removeLast( ){
		if (isEmpty())
			throw new NoSuchElementException("ChainedArrays are empty");
		//last node
		ArrayNode<T> node = endMarker.prev;
		try{
	    	modCount++;
	    	numAdded--;
	    	//calls nested method
			return node.removeLast();
		}catch( IndexOutOfBoundsException e){	//empty node
			throw new NoSuchElementException("ChainedArrays are empty");
		}
	}
	       
	// Gets the Node at position idx, which must range from 0 to numNodes( )-1.
	// Throws IndexOutOfBoundsException if idx is not between 
	//   0 and numNodes()-1, inclusive.
	protected ArrayNode<T> getNode(int idx){
		if ((idx < 0)||(idx>nodeCount()-1))
			throw new IndexOutOfBoundsException();
		//loops through nodes to find
		ArrayNode<T> current = beginMarker.next;
		for(int i=0; i<idx; i++){
			 current = current.next;
		}
		return current;
	}
	 
	// Gets the Node at position idx, which must range in position 
	// from lower to upper.
	// Throws IndexOutOfBoundsException if idx is not between 
	//   lower and upper, inclusive.
	protected ArrayNode<T> getNode( int idx, int lower, int upper){
		if ((idx < lower)||(idx>upper))
			throw new IndexOutOfBoundsException();
		return getNode(idx);
	}
	
	// Return true if the items in other are equal to the items in
	// this ChainedArrays (same order, and same values according to equals).
	// Requires the provided iterator to be implemented correctly.
	public boolean equals( Object other ){
		 if (!(other instanceof ChainedArrays))
			 return false;
		 Iterator<T> it1 = this.iterator();
		 @SuppressWarnings("unchecked")
		 ChainedArrays<T> other_casted = (ChainedArrays<T>) other;
		 Iterator<T> it2 = other_casted.iterator();
		 boolean ans = true;
		 while(it1.hasNext()&&it2.hasNext()){
			 ans &= isEqual( it1.next( ), it2.next( ) );
		 }
		 return ans;
	}
	 //Returns pretty representation of the ChainedArrays
	public String toString(){
		String str = "|";
		ArrayNode<T> temp = beginMarker.next;
		while (temp != endMarker){
			str+= temp.toString() + "|";
			temp = temp.next;
		}
		return str;
	}
	// Return true if two objects are equal; works if objects can be
	// null. Used internally for implementation of equals(other).
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
	// Returns an iterator over the elements in the proper sequence.
	public Iterator<T> iterator(){
		return new ChainedArraysIterator();
	}
	
	// Iterator for ChainedArrays. (See the description below.)
	
	
	private class ChainedArraysIterator implements Iterator<T> {
		ArrayNode<T> currentNode;
		T current;
		int idx, expectedModCount;
		// Constructor
		// Target Complexity: O(1)
		public ChainedArraysIterator(){
			currentNode = beginMarker.next;
			current = currentNode.getFirst();
			idx = 0;
			expectedModCount=modCount;
		}

		// Returns true if the iterator can be moved to the next() element.
		public boolean hasNext(){
			if (expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			//Check this condition, may need to check for empty future nodes
			if ((current == null)){//&&(currentNode.getLast()==current)){
				return false;
			}
			return true;
			//current is not endmarker, 
			
			/*if ((currentNode.getLast()!=current))
				return true;
			if (currentNode.next.getArraySize()!= 0)
				return true;
			return false;*/
		}

		// Move the iterator forward and return the passed-over element
		public T next(){
			if (expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			T passed = current;
			if ((currentNode.next==endMarker)&&(currentNode.getLast()==current)){
				currentNode = endMarker;
				current = null;
				return passed;
			}
			if (currentNode.indexOf(current)==currentNode.getArraySize()-1){
				currentNode = currentNode.next;
				current = currentNode.getFirst();
				idx = 0;
				return passed;
			}	
			idx++;
			current = currentNode.get(idx);
			return passed;
		}

		// The following operation is part of the Iterator interface
		// but is not supported by the iterator. 
		// Throws an UnsupportedOperationException if invoked.
		public void remove(){
			throw new UnsupportedOperationException();
		}
		
		public String toString(){
			throw new UnsupportedOperationException();
		}
	}
}