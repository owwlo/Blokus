
package org.owwlo.Blokus;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.Map;

import org.cheat.client.GameApi.Container;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.SetTurn;
import org.cheat.client.GameApi.UpdateUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.owwlo.Blokus.BlokusPresenter.View;
import org.owwlo.Blokus.BlokusPresenter.ViewState;
import org.owwlo.Blokus.Model.BlokusLogic;
import org.owwlo.Blokus.Model.BlokusState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@RunWith(JUnit4.class)
public class BlokusPresenterTest {
    private final Map<String, Object> info1 = ImmutableMap.<String, Object> of(
            Constants.JSON_USER_ID, 1);
    private final Map<String, Object> info2 = ImmutableMap.<String, Object> of(
            Constants.JSON_USER_ID, 2);
    private final List<Map<String, Object>> playersInfo = ImmutableList.of(
            info1, info2);

    private final ImmutableMap<String, Object> emptyState = ImmutableMap.<String, Object> of();

    private BlokusPresenter blokusPresenter;
    private View mockView;
    private Container mockContainer;

    @Before
    public void runBefore() {
        mockView = Mockito.mock(View.class);
        mockContainer = Mockito.mock(Container.class);
        blokusPresenter = new BlokusPresenter(mockView, mockContainer);
        verify(mockView).setPresenter(blokusPresenter);
    }

    @After
    public void runAfter() {
        verifyNoMoreInteractions(mockContainer);
        verifyNoMoreInteractions(mockView);
    }

    @Test
    public void testEmptyStateForA() {
        blokusPresenter.updateUI(createUpdateUI(1, 0, emptyState));
        verify(mockContainer).sendMakeMove(
                BlokusLogic.getMoveInitial(ImmutableList.<Integer> of(1, 2)));
    }

    @Test
    public void testEmptyStateForB() {
        blokusPresenter.updateUI(createUpdateUI(2, 0, emptyState));
        verify(mockContainer).sendMakeMove(
                BlokusLogic.getMoveInitial(ImmutableList.<Integer> of(1, 2)));
    }

    /**
     * This part will fail because there is an 2D integer array being as a
     * parameter pass to view. Mockito can not treat the two as the same even if
     * the contents are the same.
     */
    @Test
    public void testEmptyMiddleStateForATurnOfA() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(1, 1, testState));
        verify(mockView).setViewState(ViewState.MAKE_MOVE);
        verify(mockView).setGameBoard(1, ImmutableList.<Integer> of(1, 2),
                BlokusState.getStateFromApiState(testState).getBitmapStr());
        verify(mockView).pickFromValidPiece(Utils.getIndicesInRange(1, 20));
    }

    @Test
    public void testEmptyMiddleStateForATurnOfB() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(1, 2, testState));
        verify(mockView).setViewState(ViewState.MAKE_MOVE);
        verify(mockView).setGameBoard(1, ImmutableList.<Integer> of(1, 2),
                BlokusState.getStateFromApiState(testState).getBitmapStr());
        verify(mockView).pickFromValidPiece(Utils.getIndicesInRange(1, 20));
    }

    @Test
    public void testEmptyMiddleStateForBTurnOfA() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(2, 1, testState));
        verify(mockView).setViewState(ViewState.VIEW_ONLY);
        verify(mockView).setGameBoard(2, ImmutableList.<Integer> of(1, 2),
                BlokusState.getStateFromApiState(testState).getBitmapStr());
    }

    @Test
    public void testEmptyMiddleStateForBTurnOfB() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of())
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(2, 2, testState));
        verify(mockView).setViewState(ViewState.VIEW_ONLY);
        verify(mockView).setGameBoard(2, ImmutableList.<Integer> of(1, 2),
                BlokusState.getStateFromApiState(testState).getBitmapStr());
    }

    @Test
    public void testGameOverWinStateForA() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0, 1))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(1, 2, testState));
        verify(mockView).gaofushuai();
    }

    @Test
    public void testGameOverWinStateForB() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0, 1))
                                .put("2", ImmutableList.<Integer> of(1, 2, 3))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(2, 2, testState));
        verify(mockView).gaofushuai();
    }

    @Test
    public void testGameOverLoseStateForA() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0, 1))
                                .put("2", ImmutableList.<Integer> of(1))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(2, 2, testState));
        verify(mockView).diaosi();
    }

    @Test
    public void testGameOverLoseStateForB() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0, 1))
                                .put("2", ImmutableList.<Integer> of(1, 2, 3))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(1, 2, testState));
        verify(mockView).diaosi();
    }

    @Test
    public void testGameOverDrawStateForA() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0, 1))
                                .put("2", ImmutableList.<Integer> of(1, 2))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(1, 2, testState));
        verify(mockView).gaofushuai();
    }

    @Test
    public void testGameOverDrawStateForB() {
        Map<String, Object> testState = ImmutableMap
                .<String, Object> builder()
                .put(Constants.JSON_TURN, 1)
                .put(Constants.JSON_USER_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_PASS_LIST, ImmutableList.<Integer> of(1, 2))
                .put(Constants.JSON_BITMAP, "0,0,1 0,5,2 0,6,2")
                .put(Constants.JSON_USER_USED_PIECES,
                        ImmutableMap.<String, List<Integer>> builder()
                                .put("1", ImmutableList.<Integer> of(0, 1))
                                .put("2", ImmutableList.<Integer> of(1, 2))
                                .build()).build();

        blokusPresenter.updateUI(createUpdateUI(2, 2, testState));
        verify(mockView).gaofushuai();
    }

    private UpdateUI createUpdateUI(
            int yourPlayerId, int turnOfPlayerId, Map<String, Object> state) {
        return new UpdateUI(yourPlayerId, playersInfo, state,
                emptyState, // we ignore lastState
                ImmutableList.<Operation> of(new SetTurn(turnOfPlayerId)),
                0, // We don't concern who make the last move.
                ImmutableMap.<Integer, Integer> of()); // Token is not
                                                       // considered.
    }
}
