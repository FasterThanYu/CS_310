// SimpleStack class from Weiss primarily from Data Structures and
// Problem Solving Using Java 4th edition. Uses singly linked nodes to
// implement the stack.  This class does not require any modifications
// or additions.
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void push( x )         --> Insert x
// void pop( )            --> Remove most recently inserted item
// AnyType top( )         --> Return most recently inserted item
// AnyType topAndPop( )   --> Return and remove most recent item
// boolean isEmpty( )     --> Return true if empty; else false
// void clear( )          --> Remove all items
// ******************ERRORS********************************
// top, pop, or topAndPop on empty stack
//
// @author Mark Allen Weiss
public class SimpleStack<AnyType>{

  // Tracks the top node of stack.
  private Node<AnyType> topOfStack;

  // Construct the stack.
  public SimpleStack( ) {
    topOfStack = null;
  }

  // Test if the stack is logically empty.
  // @return true if empty, false otherwise.
  public boolean isEmpty( ) {
    return topOfStack == null;
  }

  // Make the stack logically empty.
  public void clear( ) {
    topOfStack = null;
  }

  // Insert a new item into the stack.
  // @param x the item to insert.
  public void push( AnyType x ) {
    topOfStack = new Node<AnyType>( x, topOfStack );
  }

  // Remove the most recently inserted item from the stack.
  // @throws UnderflowException if the stack is empty.
  public void pop( ) {
    if( isEmpty( ) )
      throw new RuntimeException( "SimpleStack pop" );
    topOfStack = topOfStack.next;
  }

  // Get the most recently inserted item in the stack.
  // Does not alter the stack.
  // @return the most recently inserted item in the stack.
  // @throws UnderflowException if the stack is empty.
  public AnyType getTop( ) {
    if( isEmpty( ) )
      throw new RuntimeException( "SimpleStack empty in getTop" );
    return topOfStack.data;
  }

  // Return and remove the most recently inserted item
  // from the stack.
  // @return the most recently inserted item in the stack.
  // @throws UnderflowException if the stack is empty.
  public AnyType topAndPop( ) {
    if( isEmpty( ) )
      throw new RuntimeException( "SimpleStack empty in topAndPop" );

    AnyType topItem = topOfStack.data;
    topOfStack = topOfStack.next;
    return topItem;
  }

  // A singly linked Node which contains data and a link to another
  public class Node<T>{
    public T data;
    public Node<T> next;

    public Node(T d, Node<T> n ){
      this.data = d;
      this.next = n;
    }
  }

}
