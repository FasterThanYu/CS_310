import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
// An implemntation of an ExpandbleBoard intended to favor reduced
// memory over speed of operations.  Most operations require O(E) time
// to complete where E is the number of non-fill elements that have
// been set on the board.  Internally, elements are stored in several
// lists sorted in different orders to facilitate the calculation of
// the longest sequence.
// 
// Target Space Complexity: O(E)
//  E: The number of elements that have been set on the board
public class SparseBoard<T> implements ExpandableBoard<T>{

	private int minRow, maxRow, minCol, maxCol;
	private T fillElem;
	private AdditiveList<RowColElem<T>> board = new AdditiveList<RowColElem<T>>();
//	SimpleStack<RowColElem<T>> undos = new SimpleStack<RowColElem<T>>();
//	SimpleStack<RowColElem<T>> redos = new SimpleStack<RowColElem<T>>();
	private List<RowColElem<T>> longest = new ArrayList<RowColElem<T>>();
  // Workhorse constructor.  Initially any get() should return the
  // fillElem specified. Set up all internal data structures to
  // facilitate longest sequence retrieval, undo/redo capabilities.
  // The fillElem cannot be null: passing null for this parameter will
  // result in a RuntimeException with the message "Cannot set
  // elements to null".  The runtime of the constructor should not
  // depend on the initial size of the board.
  //
  // Runtime: O(1) (worst-case)
  public SparseBoard(int minRow, int maxRow, int minCol, int maxCol, T fillElem){
	  if (fillElem == null)
		  throw new RuntimeException();
	  this.minRow = minRow;
	  this.maxRow = maxRow;
	  this.minCol = minCol;
	  this.maxCol = maxCol;
	  this.fillElem = fillElem;
  }

  // Convenience 1-arg constructor, creates a single cell board with
  // given fill element. The initial extent of the board is a single
  // element at 0,0.  May wish to call the first constructor in this
  // one to minimize code duplication.
  public SparseBoard(T fillElem){
	  this(0,0,0,0,fillElem);
  }

  // Convenience 2-arg constructor, creates a board with given fill
  // element and copies elements from T 2-D array. Assumes upper left
  // is coordinate 0,0 and lower right is size of 2-D array.  The
  // board should not have any undo/redo history but should have a
  // longest sequence calculated from the contents of 2-D array.
  //
  // There is no target complexity for this method. Use of repeated
  // set() calls is suggested to simplify it.
  public SparseBoard(T[][] x, T fillElem){
  //Goes through each element of array, and uses add method to add to AdditiveList
	  minRow = 0;
	  minCol = 0;
	  maxRow = x.length-1;
	  maxCol = x[0].length-1;
	  if (fillElem == null)
		  throw new RuntimeException("Cannot set elements to null");
	  this.fillElem = fillElem;
	  for (int i=0; i<maxRow-minRow+1; i++){
		  	for (int j=0; j<maxCol-minCol+1; j++){
		  		if (!(x[i][j].equals(fillElem))){
		  			set(i,j,x[i][j]);
		  		}
		  	}
	  }
  }

  // Access the extent of the board: all explicitly set elements are
  // within the bounds established by these four methods.
  //
  // Target complexity: O(1)
  public int getMinRow(){
	  return minRow;
  }
  public int getMaxRow(){
	  return maxRow;
  }
  public int getMinCol(){
	  return minCol;
  }
  public int getMaxCol(){
	  return maxCol;
  }
  // Retrieve the fill element for the board.
  public T getFillElem(){
	  return fillElem;
  }

  // Change the fill element for the board. To make this efficient,
  // only change an internal field which dictates what should be
  // returned when an element that has not been explicitly set is
  // requested via a call to get().
  //
  // Target complexity: O(1) (worst-case)
  public void setFillElem(T f){
	  fillElem = f;
  }

  // Retrieve the longest sequence present on the board. If there is a
  // tie, the earliest longest sequence to appear on the board should
  // be returned.  The list returned should be independent of any
  // internal board data structures so that the list can be changed
  // and not affect the board.  This implies a copy of any internal
  // board lists should be made and returned.  The longest sequence on
  // a board that is filled with only the fill element is the empty
  // list [].
  //
  // Target Complexity: O(L) (worst case)
  // L: length of the longest sequence
  public List< RowColElem<T> > getLongestSequence(){
	  //First check for longest sequence in rowCol
	  List<RowColElem<T>> rowCol = elementsInRowColOrder();
	  if (rowCol.size()==1){
		  longest = new ArrayList<RowColElem<T>>();
		  longest.add(rowCol.get(0));
	  }
		//Creates temp longest list to hold sequences
	  ArrayList<RowColElem<T>> tempLongest = new ArrayList<RowColElem<T>>();
	  for (int i=0; i<rowCol.size()-1; i++){
	  //case previous sequence just terminated
		  if (tempLongest.isEmpty()){
			  tempLongest.add(rowCol.get(i));
		  }
		  if ((rowCol.get(i).getRow()==rowCol.get(i+1).getRow())//same row
				  //Columns are adjacent
				  && (rowCol.get(i).getCol()+1==rowCol.get(i+1).getCol())){
			  //add to tempLongest
			  tempLongest.add(rowCol.get(i+1));
		  }
		  //Terminate search
		  else {
			  tempLongest = new ArrayList<RowColElem<T>>();
		  }
		  //updates longest if applicable
		  if (tempLongest.size()>longest.size())
			  longest = tempLongest;
	  }
	  
	  
	  //checks for colRow
	  List<RowColElem<T>> colRow = elementsInColRowOrder();
	  tempLongest = new ArrayList<RowColElem<T>>();
	  //same as above
	  for (int i=0; i<colRow.size()-1; i++){
	  //same as above
		  if (tempLongest.isEmpty())
			  tempLongest.add(colRow.get(i));
		  if ((colRow.get(i).getCol()==colRow.get(i+1).getCol())//same col
				  //Rows are adjacent
				  && (colRow.get(i).getRow()+1==colRow.get(i+1).getRow())){
			  //add to tempLongest
			  tempLongest.add(colRow.get(i+1));
		  }
		  //if not adjacent
		  else {
			  tempLongest = new ArrayList<RowColElem<T>>();
		  }
		  //updates longest if applicable
		  if (tempLongest.size()>longest.size())
			  longest = tempLongest;
	  }
	  
	  //Checks for longest in diagRow
	  List<RowColElem<T>> diagRow = elementsInDiagRowOrder();
	  //same as above
	  tempLongest = new ArrayList<RowColElem<T>>();
	  for (int i=0; i<diagRow.size()-1; i++){
		  if (tempLongest.isEmpty())
			  tempLongest.add(diagRow.get(i));
		  RowColElem<T> tempElem = diagRow.get(i);
		  RowColElem<T> nextElem = diagRow.get(i+1);
		  if (((tempElem.getCol()-tempElem.getRow())==(nextElem.getCol()-nextElem.getRow()))
				  && (diagRow.get(i).getCol()==(nextElem.getCol()-1))){
			  //add to tempLongest
			  tempLongest.add(nextElem);
		  }
		  else {
			  tempLongest = new ArrayList<RowColElem<T>>();
		  }
		  //updates longest if applicable
		  if (tempLongest.size()>longest.size())
			  longest = tempLongest;
	  }
	  
	  
	//Checks for longest in reverseDiagRow
	  List<RowColElem<T>> reverseDiagRow = elementsInADiagReverseRowOrder();
	  tempLongest = new ArrayList<RowColElem<T>>();
	  for (int i=0; i<reverseDiagRow.size()-1; i++){
	  //same as above
		  if (tempLongest.isEmpty())
			  tempLongest.add(reverseDiagRow.get(i));
			  //creates temps to save space on the line
		  RowColElem<T> tempElem = reverseDiagRow.get(i);
		  RowColElem<T> nextElem = reverseDiagRow.get(i+1);
		  //conditional to see if adjacent
		  if (((tempElem.getCol()+tempElem.getRow())==(nextElem.getCol()+nextElem.getRow()))
				  && ((nextElem.getCol())==(tempElem.getCol()+1))){
			  //add to tempLongest
			  tempLongest.add(nextElem);
		  }
		  //terminate sequence
		  else {
			  tempLongest = new ArrayList<RowColElem<T>>();
		  }
		  //updates longest if applicable
		  if (tempLongest.size()>longest.size())
			  longest = tempLongest;
	  }
	  
	  return longest;
  }

  // Retrieve an element at virtual row/col specified. Any row/col may
  // be requested. If it is beyond the extent of the board determined
  // by min/max row/col, the fill element is returned.  If the element
  // has not been explicitly set, the fill element is returned.
  // 
  // Complexity: O(E)
  //  E: The number of elements that have been set on the board
  public T get(int row, int col){
  //Checks bounds
	  if ((row > maxRow)||(row < minRow))
		  return fillElem;
	  if ((col > maxCol)||(col < minCol))
		  return fillElem;
		  //create iterator
	  Iterator<RowColElem<T>> rowIt = board.listIterator();
	  //iterate through list 
	  while (rowIt.hasNext()){
		  RowColElem<T> elem = rowIt.next();
		  if ((elem.getRow()==row)&&(elem.getCol()==col))
			  return elem.getElem();
	  }
	  return fillElem;
  }

  // Update internals to reflect an increase in the board extents by
  // one row on the bottom.  This method should not change the memory
  // footprint of the SparseBoard.
  // 
  // Target Complexity: O(1) (worst-case)
  public void addRowBottom(){
	  maxRow++;
  }

  // Update internals to reflect an increase in the board extents by
  // one column on the right.  This method should not change the
  // memory footprint of the SparseBoard.
  //
  // Target Complexity: O(1) (worst-case)
  public void addColRight(){
	  maxCol++;
  }

  // Perform expansion for the board. Adjust any internal fields so
  // that the board tells the world it is large enough to include the
  // (row,col) position specified.  No new memory should be allocated.
  // Always return 0.
  //
  // Target Complexity: O(1) (worst-case)
  public int expandToInclude(int row, int col){
  //adds to max values. That's it.
	  if (row > maxRow)
		  maxRow = row;
	  else if (row < minRow)
		  minRow = row;
	  if (col > maxCol)
		  maxCol = col;
	  else if (col < minCol)
		  minCol = col;
	  return 0;
  }

  // Set element at row/col position to be x. Update internals to
  // reflect that the set may have created a new longest sequence.
  // Also update internals to allow undoSet() to be used and disable
  // redoSet() until a set has been undone.  Once an element is set,
  // it cannot be set again; attempts to do so raise a runtime
  // exception with the message: "Element 4 -2 already set to XX"
  // where the row/col indices and string representation of the
  // element are adjusted to match the call made.  Setting an element
  // to the fill element of board has no effect on the board.  It is
  // not allowed to set elements of the board to be null. Attempting
  // to do so will generate a RuntimeException with the message
  // "Cannot set elements to null"
  //
  // Target Complexity: O(E)
  //  E: The number of elements that have been set on the board
  public void set(int row, int col, T x){
	  //checks for null
	  if (x == null)
		  throw new RuntimeException("Cannot set elements to null");
	  //expands board as necessary
	  expandToInclude(row,col);
	  //creates RowColElem to put in linked list
	  RowColElem<T> place = new RowColElem<T>(row, col, x);
	  //Creates iterator to step through each row of the board
	  ListIterator<RowColElem<T>> rowIt = board.listIterator();
	  while (rowIt.hasNext()){
		  //creates temporary variable to hold each column, then iterator
		  //to step through that column
		  RowColElem<T> elem = rowIt.next();
		  //Case where space is already set
		  if ((elem.getRow()==row)&&(elem.getCol()==col)){
			  String str = "Element "+row+" "+col+" already set to "
					  +elem.getElem().toString();
			  throw new RuntimeException(str);
		  }
		  if (!rowIt.hasNext()){
			  rowIt.previous();
			  rowIt.add(place);
			  break;
		  }
		  
		 
		  //Case where row is found, but needs to be inserted
		  else if ((elem.getRow()==row)&&(elem.getCol()>col)){
				rowIt.previous();		//not sure whether needed
			  rowIt.add(place);
			  break;
		  }
			  //Case row not found and must be created
		  
		  if (elem.getRow() > row){
			  	rowIt.previous();		//not sure whether needed
			  	rowIt.add(place);
			  	break;
		  }
		  
		  
	  }
	  //if empty, adds to board
	  if (board.isEmpty()){
		  board.add(place);
	  }
	  
	  
	  
	//RowCol list add
	  for (int i=0; i<rowColOrder.size(); i++){
		  RowColElem<T> temp = rowColOrder.get(i);
		  if (place.getRow()<temp.getRow()){
			  rowColOrder.add(i,place);
			  break;
		  }
		  if (place.getCol()<temp.getCol()){
			  rowColOrder.add(i,place);
			  break;
		  }
		  if (i==rowColOrder.size()-1){
			  rowColOrder.add(place);
			  break;
		  }
			  			  
	  }
		  //Checks if list is empty
	  if (rowColOrder.isEmpty()){
		  rowColOrder.add(place);
	  }
		  
		  //Adds element to colRow list
		  //List<RowColElem<T>> colRowOrder = elementsInColRowOrder();
		  for (int i=0; i<colRowOrder.size(); i++){
			  RowColElem<T> temp = colRowOrder.get(i);
			  //looks for where to put it
			  if (place.getCol()<temp.getCol()){
				  colRowOrder.add(i,place);
				  break;
			  }
			  if (place.getRow()<temp.getRow()){
				  colRowOrder.add(i,place);
				  break;
			  }			
			  if (i==colRowOrder.size()-1){
				  colRowOrder.add(place);
				  break;
			  }
		  }
		  if (colRowOrder.isEmpty()){
			  colRowOrder.add(place);
		  }
		  
		  //Adds element to diagRow List
		  //List<RowColElem<T>> diagRowOrder = elementsInDiagRowOrder();
		  
		  for (int i=0; i<diagRowOrder.size(); i++){
			  //same as above, but for different list
			  RowColElem<T> temp = diagRowOrder.get(i);
			  if ((temp.getCol()-temp.getRow())>(place.getCol()-place.getRow())){
				  diagRowOrder.add(i,place);
				  break;
			  }
			  if(place.getCol()<temp.getCol()){
				  diagRowOrder.add(i,place);/*
			  for (int j=i+1; j<diagRowOrder.size(); j++){
				  temp = diagRowOrder.get(j);
				  diagRowOrder.set(j, temp);
			  }*/
			  break;
				  }			
			  if (i==diagRowOrder.size()-1){
				  diagRowOrder.add(place);
				  break;
			  }
		  }
		  
		  if (diagRowOrder.isEmpty()){
			  diagRowOrder.add(place);
		  }
		  
		  
		  //adding to diagReverseRowOrder
		  
		  for (int i=0; i<diagReverseRowOrder.size(); i++){
			  //same idea as above
			  RowColElem<T> temp = diagReverseRowOrder.get(i);
			  if ((temp.getCol()+temp.getRow())>(place.getCol()+place.getRow())){ 
				  diagReverseRowOrder.add(i,place);
				  break;
			  }
			  if ((temp.getCol()>place.getCol())&&((temp.getCol()+temp.getRow())==
					  (place.getCol()+place.getRow()))){
				  diagReverseRowOrder.add(i,place);
				  break;
			  }	
			  if (i==diagReverseRowOrder.size()-1){
				  diagReverseRowOrder.add(place);
				  break;
			  }
		  }
		  if (diagReverseRowOrder.isEmpty()){
			  diagReverseRowOrder.add(place);
		  }
	  
	  //now do undo/redo stuff
//	  redos = new SimpleStack<RowColElem<T>>();
//	  undos.push(place);
	  
	  
	  // Check longest sequence
	  longest = getLongestSequence();
  }

  // Produce copies of the internal lists of the explicitly set
  // elements on the board.  Only elements that have been explictily
  // set should be included. Each method produces a list that is
  // sorted in an order dictated by the method name. The returned
  // lists should be copies so that subsequenent modification to the
  // lists does not affect the board.
  //
  // Target Complexity: O(E) (worst-case)
  

  private ArrayList<RowColElem<T>> colRowOrder = new ArrayList<RowColElem<T>>();
  private ArrayList<RowColElem<T>> rowColOrder = new ArrayList<RowColElem<T>>();
  private ArrayList<RowColElem<T>> diagRowOrder = new ArrayList<RowColElem<T>>();
  private ArrayList<RowColElem<T>> diagReverseRowOrder = new ArrayList<RowColElem<T>>();
  public List<RowColElem<T>> elementsInRowColOrder(){
	  return rowColOrder;
  }

  public List<RowColElem<T>> elementsInColRowOrder(){
	  return colRowOrder;
  }

  public List<RowColElem<T>> elementsInDiagRowOrder(){
	  return diagRowOrder;
  }

  public List<RowColElem<T>> elementsInADiagReverseRowOrder(){
	  return diagReverseRowOrder;
  }

  // Undo an explicit set(row,col,x) operation by changing an element
  // to its previous state.  Repeated calls to undoSet() can be made
  // to restore the board to an earlier state.  Each call to undoSet()
  // enables a call to redoSet() to be made to move forward in the
  // history of the board state. Calls to undoSet() do not change the
  // extent of boards: they do not shrink to a smaller size once grown
  // even after an undo call.  If there are no sets to undo, this
  // method throws a runtime exception with the message
  // "Undo history is empty"
  //
  // Target Complexity: O(1) (worst case)
  public void undoSet(){
	  if (!board.canUndo())
		  throw new RuntimeException("Undo history is empty");
	 //calls additiveList's undo method 
	  board.undo();
	 
  }

  // Redo a set that was undone via undoSet().  Every call to
  // undoSet() moves backward in the history of the board state and
  // enables a corresponding call to redoSet() which will move forward
  // in the history.  At any point, a call to set(row,col,x) will
  // erase 'future' history that can be redone via redoSet().  If
  // there are no moves that can be redone because of a call to set()
  // or undoSet() has not been called, this method generates a
  // RuntimeException with the message "Redo history is empty".
  //
  // Target Complexity: O(1)
  public void redoSet(){
	  if (!board.canRedo())
		  throw new RuntimeException("Redo history is empty");
		  //calls additivelist's redo method
	  board.redo();
	  
  }

  // toString() - create a pretty representation of board.
  //
  // Examples:
  //   |  1|  2|  3|
  //   +---+---+---+
  // 1 |   |   |   |
  //   +---+---+---+
  // 2 |   |   |   |
  //   +---+---+---+
  // 3 |   |   |   |
  //   +---+---+---+
  //
  //    | -4| -3| -2| -1|  0|  1|  2|  3|
  //    +---+---+---+---+---+---+---+---+
  // -2 |  A|   |   |   |   |   |   |   |
  //    +---+---+---+---+---+---+---+---+
  // -1 |   |   |  B|   |   |   |   |   |
  //    +---+---+---+---+---+---+---+---+
  //  0 |   |   |   |   |   |   |   |   |
  //    +---+---+---+---+---+---+---+---+
  //  1 |   |   |   |   |   |  A|   |   |
  //    +---+---+---+---+---+---+---+---+
  //  2 |   |   |   |   |   |   |   |   |
  //    +---+---+---+---+---+---+---+---+
  //  3 |   |   |   |   |   |   |   |   |
  //    +---+---+---+---+---+---+---+---+
  //
  // Target Complexity: O(R*C)
  //   R: number of rows
  //   C: number of columns
  // 
  // Constraint: No array or arraylist allocation is allowed in this
  // method.
  // 
  // Note: to adhere to target runtime complexity employ a
  // StringBuilder and an iterator over an internal list of the boards
  // set elements.
  public String toString(){
	  String bb = "";
	  StringBuilder builder = new StringBuilder();
	  
	  //initial top left bar
	  builder.append("    |");
	  
	  //puts column numbers
	  for (int j=0; j<maxCol-minCol+1;j++){
		  builder.append(String.format("%1$3d|", minCol+j));
	  }
	  
	  //puts in divider line
	  builder.append("\n    +");
	  for (int k=0; k<maxCol-minCol+1;k++){
		  builder.append("---+");
	  }
	  //shortcut if no elements have been explicitly set
	  if (rowColOrder.isEmpty()){
		  for (int i=0; i<=maxRow-minRow;i++){
			  //Adds new line and row number
			  builder.append("\n");
			  builder.append(String.format("%1$3d |",minRow+i));
			  
			  //Loop to put in board elements
			  for (int j=0; j<=maxCol-minCol;j++){
				  builder.append(String.format("%1$3s|", fillElem));
			  }
			  
			  //puts in divider line
			  builder.append("\n    +");
			  for (int k=0; k<maxCol-minCol+1;k++){
				  builder.append("---+");
			  }
		  }
		  bb = builder.toString()+"\n";
		  return bb;
	  }
	  
	  
	  //for when elements have been set
	  ListIterator<RowColElem<T>> rowIt = board.listIterator();
	  //makes rowColElem from iterator
	  RowColElem<T> temp1;
	  
	  //fails a lot of tests without the try-catch
	  try{
		  temp1 = rowIt.next();
	  } catch (RuntimeException e){
		  temp1 = rowIt.previous();
	  }
	  for (int i=minRow; i<=maxRow; i++){
			//add newline and row number
		  builder.append("\n");
		  builder.append(String.format("%1$3d |",i));
		  for (int j=minCol; j<= maxCol; j++){
			  if (temp1.getCol()==j && temp1.getRow()==i){
					//add to string builder
					//increment temp1/rowIter
				  builder.append(String.format("%1$3s|", temp1.getElem()));
				  //again, needs this
				  try{
					  temp1 = rowIt.next();
				  } catch (RuntimeException e){
					  temp1 = rowIt.previous();
				  }
				}
			  else{
					//add fillElem to builder
				  builder.append(String.format("%1$3s|", fillElem));
				}
			}
		//puts in divider line
		  builder.append("\n    +");
		  for (int k=0; k<maxCol-minCol+1;k++){
			  builder.append("---+");
		  }
	  }
	 
	  bb = builder.toString()+"\n";
	  return bb;
  }

}