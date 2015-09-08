package sample;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Xenocidist on 6/24/15.
 */
public class ScoreBoard {
    private static final double HEIGHT = 35;
    private static final double BUFFER = 150;

    private double WIDTH;

    public StackPane scoreboard;
    private Text scoreUpdate;
    private Text score;

    private int scoreInt;


    public ScoreBoard(double WIDTH){
        this.WIDTH = WIDTH;
        this.scoreboard = createScoreboard();
        this.scoreInt = 0;

    }
    private StackPane createScoreboard(){
        //group base and score
        HBox hb = new HBox();
        StackPane sp = new StackPane();
        //hb.setAlignment(Pos.CENTER);
        Group g = new Group();

        //base
        Rectangle base = new Rectangle();

        base.setHeight(HEIGHT);
        base.setWidth(WIDTH - BUFFER);
        base.setArcWidth(10);
        base.setArcHeight(10);
        base.setFill(Color.LIGHTGREY);

        //score
        Text score = new Text("0");
        score.setFill(Color.BLUE);
        score.setFont(new Font("Calibri", 20));
        this.score = score;

        //update
        Text scoreUpdate = new Text();
        scoreUpdate.setFill(Color.BLUE);
        scoreUpdate.setFont(new Font("Calibri", 12));
        this.scoreUpdate = scoreUpdate;

        g.getChildren().addAll(base, score, scoreUpdate);
        sp.getChildren().addAll(base, score, scoreUpdate);
        hb.getChildren().add(g);
        return sp;
    }
    public void updateScore(int scoreUpdate){
        this.scoreInt += scoreUpdate;
        this.score.setText(Integer.toString(scoreInt));
        this.scoreUpdate.setText("+"+Integer.toString(scoreUpdate));
        playFlickerAnimation();
    }

    private void playFlickerAnimation(){
        playSizeAnimation(this.scoreUpdate);
        playSlideAnimation(this.scoreUpdate);
    }
    private void playSizeAnimation(Text scoreUpdate){
        FadeTransition st = new FadeTransition(Duration.millis(100), scoreUpdate);
        st.setFromValue(.0001);
        st.setToValue(1);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }
    private void playSlideAnimation(Text scoreUpdate){
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), scoreUpdate);
        tt.setFromX(30); //figure out these values
        tt.setFromY(-10);
        tt.setToX(30);
        tt.setToY(10);
        tt.play();
    }
}
