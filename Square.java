
public class Square {

	private int sqValue;
	boolean tSide;
	boolean rSide;
	boolean bSide;
	boolean lSide;
	
	private int row;
	private int col;
	
	public Square(int numR, int numC, int val, boolean top, boolean right, boolean bottom, boolean left){
		this.row = numR;
		this.col = numC;
		this.sqValue = val;
		
		this.tSide = top;
		this.rSide = right;
		this.bSide = bottom;
		this.lSide = left;
	}

	public int getSqValue() {
		return sqValue;
	}

	public void setSqValue(int sqValue) {
		this.sqValue = sqValue;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public void setTop(boolean t){
		this.tSide = t;
	}
	
	public void setRight(boolean r){
		this.rSide = r;
	}
	
	public void setBottom(boolean b){
		this.bSide = b;
	}
	
	public void setLeft(boolean l){
		this.lSide = l;
	}
	
	public boolean getTop(){
		return this.tSide;
	}
	
	public boolean getRight(){
		return this.rSide;
	}
	
	public boolean getBottom(){
		return this.bSide;
	}
	
	public boolean getLeft(){
		return this.lSide;
	}
}
