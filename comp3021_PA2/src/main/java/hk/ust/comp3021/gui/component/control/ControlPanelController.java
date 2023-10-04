package hk.ust.comp3021.gui.component.control;

import hk.ust.comp3021.actions.Action;
import hk.ust.comp3021.actions.InvalidInput;
import hk.ust.comp3021.actions.Move;
import hk.ust.comp3021.actions.Undo;
import hk.ust.comp3021.entities.Player;
import hk.ust.comp3021.game.InputEngine;
import hk.ust.comp3021.gui.utils.Message;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Control logic for a {@link ControlPanel}.
 * ControlPanelController serves as {@link InputEngine} for the game.
 * It caches users input (move actions) and provides them to the {@link hk.ust.comp3021.gui.scene.game.GUISokobanGame}.
 */
public class ControlPanelController implements Initializable, InputEngine {
    @FXML
    private FlowPane playerControls;

    BlockingQueue<Action> cache = new ArrayBlockingQueue<>(100);

    /**
     * Fetch the next action made by users.
     * All the actions performed by users should be cached in this class and returned by this method.
     *
     * @return The next action made by users.
     */
    @Override
    public @NotNull Action fetchAction() {
        // TODO
        try {
            return this.cache.take();
        } catch (InterruptedException e) {
            return new InvalidInput(0, "Failed to fetch action");
        }
    }

    /**
     * Initialize the controller as you need.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        this.playerControls.addEventHandler(MyEvent.MY_EVENT, e -> cache.add(e.move));
    }

    /**
     * Event handler for the undo button.
     * Cache the undo action and return it when {@link #fetchAction()} is called.
     *
     * @param event Event data related to clicking the button.
     */
    public void onUndo(ActionEvent event) {
        // TODO
        this.cache.add(new Undo(0));
    }

    /**
     * Adds a player to the control player.
     * Should add a new movement button group for the player.
     *
     * @param player         The player.
     * @param playerImageUrl The URL to the profile image of the player
     */
    public void addPlayer(Player player, URL playerImageUrl) {
        // TODO
        try {
            var grid = new MovementButtonGroup();
            grid.getController().setPlayer(player);
            grid.getController().setPlayerImage(playerImageUrl);
            this.playerControls.getChildren().add(grid);
        } catch (IOException e) {
            Message.error("Failed to load movement button group", e.getMessage());
        }
    }

    static class MyEvent extends Event {
        static final EventType<MyEvent> MY_EVENT = new EventType<>("MY_EVENT");
        private final Move move;
        MyEvent(Move move) {
            super(MY_EVENT);
            this.move = move;
        }
    }

}
