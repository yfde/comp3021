package hk.ust.comp3021.gui.scene.start;

import hk.ust.comp3021.gui.component.maplist.MapEvent;
import hk.ust.comp3021.gui.component.maplist.MapList;
import hk.ust.comp3021.gui.component.maplist.MapModel;
import hk.ust.comp3021.gui.utils.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Control logic for {@link  StartScene}.
 */
public class StartController implements Initializable {
    @FXML
    private MapList mapList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button openButton;

    /**
     * Initialize the controller.
     * Load the built-in maps to {@link this#mapList}.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        var list = this.mapList.getController().getList();
        try {
            list.getItems().add(MapModel.load(Objects.requireNonNull(getClass().getClassLoader().getResource("map00.map"))));
            list.getItems().add(MapModel.load(Objects.requireNonNull(getClass().getClassLoader().getResource("map01.map"))));
        } catch (IOException e) {
            Message.error("Failed to load the default game map files", e.getMessage());
        }
        this.openButton.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
        this.deleteButton.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());
    }

    /**
     * Event handler for the open button.
     * Display a file chooser, load the selected map and add to {@link this#mapList}.
     * If the map is invalid or cannot be loaded, display an error message.
     *
     * @param event Event data related to clicking the button.
     */
    @FXML
    private void onLoadMapBtnClicked(ActionEvent event) {
        // TODO
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Map Files", "*.map"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialDirectory(new File("./src/main/resources"));
        var file = fileChooser.showOpenDialog(this.openButton.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            var list = this.mapList.getController().getList();
            var map = MapModel.load(file.toURI().toURL());
            list.getItems().removeIf(item -> item.file().equals(map.file()));
            list.getItems().add(0, map);
        } catch (IOException | RuntimeException e) {
            Message.error("Failed to load: " + file.getName(), e.getMessage());
        }
    }

    /**
     * Handle the event when the delete button is clicked.
     * Delete the selected map from the {@link this#mapList}.
     */
    @FXML
    public void onDeleteMapBtnClicked() {
        // TODO
        var list = this.mapList.getController().getList();
        list.getItems().remove(list.getSelectionModel().selectedItemProperty().get());
        list.getSelectionModel().clearSelection();
    }

    /**
     * Handle the event when the map open button is clicked.
     * Retrieve the selected map from the {@link this#mapList}.
     * Fire an {@link MapEvent} so that the {@link hk.ust.comp3021.gui.App} can handle it and switch to the game scene.
     */
    @FXML
    public void onOpenMapBtnClicked() {
        // TODO
        var list = this.mapList.getController().getList();
        this.mapList.fireEvent(new MapEvent(MapEvent.OPEN_MAP_EVENT_TYPE, list.getSelectionModel().selectedItemProperty().get()));
    }

    /**
     * Handle the event when a file is dragged over.
     *
     * @param event The drag event.
     * @see <a href="https://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm">JavaFX Drag and Drop</a>
     */
    @FXML
    public void onDragOver(DragEvent event) {
        // TODO
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
    }

    /**
     * Handle the event when the map file is dragged to this app.
     * <p>
     * Multiple files should be supported.
     * Display error message if some dragged files are invalid.
     * All valid maps should be loaded.
     *
     * @param dragEvent The drag event.
     * @see <a href="https://docs.oracle.com/javafx/2/drag_drop/jfxpub-drag_drop.htm">JavaFX Drag and Drop</a>
     */
    @FXML
    public void onDragDropped(DragEvent dragEvent) {
        // TODO
        var list = this.mapList.getController().getList();
        for (var file : dragEvent.getDragboard().getFiles()) {
            try {
                var map = MapModel.load(file.toURI().toURL());
                list.getItems().removeIf(item -> item.file().equals(map.file()));
                list.getItems().add(0, map);
            } catch (IOException | RuntimeException e) {
                Message.error("Failed to load: " + file.getName(), e.getMessage());
            }
        }
    }

}
