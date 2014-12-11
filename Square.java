import java.util.ArrayList;


public class Square {

	private int sqValue;
	boolean tSide;
	boolean rSide;
	boolean bSide;
	boolean lSide;
	
	private int row;
	private int col;
	
	Point v1;
	Point v2;
	Point v3;
	Point v4;
	
	Edge tEdge;
	Edge rEdge;
	Edge bEdge;
	Edge lEdge;
	
	public Square(int numR, int numC, int val, int i, int j, boolean top, boolean right, boolean bottom, boolean left){
		this.row = numR;
		this.col = numC;
		this.sqValue = val;
		
		this.tSide = top;
		this.rSide = right;
		this.bSide = bottom;
		this.lSide = left;
		
		v1 = new Point(i, j);
		v2 = new Point(i, j+1);
		v3 = new Point(i+1, j);
		v4 = new Point(i+1, j+1);
		
		setPts(v1, v2, v3, v4);
		
		lEdge = new Edge(v1,v3);
		tEdge = new Edge(v1,v2);
		rEdge = new Edge(v2,v4);
		bEdge = new Edge(v3,v4);
		
	}
	
	public void setPts(Point pt1, Point pt2, Point pt3, Point pt4){
		this.v1 = pt1;
		this.v2 = pt2;
		this.v3 = pt3;
		this.v4 = pt4;
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
