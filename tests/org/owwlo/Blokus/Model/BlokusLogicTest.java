
package org.owwlo.Blokus.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.Set;
import org.cheat.client.GameApi.VerifyMove;
import org.cheat.client.GameApi.VerifyMoveDone;
import org.junit.Before;
import org.junit.Test;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Model.BlokusLogic.BoardListener;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class BlokusLogicTest {
    private BlokusLogic bl;

    private final String bitlistForCommonValidState = "0,0,1 0,5,2 0,6,2";

    private List<Integer> usedPieceForUser1 = ImmutableList.<Integer> of(0);
    private List<Integer> usedPieceForUser2 = ImmutableList.<Integer> of(1);

    /**
     * A map record each piece players used in the game. Used as a help to
     * verify move.
     */
    private final Map<String, List<Integer>> commonValidUsedPieceMap = ImmutableMap
            .<String, List<Integer>> builder().put("1", usedPieceForUser1)
            .put("2", usedPieceForUser2).build();
    /*
     * A valid state with the following fields: turn, user-list, pieceFor1,
     * pieceFor2, user-used-pieces
     */
    private Map<String, Object> commonValidState = ImmutableMap
            .<String, Object> builder().put(Constants.JSON_TURN, 2)
            .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
            .put(Constants.getPieceForUserString(1), getIndicesInRange(1, 20))
            .put(Constants.getPieceForUserString(2), getIndicesInRange(0, 19))
            .put(Constants.JSON_BITMAP, bitlistForCommonValidState)
            .put(Constants.JSON_USER_USED_PIECES, commonValidUsedPieceMap)
            .build();

    private final Map<String, Object> info1 = ImmutableMap.<String, Object> of(
            Constants.JSON_USER_ID, 1);
    private final Map<String, Object> info2 = ImmutableMap.<String, Object> of(
            Constants.JSON_USER_ID, 2);
    private final List<Map<String, Object>> playersInfo = ImmutableList.of(
            info1, info2);

    @Before
    public void setUp() throws Exception {
        bl = new BlokusLogic();
    }

    @Test
    public void testInitialMove() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of())
                                .put("2", ImmutableList.<Integer> of())
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(), "2",
                                ImmutableList.<Integer> of(0))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,0"));
        assertOK(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testInitialMoveOtherPalyer() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of())
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,5"));
        assertOK(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testInitialMoveFromNonEmptyState() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 2), new Set(
                        Constants.JSON_POINT, "1,4"));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testMoveByWrongPlayer() {
        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,0"));
        assertHacker(createVerifyMove(1, playersInfo, null, commonValidState,
                operationsFor2, 2));
    }

    @Test
    public void testPlayerCheating() {
        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,0"));
        assertHacker(createVerifyMove(1, playersInfo, null, commonValidState,
                operationsFor2, 2));
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
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "1,4"));
        assertOK(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testCorrectMoveDiffColorEdgeConnected() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "1,3,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "1,3,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "1,4"));
        assertOK(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testWin() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(1))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, true));
        assertOK(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testIllegalMoveNotConnected() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,10,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "1,10"));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testIllegalMoveEdgeConnected() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 0,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,4"));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testIllegalMoveOutOfEdge() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of())
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,9999"));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testIllegalMoveWrongPlayer() {
        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,0"));
        assertHacker(createVerifyMove(1, playersInfo, null, commonValidState,
                operationsFor2, 2));
    }

    @Test
    public void testIllegalMoveOverlay() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "0,0"));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    /**
     * @author Yoav Zibin
     */
    private void assertHacker(VerifyMove verifyMove) {
        VerifyMoveDone verifyDone = BlokusLogic.verify(verifyMove);
        assertEquals(verifyMove.getLastMovePlayerId(),
                verifyDone.getHackerPlayerId());
    }

    /**
     * @author Yoav Zibin
     */
    private void assertOK(VerifyMove verifyMove) {
        VerifyMoveDone verifyDone = BlokusLogic.verify(verifyMove);
        assertEquals(verifyDone.getHackerPlayerId(), 0);
    }

    @Test
    public void testPieceRotation() {
        Piece p = new Piece(Constants.BlokusPiece.pieceStringList.get(0));
        p.rotate();
    }

    @Test
    public void testPieceSelectionCorrect() {
        assertTrue(BlokusLogic.isPieceSelectionLegal(1, 1,
                commonValidUsedPieceMap));
    }

    @Test
    public void testPieceSelectionIllegal() {
        assertFalse(BlokusLogic.isPieceSelectionLegal(2, 1,
                commonValidUsedPieceMap));
    }

    @Test
    public void testLoadPieceFromString() {
        Piece p = new Piece(Constants.BlokusPiece.pieceStringList.get(0));
        p.printPiece();
        assertTrue(p.getWidth() == 1 && p.getHeight() == 1
                && p.getBitmap()[0][0]);
    }

    @Test
    public void testBoardListener() {
        BoardListener bll = new BoardListener() {
            @Override
            public void onBoardBitmapChanged(int[][] bmp) {
            }
        };
        bl.addBoardListener(bll);
        bl.removeBoardListener(bll);
    }

    @Test
    public void testWrongPlayer() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 2), new Set(Constants.JSON_PASS, false),
                new Set(Constants.JSON_USE_PIECE, 0), new Set(
                        Constants.JSON_POINT, "1,4"));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }

    @Test
    public void testOperationWrong() {
        List<Map<String, Object>> playersInfo = ImmutableList.of(
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 1)),
                (Map<String, Object>) (ImmutableMap.<String, Object> of(
                        Constants.JSON_USER_ID, 2)));

        Map<String, Object> lastState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 2)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        Map<String, Object> currentState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2 1,4,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> of("1",
                                ImmutableList.<Integer> of(0), "2",
                                ImmutableList.<Integer> of(0, 2))).build();

        List<Operation> operationsFor2 = ImmutableList.<Operation> of(new Set(
                Constants.JSON_TURN, 1));
        assertHacker(createVerifyMove(1, playersInfo,
                currentState, lastState, operationsFor2, 2));
    }
}
