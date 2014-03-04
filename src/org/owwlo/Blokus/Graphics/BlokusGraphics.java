
package org.owwlo.Blokus.Graphics;

import java.util.List;

import org.owwlo.Blokus.BlokusPresenter;
import org.owwlo.Blokus.BlokusPresenter.ViewState;
import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Model.Piece;
import org.owwlo.Blokus.Model.Point;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BlokusGraphics extends Composite implements BlokusPresenter.View {
    private final static int PLAYER_NUMBER = 2;
    private final static String TRY_BLOCK_STYLE = "color-try";
    private final static String PLAYER_A_BLOCK_STYLE = "color-icon";
    private final static String PLAYER_B_BLOCK_STYLE = "color-icon2";

    private BlokusPresenter blokusPresenter;
    private Piece currentPiece;
    private List<Button> btns = Lists.newArrayList();
    private List<Integer> tryPoint = Lists.newArrayList();

    private static BlokusGraphicsUiBinder uiBinder = GWT.create(BlokusGraphicsUiBinder.class);

    interface BlokusGraphicsUiBinder extends UiBinder<Widget, BlokusGraphics> {
    }

    @UiField
    VerticalPanel buttonContainer;

    public BlokusGraphics() {
        initWidget(uiBinder.createAndBindUi(this));
        initBoard();
    }

    private void initBoard() {
        final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
        for (int i = 0; i < size; i++) {
            HorizontalPanel hpInner = new HorizontalPanel();
            for (int j = 0; j < size; j++) {
                Button btn = new Button();
                btn.setSize("20px", "20px");
                btn.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        Button btn = (Button) event.getSource();
                        int index = btns.indexOf(btn);
                        int row = index / size;
                        int col = index % size;
                        blokusPresenter.makeMove(currentPiece, new Point(row, col));
                    }
                });
                btn.addMouseOverHandler(new MouseOverHandler() {

                    @Override
                    public void onMouseOver(MouseOverEvent event) {
                        Button btn = (Button) event.getSource();
                        int index = btns.indexOf(btn);
                        int row = index / size;
                        int col = index % size;
                        blokusPresenter.tryCurrentPieceWithPosition(currentPiece, new Point(row,
                                col));
                    }
                });
                btns.add(btn);
                hpInner.add(btn);
            }
            buttonContainer.add(hpInner);
        }
    }

    @Override
    public void pickFromValidPiece(List<Integer> pieces) {
        int pieceIndex = pieces.get(0);
        currentPiece = Piece.getPiece(pieceIndex);
        blokusPresenter.chosePiece(pieceIndex);
    }

    @Override
    public void updateTryBlock(Piece piece, Point pos) {
        final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
        if (piece != null) {
            for (Point p : piece.getPointList()) {
                int index = (pos.x + p.x) * size + (pos.y + p.y);
                tryPoint.add(index);
                Button btn = btns.get(index);
                btn.setStyleName(TRY_BLOCK_STYLE);
            }
        }
    }

    @Override
    public void setViewState(ViewState vs) {
        if (vs == ViewState.MAKE_MOVE) {
            for (Button btn : btns) {
                btn.setEnabled(true);
            }
        } else if (vs == ViewState.VIEW_ONLY) {
            for (Button btn : btns) {
                btn.setEnabled(false);
            }
        }

    }

    @Override
    public void cleanTryBlock() {
        final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
        for (int index : tryPoint) {
            btns.get(index).removeStyleName(TRY_BLOCK_STYLE);
        }
        tryPoint.clear();
    }

    @Override
    public void setPresenter(BlokusPresenter blokusPresenter) {
        this.blokusPresenter = blokusPresenter;
    }

    @Override
    public void setGameBoard(int yourPlayerId, List<Integer> playerList, String bitmap) {
        final int size = Constants.boardSizeMap.get(PLAYER_NUMBER);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int index = i * size + j;
                Button btn = btns.get(index);
                btn.setStyleName("");
            }
        }
        String[] bitmapStrSpl = bitmap.split(" ");
        for (String str : bitmapStrSpl) {
            if (str.length() <= 1) {
                continue;
            }
            String[] inner = str.split(",");
            int row = Integer.parseInt(inner[0]);
            int col = Integer.parseInt(inner[1]);
            int color = Integer.parseInt(inner[2]);
            int index = row * size + col;
            Button btn = btns.get(index);
            if (color == yourPlayerId) {
                btn.setStyleName(PLAYER_B_BLOCK_STYLE);
            } else {
                btn.setStyleName(PLAYER_A_BLOCK_STYLE);
            }
        }
    }

    @Override
    public void diaosi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void gaofushuai() {
        // TODO Auto-generated method stub

    }

}
