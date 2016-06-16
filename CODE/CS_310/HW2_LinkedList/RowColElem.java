// Simple class to encapsulate a triple row,col,element in boards.
// The class is immutable
public class RowColElem<T>{
  private int row;
  private int col;
  private T elem;

  // Create a RowColElem with the parameter parts
  public RowColElem(int r, int c, T e){
    this.row = r;
    this.col = c;
    this.elem = e;
  }

  // Return the row
  public int getRow(){
    return this.row;
  }

  // Return the column
  public int getCol(){
    return this.col;
  }

  // Return the element
  public T getElem(){
    return this.elem;
  }

  // Return a pretty string version of the triple formated as
  // (row,col,elem)
  public String toString(){
    return String.format("(%d,%d,%s)",row,col,elem);
  }

  @SuppressWarnings("unchecked")
  // Perform a deep equality check between this RowColElem and another
  // object
  public boolean equals(Object other){
    if(other == null || !(other instanceof RowColElem)){
      return false;
    }
    RowColElem<T> that = (RowColElem<T>) other;
    return
      this.row == that.row &&
      this.col == that.col &&
      this.elem.equals(that.elem);
  }
}
