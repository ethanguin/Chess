package ui;

import model.GameData;

public interface DisplayRequests {
    void updateBoard(GameData game);

    void message(String message);

    void error(String message);
}