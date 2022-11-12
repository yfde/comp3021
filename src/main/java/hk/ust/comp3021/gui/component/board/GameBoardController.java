package hk.ust.comp3021.gui.component.board;

import hk.ust.comp3021.entities.Box;
import hk.ust.comp3021.entities.Empty;
import hk.ust.comp3021.entities.Player;
import hk.ust.comp3021.entities.Wall;
import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.game.Position;
import hk.ust.comp3021.game.RenderingEngine;
import hk.ust.comp3021.gui.utils.Message;
import hk.ust.comp3021.gui.utils.Resource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static hk.ust.comp3021.utils.StringResources.UNDO_QUOTA_TEMPLATE;
import static hk.ust.comp3021.utils.StringResources.UNDO_QUOTA_UNLIMITED;

/**
 * Control logic for a {@link GameBoard}.
 * <p>
 * GameBoardController serves the {@link RenderingEngine} which draws the current game map.
 */
public class GameBoardController implements RenderingEngine, Initializable {
    @FXML
    private GridPane map;

    @FXML
    private Label undoQuota;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Draw the game map in the {@link #map} GridPane.
     *
     * @param state The current game state.
     */
    @Override
    public void render(@NotNull GameState state) {
        // TODO
        Platform.runLater(() -> {
            this.undoQuota.setText(state.getUndoQuota()
                    .map(it -> String.format(UNDO_QUOTA_TEMPLATE, it))
                    .orElse(UNDO_QUOTA_UNLIMITED));
            for (int y = 0; y < state.getMapMaxHeight(); y++) {
                for (int x = 0; x < state.getMapMaxWidth(); x++) {
                    final var entity = state.getEntity(Position.of(x, y));
                    if (entity == null) {
                        continue;
                    }
                    try {
                        var cell = new Cell();
                        cell.getController().setImage(switch (entity) {
                            case Wall ignored -> Resource.getWallImageURL();
                            case Box b -> {
                                if (state.getDestinations().contains(Position.of(x, y))) {
                                    cell.getController().markAtDestination();
                                }
                                yield Resource.getBoxImageURL(b.getPlayerId());
                            }
                            case Player p -> Resource.getPlayerImageURL(p.getId());
                            case Empty ignored -> {
                                if (state.getDestinations().contains(Position.of(x, y))) {
                                    yield Resource.getDestinationImageURL();
                                } else {
                                    yield Resource.getEmptyImageURL();
                                }
                            }
                        });
                        this.map.add(cell, x, y);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     * Display a message via a dialog.
     *
     * @param content The message
     */
    @Override
    public void message(@NotNull String content) {
        Platform.runLater(() -> Message.info("Sokoban", content));
    }
}
