package org.owwlo.Blokus.Model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.VerifyMove;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Model.BlokusLogic.BoardListener;
import org.owwlo.Blokus.Model.BlokusLogic.MovablePiece;

public class BlokusLogicTest {
	private BlokusLogic bl;

	// private VerifyMove move(
	// int lastMovePlayerId, Map<String, Object> lastState, List<Operation>
	// lastMove) {
	// return new VerifyMove(wId, playersInfo, emptyState,
	// lastState, lastMove, lastMovePlayerId);
	// }

	@Before
	public void setUp() throws Exception {
		bl = new BlokusLogic(7, 7);
	}

	@Test
	public void testInitialMove() {
		MovablePiece mp1 = new MovablePiece(0);
		mp1.addPoint(new Point(0, 0));
		mp1.addPoint(new Point(1, 0));
		mp1.addPoint(new Point(2, 0));
		mp1.addPoint(new Point(2, 1));
		mp1.setPosition(new Point(0, 0));
		assertTrue(bl.canFit(mp1, true));
	}

	@Test
	public void testInitialMove_OtherPalyer() {
		fail();
	}

	@Test
	public void testInitialMoveByWrongPlayer() {
		fail();
	}

	@Test
	public void testInitialMoveFromNonEmptyState() {
		fail();
	}

	@Test
	public void testMoveByWrongPlayer() {
		fail();
	}

	@Test
	public void testPlayerCheating() {
		fail();
	}

	@Test
	public void testCorrectMove() {
		fail();
	}

	@Test
	public void testCorrectMove_DiffColorEdgeConnected() {
		fail();
	}

	@Test
	public void testWin() {
		fail();
	}

	@Test
	public void testIllegalMove_NotConnected() {
		fail();
	}

	@Test
	public void testIllegalMove_EdgeConnected() {
		fail();
	}

	@Test
	public void testIllegalMove_OutOfEdge() {
		fail();
	}

	@Test
	public void testIllegalMove_WrongPlayer() {
		fail();
	}

	@Test
	public void testIllegalMove_Overlay() {
		fail();
	}

	@Test
	public void testPieceRotation() {
		fail();
	}

	@Test
	public void testPieceSelectionCorrect() {
		fail();
	}

	@Test
	public void testPieceSelectionIllegal() {
		fail();
	}

	@Test
	public void testLoadPieceFromString() {
		Piece p = new Piece(Constants.BlokusPiece.pieceStringList.get(0));
		p.printPiece();
		assertTrue(p.getWidth() == 1 && p.getHeight() == 1
				&& p.getBitmap()[0][0] == true);
	}

	@Test
	public void testBoardListener() {
		BoardListener bll = new BoardListener() {
			@Override
			public void OnBoardBitmapChanged(int[][] bmp) {
			}
		};
		bl.addBoardListener(bll);
		bl.removeBoardListener(bll);
	}

	@Test
	public void testApplyMove() {
		fail();
	}
}
