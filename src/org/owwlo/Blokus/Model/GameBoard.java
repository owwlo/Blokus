package org.owwlo.Blokus.Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A Board item used in Blokus.
 * @author owwlo
 */

public class GameBoard {
	private List<MovablePiece> pieceList = new ArrayList<MovablePiece>();
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
	 * Attention: This method cost much because it will generate
	 * a new bitmap for the board.
	 */
	public void resetBoardSize(int x, int y) {
		throw new NotImplementedException();
	}

	/**
	 * Create a x*y GameBoard.
	 */
	public GameBoard(int x, int y) {
		xLen = x;
		yLen = y;
		boardBitmp = new int[y][x];
	}

	/**
	 * Judge whether the Piece can fit into the board.
	 * @param p The piece to be test.
	 * @return If it is fit.
	 */
	public boolean canFit(Piece piece, Point point) {
		return false;
	}

	public static class MovablePiece extends Piece {
		private Point position;

		public MovablePiece(){
			position = new Point();
		}

		public MovablePiece(String data, Point point){
			super(data);
			position = point;
		}

		public void setPosition(Point p) {
			position = p;
		}

		public Point getPosition() {
			return position;
		}
	}
}
