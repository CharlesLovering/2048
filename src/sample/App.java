package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Xenocidist on 6/24/15.
 */
public class App {
    private static final double WIDTH = 420;
    private static final int HEIGHT = 475;
    private static final int SPACING = 5;
    private GameBoard BOARD;
    private ScoreBoard SCORE;
    private boolean canMove = true;
    private Stage primaryStage;


    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.SCORE = new ScoreBoard(WIDTH);
        this.BOARD = new GameBoard(SCORE);
        this.BOARD.populate();
        this.BOARD.printTileLocations();
       // System.out.println("");
       // BOARD.testTileLocations();
       //BOARD.printTileLocations();
        VBox root = new VBox();
        root.setSpacing(SPACING);
        root.getChildren().addAll(SCORE.scoreboard, BOARD.board); // , BOARD.board
        root.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed((e) -> handleMove(e));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2048");
        primaryStage.show();
    }

    private void handleMove(KeyEvent e) {
        KeyCode keyPressed = e.getCode();

        if ((keyPressed == KeyCode.UP) || (keyPressed == KeyCode.W)) {
            this.BOARD.moveUp();
        } else if ((keyPressed == KeyCode.DOWN) || (keyPressed == KeyCode.S)) {
            this.BOARD.moveDown();
        } else if ((keyPressed == KeyCode.RIGHT) || (keyPressed == KeyCode.D)) {
            this.BOARD.moveRight();
        } else if ((keyPressed == KeyCode.LEFT) || (keyPressed == KeyCode.A)) {
            this.BOARD.moveLeft();
        }
        this.BOARD.randomSpawn();

        if (this.BOARD.lost()){
            //add loose screen
            this.BOARD.addLoseScreen(this);
        }
        if (this.BOARD.won()){
            this.BOARD.addVictoryScreen(this);
        }

    }
    public void restart(){
       this.start(primaryStage);
    }
}
