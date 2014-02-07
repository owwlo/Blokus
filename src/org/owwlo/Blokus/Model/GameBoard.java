package org.owwlo.Blokus.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * A Board item used in Blokus. 
 * @author owwlo
 */

public class GameBoard {
	private List<Piece> pieceList = new ArrayList<Piece>();
	private int xLen = 0, yLen = 0;
	
	/*
	 *  Because we need to know which point belongs to who, 
	 *  the bitmap need to be form with INT other than BOOLEAN.
	 */
	private int boardBitmp[][] = null;
	
	/**
	 * Create a 0x0 GameBoard.
	 */
	public GameBoard() {	
	}
	
	/**
	 * Create a x*y GameBoard.
	 */
	public GameBoard(int x, int y) {
		xLen = x;
		yLen = y;
	}
	
	/**
	 * Judge whether the Piece can fit into the board.
	 * @param p The piece to be test.
	 * @return If it is fit.
	 */
	//TODO code it.
	public boolean canFit(Piece p) {
		return false;
	}
}
