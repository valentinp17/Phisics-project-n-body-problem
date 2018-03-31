package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static final int TIME_STEP = 1000 / 60;
    private double waveSpeed = 0.000008;
    //1 - draw standing wave
    //0 - draw lissajous curve
    private boolean showStandingOrLissajous = true;

    private static final double FREQUENCY_COEFF = 20;
    private static double TRANSPARENCY = 1.0;

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Standing wave");

        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        canvas = new Canvas();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawAxis(gc);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        Wave wave1 = new Wave(50, 0.05,0,
                Color.color(1, 0.5216, 0.102, TRANSPARENCY), Orientation.HORIZONTAL);
        Wave wave2 = new Wave(50, 0.05,0,
                Color.color(0.2588, 0.298, 1, TRANSPARENCY), Orientation.HORIZONTAL);
         Wave wave3 = new Wave(50, 0.05,0,
                 Color.color(0.2588, 0.298, 1), Orientation.VERTICAL);
        StandingWave standingWave = new StandingWave(wave1, wave2, Color.color(0, 0, 0));
        LissajousCurve lissajousCurve = new LissajousCurve(wave1, wave3, Color.color(0.1255, 0.7961, 0.2431));

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(TIME_STEP), new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent t) {
                   gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                   drawAxis(gc);

                   wave1.updatePoints(waveSpeed);
                   wave1.drawWave(gc, TRANSPARENCY);

                   wave2.updatePoints(-waveSpeed);
                   wave3.updatePoints(-waveSpeed);

                   if(showStandingOrLissajous) {
                        wave2.drawWave(gc, TRANSPARENCY);
                        standingWave.updatePoints();
                        standingWave.drawWave(gc);
                   } else {
                        wave3.drawWave(gc, TRANSPARENCY);

                        lissajousCurve.updatePoints();
                        lissajousCurve.drawWave(gc);
                   }
              }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        scene.setOnKeyReleased((KeyEvent e) -> {
             if(timeline.getStatus() == Animation.Status.PAUSED) {
                  timeline.play();

             } else {
                  timeline.pause();
             }
        });

         //------------------------------------- Add start nodes
         Pane leftPane = new Pane();
         leftPane.setPrefSize(200, 600);

         canvas.setHeight(600);
         canvas.setWidth(600);

         root.setLeft(leftPane);
         root.setCenter(canvas);

         VBox vBox1 = new VBox();
         vBox1.setPadding(new Insets(10, 10, 10, 10));
         leftPane.getChildren().add(vBox1);

         //Wave 1 interface
         Label wave1Label = new Label("WAVE 1");

         Slider wave1AmplitudeSlider = new Slider(0, 200, 50);
         wave1AmplitudeSlider.setMaxSize(180, 20);
         wave1AmplitudeSlider.setPrefSize(180, 20);
         Label wave1AmplitudeLabel = new Label("Amplitude " + wave1AmplitudeSlider.getValue());

         wave1AmplitudeSlider.valueProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                   wave1AmplitudeLabel.textProperty().setValue( "Amplitude " +
                           String.valueOf((int) wave1AmplitudeSlider.getValue()));
                   wave1.setWaveAmplitude((int) wave1AmplitudeSlider.getValue());
              }
         });

         Slider wave1FrequencySlider = new Slider(0.001, 0.1, 0.05);
         Label wave1FrequencyLabel = new Label("Frequency " + wave1FrequencySlider.getValue() * FREQUENCY_COEFF);

         wave1FrequencySlider.setMaxSize(180, 20);
         wave1FrequencySlider.setPrefSize(180, 20);

         wave1FrequencySlider.valueProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                   wave1FrequencyLabel.textProperty().setValue("Frequency " +
                           String.valueOf((Math.round(wave1FrequencySlider.getValue() * 100) / 100.0) * FREQUENCY_COEFF));
                   wave1.setWaveFrequency(Math.round(wave1FrequencySlider.getValue() * 100) / 100.0);
              }
         });

         vBox1.getChildren().addAll(wave1Label, wave1AmplitudeLabel, wave1AmplitudeSlider,
                 wave1FrequencyLabel, wave1FrequencySlider);


         //Wave 2 interface
         Label wave2Label = new Label("WAVE 2");

         VBox vBox2 = new VBox();
         vBox2.setLayoutY(120);
         vBox2.setPadding(new Insets(10, 10, 10, 10));
         leftPane.getChildren().add(vBox2);

         Slider wave2AmplitudeSlider = new Slider(0, 200, 50);
         wave2AmplitudeSlider.setMaxSize(180, 20);
         wave2AmplitudeSlider.setPrefSize(180, 20);
         Label wave2AmplitudeLabel = new Label("Amplitude " + wave1AmplitudeSlider.getValue());

         wave2AmplitudeSlider.valueProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                   if(showStandingOrLissajous) {
                        wave2AmplitudeLabel.textProperty().setValue( "Amplitude " +
                                String.valueOf((int) wave2AmplitudeSlider.getValue()));
                        wave2.setWaveAmplitude((int) wave2AmplitudeSlider.getValue());
                   } else {
                        wave2AmplitudeLabel.textProperty().setValue( "Amplitude " +
                                String.valueOf((int) wave2AmplitudeSlider.getValue()));
                        wave3.setWaveAmplitude((int) wave2AmplitudeSlider.getValue());
                   }

              }
         });

         Slider wave2FrequencySlider = new Slider(0.001, 0.1, 0.05);
         Label wave2FrequencyLabel = new Label("Frequency " + wave1FrequencySlider.getValue() * FREQUENCY_COEFF);

         wave2FrequencySlider.setMaxSize(180, 20);
         wave2FrequencySlider.setPrefSize(180, 20);

         wave2FrequencySlider.valueProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                   if(showStandingOrLissajous) {
                        wave2FrequencyLabel.textProperty().setValue("Frequency " +
                                String.valueOf((Math.round(wave2FrequencySlider.getValue() * 100) / 100.0)* FREQUENCY_COEFF));
                        wave2.setWaveFrequency((Math.round(wave2FrequencySlider.getValue() * 100) / 100.0));
                   } else {
                        wave2FrequencyLabel.textProperty().setValue("Frequency " +
                                String.valueOf((Math.round(wave2FrequencySlider.getValue()* 100) / 100.0)* FREQUENCY_COEFF ));
                        wave3.setWaveFrequency((Math.round(wave2FrequencySlider.getValue() * 100) / 100.0));
                   }

              }
         });

         vBox2.getChildren().addAll(wave2Label, wave2AmplitudeLabel, wave2AmplitudeSlider,
                 wave2FrequencyLabel, wave2FrequencySlider);

         VBox vBox3 = new VBox();
         vBox2.setLayoutY(240);
         vBox2.setPadding(new Insets(10, 10, 10, 10));
         leftPane.getChildren().add(vBox3);

         //-------------------------------------
         // transperency slider
         Slider wavesTransperencySlider = new Slider(0.0, 1.0, 1.0);
         wavesTransperencySlider.setMaxSize(180, 20);
         wavesTransperencySlider.setPrefSize(180, 20);

         wavesTransperencySlider.valueProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                   TRANSPARENCY = wavesTransperencySlider.getValue() ;
              }
         });


         vBox3.getChildren().addAll(wavesTransperencySlider);
         vBox3.setLayoutY(530);

         //Radio buttons
         //-------------------------------------
         ToggleGroup toggleGroup = new ToggleGroup();

         RadioButton rb1 = new RadioButton("Standing wave");
         rb1.setToggleGroup(toggleGroup);
         rb1.setSelected(true);
         rb1.setUserData("Standing Wave");


         RadioButton rb2 = new RadioButton("Lissajous curve");
         rb2.setToggleGroup(toggleGroup);
         rb1.setUserData("Lissajous Curve");

         toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
              @Override
              public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                   if(rb1.isSelected()) {
                        wave2.setWaveFrequency(wave3.getWaveFrequency());
                        showStandingOrLissajous = true;
                   }
                   if(rb2.isSelected()) {
                        wave3.setWaveFrequency(wave2.getWaveFrequency());
                        showStandingOrLissajous = false;

                   }
              }
         });

         VBox vBox4 = new VBox();
         vBox4.getChildren().addAll(rb1, rb2);
         vBox4.setLayoutY(550);
         vBox4.setPadding(new Insets(10, 10, 10, 10));
         leftPane.getChildren().add(vBox4);
         //-------------------------------------

    }

    private void drawAxis(GraphicsContext gc) {
         gc.setStroke(new Color(0.2078, 0.2078, 0.2078, 0.3176));
         gc.setLineWidth(2);
         gc.setFill(Color.color(0, 0, 0));
         //Axis
         gc.strokeLine(0,0, 0, canvas.getHeight());
         gc.strokeLine(0,canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);

         //Arrows
         gc.strokeLine(0, 0, 5, 5);

         gc.strokeLine(600, 300, 595, 305);
         gc.strokeLine(600, 300, 595, 295);

         //divisions
         gc.setLineWidth(1);
         for(int i = 0; i < 600; i += 120) {
              gc.fillText(i / 120 + "", i - 3, 315);
              gc.strokeLine(i, 295, i, 305);
         }

         for(int i = 10; i < 600; i += 20) {
              gc.strokeLine(0, i, 5, i);
         }

         // dimensions
         gc.fillText("U", 10, 10);
         gc.fillText("t", 590, 315);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
