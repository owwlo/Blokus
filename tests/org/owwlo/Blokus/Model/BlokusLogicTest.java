package org.owwlo.Blokus.Model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.cheat.client.GameApi.Delete;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.Set;
import org.cheat.client.GameApi.SetVisibility;
import org.cheat.client.GameApi.Shuffle;
import org.cheat.client.GameApi.VerifyMove;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Model.BlokusLogic.BoardListener;
import org.owwlo.Blokus.Model.BlokusLogic.MovablePiece;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class BlokusLogicTest {
	private BlokusLogic bl;
	
	private final Map<String, Object> emptyState = ImmutableMap.<String, Object>of();

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

	/**
	 * @author Yoav Zibin
	 */
	List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		List<Integer> keys = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			keys.add(i);
		}
		return keys;
	}

	private VerifyMove createVerifyMove(int yourPlayerId,
			List<Map<String, Object>> playersInfo, Map<String, Object> state,
			Map<String, Object> lastState, List<Operation> lastMove,
			int lastMovePlayerId) {
		return new VerifyMove(yourPlayerId, playersInfo, state, lastState,
				lastMove, lastMovePlayerId);
	}

	@Test
	public void testCorrectMove() {
		  Map<String, Object> info1 = ImmutableMap.<String, Object>of(Constants.JSON_USER_ID, 1);
		  Map<String, Object> info2 = ImmutableMap.<String, Object>of(Constants.JSON_USER_ID, 2);
		  List<Map<String, Object>> playersInfo = ImmutableList.of(info1, info2);
		
		Map<String, Object> state = ImmutableMap
				.<String, Object> builder()
				.put(Constants.JSON_TURN, 2)
				.put(Constants.JSON_USER_LIST,
						ImmutableList.<Integer> builder().add(1, 2))
				.put(Constants.getPieceForUserString(1),
						getIndicesInRange(0, 20))
				.put(Constants.getPieceForUserString(2),
						getIndicesInRange(0, 19)).build();

		List<Operation> operations = ImmutableList.<Operation> of(new Set(
				Constants.JSON_TURN, 1), new Set("pass", false), new Set(
				"usePiece", 3), new Set("point", "0,0"));
		createVerifyMove(2, playersInfo, state, emptyState, operations, 1);
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
