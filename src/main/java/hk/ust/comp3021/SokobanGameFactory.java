package hk.ust.comp3021;

import hk.ust.comp3021.game.GameMap;
import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.game.SokobanGame;
import hk.ust.comp3021.replay.StreamInputEngine;
import hk.ust.comp3021.replay.TerminalRenderingEngine;
import hk.ust.comp3021.replay.ReplaySokobanGame;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Factory for creating Sokoban games
 */
public class SokobanGameFactory {

    /**
     * Create a Sokoban game.
     *
     * @param mapFile     map file.
     * @param mode        mode of the game.
     * @param fps         rendering fps.
     * @param actionFiles action files.
     * @return The Sokoban game.
     * @throws IOException if mapFile cannot be load
     */
    public static @NotNull SokobanGame createReplayGame(@NotNull String mapFile,
                                                        ReplaySokobanGame.Mode mode,
                                                        int fps,
                                                        @NotNull String[] actionFiles
    ) throws IOException {
        Path file = Path.of(mapFile);
        final var gameMap = loadGameMap(file);
        final var inputEngines = new StreamInputEngine[actionFiles.length];
        for (int i = 0; i < actionFiles.length; i++) {
            inputEngines[i] = new StreamInputEngine(new FileInputStream(actionFiles[i]));
        }
        return new ReplaySokobanGame(
            mode,
            fps,
            new GameState(gameMap),
            inputEngines,
            new TerminalRenderingEngine(System.out)
        );
    }

    /**
     * @param mapFile The file containing the game map.
     * @return The parsed game map.
     * @throws IOException When there is an issue loading the file.
     */
    public static @NotNull GameMap loadGameMap(@NotNull Path mapFile) throws IOException {
        final var fileContent = Files.readString(mapFile);
        return GameMap.parse(fileContent);
    }

}
