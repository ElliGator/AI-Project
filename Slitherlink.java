import java.awt.BorderLayout;
import java.util.Scanner;

import javax.swing.*;

/*File grid format is:
 * number of rows i.e. 5
 * number of columns i.e. 5
 * row, column, value
 * row, column, value
 * ...*/
public class Slitherlink{
	
	public static void main(String[] args){
		SlitherBoard app = new SlitherBoard();
		JFrame mainWin = new JFrame("AI - Slitherlink Project");
		mainWin.add(app);
		
		/*Window settings and display*/
		mainWin.pack();
		mainWin.setSize(300,300);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.setVisible(true);
		
	}
}
