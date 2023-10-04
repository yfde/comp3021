package hk.ust.comp3021.tui;

import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.game.InputEngine;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Scanner;

/**
 * An input engine that fetches actions from terminal input.
 */
public class TerminalInputEngine implements InputEngine {

    /**
     * The {@link Scanner} for reading input from the terminal.
     */
    private final Scanner terminalScanner;

    /**
     * @param terminalStream The stream to read terminal inputs.
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    /**
     * Fetch an action from user in terminal to process.
     *
     * @return the user action.
     */
    @Override
    public @NotNull Action fetchAction() {
        // This is an example showing how to read a line from the Scanner class.
        // Feel free to change it if you do not like it.
        final var inputLine = terminalScanner.nextLine();

        // TODO
        Action result = switch (inputLine) {
            case "w" -> new Move.Up(0);
            case "s" -> new Move.Down(0);
            case "a" -> new Move.Left(0);
            case "d" -> new Move.Right(0);
            case "k" -> new Move.Up(1);
            case "j" -> new Move.Down(1);
            case "h" -> new Move.Left(1);
            case "l" -> new Move.Right(1);
            case "u" -> new Undo(0);
            case "exit" -> new Exit(0);
            default -> new InvalidInput(0, "Invalid Input.");
        };

        return result;
        // throw new NotImplementedException();
    }
}
