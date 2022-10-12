package hk.ust.comp3021.gui.component.maplist;

import hk.ust.comp3021.game.GameMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * Data model for storing the metadata of a  game map.
 *
 * @param name    The name of the map.
 * @param file    The location of the map.
 * @param loadAt  The date and time when the map wad loaded.
 * @param gameMap The game map itself.
 */
public record MapModel(String name, Path file, Date loadAt, GameMap gameMap) {

    /**
     * @param gameMapFile The path to the game map files.
     * @return The map model.
     * @throws IOException When fails to access the file.
     */
    public static MapModel load(Path gameMapFile) throws IOException {
        var gameMap = GameMap.parse(Files.readString(gameMapFile));
        return new MapModel(gameMapFile.getFileName().toString(), gameMapFile, new Date(), gameMap);
    }
}
