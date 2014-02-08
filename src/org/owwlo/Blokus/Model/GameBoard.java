package org.owwlo.Blokus.Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Utils;

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
		boolean pieceBitmap[][] = piece.getBitmap();
		int pieceWidth = piece.getWidth();
		int pieceHeight = piece.getHeight();
		List<Point> piecePointList = piece.getPointList();
		boolean isCornerOccupy = false;
		if(point.x < 0 || point.y < 0) return false;
		for(Point p : piecePointList) {
			if(point.x + p.x >= xLen) return false;
			if(point.y + p.y >= yLen) return false;
			if(boardBitmp[point.y + p.y][point.x + p.y] != Constants.NO_OCCUPY_POINT_VALUE)
				return false;
			if(check4DirectionOccupy(point.x + p.x, point.y + p.y)) return false;
			if(check4CornerOccupy(point.x + p.x, point.y + p.y))
				isCornerOccupy = true;
		}
		return true;
	}

	private boolean check4CornerOccupy(int x, int y) {
		return false;
	}

	private boolean check4DirectionOccupy(int x, int y) {
		if(Utils.Range(x+1, 0, xLen)) {
			if(boardBitmp[y][x+1] != Constants.NO_OCCUPY_POINT_VALUE)
				return true;
		}
		if(Utils.Range(x-1, 0, xLen)) {
			if(boardBitmp[y][x-1] != Constants.NO_OCCUPY_POINT_VALUE)
				return true;
		}
		if(Utils.Range(y+1, 0, yLen)) {
			if(boardBitmp[y+1][x] != Constants.NO_OCCUPY_POINT_VALUE)
				return true;
		}
		if(Utils.Range(y-1, 0, yLen)) {
			if(boardBitmp[y-1][x] != Constants.NO_OCCUPY_POINT_VALUE)
				return true;
		}
		return false;
	}

	public boolean canFit(MovablePiece mp) {
		return canFit(mp, mp.getPosition());
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

		public final Point getPosition() {
			return position;
		}
	}
}
