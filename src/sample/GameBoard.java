package sample;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Xenocidist on 6/24/15.
 */
public class GameBoard {
    //display
    Group board;
    ScoreBoard scoreBoard;

    //display parameters
    private double TILE_SIZE;
    private static final int BUFFER = 10;
    private static final int SPACING = 5;
    private static final int STARTING_TILES = 2;
    private static final int BOARD_SIZE = 4;
    private static final double BOARD_DIMN = 400;

    //game information
    public int TileLocations[][];
    public ArrayList<GameTile> tiles;
    private double moved; //keep track of how many old ones moved
    private int scoreUpdate;
    private boolean alreadyWon;
    private int highestBlock;


    /**
     * Constructor
     */
    public GameBoard(ScoreBoard scoreBoard){
        this.TileLocations = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++ ){
            for (int j = 0; j < BOARD_SIZE; j++){
                TileLocations[i][j] = 0;
            }
        }
        this.tiles = new ArrayList<GameTile>(BOARD_SIZE * BOARD_SIZE);
        this.board = createBoard(BOARD_SIZE, BOARD_DIMN);
        this.scoreBoard = scoreBoard;
        this.scoreUpdate = 0;
        this.alreadyWon = false;
        this.moved = 0;
    }

    /**
     * @param BOARD_SIZE
     * @param BOARD_DIMN
     * @return
     */
    public Group createBoard(int BOARD_SIZE, double BOARD_DIMN){
        Group g = new Group();
        Rectangle base = new Rectangle();
        base.setHeight(BOARD_DIMN);
        base.setWidth(BOARD_DIMN);
        base.setArcWidth(10);
        base.setArcHeight(10);
        base.setFill(Color.LIGHTGREY);
        g.getChildren().add(base);

        TILE_SIZE = (BOARD_DIMN - (2 * BUFFER) - ((BOARD_SIZE - 1) * SPACING))   /  (BOARD_SIZE * 1.0);

        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                Rectangle r = new Rectangle();
                r.setWidth(TILE_SIZE);
                r.setHeight(TILE_SIZE);
                r.setFill(Color.DARKGRAY);
                r.setTranslateX(BUFFER + TILE_SIZE * i + SPACING * i);
                r.setTranslateY(BUFFER + TILE_SIZE * j + SPACING * j);
                r.setArcHeight(5);
                r.setArcWidth(5);
                g.getChildren().add(r);
            }
        }
        return g;
    }
    public void moveUp(){
        boolean win = false;
        for (int j = 0; j < BOARD_SIZE; j++){
            for (int i = 0; i < BOARD_SIZE; i++){

                //PUSH
                for (int x = 0; x < BOARD_SIZE; x++){
                    int z = x;
                    boolean found = false;

                    if(TileLocations[x][j] == 0){ //not filled find next
                        while (!found && z < BOARD_SIZE){
                            if(TileLocations[z][j] != 0){
                                found = true;
                                if (z!=x){
                                    updateDisplayPush(z,j,x,j,TileLocations[z][j]); //move it from x,y to x1,y2
                                    TileLocations[x][j] = TileLocations[z][j];
                                    TileLocations[z][j] = 0;
                                    moved++; //used for random or win stuff
                                } else {
                                }
                            }
                            z++;
                        }
                    } else {
                    }
                }
                //MERGE
                if((TileLocations[i][j] != 0)&&(i+1 <BOARD_SIZE)){
                    if(TileLocations[i+1][j] == TileLocations[i][j]){
                        moved++;
                        updateDisplayMerge(i+1, j, i, j, TileLocations[i][j] * 2);
                        TileLocations[i][j] *= 2;
                        TileLocations[i+1][j] = 0;
                        this.scoreUpdate+=TileLocations[i][j];
                        if (highestBlock > TileLocations[i][j]){
                            this.highestBlock = TileLocations[i][j];
                        }
                    }
                } else {
                }
            }
        }
        if (scoreUpdate != 0) scoreBoard.updateScore(scoreUpdate);
        scoreUpdate = 0;
    }
    public void moveDown(){
        for (int j = 0; j < BOARD_SIZE; j++){
            for (int i = BOARD_SIZE - 1; i > -1; i--){

                //PUSH
                for (int x = BOARD_SIZE - 1; x > -1; x--){
                    int z = x;
                    boolean found = false;

                    if(TileLocations[x][j] == 0){ //not filled find next
                        while (!found && z > -1){
                            if(TileLocations[z][j] != 0){
                                found = true;
                                TileLocations[x][j] = TileLocations[z][j];
                                if (z!=x){
                                    updateDisplayPush(z,j,x,j,TileLocations[z][j]); //move it from x,y to x1,y2
                                    TileLocations[z][j] = 0;
                                    moved++; //used for random or win stuff
                                }
                            }
                            z--;
                        }
                    } else {
                    }
                }
                //MERGE
                if((TileLocations[i][j] != 0)&&(i-1 > -1)){
                    if(TileLocations[i-1][j] == TileLocations[i][j]){
                        moved++;
                        updateDisplayMerge(i - 1, j, i, j, TileLocations[i][j] * 2);
                        TileLocations[i][j] *= 2;
                        TileLocations[i-1][j] = 0;
                        this.scoreUpdate += TileLocations[i][j];
                        if (highestBlock > TileLocations[i][j]){
                            this.highestBlock = TileLocations[i][j];
                        }
                    }
                }
            }
        }
        if (scoreUpdate != 0) scoreBoard.updateScore(scoreUpdate);
        scoreUpdate = 0;
    }
    public void moveLeft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {

                //PUSH
                for (int x = 0; x < BOARD_SIZE; x++) {
                    int z = x;
                    boolean found = false;

                    //not filled find next
                    if (TileLocations[i][x] == 0) while (!found && z < BOARD_SIZE) {
                        if (TileLocations[i][z] != 0) {
                            found = true;
                            if (z != x) {
                                updateDisplayPush(i, z, i, x, TileLocations[i][z]); //move it from x,y to x1,y2
                                TileLocations[i][x] = TileLocations[i][z];
                                TileLocations[i][z] = 0;
                                //UPDATE DISPLAY HERE!
                                moved++; //used for random or win stuff
                            } else {
                            }
                        }
                        z++;
                    }
                    else {
                    }
                }
                //MERGE
                if ((TileLocations[i][j] != 0) && (j + 1 < BOARD_SIZE)) {
                    if (TileLocations[i][j + 1] == TileLocations[i][j]) {
                        moved++;
                        updateDisplayMerge(i, j + 1, i, j, TileLocations[i][j] * 2); //move it from x,y to x1,y2
                        TileLocations[i][j] *= 2;
                        TileLocations[i][j + 1] = 0;
                        this.scoreUpdate += TileLocations[i][j];
                        if (highestBlock > TileLocations[i][j]){
                            this.highestBlock = TileLocations[i][j];
                        }
                        //UPDATE DISPLAY HERE!
                    }
                }
            }
        }
        if (scoreUpdate != 0) scoreBoard.updateScore(scoreUpdate);
        scoreUpdate = 0;
    }
    public void moveRight(){
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = BOARD_SIZE - 1; j > -1; j--){

                //PUSH
                for (int x = BOARD_SIZE - 1; x > -1; x--){
                    int z = x;
                    boolean found = false;

                    if(TileLocations[i][x] == 0){ //not filled find next
                        while (!found && z > -1){
                            if(TileLocations[i][z] != 0){
                                found = true;
                                TileLocations[i][x] = TileLocations[i][z];
                                if (z!=x){
                                    updateDisplayPush(i,z,i,x,TileLocations[i][z]); //move it from x,y to x1,y2
                                    TileLocations[i][z] = 0;
                                    moved++; //used for random or win stuff
                                }
                            }
                            z--;
                        }
                    } else {
                    }
                }
                //MERGE
                if((TileLocations[i][j] != 0)&&(j-1 > -1)){
                    if(TileLocations[i][j - 1] == TileLocations[i][j]){
                        updateDisplayMerge(i, j - 1, i, j, TileLocations[i][j] * 2); //move it from x,y to x1,y2
                        TileLocations[i][j] *= 2;
                        TileLocations[i][j-1] = 0;
                        this.scoreUpdate += TileLocations[i][j];
                        if (highestBlock > TileLocations[i][j]){
                            this.highestBlock = TileLocations[i][j];
                        }
                        moved++;
                    }
                }
            }
        }
        if (scoreUpdate != 0) scoreBoard.updateScore(scoreUpdate);
        scoreUpdate = 0;
    }

    /**
     * spawns a random tile to a random location
     */
    public void randomSpawn(){
        GameTile g = new GameTile();
        if (this.moved != 0) {
            randomLocation(g);
            g.setTileDisplay(TILE_SIZE);
            addTile(g);
            g.setCoordinates(BUFFER, SPACING, TILE_SIZE); //order?
        }
        this.moved = 0;
    }
    private void randomLocation(GameTile t){
        int freeLocations[][] = new int[2][16];
        int length = 0;
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (TileLocations[i][j] == 0){
                    freeLocations[0][length] = i;
                    freeLocations[1][length] = j;
                    length++;

                }
            }
        }
        System.out.println("freeLocations length = " + length);


        int index = (new Random()).nextInt(length); //this may need to be +1
        int value = Math.random() < 0.9 ? 2 : 4;

        System.out.println("index = " + index);

        this.TileLocations[freeLocations[0][index]][freeLocations[1][index]] = value;

        t.value = value;
        t.xLocation = freeLocations[0][index];
        t.yLocation = freeLocations[1][index];
    }

    private void addTile(GameTile t){
        this.tiles.add(t);
        this.board.getChildren().add(t.tileDisplay);
        t.playEntryAnimation();
    }
    private void removeTile(GameTile t){
        this.board.getChildren().remove(t.tileDisplay);
        this.tiles.remove(t);
    }

    public void testTileLocations(){
        int k = 0;

        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                TileLocations[i][j] = 0;
                k++;
            }
        }

        TileLocations[1][0] = 2;
        TileLocations[2][0] = 2;
        TileLocations[0][1] = 0;
    }

    /**
     * prints tile locations
     */
    public void printTileLocations(){
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                System.out.print(TileLocations[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /**
     * @return board size
     */
    public int getBoardSize(){
        return BOARD_SIZE;
    }

    /**
     * Fills board with starting tiles
     */
    public void populate(){
        for (int i = 0; i < STARTING_TILES; i++) {
            GameTile t = new GameTile();
            t.randomLocation(this, TILE_SIZE);
            t.setCoordinates(BUFFER, SPACING, TILE_SIZE);
            board.getChildren().add(t.tileDisplay);
        }
    }

    /**
     * findTile, finds a tile in the array list of gametiles
     * @param x = the xLoc
     * @param y = the yLoc
     * @return = the found GameTile
     */
    public GameTile findTile(int x, int y){
        int i = 0;
        for (GameTile tile : tiles){
            if (tile.isAt(x, y)){
                return tile;
            }
            i++;
        }
        return null;
    }

    private void updateDisplayMerge(int fromX, int fromY, int toX, int toY, int value){ //really want to delete after we merge
        GameTile tOld = findTile(toX, toY);
        GameTile tNew = findTile(fromX, fromY);
        TranslateTransition tt = tNew.deltaMerge(fromX, fromY, toX, toY, value, BUFFER, SPACING, TILE_SIZE);
        tt.setOnFinished(e -> {removeTile(tOld);});
    }

    private void updateDisplayPush(int fromX, int fromY, int toX, int toY, int value){
        findTile(fromX, fromY).deltaPush(fromX, fromY, toX, toY, value, BUFFER, SPACING, TILE_SIZE);
    }

    public boolean lost(){
        boolean value = true;
        //next / below  + last one is different
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                try {
                    if ((TileLocations[i][j] == TileLocations[i][j+1])||(TileLocations[i][j] == 0)){
                        value = false;
                        return value;
                    } //else no match
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Out of range");
                }

                try {
                    if ((TileLocations[i][j] == TileLocations[i+1][j])||(TileLocations[i][j] == 0)){
                        value = false;
                        return value;
                    } //else no match
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Out of range");
                }

                try {
                    if ((TileLocations[i][j] == TileLocations[i][j-1])||(TileLocations[i][j] == 0)){
                        value = false;
                        return value;
                    } //else no match
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Out of range");
                }

                try {
                    if ((TileLocations[i][j] == TileLocations[i-1][j])||(TileLocations[i][j] == 0)){
                        value = false;
                        return value;
                    } //else no match
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Out of range");
                }
            }
        }
        return true;
    }

    public void addLoseScreen(App app){
        StackPane sp = new StackPane();
        Rectangle r = new Rectangle();
        r.setHeight(BOARD_DIMN);
        r.setWidth(BOARD_DIMN);
        r.setFill(Color.DARKGRAY);
        r.setOpacity(.8);

        Text t = new Text();
        t.setText("GAME OVER");
        t.setFont(new Font("Calibri", 45));
        t.setFill(Color.FIREBRICK);
        t.setTranslateY(-25);

        //button
        StackPane button = new StackPane();
        button.setTranslateY(25);

        Rectangle rb = new Rectangle();
        rb.setHeight(50);
        rb.setWidth(100);
        rb.setFill(Color.DARKGREY);
        rb.setArcHeight(5);
        rb.setArcWidth(5);

        Text tb = new Text();
        tb.setText("Try Again!");
        tb.setFont(new Font("Calibri", 20));
        tb.setFill(Color.BLUE);
        button.getChildren().addAll(rb,tb);
        button.setOnMouseClicked(e -> app.restart());

        sp.getChildren().addAll(r,t,button);
        board.getChildren().add(sp);
    }

    public void addVictoryScreen(App app){
        StackPane sp = new StackPane();
        Rectangle r = new Rectangle();
        r.setHeight(BOARD_DIMN);
        r.setWidth(BOARD_DIMN);
        r.setFill(Color.DARKGRAY);
        r.setOpacity(.8);

        Text t = new Text();
        t.setText("VICTORY");
        t.setFont(new Font("Calibri", 45));
        t.setFill(Color.GREEN);

        //button - keep going

        //button - play again

        sp.getChildren().addAll(r,t);
        board.getChildren().add(sp);
    }

    public boolean won(){
        if (!this.alreadyWon && highestBlock == 2048){
            this.alreadyWon = true;
            return true;
        } else {
            return false;
        }

    }
}