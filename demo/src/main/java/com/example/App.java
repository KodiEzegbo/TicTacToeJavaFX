package com.example;

import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX interface for a human-versus-CPU Tic-Tac-Toe game. */
public class App extends Application {
    private final Board board = new Board();
    private final HumanPlayer human = new HumanPlayer('X', new Scanner(""));
    private final CpuPlayer cpu = new CpuPlayer('O');
    private final Button[][] cells = new Button[3][3];
    private Label status;
    private boolean gameOver;

    @Override
    public void start(Stage stage) {
        Label eyebrow = new Label("HUMAN VS CPU");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Tic-Tac-Toe");
        title.getStyleClass().add("title");

        status = new Label();
        status.getStyleClass().add("status");
        status.setAlignment(Pos.CENTER);

        GridPane boardView = createBoardView();
        Button restart = new Button("New game");
        restart.getStyleClass().add("restart-button");
        restart.setOnAction(event -> restartGame());

        VBox content = new VBox(20, eyebrow, title, status, boardView, restart);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("content");

        StackPane root = new StackPane(content);
        root.setPadding(new Insets(30));
        Scene scene = new Scene(root, 520, 690);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        stage.setTitle("Tic-Tac-Toe");
        stage.setMinWidth(480);
        stage.setMinHeight(650);
        stage.setScene(scene);
        updateStatusForTurn();
        refreshBoard();
        stage.show();
    }

    private GridPane createBoardView() {
        GridPane boardView = new GridPane();
        boardView.setAlignment(Pos.CENTER);
        boardView.setHgap(10);
        boardView.setVgap(10);
        boardView.getStyleClass().add("board");

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                Button cell = new Button();
                cell.setPrefSize(112, 112);
                cell.getStyleClass().add("cell");
                int selectedRow = row;
                int selectedColumn = column;
                cell.setOnAction(event -> playHumanMove(selectedRow, selectedColumn));
                cells[row][column] = cell;
                boardView.add(cell, column, row);
            }
        }
        return boardView;
    }

    private void playHumanMove(int row, int column) {
        if (gameOver || !human.makeMove(board, row, column)) {
            return;
        }
        refreshBoard();
        if (finishIfGameOver(human)) {
            return;
        }

        status.setText("CPU is thinking...");
        cpu.makeMove(board);
        refreshBoard();
        if (!finishIfGameOver(cpu)) {
            updateStatusForTurn();
        }
    }

    private boolean finishIfGameOver(Player lastPlayer) {
        if (board.checkWin()) {
            gameOver = true;
            String result = lastPlayer.getName() + " (" + lastPlayer.getMarker() + ") WINS!";
            status.setText(result);
            refreshBoard();
            showGameOverAlert(result.toUpperCase());
            return true;
        }
        if (board.isFull()) {
            gameOver = true;
            String result = "IT'S A DRAW!";
            status.setText(result);
            refreshBoard();
            showGameOverAlert(result);
            return true;
        }
        return false;
    }

    private void showGameOverAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshBoard() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                char marker = board.getCell(row, column);
                Button cell = cells[row][column];
                cell.setText(marker == ' ' ? "" : Character.toString(marker));
                cell.setDisable(gameOver || marker != ' ');
                cell.getStyleClass().removeAll("x-cell", "o-cell");
                if (marker == 'X') {
                    cell.getStyleClass().add("x-cell");
                } else if (marker == 'O') {
                    cell.getStyleClass().add("o-cell");
                }
            }
        }
    }

    private void updateStatusForTurn() {
        status.setText(human.getName() + " (X), your move.");
    }

    private void restartGame() {
        board.initialize();
        gameOver = false;
        refreshBoard();
        updateStatusForTurn();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
