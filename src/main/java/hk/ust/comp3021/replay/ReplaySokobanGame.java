package hk.ust.comp3021.replay;


import hk.ust.comp3021.actions.Action;
import hk.ust.comp3021.actions.ActionResult;
import hk.ust.comp3021.actions.Exit;
import hk.ust.comp3021.game.AbstractSokobanGame;
import hk.ust.comp3021.game.GameState;
import hk.ust.comp3021.game.InputEngine;
import hk.ust.comp3021.game.RenderingEngine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static hk.ust.comp3021.utils.StringResources.*;

/**
 * A thread-safe Sokoban game.
 * The game should be able to run in a separate thread, and games running in parallel should not interfere with each other.
 * <p>
 * The game can run in two modes:
 * 1. {@link Mode#ROUND_ROBIN} mode: all input engines take turns to perform actions, starting from the first specified input engine.
 * Example: suppose there are two input engines, A and B, whose actions are [R, L], [R, L], respectively.
 * In this mode, the game will perform the following actions in order: A.R, B.R, A.L, B.L.
 * 2. {@link Mode#FREE_RACE} mode: all input engines perform actions simultaneously. The actions processed can be in any order.
 * There could be a chance that two runs of the same game process actions in different orders.
 * <p>
 * {@link hk.ust.comp3021.Sokoban#replayGame(int, String, Mode, int, String[])} runs multiple games in parallel.
 */
public class ReplaySokobanGame extends AbstractSokobanGame {
    /**
     * Mode of scheduling actions among input engines.
     */
    public enum Mode {
        /**
         * All input engines take turns to perform actions, starting from the first specified input engine.
         */
        ROUND_ROBIN,

        /**
         * All input engines perform actions concurrently without enforcing the order.
         */
        FREE_RACE,
    }

    protected final Mode mode;
    /**
     * Indicated the frame rate of the rendering engine (in FPS).
     */
    protected final int frameRate;

    /**
     * Default frame rate.
     */
    protected static final int DEFAULT_FRAME_RATE = 60;

    /**
     * The list of input engines to fetch inputs.
     */
    protected final List<? extends InputEngine> inputEngines;

    /**
     * The rendering engine to render the game status.
     */
    protected final RenderingEngine renderingEngine;

    /**
     * Create a new instance of ReplaySokobanGame.
     * Each input engine corresponds to an action file and will produce actions from the action file.
     *
     * @param mode            The mode of the game.
     * @param frameRate       Rendering fps.
     * @param gameState       The game state.
     * @param inputEngines    the input engines.
     * @param renderingEngine the rendering engine.
     */
    public ReplaySokobanGame(
            @NotNull Mode mode,
            int frameRate,
            @NotNull GameState gameState,
            @NotNull List<? extends InputEngine> inputEngines,
            @NotNull RenderingEngine renderingEngine
    ) {
        super(gameState);
        if (inputEngines.size() == 0)
            throw new IllegalArgumentException("No input engine specified");
        this.mode = mode;
        this.frameRate = frameRate;
        this.renderingEngine = renderingEngine;
        this.inputEngines = inputEngines;
    }

    /**
     * @param gameState       The game state.
     * @param inputEngines    the input engines.
     * @param renderingEngine the rendering engine.
     */
    public ReplaySokobanGame(
            @NotNull GameState gameState,
            @NotNull List<? extends InputEngine> inputEngines,
            @NotNull RenderingEngine renderingEngine) {
        this(Mode.FREE_RACE, DEFAULT_FRAME_RATE, gameState, inputEngines, renderingEngine);
    }

    // TODO: add any method or field you need.

    private int currentInputEngine = 0;

    private final ArrayList<Integer> aliveInputEngines = new ArrayList<>();

    private final Object fetchQueue = new Object();

    /**
     * The implementation of the Runnable for each input engine thread.
     * Each input engine should run in a separate thread.
     * <p>
     * Assumption:
     * 1. the last action fetch-able from the input engine is always an {@link Exit} action.
     * <p>
     * Requirements:
     * 1. All actions fetched from input engine should be processed in the order they are fetched.
     * 2. All actions before (including) the first {@link Exit} action should be processed
     * (passed to {@link this#processAction} method).
     * 3. Any actions after the first {@link Exit} action should be ignored
     * (not passed to {@link this#processAction}).
     */
    private class InputEngineRunnable implements Runnable {
        private final int index;
        private final InputEngine inputEngine;

        private InputEngineRunnable(int index, @NotNull InputEngine inputEngine) {
            this.index = index;
            this.inputEngine = inputEngine;
        }

        @Override
        public void run() {
            // TODO: modify this method to implement the requirements.
            while (!shouldStop()) {
                if (mode == Mode.ROUND_ROBIN) {
                    synchronized (fetchQueue) {
                        if (aliveInputEngines.get(currentInputEngine) == index) {
                            final var action = inputEngine.fetchAction();
                            if (action instanceof Exit) {
                                aliveInputEngines.remove(Integer.valueOf(index));
                                if (aliveInputEngines.size() == 0) {
                                    break;
                                }
                                currentInputEngine = currentInputEngine % aliveInputEngines.size();
                                fetchQueue.notifyAll();
                                break;
                            }
                            final var result = syncedProcessAction(action);
                            if (result instanceof ActionResult.Failed failed) {
                                synchronized (renderingEngine) {
                                    renderingEngine.message(failed.getReason());
                                }
                            }
                            currentInputEngine = (currentInputEngine + 1) % aliveInputEngines.size();
                            fetchQueue.notifyAll();
                        } else {
                            try {
                                fetchQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (mode == Mode.FREE_RACE) {
                    final var action = inputEngine.fetchAction();
                    if (action instanceof Exit) {
                        synchronized (aliveInputEngines) {
                            aliveInputEngines.remove(Integer.valueOf(index));
                        }
                        break;
                    }
                    final var result = syncedProcessAction(action);
                    if (result instanceof ActionResult.Failed failed) {
                        synchronized (renderingEngine) {
                            renderingEngine.message(failed.getReason());
                        }
                    }
                }
            }
        }
    }

    private ActionResult syncedProcessAction(@NotNull Action action) {
        synchronized (state) {
            return processAction(action);
        }
    }

    private void syncedRender() {
        synchronized (state) {
            final var undoQuotaMessage = state.getUndoQuota()
                    .map(it -> String.format(UNDO_QUOTA_TEMPLATE, it))
                    .orElse(UNDO_QUOTA_UNLIMITED);
            synchronized (renderingEngine) {
                renderingEngine.message(undoQuotaMessage);
                renderingEngine.render(state);
            }
        }
    }

    /**
     * The implementation of the Runnable for the rendering engine thread.
     * The rendering engine should run in a separate thread.
     * <p>
     * Requirements:
     * 1. The game map should be rendered at least once before any action is processed (the initial state should be rendered).
     * 2. The game map should be rendered after the last action is processed (the final state should be rendered).
     */
    private class RenderingEngineRunnable implements Runnable {
        /**
         * NOTE: You are NOT allowed to use {@link java.util.Timer} or {@link java.util.TimerTask} in this method.
         * Please use a loop with {@link Thread#sleep(long)} instead.
         */
        @Override
        public void run() {
            // TODO: modify this method to implement the requirements.
            double period = 1000 / (float) frameRate;
            double next = System.currentTimeMillis();
            do {
                if (System.currentTimeMillis() >= next) {
                    syncedRender();
                    next += period;
                }
            } while (!shouldStop() && !aliveInputEngines.isEmpty());
            syncedRender();
            renderingEngine.message(GAME_EXIT_MESSAGE);
            if (state.isWin()) {
                renderingEngine.message(WIN_MESSAGE);
            }
        }
    }

    /**
     * Start the game.
     * This method should spawn new threads for each input engine and the rendering engine.
     * This method should wait for all threads to finish before return.
     */
    @Override
    public void run() {
        // TODO
        for (int i = 0; i < inputEngines.size(); i++) {
            aliveInputEngines.add(i);
        }
        Thread renderThread = new Thread(new RenderingEngineRunnable());
        renderThread.start();
        List<Thread> inputThreads = new ArrayList<>();
        for (int i = 0; i < inputEngines.size(); i++) {
            Thread inputThread = new Thread(new InputEngineRunnable(i, inputEngines.get(i)));
            inputThreads.add(inputThread);
            inputThread.start();
        }
        while (true) {
            if (shouldStop() || aliveInputEngines.isEmpty()) {
                for (Thread inputThread : inputThreads) {
                    try {
                        inputThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    renderThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
