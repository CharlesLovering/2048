package sample;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Xenocidist on 6/24/15.
 */
public class GameTile {
    public int value = 0;
    public int xLocation;
    public int yLocation;

    protected StackPane tileDisplay;

    protected GameTile() {
        this.tileDisplay = new StackPane();
    }

    private void updateTileDisplay(double TILE_SIZE) {

        Rectangle base = new Rectangle();
        base.setHeight(TILE_SIZE);
        base.setWidth(TILE_SIZE);
        base.setArcWidth(5);
        base.setArcHeight(5);

        Text text = new Text(Integer.toString(this.value));
        text.setFill(Color.WHITE);
        text.setFont(new Font("Calibri", 36));
        addColor(base, text, value, "Cool Ice");
        this.tileDisplay.getChildren().set(0, base);
        this.tileDisplay.getChildren().set(1, text);
        StackPane.setAlignment(text, Pos.CENTER);
    }

    public void setTileDisplay(double TILE_SIZE) {
        StackPane display = new StackPane();
        Rectangle base = new Rectangle();
        base.setHeight(TILE_SIZE);
        base.setWidth(TILE_SIZE);
        base.setArcWidth(5);
        base.setArcHeight(5);

        Text text = new Text(Integer.toString(this.value));
        text.setFill(Color.WHITE);
        text.setFont(new Font("Calibri", 36));
        addColor(base, text, value, "Cool Ice");
        display.getChildren().addAll(base, text);
        StackPane.setAlignment(text, Pos.CENTER);
        this.tileDisplay = display;
    }

    public void randomLocation(GameBoard board, double TILE_SIZE) {
        int rowIndex = (new Random()).nextInt(board.getBoardSize());
        int columnIndex = (new Random()).nextInt(board.getBoardSize());
        for (int j = 0; j < board.getBoardSize(); j++) {
            for (int i = 0; i < board.getBoardSize(); i++) {
                if (board.TileLocations[rowIndex][columnIndex] == 0) { //if not visited
                    int tVal = Math.random() < 0.9 ? 2 : 4;
                    board.TileLocations[rowIndex][columnIndex] = tVal;
                    this.value = tVal;
                    this.xLocation = rowIndex;
                    this.yLocation = columnIndex;
                    board.tiles.add(this);
                    this.setTileDisplay(TILE_SIZE);
                    return;
                } else {
                    columnIndex = (columnIndex + 1) % 3;
                }
            }
            rowIndex = (rowIndex + 1) % 3;
        }
    }

    public boolean isAt(int x, int y) {
        if ((this.xLocation == x) && (this.yLocation == y)) {
            return true;
        } else return false;
    }

    public void setCoordinates(int BUFFER, int SPACING, double TILE_SIZE) {
        this.tileDisplay.setTranslateY(BUFFER + TILE_SIZE * this.xLocation + SPACING * this.xLocation);
        this.tileDisplay.setTranslateX(BUFFER + TILE_SIZE * this.yLocation + SPACING * this.yLocation);
    }

    public TranslateTransition deltaMerge(int fromX, int fromY, int toX, int toY, int value, int BUFFER, int SPACING, double TILE_SIZE) {
        this.xLocation = toX;
        this.yLocation = toY;
        this.value = value;
        TranslateTransition tt = playMove(fromX, fromY, toX, toY, BUFFER, SPACING, TILE_SIZE);
        updateTileDisplay(TILE_SIZE);
        playBounceAnimation();
        return tt;
    }

    public void deltaPush(int fromX, int fromY, int toX, int toY, int value, int BUFFER, int SPACING, double TILE_SIZE) {
        this.xLocation = toX;
        this.yLocation = toY;
        this.value = value;
        playMove(fromX, fromY, toX, toY, BUFFER, SPACING, TILE_SIZE);
        updateTileDisplay(TILE_SIZE);
    }

    /**
     * Transforms i,j coordinate to relative board position
     */
    public double transformCoordinate(int coord, int BUFFER, int SPACING, double TILE_SIZE) {
        return (BUFFER + TILE_SIZE * coord + SPACING * coord);
    }

    public void playBounceAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), this.tileDisplay);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.1);
        st.setToY(1.1);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    public TranslateTransition playMove(int fromX, int fromY, int toX, int toY, int BUFFER, int SPACING, double TILE_SIZE) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), this.tileDisplay);
        tt.setFromX(tileDisplay.getTranslateX());
        tt.setFromY(tileDisplay.getTranslateY());
        tt.setToX(transformCoordinate(yLocation, BUFFER, SPACING, TILE_SIZE));
        tt.setToY(transformCoordinate(xLocation, BUFFER, SPACING, TILE_SIZE));
        tt.play();
        return tt;
    }

    public void playEntryAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), this.tileDisplay);
        st.setFromX(.4);
        st.setFromY(.4);
        st.setToX(1);
        st.setToY(1);
        st.setCycleCount(1);
        st.play();
    }

    private void addColor(Rectangle base, Text text, int value, String palette) {
        switch (palette) {
            case "Original":
                setColor0(base, text, value);
                break;
            case "Fun":
                setColor2(base, text, value);
                break;
            case "Three":
                setColor1(base, text, value);
                break;
            case "Cool Ice":
                setColor3(base, text, value);
                break;
        }
    }

    private void setColor0(Rectangle base, Text text, int value) {
        switch (value) {
            case 2:
                base.setFill(Color.MINTCREAM);
                text.setFill(Color.BLACK);
                break;
            case 4:
                base.setFill(Color.MEDIUMSPRINGGREEN);
                text.setFill(Color.BLACK);
                break;
            case 8:
                base.setFill(Color.CYAN);
                text.setFill(Color.BLACK);
                break;
            case 16:
                base.setFill(Color.ROYALBLUE);
                break;
            case 32:
                base.setFill(Color.MIDNIGHTBLUE);
                break;
            case 64:
                base.setFill(Color.DARKCYAN);
                break;
            case 128:
                base.setFill(Color.BLUE);
                break;
            case 256:
                base.setFill(Color.STEELBLUE);
                break;
            case 512:
                base.setFill(Color.DEEPSKYBLUE);
                break;
            case 1024:
                base.setFill(Color.BLUEVIOLET);
                break;
            case 2048:
                base.setFill(Color.GOLD);
                //VBox victoryScreen = win();
                break;
            case 4096:
                base.setFill(Color.HOTPINK);
                break;
            case 8192:
                base.setFill(Color.BLACK);
        }
    }

    private void setColor1(Rectangle base, Text text, int value) {
        switch (value) {
            case 2:
                base.setFill(Color.web("#e41a1c"));
                break;
            case 4:
                base.setFill(Color.web("#377eb8"));
                break;
            case 8:
                base.setFill(Color.web("#4daf4a"));
                break;
            case 16:
                base.setFill(Color.web("#984ea3"));
                break;
            case 32:
                base.setFill(Color.web("#ffzf00"));
                break;
            case 64:
                base.setFill(Color.web("#ffff33"));
                break;
            case 128:
                base.setFill(Color.web("#f781bf"));
                break;
            case 256:
                base.setFill(Color.STEELBLUE);
                break;
            case 512:
                base.setFill(Color.DEEPSKYBLUE);
                break;
            case 1024:
                base.setFill(Color.BLUEVIOLET);
                break;
            case 2048:
                base.setFill(Color.GOLD);
                //VBox victoryScreen = win();
                break;
            case 4096:
                base.setFill(Color.HOTPINK);
                break;
            case 8192:
                base.setFill(Color.BLACK);
        }
    }

    private void setColor2(Rectangle base, Text text, int value) {
        switch (value) {
            case 2:
                base.setFill(Color.web("#1b9e77"));
                //text.setFill(Color.BLACK);
                break;
            case 4:
                base.setFill(Color.web("#d95f02"));
                text.setFill(Color.BLACK);
                break;
            case 8:
                base.setFill(Color.web("#7570b3"));
                text.setFill(Color.BLACK);
                break;
            case 16:
                base.setFill(Color.web("#e7298a"));
                break;
            case 32:
                base.setFill(Color.web("#66a61e"));
                break;
            case 64:
                base.setFill(Color.web("#e6ab92"));
                break;
            case 128:
                base.setFill(Color.web("#a6761d"));
                break;
            case 256:
                base.setFill(Color.web("#666666"));
                break;
            case 512:
                base.setFill(Color.DEEPSKYBLUE);
                break;
            case 1024:
                base.setFill(Color.BLUEVIOLET);
                break;
            case 2048:
                base.setFill(Color.GOLD);
                //VBox victoryScreen = win();
                break;
            case 4096:
                base.setFill(Color.HOTPINK);
                break;
            case 8192:
                base.setFill(Color.BLACK);
        }
    }

    private void setColor3(Rectangle base, Text text, int value){
        switch (value) {
            case 2:
                base.setFill(Color.web("#ffffcc"));
                text.setFill(Color.BLACK);
                break;
            case 4:
                base.setFill(Color.web("#c7e9b4"));
                text.setFill(Color.BLACK);
                break;
            case 8:
                base.setFill(Color.web("#7fcdbb"));
                //text.setFill(Color.BLACK);
                break;
            case 16:
                base.setFill(Color.web("#41b6c4"));
                break;
            case 32:
                base.setFill(Color.web("#2c7fb8"));
                break;
            case 64:
                base.setFill(Color.web("#253494"));
                break;
            case 128:
                base.setFill(Color.web("#1b7837"));
                break;
            case 256:
                base.setFill(Color.web("#666666"));
                break;
            case 512:
                base.setFill(Color.web("#762a83"));
                break;
            case 1024:
                base.setFill(Color.BLUEVIOLET);
                break;
            case 2048:
                base.setFill(Color.GOLD);
                //VBox victoryScreen = win();
                break;
            case 4096:
                base.setFill(Color.HOTPINK);
                break;
            case 8192:
                base.setFill(Color.BLACK);
        }
    }



}