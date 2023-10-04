package hk.ust.comp3021.gui.component.maplist;

import hk.ust.comp3021.game.GameMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;

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
     * @param gameMapURL The path to the game map files.
     * @return The map model.
     * @throws IOException When fails to access the file.
     */
    public static MapModel load(URL gameMapURL) throws IOException {
        Path gameMapFile = null;
        try {
            gameMapFile = Paths.get(gameMapURL.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        var reader = new BufferedReader(new InputStreamReader(gameMapURL.openStream()));
        var content = reader.lines().collect(Collectors.joining("\n"));
        var gameMap = GameMap.parse(content);
        return new MapModel(gameMapFile.getFileName().toString(), gameMapFile, new Date(), gameMap);
    }
}
