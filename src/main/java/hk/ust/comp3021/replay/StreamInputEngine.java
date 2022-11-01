package hk.ust.comp3021.replay;

import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.game.InputEngine;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * An input engine that fetches actions from terminal input.
 */
public class StreamInputEngine implements InputEngine {

    /**
     * The {@link Scanner} for reading input from the terminal.
     */
    private final Scanner terminalScanner;

    private final int playerId;

    public StreamInputEngine(InputStream fileStream) {
        this.terminalScanner = new Scanner(fileStream);
        this.playerId = Integer.parseInt(terminalScanner.nextLine());
    }

    /**
     * Fetch an action from user in terminal to process.
     * <p>
     * If all lines are exhausted, an {@link Exit} action will be returned.
     *
     * @return the user action.
     */
    @Override
    public @NotNull Action fetchAction() {
        try {
            while (true) {
                var inputLine = terminalScanner.nextLine();
                final var move = inputLine.charAt(0);
                try {
                    return char2Action(playerId, move);
                } catch (IllegalArgumentException ignored) {
                }
            }
        } catch (NoSuchElementException e) {
            return new Exit(-1);
        }
    }

    public static Action char2Action(int playerId, Character s) {
        return switch (s) {
            case 'H', 'h' -> new Move.Left(playerId);
            case 'J', 'j' -> new Move.Down(playerId);
            case 'K', 'k' -> new Move.Up(playerId);
            case 'L', 'l' -> new Move.Right(playerId);
            case 'U', 'u' -> new Undo(playerId);
            case 'E', 'e' -> new Exit(playerId);
            default -> throw new IllegalArgumentException(String.valueOf(s));
        };
    }
}
