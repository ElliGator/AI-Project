import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;



public class SlitherBoard extends JPanel{
	Scanner sc;
	/***Array of square objects containing values for the puzzle**/
	Square[][] squares;
	/***Number of rows of board**/
	int r;
	/***Number of columns of board**/
	int c;
	/***Vertical bar for drawing '|' **/
	String vertical;
	/***Horizontal bar for drawing '-' **/
	String horizontal;
	String plus;
	/***Helps in creating the grid from '+' string and Square objects**/
	StringBuilder sb;
	
	/*Drawing Components*/
	/***Panel for selecting game file and starting game**/
	JPanel selectionPanel;
	JTextField gridPath;
	/***Get Grid Button**/
	JButton ggBtn;
	boolean btnPressed = false;
	
	/*Fields for user moves*/
	JTextField rowMove;
	JTextField colMove;
	JTextField sideMove;
	/***Make move button**/
	JButton moveBtn;
	/***Remove move button**/
	JButton rmBtn;
	/***Check for solution button**/
	JButton solBtn;
	/***Panel where the grid will be displayed**/
	JPanel gridPanel;
	
	/*Grid Components*/
	int[][] gridArray;
	int rowSize;
	int colSize;

	/***Used to check if the submitted solution is a true solution**/
	ArrayList<Edge> sol = new ArrayList<Edge>();
	
	class makeGrid implements ActionListener{
		public void actionPerformed(ActionEvent evnt){
			try {
				btnPressed = true;
				if(gridPath.getText().isEmpty())
					JOptionPane.showMessageDialog(null,"Enter a file name");
				else{
					File f = new File(gridPath.getText());
					sc = new Scanner(f);
					sc.useDelimiter("[(]|[)]|\\s+|,");
					rowSize = sc.nextInt();
					colSize = sc.nextInt();
					gridArray = new int[rowSize][colSize];
					
					/**Initialize grid elements have a null value of 1000*/
					for(int i = 0; i < rowSize; i++){
						for(int j = 0; j < colSize; j++){
							gridArray[i][j] = 1000;
						}
					}
					
					/**Overwrite array with new data for certain grid locations*/
					while(sc.hasNextLine()){
						int row = sc.nextInt();
						int col = sc.nextInt();
						int val = sc.nextInt();
						gridArray[row][col] = val;
					}
					sc.close();
				}
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			makeSquares(rowSize,colSize,gridArray);
		}
	}
	
	class makeMoves implements ActionListener{
		public void actionPerformed(ActionEvent evnt){
			if(rowMove.getText().isEmpty() || colMove.getText().isEmpty() || sideMove.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Enter Move");
			else
				userMoves_Make(rowMove.getText(),colMove.getText(),sideMove.getText());
		}
	}
	
	class removeMoves implements ActionListener{
		public void actionPerformed(ActionEvent evnt){
			if(rowMove.getText().isEmpty() || colMove.getText().isEmpty() || sideMove.getText().isEmpty())
				JOptionPane.showMessageDialog(null,"Enter move to undo");
			else
				userMoves_Remove(rowMove.getText(),colMove.getText(),sideMove.getText());
		}
	}
	
	class checkSolution implements ActionListener{
		public void actionPerformed(ActionEvent evnt){
			//Check if squares with numbers have correct number of lines surrounding it
			int counter = 0;
			boolean noErrors = false;
			boolean brokeout = false;
			outerloop1:
				for(int a = 0; a < rowSize; a++){
					for(int b = 0; b < colSize; b++){
						if(squares[a][b].tSide == true)
							counter++;
						if(squares[a][b].rSide == true)
							counter++;
						if(squares[a][b].bSide == true)
							counter++;
						if(squares[a][b].lSide == true)
							counter++;
						
						if(counter > squares[a][b].getSqValue() && squares[a][b].getSqValue() != 1000){
							JOptionPane.showMessageDialog(null,"There are numbers that have an incorrect number of lines");
							noErrors = false;
							brokeout = true;
							break outerloop1;
						}
						else if(counter < squares[a][b].getSqValue() && squares[a][b].getSqValue() != 1000){
							JOptionPane.showMessageDialog(null,"There are numbers that have an incorrect number of lines");
							noErrors = false;
							brokeout = true;
							break outerloop1;
						}
						else if(counter == squares[a][b].getSqValue()){
							boolean err = errorChecking(true, a, b);
							if(err == false){
								noErrors = false;
								JOptionPane.showMessageDialog(null,"Illegal Move");
								brokeout = true;
								break outerloop1;
							}
							else
								noErrors = true;
						}
						counter = 0;
					}
				}
			if(brokeout != true){
				boolean solution = isSolution();
				if(noErrors && solution)
					JOptionPane.showMessageDialog(null,"Congratulations! You solved the puzzle");
				else if(solution == false)
					JOptionPane.showMessageDialog(null,"Not a solution");
			}
		}
	}
	
	public SlitherBoard(){
		r = 0;
		c = 0;
		makeGameWindow();
		
		/*Drawing the grid*/
		gridPanel = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				plus = "+ ";
				horizontal = "-";
				vertical = "|";
				if(btnPressed){
					drawString(g, createGrid(plus, horizontal, vertical), 25, 25);
				}
			}
		};
		add(gridPanel,BorderLayout.CENTER);
	}
	
	public void makeSquares(int rows, int cols, int[][] a){
		squares = new Square[rows][cols];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				squares[i][j] = new Square(rows,cols,a[i][j],i,j,false,false,false,false);
			}
		}
		repaint();
	}
	
	public void makeGameWindow(){
		/*Game file selection settings and listeners*/
		selectionPanel = new JPanel(new FlowLayout());
		selectionPanel.setPreferredSize(new Dimension(100,50));
		
		gridPath = new JTextField("2x2.txt");
		gridPath.setPreferredSize(new Dimension(75,24));
		ggBtn = new JButton("Get Grid");
		ggBtn.addActionListener(new makeGrid());
		
		rowMove = new JTextField("0");
		rowMove.setPreferredSize(new Dimension(75,24));
		colMove = new JTextField("0");
		colMove.setPreferredSize(new Dimension(75,24));
		sideMove = new JTextField("L");
		sideMove.setPreferredSize(new Dimension(75,24));
		moveBtn = new JButton("Make Move");
		moveBtn.addActionListener(new makeMoves());
		
		rmBtn = new JButton("Remove");
		rmBtn.addActionListener(new removeMoves());
		
		solBtn = new JButton("Done");
		solBtn.addActionListener(new checkSolution());
		
		/*Container Content additions*/
		selectionPanel.add(gridPath);
		selectionPanel.add(ggBtn);
		selectionPanel.add(rowMove);
		selectionPanel.add(colMove);
		selectionPanel.add(sideMove);
		selectionPanel.add(moveBtn);
		selectionPanel.add(rmBtn);
		selectionPanel.add(solBtn);
		setLayout(new BorderLayout());
		add(selectionPanel,BorderLayout.EAST);
	}
	
	public String createGrid(String p, String h, String v){
		sb = new StringBuilder();
		int tempVal;
		int i;
		int j;
		int k;

		/*'i' makes rows of grid*/
		for(i=0; i < rowSize; i++){
			/*'j' makes columns of grid*/
			for(j=0; j < colSize+1; j++){
				sb.append(p);
				if(i < rowSize && j < colSize){
					if(squares[i][j].getTop() == true)
						sb.append(h);
				}
				
			}
			sb.append("\n");
			
			
			if(i < rowSize){
				/*Inner loop for grid numbers*/
				if(squares[i][0].getLeft() == true)
					sb.append(v);
				for(k = 0; k < colSize; k++){
					tempVal = squares[i][k].getSqValue();					
					
					if(tempVal == 1000)
						sb.append("    ");
					else
						sb.append(" " + tempVal + " ");
					/*Try and find relationship between right and left side of squares*/
					if(k < colSize-1){
						if(squares[i][k].getRight() == true && squares[i][k+1].getLeft() == true)
							sb.append(v);
					}
				}
				if(squares[i][colSize-1].getRight() == true)
					sb.append(v);
				sb.append("\n");
			}
					
		}
		/*Make last row of grid pluses and control rowSize-1 bottom side*/
		for(int b = 0; b < rowSize+1; b++){
			sb.append(p);
			if(b < rowSize){
				if(squares[rowSize-1][b].getBottom() == true)
					sb.append(h);
			}
		}
		return sb.toString();
	}
	
	private void drawString(Graphics g, String s, int x, int y){
		for (String line : s.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

	public void userMoves_Make(String rMove, String cMove, String sMove){
		int uRow = Integer.parseInt(rMove);
		int uCol = Integer.parseInt(cMove);
		
		if(sMove.equals("T") || sMove.equals("t")){
			squares[uRow][uCol].tSide = true;
			if(sol.contains(squares[uRow][uCol].tEdge) == false)
				sol.add(squares[uRow][uCol].tEdge);
			repaint();
		}
		else if(sMove.equals("R") || sMove.equals("r")){
			if(uCol + 1 >= colSize){
				squares[uRow][uCol].rSide = true;
				if(sol.contains(squares[uRow][uCol].rEdge) == false)
					sol.add(squares[uRow][uCol].rEdge);
			}
			else{
				squares[uRow][uCol].rSide = true;
				squares[uRow][uCol+1].lSide = true;
				if(sol.contains(squares[uRow][uCol].rEdge) == false){
					sol.add(squares[uRow][uCol].rEdge);
				}
			}
			repaint();
		}
		else if(sMove.equals("B") || sMove.equals("b")){
			if(uRow + 1 >= rowSize){
				squares[uRow][uCol].bSide = true;
				if(sol.contains(squares[uRow][uCol].bEdge) == false)
					sol.add(squares[uRow][uCol].bEdge);
			}
			else{
				squares[uRow+1][uCol].tSide = true;
				squares[uRow][uCol].bSide = true;
				if(sol.contains(squares[uRow+1][uCol].tEdge) == false){
					sol.add(squares[uRow+1][uCol].tEdge);
				}
			}
			repaint();
		}
		else if(sMove.equals("L") || sMove.equals("l")){
			if(uCol == 0){
				squares[uRow][uCol].lSide = true;
				if(sol.contains(squares[uRow][uCol].lEdge) == false)
					sol.add(squares[uRow][uCol].lEdge);
			}
			else{
				squares[uRow][uCol].lSide = true;
				squares[uRow][uCol-1].rSide = true;
				if(sol.contains(squares[uRow][uCol].lEdge) == false){
					sol.add(squares[uRow][uCol].lEdge);
				}
			}
			repaint();
		}
		else{
			JOptionPane.showMessageDialog(null,"Invalid Move");
		}
	}
	
	public void userMoves_Remove(String rMove, String cMove, String sMove){
		int uRow = Integer.parseInt(rMove);
		int uCol = Integer.parseInt(cMove);
		
		if(sMove.equals("T" ) || sMove.equals("t")){
			squares[uRow][uCol].tSide = false;
			sol.remove(squares[uRow][uCol].tEdge);
			repaint();
		}
		else if(sMove.equals("R") || sMove.equals("r")){
			if(uCol == rowSize-1){
				squares[uRow][uCol].rSide = false;
				sol.remove(squares[uRow][uCol].rEdge);
			}
			else{
				squares[uRow][uCol].rSide = false;
				squares[uRow][uCol+1].lSide = false;
				sol.remove(squares[uRow][uCol].rEdge);
				}
			repaint();
		}
		else if(sMove.equals("B") || sMove.equals("b")){
			if(uRow + 1 >= rowSize){
				squares[uRow][uCol].bSide = false;
				sol.remove(squares[uRow][uCol].bEdge);
			}
			else{
				squares[uRow+1][uCol].tSide = false;
				squares[uRow][uCol].bSide = false;
				sol.remove(squares[uRow+1][uCol].tEdge);
				sol.remove(squares[uRow][uCol].bEdge);
			}
			repaint();
		}
		else if(sMove.equals("L") || sMove.equals("l")){
			if(uCol == 0){
				squares[uRow][uCol].lSide = false;
				sol.remove(squares[uRow][uCol].lEdge);
			}
			else{
				squares[uRow][uCol].lSide = false;
				squares[uRow][uCol-1].rSide = false;
				sol.remove(squares[uRow][uCol].lEdge);
			}
			repaint();
		}
		else{
			JOptionPane.showMessageDialog(null,"Invalid Move");
		}
	}
	
	public boolean errorChecking(boolean s, int i, int j){
		/*    |         |            
		 *    ---     ---      __|__     __ __
		 *    |         |                  |
		 */
		
		if((i+1 != rowSize) && (j+1 != colSize)){
			if(squares[i][j].lSide == true && squares[i][j].bSide == true && squares[i+1][j].lSide == true)
				return false;
				
			else if(squares[i][j].rSide == true && squares[i][j].bSide == true && squares[i+1][j].rSide == true)
				return false;
			
			else if(squares[i][j].bSide == true && squares[i][j].rSide == true && squares[i][j+1].bSide == true)
				return false;
			
			else if(squares[i][j].bSide == true && squares[i+1][j].rSide == true && squares[i][j+1].bSide == true)
				return false;
		}
		else if((i == rowSize-1 || j == colSize-1) && i+1 < colSize){
			if(squares[i][j].rSide == true && squares[i][j].bSide == true && squares[i+1][j].rSide == true)
				return false;
		}
		else 
			return true;
		
		return true;
	}
	
	public boolean isSolution(){
		int i;
		/*Save points of the first edge that is in the array*/
		Point begin = sol.get(0).pt1;
		Point temp = sol.get(0).pt2;
		/*Remove this edge from the arraylist*/
		sol.remove(0);
		
		/*If the beginning edge point does not exist it returns false*/
		/*Iterate until the arraylist is empty*/
		out:
		while(!sol.isEmpty()){
			for(i = 0; i < sol.size(); i++){
				/*If the the first point of a edge(i) matches pt2 of the first edge, save this new point as temp 
				 * and remove it from the arraylist
				 */
				if(compare(temp,sol.get(i).pt2) || compare(begin, sol.get(i).pt1) || compare(temp,sol.get(i).pt1) || compare(begin, sol.get(i).pt2)){
					temp = sol.get(i).pt2;
					begin = sol.get(i).pt1;
					sol.remove(i);
				}
				if(sol.size() == 0)
					break out;
			}
		}
		if(sol.isEmpty())
			return true;
		
		return false;
	}
	
	public boolean compare(Point p1, Point p2){
		if(p1.x == p2.x && p1.y == p2.y)
			return true;
		return false;
	}
}