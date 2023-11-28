package ui;

import chess.ChessGame;
import chess.ChessPositionImpl;
import model.GameData;
import req_Res.ResponseException;

import java.util.Arrays;
import java.util.List;

import static util.EscapeSequences.*;

public class ChessClient {
    enum State {LOGGED_OUT, LOGGED_IN, OBSERVING, BLACK_PLAYER, WHITE_PLAYER}

    private State userState = State.LOGGED_OUT;
    private String authToken;
    private GameData gameData;
    private GameData[] games;
    final private ServerFacade server;

    public ChessClient() {
        server = new ServerFacade("http://localhost:8080");
    }

    public String eval(String input) {
        String result = "Error with command. Try using \"Help\"";
        input = input.toLowerCase();
        String[] tokens = input.split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (command.equals("quit")) {
            return "quit";
        }
        if (command.equals("help")) {
            return help();
        }
        try {
            if (command.equals("clear")) {
                return clear();
            }
            if (command.equals("login")) {
                return login(params);
            }
            if (command.equals("register")) {
                return register(params);
            }
            if (command.equals("logout")) {
                return logout(params);
            }
            if (command.equals("create")) {
                return create(params);
            }
            if (command.equals("observe")) {
                return observe(params);
            }
            if (command.equals("join")) {
                return join(params);
            }
            if (command.equals("list")) {
                return list();
            }
            if (command.equals("redraw")) {
                return redraw(params);
            }
            if (command.equals("leave")) {
                return leave(params);
            }
            if (command.equals("move")) {
                return move(params);
            }
            if (command.equals("resign")) {
                return resign(params);
            }
            if (command.equals("legal")) {
                return legal(params);
            }

        } catch (Exception e) {
            result = "Error with command: " + e.getMessage();
        }


        return result;
    }

    private String clear() throws Exception {
        server.clear();
        userState = State.LOGGED_OUT;
        gameData = null;
        return "Successfully cleared";
    }

    private record HelpCommand(String command, String description) {
    }

    static final List<HelpCommand> loggedOutCommands = List.of(
            new HelpCommand("register <USERNAME> <PASSWORD> <EMAIL>", "to create a new account"),
            new HelpCommand("login <USERNAME> <PASSWORD>", "to play chess"),
            new HelpCommand("quit", "playing chess"),
            new HelpCommand("help", "with possible commands")
    );
    static final List<HelpCommand> loggedInCommands = List.of(
            new HelpCommand("create <NAME>", "a game"),
            new HelpCommand("list", "games"),
            new HelpCommand("join <ID> [WHITE|BLACK]", "a game"),
            new HelpCommand("observe <ID>", "a game"),
            new HelpCommand("logout", "when you are done"),
            new HelpCommand("quit", "playing chess"),
            new HelpCommand("help", "with possible commands")
    );

    private String help() {
        List<HelpCommand> helpCommandList = switch (userState) {
            case LOGGED_IN -> loggedInCommands;
            case LOGGED_OUT -> loggedOutCommands;
            default -> loggedOutCommands;
        };

        StringBuilder sb = new StringBuilder();
        for (var me : helpCommandList) {
            sb.append(String.format("  %s%s%s - %s%s%s%n", SET_TEXT_COLOR_YELLOW, me.command, RESET_TEXT_COLOR, RESET_TEXT_COLOR, me.description, RESET_TEXT_COLOR));
        }
        return sb.toString();
    }


    public void printPrompt() {//edit later to reflect who won and who played
        System.out.print(RESET_TEXT_COLOR + String.format("\n[%s] >>> ", userState) + SET_TEXT_COLOR_BLUE);
    }

    private String login(String[] params) throws ResponseException {
        if (userState == State.LOGGED_OUT && params.length == 2) {
            var response = server.login(params[0], params[1]);
            authToken = response.getAuthToken();
            userState = State.LOGGED_IN;
            return String.format("Logged in as %s", params[0]);
        }
        return "Failed to login user";
    }

    private String register(String[] params) throws ResponseException {
        if (userState == State.LOGGED_OUT && params.length == 3) {
            var response = server.register(params[0], params[1], params[2]);
            authToken = response.getAuthToken();
            userState = State.LOGGED_IN;
            return String.format("Logged in as %s", params[0]);
        }
        return "Failed to register new user";
    }

    private String logout(String[] ignore) throws ResponseException {
        verifyAuth();

        if (userState != State.LOGGED_OUT) {
            server.logout(authToken);
            userState = State.LOGGED_OUT;
            authToken = null;
            return "Logged out";
        }
        return "Failed to logout user";
    }

    private void verifyAuth() throws ResponseException {
        if (authToken == null) {
            throw new ResponseException(401, "Please login or register");
        }
    }

    private String create(String[] params) throws ResponseException {
        verifyAuth();

        if (params.length == 1 && userState == State.LOGGED_IN) {
            var gameData = server.createGame(authToken, params[0]);
            return String.format("Create %d", gameData.getGameID());
        }
        return "Failed to create new game";
    }

    private String list() throws ResponseException {
        verifyAuth();
        games = server.listGames(authToken);
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < games.length; i++) {
            var game = games[i];
            var gameText = String.format("%d. %s ID:%d white:%s black:%s state: %s%n", i + 1, game.getGameName(), game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getState());
            sb.append(gameText);
        }
        return sb.toString();
    }

    private String join(String[] params) throws Exception {
        verifyAuth();
        if (userState == State.LOGGED_IN) {
            if (params.length == 2 && (params[1].equalsIgnoreCase("WHITE") || params[1].equalsIgnoreCase("BLACK"))) {
                var gameID = Integer.parseInt(params[0]);
                var color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
                gameData = server.joinGame(authToken, gameID, color);
                userState = (color == ChessGame.TeamColor.WHITE ? State.WHITE_PLAYER : State.BLACK_PLAYER);
                printGameBothSides();
                return String.format("Joined %d as %s", gameID, color);
            }
        }
        return "Failed to join game";
    }

    private String observe(String[] params) throws Exception {
        verifyAuth();
        if (userState == State.LOGGED_IN) {
            if (params.length == 1) {
                var gameID = Integer.parseInt(params[0]);
                gameData = server.observeGame(authToken, gameID);
                userState = State.OBSERVING;
                printGameBothSides();
                return String.format("Joined %d as observer", gameID);
            }
        }

        return "Failed to observe game";
    }

    private String redraw(String[] params) throws Exception {
        verifyAuth();
        if (userState == State.BLACK_PLAYER || userState == State.WHITE_PLAYER || userState == State.OBSERVING) {
            printGameBothSides();
            return "";
        }
        return "Failure";
    }

    private String legal(String[] params) throws Exception {
        verifyAuth();
        if (userState == State.BLACK_PLAYER || userState == State.WHITE_PLAYER || userState == State.OBSERVING) {
            if (params.length == 1) {
                var pos = new ChessPositionImpl(params[0]);
                StringBuilder sb = new StringBuilder();
                for (var move : gameData.getGame().validMoves(pos)) {
                    sb.append(move.toString());
                    sb.append("\n");
                }

                return sb.toString();
            }
        }
        return "Failure";
    }

    private String move(String[] params) {
        return "Method not implemented yet";
    }

    private String leave(String[] params) {
        if (userState == State.WHITE_PLAYER || userState == State.BLACK_PLAYER || userState == State.OBSERVING) {
            userState = State.LOGGED_IN;
            gameData = null;
            return "Left game";
        }
        return "Failure";
    }

    private String resign(String[] params) {
        return "Method not implemented yet";
    }

    private void printGameBothSides() {
        System.out.println("\n");
        System.out.println(gameData.getGame().getBoard().toString(ChessGame.TeamColor.WHITE));
        System.out.println("\n");
        System.out.println(gameData.getGame().getBoard().toString(ChessGame.TeamColor.BLACK));
    }
}
