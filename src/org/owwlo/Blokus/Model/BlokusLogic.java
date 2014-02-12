package org.owwlo.Blokus.Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cheat.client.GameApi.Delete;
import org.cheat.client.GameApi.EndGame;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.Set;
import org.cheat.client.GameApi.VerifyMove;
import org.cheat.client.GameApi.VerifyMoveDone;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Constants.BlokusPiece;
import org.owwlo.Blokus.Utils;

/**
 * A Board item used in Blokus.
 * 
 * @author owwlo
 */

public class BlokusLogic {
	private static final String TURN = "turn";

	private List<MovablePiece> pieceList = new ArrayList<MovablePiece>();
	private List<BoardListener> boardLinsterList = new LinkedList<BoardListener>();
	private List<Piece> pieceBowl = new ArrayList<Piece>() {
		{
			for (String str : BlokusPiece.pieceStringList) {
				this.add(new Piece(str));
			}
			if (Constants.DEBUG) {
				System.out.println("Count of Pieces loaded: " + this.size());
			}
		}
	};

	private int xLen = 0, yLen = 0;

	/*
	 * Because we need to know which point belongs to who, the bitmap need to be
	 * form with INT other than BOOLEAN.
	 */
	private int boardBitmp[][] = null;

	/**
	 * Create a 0x0 GameBoard.
	 */
	public BlokusLogic() {
	}

	/**
	 * Attention: This method cost much because it will generate a new bitmap
	 * for the board.
	 */
	public void resetBoardSize(int x, int y) {
	}

	/**
	 * Create a x*y GameBoard.
	 */
	public BlokusLogic(int x, int y) {
		xLen = x;
		yLen = y;
		boardBitmp = new int[y][x];
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				boardBitmp[i][j] = Constants.NO_OCCUPY_POINT_VALUE;
			}
		}
		notifyOnBitmapChanged(boardBitmp);
	}

	/**
	 * Judge whether the Piece can fit into the board.
	 * 
	 * @param p
	 *            The piece to be test.
	 * @param isInitial
	 *            Default false.
	 * @return If it is fit.
	 */
	public boolean canFit(Piece piece, Point point, int owner, boolean isInitial) {
		List<Point> piecePointList = piece.getPointList();
		boolean isCornerOccupy = false;
		if (point.x < 0 || point.y < 0)
			return false;
		for (Point p : piecePointList) {
			if (point.x + p.x >= xLen)
				return false;
			if (point.y + p.y >= yLen)
				return false;
			if (boardBitmp[point.y + p.y][point.x + p.y] != Constants.NO_OCCUPY_POINT_VALUE)
				return false;
			if (check4DirectionOccupy(point.x + p.x, point.y + p.y, owner))
				return false;
			if (!isInitial) {
				if (check4CornerOccupy(point.x + p.x, point.y + p.y, owner))
					isCornerOccupy = true;
			}
		}
		if (!isInitial) {
			if (!isCornerOccupy)
				return false;
		}
		return true;
	}

	public final int[][] getBitmap() {
		return boardBitmp;
	}

	private boolean check4CornerOccupy(int x, int y, int color) {
		if (Utils.Range(x + 1, 0, xLen)) {
			if (Utils.Range(y + 1, 0, yLen)) {
				if (boardBitmp[y + 1][x + 1] == color)
					return true;
			}
			if (Utils.Range(y - 1, 0, yLen)) {
				if (boardBitmp[y - 1][x + 1] == color)
					return true;
			}
		}
		if (Utils.Range(x - 1, 0, xLen)) {
			if (Utils.Range(y + 1, 0, yLen)) {
				if (boardBitmp[y + 1][x - 1] == color)
					return true;
			}
			if (Utils.Range(y - 1, 0, yLen)) {
				if (boardBitmp[y - 1][x - 1] == color)
					return true;
			}
		}
		return false;
	}

	private boolean check4DirectionOccupy(int x, int y, int color) {
		if (Utils.Range(x + 1, 0, xLen)) {
			if (boardBitmp[y][x + 1] == color)
				return true;
		}
		if (Utils.Range(x - 1, 0, xLen)) {
			if (boardBitmp[y][x - 1] == color)
				return true;
		}
		if (Utils.Range(y + 1, 0, yLen)) {
			if (boardBitmp[y + 1][x] == color)
				return true;
		}
		if (Utils.Range(y - 1, 0, yLen)) {
			if (boardBitmp[y - 1][x] == color)
				return true;
		}
		return false;
	}

	public boolean canFit(MovablePiece mp) {
		return canFit(mp, mp.getPosition(), mp.getOwnerId(), false);
	}

	public boolean canFit(MovablePiece mp, boolean isInitial) {
		return canFit(mp, mp.getPosition(), mp.getOwnerId(), isInitial);
	}

	public boolean addPiece(MovablePiece mp) {
		return addPiece(mp, false);
	}

	public boolean addPiece(MovablePiece mp, boolean isInitial) {
		if (canFit(mp, isInitial)) {
			pieceList.add(mp);
			List<Point> pointList = mp.getPointList();
			Point piecePosition = mp.getPosition();
			for (Point p : pointList) {
				boardBitmp[piecePosition.y + p.y][piecePosition.x + p.x] = mp
						.getOwnerId();
			}
			notifyOnBitmapChanged(boardBitmp);
			if (Constants.DEBUG) {
				printBoard();
			}
			return true;
		}
		return false;
	}

	public static class MovablePiece extends Piece {
		private Point position;
		private int ownerId;

		/**
		 * "ownid" is a must because we need to know this piece belongs to whom.
		 * 
		 * @param ownid
		 */
		public MovablePiece(int ownid) {
			ownerId = ownid;
			position = new Point();
		}

		public MovablePiece(int ownid, String data, Point point) {
			super(data);
			ownerId = ownid;
			position = point;
		}

		public void setPosition(Point p) {
			position = p;
		}

		public final Point getPosition() {
			return position;
		}

		public final int getOwnerId() {
			return ownerId;
		}

		@Override
		public void printPiece() {
			System.out.println("PieceBitMapForOwner: " + ownerId);
			super.printPiece();
		}
	}

	public void printBoard() {
		System.out.println("********BoardBitMap*********");
		for (int i = 0; i < yLen; i++) {
			for (int j = 0; j < xLen; j++) {
				System.out.print(boardBitmp[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("****************************");
		System.out.println();
	}

	/**
	 * Listener for observing if anyone has made a move.
	 * 
	 * @author owwlo
	 */
	public static interface BoardListener {
		public void OnBoardBitmapChanged(final int bmp[][]);
	}

	public void addBoardListener(BoardListener listener) {
		boardLinsterList.add(listener);
	}

	public void removeBoardListener(BoardListener listener) {
		boardLinsterList.remove(listener);
	}

	public void notifyOnBitmapChanged(final int bmp[][]) {
		for (BoardListener listener : boardLinsterList) {
			listener.OnBoardBitmapChanged(bmp);
		}
	}

	public final List<Piece> getAllPiece() {
		return pieceBowl;
	}

	public VerifyMoveDone verify(VerifyMove verifyMove) {
		try {
			checkMoveIsLegal(verifyMove);
			return new VerifyMoveDone();
		} catch (Exception e) {
			return new VerifyMoveDone(verifyMove.getLastMovePlayerId(),
					e.getMessage());
		}
	}

	private void checkMoveIsLegal(VerifyMove verifyMove) throws Exception {
		List<Operation> lastMove = verifyMove.getLastMove();
		Map<String, Object> lastState = verifyMove.getLastState();
		Map<String, Object> currentState = verifyMove.getState();
		for (Operation op : lastMove) {
			if (op instanceof Set) {
				@SuppressWarnings("unchecked")
				Map<String, String> value = (Map<String, String>) ((Set) op)
						.getValue();
				Piece usePiece = Piece.getPiece(Integer.parseInt(value
						.get("Piece_Id")));
				String ords[] = value.get("point").split(",");
				Point usePoint = new Point(Integer.parseInt(ords[0]),
						Integer.parseInt(ords[1]));

				boolean isInitialMoveForPlayer = checkInitialMoveForPlayer(verifyMove
						.getLastMovePlayerId());
				canFit(usePiece, usePoint, verifyMove.getLastMovePlayerId(),
						isInitialMoveForPlayer);
			} else if (op instanceof EndGame) {
				// TODO when will receive EndGame message??
			} else {
				// No other operation is needed. So, receiving other operation
				// means there being a hacker.
				throw new Exception("Hacker!");
			}
		}
	}

	private boolean checkInitialMoveForPlayer(int lastMovePlayerId) {
		for (int i = 0; i < yLen; i++) {
			for (int j = 0; j < xLen; j++) {
				if (boardBitmp[i][j] == lastMovePlayerId) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		BlokusLogic gb = new BlokusLogic(5, 5);
		MovablePiece mp1 = new MovablePiece(0);
		mp1.addPoint(new Point(0, 0));
		mp1.addPoint(new Point(1, 0));
		mp1.addPoint(new Point(2, 0));
		mp1.addPoint(new Point(2, 1));
		mp1.setPosition(new Point(0, 0));

		gb.addPiece(mp1, true);

		MovablePiece mp2 = new MovablePiece(0);
		mp2.addPoint(new Point(0, 0));
		mp2.addPoint(new Point(1, 0));
		mp2.addPoint(new Point(0, 1));
		mp2.addPoint(new Point(1, 1));
		mp2.setPosition(new Point(3, 2));

		System.out.println(gb.canFit(mp2));
	}
}
