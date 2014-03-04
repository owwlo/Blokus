
package org.owwlo.Blokus.Graphics;

import org.owwlo.Blokus.BlokusPresenter;
import org.owwlo.Blokus.Model.BlokusLogic;
import org.owwlo.Blokus.Shared.GameApi;
import org.owwlo.Blokus.Shared.GameApi.Game;
import org.owwlo.Blokus.Shared.GameApi.IteratingPlayerContainer;
import org.owwlo.Blokus.Shared.GameApi.UpdateUI;
import org.owwlo.Blokus.Shared.GameApi.VerifyMove;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

public class BlokusEntry implements EntryPoint {
    IteratingPlayerContainer container;
    BlokusPresenter blokusPresenter;

    @Override
    public void onModuleLoad() {
        Game game = new Game() {
            @Override
            public void sendVerifyMove(VerifyMove verifyMove) {
                container.sendVerifyMoveDone(BlokusLogic.verify(verifyMove));
            }

            @Override
            public void sendUpdateUI(UpdateUI updateUI) {
                blokusPresenter.updateUI(updateUI);
            }
        };
        container = new IteratingPlayerContainer(game, 2);
        BlokusGraphics blokusGraphics = new BlokusGraphics();
        blokusPresenter =
                new BlokusPresenter(blokusGraphics, container);
        final ListBox playerSelect = new ListBox();
        playerSelect.addItem("WhitePlayer");
        playerSelect.addItem("BlackPlayer");
        playerSelect.addItem("Viewer");
        playerSelect.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int selectedIndex = playerSelect.getSelectedIndex();
                int playerId = selectedIndex == 2 ? GameApi.VIEWER_ID
                        : container.getPlayerIds().get(selectedIndex);
                container.updateUi(playerId);
            }
        });
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(blokusGraphics);
        flowPanel.add(playerSelect);
        RootPanel.get("mainDiv").add(flowPanel);
        container.sendGameReady();
        container.updateUi(container.getPlayerIds().get(0));
    }
}
