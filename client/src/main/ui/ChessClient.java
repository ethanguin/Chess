package ui;

import chess.*;
import model.GameData;
import model.GameState;
import req_Res.ResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ui.ChessClient.State.BLACK_PLAYER;
import static util.EscapeSequences.*;

public class ChessClient implements DisplayRequests {
    enum State {
        LOGGED_OUT, LOGGED_IN, OBSERVER, BLACK_PLAYER, WHITE_PLAYER;

        public boolean isTurn(ChessGame.TeamColor color) {
            return (color.toString().equals(this.toString()));
        }
    }

    final private WebSocketFacade webSocket;
    final private ServerFacade server;
    private String authToken;
    private GameData gameData;
    private GameData[] gameDataList;
    private State userState = State.LOGGED_OUT;

    public ChessClient() throws Exception {
        server = new ServerFacade("http://localhost:8080");
        webSocket = new WebSocketFacade("ws://localhost:8080/connect", this);
    }

    public String execute(String input) {
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

    private record HelpCommand(String command, String description) {
    }

    static final List<HelpCommand> loggedOutCommands = List.of(
            new HelpCommand("register <USERNAME> <PASSWORD> <EMAIL>", "create a new account"),
            new HelpCommand("login <USERNAME> <PASSWORD>", "login specified user"),
            new HelpCommand("quit", "quit the client program"),
            new HelpCommand("help", "lists possible commands")
    );
    static final List<HelpCommand> loggedInCommands = List.of(
            new HelpCommand("create <NAME>", "create a new game"),
            new HelpCommand("list", "list all current games"),
            new HelpCommand("join <ID> [WHITE|BLACK]", "join a game"),
            new HelpCommand("observe <ID>", "observe a game"),
            new HelpCommand("logout", "logout current user"),
            new HelpCommand("quit", "quit the client program"),
            new HelpCommand("help", "lists possible commands")
    );

    static final List<HelpCommand> observingCommands = List.of(
            new HelpCommand("legal", "moves for the current board"),
            new HelpCommand("redraw", "redraw the board"),
            new HelpCommand("leave", "leave the current game"),
            new HelpCommand("quit", "quit the client program"),
            new HelpCommand("help", "lists possible commands")
    );

    static final List<HelpCommand> playingCommands = List.of(
            new HelpCommand("redraw", "redraw the board"),
            new HelpCommand("leave", "leave the current game"),
            new HelpCommand("move <FromColumn><FromRow><ToColumn><ToRow> [q|r|b|n]", "move a piece [with optional promotion]"),
            new HelpCommand("resign", "resign the game without leaving it"),
            new HelpCommand("legal <Column><Row>", "gives the legal moves a given piece can do"),
            new HelpCommand("quit", "quit the client program"),
            new HelpCommand("help", "lists possible commands")
    );

    private String help() {
        List<HelpCommand> helpCommandList = switch (userState) {
            case LOGGED_IN -> loggedInCommands;
            case OBSERVER -> observingCommands;
            case BLACK_PLAYER, WHITE_PLAYER -> playingCommands;
            default -> loggedOutCommands;
        };

        StringBuilder sb = new StringBuilder();
        for (var me : helpCommandList) {
            sb.append(String.format("  %s%s%s - %s%s%s%n", SET_TEXT_COLOR_YELLOW, me.command, RESET_TEXT_COLOR, RESET_TEXT_COLOR, me.description, RESET_TEXT_COLOR));
        }
        return sb.toString();
    }

    private String clear() throws Exception {
        server.clear();
        userState = State.LOGGED_OUT;
        gameData = null;
        return "Successfully cleared";
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
        gameDataList = server.listGames(authToken);
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < gameDataList.length; i++) {
            var game = gameDataList[i];
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
                userState = (color == ChessGame.TeamColor.WHITE ? State.WHITE_PLAYER : BLACK_PLAYER);
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
                userState = State.OBSERVER;
                printGameBothSides();
                return String.format("Joined %d as observer", gameID);
            }
        }

        return "Failed to observe game";
    }

    private String redraw(String[] params) throws Exception {
        verifyAuth();
        if (userState == BLACK_PLAYER || userState == State.WHITE_PLAYER || userState == State.OBSERVER) {
            printGameBothSides();
            return "";
        }
        return "Failure";
    }

    private String legal(String[] params) throws Exception {
        verifyAuth();
        if (userState == BLACK_PLAYER || userState == State.WHITE_PLAYER || userState == State.OBSERVER) {
            if (params.length == 1) {
                var pos = new ChessPositionImpl(params[0]);
                var highlights = new ArrayList<ChessPosition>();
                highlights.add(pos);
                for (var move : gameData.getGame().validMoves(pos)) {
                    highlights.add(move.getEndPosition());
                }
                var color = userState == BLACK_PLAYER ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                printGame(color, highlights);
                return "";
            }
        }
        return "Failure";
    }

    private String move(String[] params) throws Exception {
        verifyAuth();
        if (params.length == 1) {
            var move = new ChessMoveImpl(params[0]);
            if (isMoveLegal(move)) {
                //webSocket.sendCommand(new MoveCommand(authToken, gameData.getGameID(), move));
                return "Success";
            }
        }
        return "Failure";
    }

    public boolean isMoveLegal(ChessMoveImpl move) {
        if (isTurn()) {
            ChessGame game = gameData.getGame();
            Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
            return validMoves.contains(move);
        }
        return false;
    }

    private String leave(String[] params) {
        if (userState == State.WHITE_PLAYER || userState == BLACK_PLAYER || userState == State.OBSERVER) {
            userState = State.LOGGED_IN;
            gameData = null;
            return "Left game";
        }
        return "Failure";
    }

    private String resign(String[] params) {
        return "Method not implemented yet";
    }

    private void printGame() {
        var color = userState == BLACK_PLAYER ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        printGame(color, null);
    }

    private void printGame(ChessGame.TeamColor color, Collection<ChessPosition> highlights) {
        System.out.println("\n");
        System.out.print((gameData.getGame().getBoard()).toString(color, highlights));
        System.out.println();
    }

    private void printGameBothSides() {
        System.out.println("\n");
        System.out.println(gameData.getGame().getBoard().toString(ChessGame.TeamColor.WHITE, null));
        System.out.println("\n");
        System.out.println(gameData.getGame().getBoard().toString(ChessGame.TeamColor.BLACK, null));
    }

    public boolean isPlayer() {
        return (gameData != null && (userState == State.WHITE_PLAYER || userState == BLACK_PLAYER) && !isGameOver());
    }


    public boolean isObserver() {
        return (gameData != null && (userState == State.OBSERVER));
    }

    public boolean isGameOver() {
        return (gameData != null && gameData.getState() != GameState.UNDECIDED);
    }

    public boolean isTurn() {
        return (isPlayer() && userState.isTurn(gameData.getGame().getTeamTurn()));
    }

    @Override
    public void updateBoard(GameData updatedGameData) {
        gameData = updatedGameData;
        printGame();
        printPrompt();

        if (isGameOver()) {
            userState = State.LOGGED_IN;
            printPrompt();
            gameData = null;
        }
    }

    @Override
    public void message(String message) {
        System.out.println();
        System.out.println(SET_TEXT_COLOR_MAGENTA + "NOTIFY: " + message);
        printPrompt();
    }

    @Override
    public void error(String message) {
        System.out.println();
        System.out.println(SET_TEXT_COLOR_RED + "NOTIFY: " + message);
        printPrompt();
    }
}
