import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DenseBoard<T> implements ExpandableBoard<T>{
	
	//Sets up variables to be used throughout the methods
	private int minRow, maxRow, minCol, maxCol;
	//holds fill element
	private T fillElem;
	//Stacks used to store undos and redos
	private Stack<RowColElem<T>> undoStack=new Stack<RowColElem<T>>(),
			redoStack=new Stack<RowColElem<T>>();
	//Board arraylist of arraylists
	private ArrayList<ArrayList<T>> rows = new ArrayList<ArrayList<T>>();
	//Longest sequence
	List<RowColElem<T>> longest = new ArrayList<RowColElem<T>>();
	
	
	
	
  // Workhorse constructor, create initial space indicated by min/max
  // row/col. Initially any get() should return the fillElem
  // specified. Set up all internal data structures to facilitate
  // longest sequence retrieval, undo/redo capabilities.  The fillElem
  // cannot be null: passing null for this parameter will result in a
  // RuntimeException with the message "Cannot set elements to null"
  //
  // Runtime: O(R * C)
  //   R; number of rows which is maxRow-minRow+1
  //   C; number of cols whcih is maxCol-minCol+1
  public DenseBoard(int minRow, int maxRow, int minCol, int maxCol, T fillElem) {
	  
  	this.minRow = minRow;
  	this.maxRow = maxRow;
  	this.minCol = minCol;
  	this.maxCol = maxCol;
  	this.fillElem = fillElem;
  	//Make sure fillElem isn't null
  	if (fillElem == null)
  		throw new RuntimeException("Cannot set elements to null");
  	//populates board will nulls
  	for (int i=0; i<=maxRow-minRow; i++){
	  	ArrayList<T> cols = new ArrayList<T>();
	  	for (int j=0; j<=maxCol-minCol; j++){
			cols.add(null);
	  	}
  	
  		rows.add(cols);
  	}
  		//rows.set(i, cols);
  }
  // Convenience 1-arg constructor, creates a single cell board with
  // given fill element. The initial extent of the board is a single
  // element at 0,0 that is empty.  May wish to call the first
  // constructor in this one to minize code duplication.
  public DenseBoard(T fillElem) {
	minRow = 0;
	maxRow = 0;
	minCol = 0;
	maxCol = 0;
	//Makes sure fillElem isn't null
	if (fillElem == null){
		throw new RuntimeException("Cannot set elements to null");
	}
	//Creates 1x1 board
	this.fillElem = fillElem;
	ArrayList<T> cols = new ArrayList<T>();
	cols.add(null);
	rows.add(cols);
  }

  // Convenience 2-arg constructor, creates a board with given fill
  // element and copies elements from T 2-D array. Assumes upper left
  // is coordinate 0,0 and lower right is size of 2-D array
  public DenseBoard(T[][] x, T fillElem){
	  minRow = 0;
	  minCol = 0;
	  maxCol = x[0].length-1;
	  maxRow = x.length-1;
	  //Makes sure fillElem isn't null
	  if (fillElem==null)
		  throw new RuntimeException("Cannot set elements to null");
	  this.fillElem = fillElem;
	 //Populates board, putting nulls where x has fillElem
	  for (int i=0; i<=maxRow-minRow; i++){
	  	ArrayList<T> cols = new ArrayList<T>();
	  	for (int j=0; j<=maxCol-minCol; j++){
	  		if (!x[i][j].equals(fillElem))
	  			cols.add(x[i][j]);
	  		else
	  			cols.add(null);
	  	}
	  	rows.add(cols);
	  }
  }

  // Access the extent of the board
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

  // Change the fill element for the board. 
  public void setFillElem(T f){
	  fillElem = f;
  }

  // Retrieve the longest sequence present on the board. If there is a
  // tie, the earliest longest sequence to appear on the board should
  // be returned.  The list returned should be independent of any
  // internal board data structures so that the list can be changed
  // and not affect the board.  This implies a copy of any internal
  // board lists should be made and returned.
  
  
  //This element is kept current by calling the 
  //method after every set, undo, and redo.
  public List< RowColElem<T> > getLongestSequence(){
	  //Sets up temporary variables and loop indices for search
	  boolean sequence = true;
	  int i=0,j=0;
	  
	  //Main loop of method to search
	  while (sequence){
		  T temp = rows.get(i).get(j);
		  
		  //I wasn't able to get a working way to compare sequences
		  //of equal length, though I would do it by searching through
		  //the undo stack until I found elements from each array.
		  //Whichever sequence's element was farther down would be 
		  //considered the longest.
		  
		  //Sets up temp ArrayList to hold each sequence while searching
		  //and reset values for i and j
		  int jInitial=j,iInitial=i;
		  ArrayList<RowColElem<T>> tempList1 = new ArrayList<RowColElem<T>>();
		  
		  //Loop to check horizontal length of sequence
		  while((temp != null)&&(j<=maxCol-minCol)&&(temp.equals(rows.get(i).get(j)))){
			  RowColElem<T> match = new RowColElem<T>(i+minRow,j+minCol,temp);
			  tempList1.add(match);
			  j++;
		  }
		  //Compares to current longest sequence
		  if (tempList1.size()>longest.size()){
			  longest = tempList1;
		  }
		  
		  //Resets values
		  j=jInitial;
		  tempList1 = new ArrayList<RowColElem<T>>();
		  
		  //Loop to check vertical length of sequence
		  while ((temp != null)&&(i<=maxRow-minRow)
				  &&(temp.equals(rows.get(i).get(j)))){
			  RowColElem<T> match = new RowColElem<T>(i+minRow,j+minCol,temp);
			  tempList1.add(match);
			  i++;
		  }
		  //Compares temp list to current longest
		  if (tempList1.size() > longest.size()){
			  longest = tempList1;
		  }
		  //Resets values
		  i = iInitial;
		  tempList1 = new ArrayList<RowColElem<T>>();
		  
		  //Loop to check diagonal-right length
		  while ((temp != null)&&(i<=maxRow-minRow)&&(j<=maxCol-minCol)
				  &&(temp.equals(rows.get(i).get(j)))){
			  RowColElem<T> match = new RowColElem<T>(i+minRow,j+minCol,temp);
			  tempList1.add(match);
			  i++;
			  j++;
		  }
		  //Compares to current longest
		  if (tempList1.size() > longest.size()){
			  longest = tempList1;
		  }
		  //Resets values
		  i = iInitial;
		  j = jInitial;
		  tempList1 = new ArrayList<RowColElem<T>>();
		  
		  //Loop to check diagonal-left length
		  while((temp != null)&&(i<=maxRow-minRow)&&(j>=0)
				  &&(temp.equals(rows.get(i).get(j)))){
			  RowColElem<T> match = new RowColElem<T>(i+minRow,j+minCol,temp);
			  tempList1.add(match);
			  i++;
			  j--;
		  }
		  //Compares to current longest
		  if (tempList1.size() > longest.size()){
			  longest = tempList1;
		  }
		  //Resets values
		  i = iInitial;
		  j = jInitial;
		  tempList1 = new ArrayList<RowColElem<T>>();
		  
		  //Prepares for next round of searches.  Moves horizontal index
		  //and vertical one if needed
		  j++;
		  if (j > maxCol-minCol){
			  j = 0;
			  if (i <= maxRow-minRow)
				  i++;
		  }
		  //Exits loop
		  if (i > maxRow-minRow){
			  	sequence = false;
		  		break;
		  }
	  }
	  return longest;
  }
  
  
  
  
  
  
  

  // Retrieve an element at virtual row/col specified. Performs bounds
  // checking and necessary internal translation to retrieve from
  // physical location. Any row/col may be requested. If it is beyond
  // the extent of the board determined by min/max row/col, the fill
  // element is returned.  If the element has not been explicitly set,
  // the fill element is returned.
  // 
  // Complexity: O(1)
  
  //When an element is retrieved with get() check whether it is some 
  //internal fill value and if so, return the actual fill element.
  public T get(int row, int col){
	  //variable to be returned
	  T ret;
	  //checks if row/col are within bounds
	  if ((row<=maxRow)&&(row>=minRow)){
		  if ((col<=maxCol)&&(col>=minCol))
			  if (rows.get(row-minRow).get(col-minCol)==null)
				  ret = fillElem;
			  else
				  ret= rows.get(row-minRow).get(col-minCol);
		  //Otherwise returns fill element
		  else
			  ret = fillElem;
	  }
	  else
		  ret = fillElem;
	  return ret;
  }

  // Append a row to the bottom of the board increasing the maximum
  // row by one
   
  
  //This method adheres to the target complexity because it only has to
  //loop through all the columns once, making null values, then 
  //it adds that to rows, which takes constant time.
  public void addRowBottom(){
	  maxRow +=1;
	  ArrayList<T> tempCol = new ArrayList<T>();
	  for (int i=0; i< maxCol-minCol+1; i++){
		  tempCol.add(null);
	  }
	  rows.add(tempCol);
  }

  // Append a column to the right edge of the board increasing the
  // maximum column by one
  //
 
  
  //This method adheres to the target complexity because 
  //it goes through every row once, and adds null to the 
  //end of the row.
  public void addColRight(){
	  maxCol +=1;
	  int i;
	  for (i=0; i<rows.size(); i++){
		  rows.get(i).add(null);
	  }	  
  }

  // Set give element at row/col position to be x. Expand the board if
  // needed to create space for the position.  Update internals to
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
 
  public void set(int row, int col, T x){
	  //used if x is fillElem
	  boolean fill = true;
	  //Makes sure board is the correct size to accommodate x
	  //Makes sure x adheres to expected behavior.
	  if (x == null){
		  throw new RuntimeException("Cannot set elements to null");
	  }
	  if (x.equals(fillElem)){
		  fill = false;
	  }
	  //Expands right
	  if ((col > maxCol)&&fill){
		  expandToInclude(row,col);
	  }
	  //Expands left
	  if ((col < minCol)&&fill){
		  expandToInclude(row,col);
	  }
	  
	  //Expands down
	  if ((row > maxRow)&&fill){
		  expandToInclude(row,col);
	  }
	  
	  //Expands up
	  if ((row < minRow)&&fill){
		  expandToInclude(row,col);
	  }
	  //Throws exception if space already set
	  if (fill&&(rows.get(row-minRow).get(col-minCol)!= null)){
		  throw new RuntimeException(String.format("Element %d %d already set to ",
				  row, col)+(rows.get(row-minRow).get(col-minCol).toString()));
	  }
	  //Doing the actual setting, checking for new longest sequence,
	  //then taking care of undo/redo stacks
	  if (fill){
		  rows.get(row-minRow).set(col-minCol, x);
		  longest = getLongestSequence();
		  RowColElem<T> move = new RowColElem<T>(row,col,x); 
		  undoStack.push(move);
		  redoStack = new Stack<RowColElem<T>>();
	  }
	  
	  
	  
	  //toString();
	  //make sure to add to undo/clear redo stack	  				-done
	  //check longest sequence										-done
	  //check to make sure values already set are not overwritten 	-done
	  //values already in boxes must be null or maybe fillElem		-done 
  }

  
  
  
  
  
  
  
  // Return how many rows the board has in memory which should
  // correspond to the difference between maxRow and minRow. This
  // method is not part of the ExpandableBoard interface.
  //
  // No target complexity.
  public int getPhysicalRows(){
	  return maxRow - minRow+1;
  }

  // Return how many columns the board has in memory which should
  // correspond to the difference between maxCol and minCol. This
  // method is not part of the ExpandableBoard interface.
  // 
  // Target complexity: O(1)
  public int getPhysicalCols(){
	  return maxCol - minCol+1;
  }

  
  
  
  // Ensure that there is enough internal storage allocated so that no
  // expansion will occur if set(row,col,x) is called. Expand internal
  // space for the board if needed.  Move existing elements internally
  // if required but this method should not affect the virtual row/col
  // at which existing elements exist: if an X is at (1,2) before
  // expandToInclude(-1,10) is called, a call to get(1,2) should
  // return X after expanding.
  //
  // This method should change min/max row/col the expansion increases
  // the size of the board.
  // 
  // The method should return the number of new cells of memory N
  // which are created by it.  
  // 
  // Target Complexity: 
  //   Expansion right/down: O(N)       (amortized)
  //   Expansion left/up:    O(N + R*C) (amortized)
  //     N: new elements created which is the return value of the function
  //     R: number of rows
  //     C: number of columns
  public int expandToInclude(int row, int col){
	  //Variables to record how many cells created, along with
	  //how many columns/rows need to be created
	  int cellsCreated = 0;
	  int downDiff = row-maxRow, rightDiff = col-maxCol;
	  int upDiff = minRow-row, leftDiff = minCol-col;
	  
	  //Adds to right and shifts all cels right
	  //At the end, sets first position to null
	  if (leftDiff > 0){
		  int i=0,j=0;
		  //adding new cell
		  for (i=0; i<rows.size(); i++){
			  for (j=0;j< leftDiff; j++){
				  rows.get(i).add(null);
				  cellsCreated ++;
			  }
		  }

		  //Copied from fillAtBeginning file:
		  //shifts cells one position to right
		  for (int k=0;k<rows.size(); k++){
			  for(i=0;i<leftDiff;i++){
				  for(j=rows.get(k).size()-1; j>0; j--){
					  rows.get(k).set(j, rows.get(k).get(j-1));
				  }
				  rows.get(k).set(0,null);
			  }
		  }
		  minCol = col;
	  }
	  
	  //Adds to right if necessary
	  if (rightDiff > 0){
		  int i=0,j=0;
		  //Adds null to every row
		  for (i=0; i<rows.size(); i++){
			  for (j=0; j<rightDiff; j++){
				  rows.get(i).add(null);
				  cellsCreated ++;
			  }
			  
		  }
		  maxCol = col;
	  }
	  
	  //Adds to bottom if necessary
	  if (downDiff > 0){
		  int i=0,j=0;
		  //Creates row of nulls, then adds to end of board
		  while (j<downDiff){
			  ArrayList<T> tempCol = new ArrayList<T>();
			  for (i=0; i< maxCol-minCol+1; i++){
				  tempCol.add(null);
			  }
		  
			  rows.add(tempCol);
			  cellsCreated += maxCol-minCol+1;
			  j++;
		  }		  
		  maxRow = row;
	  }
	  
	  //Expands down, then shifts and adds blank row to top
	  if (upDiff > 0){
		  //Creates row of nulls
		  int i=0,j=0;
		  while (j<upDiff){
			  ArrayList<T> tempCol = new ArrayList<T>();
			  for (i=0; i< maxCol-minCol+1; i++){
				  tempCol.add(null);
			  }
		  
			  rows.add(tempCol);
			  cellsCreated += maxCol-minCol+1;
			  
			  //shifts all cells down
			  for(i=rows.size()-1;i>0;i--){
				  rows.set(i, rows.get(i-1));
			  } 
			  //adds blank row to top
			  tempCol = new ArrayList<T>();
			  for (i=0; i< maxCol-minCol+1; i++){
				  tempCol.add(null);
			  }
			  rows.set(0, tempCol);
			  j++;
		  }
		  minRow = row;
	  }

	  return cellsCreated;
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
	  //check if empty
	  if (undoStack.size()==0){
		  throw new RuntimeException("Undo history is empty");
	  }
	  //take out of undoStack
	  RowColElem<T> undoMove = undoStack.pop(); 
	  //change board
	  int row = undoMove.getRow();
	  int col = undoMove.getCol();
	  T x = undoMove.getElem();
	  rows.get(row-minRow).set(col-minCol,null);
	  longest = getLongestSequence();
	  //add to redoStack
	  RowColElem<T> redoMove = new RowColElem<T>(row,col,x);
	  redoStack.push(redoMove);
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
	  //Check if empty
	  if (redoStack.empty()){
		  throw new RuntimeException("Redo history is empty");
	  }
	  //Get from redoStack
	  RowColElem<T> redoMove = redoStack.pop();
	  int row = redoMove.getRow();
	  int col = redoMove.getCol();
	  T x = redoMove.getElem();
	  //change board
	  rows.get(row-minRow).set(col-minCol, x);
	  longest = getLongestSequence();
	  //add to undoStack
	  RowColElem<T> undoMove = new RowColElem<T>(row, col, x);
	  undoStack.push(undoMove);
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
  // Note: to meet adhere to this runtime complexity, normal string
  // concatenation cannot be used; instead a StringBuilder should be
  // employed.
  public String toString(){
	  String board = "";
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
	  //loops through entire board to add to stringbuilder
	  for (int i=0; i<=maxRow-minRow;i++){
		  //Adds new line and row number
		  builder.append("\n");
		  builder.append(String.format("%1$3d |",minRow+i));
		  
		  //Loop to put in board elements
		  for (int j=0; j<=maxCol-minCol;j++){
			  T elem = rows.get(i).get(j);
			  if (elem != null)
				  builder.append(String.format("%1$3s|", elem));
			  else
				  builder.append(String.format("%1$3s|", fillElem));
		  }
		  
		  //puts in divider line
		  builder.append("\n    +");
		  for (int k=0; k<maxCol-minCol+1;k++){
			  builder.append("---+");
		  }
	  }
	  board = builder.toString()+"\n";
	  
	  //put row numbers vertically					-done
	  
	  //then all in one loop:
	  //put column numbers on top row				-done
	  //format each element as it is 
	  //being put into the array					-done
	  //put in between lines in as each 
	  //element is added							-done
	  return board;
  }
  	
}