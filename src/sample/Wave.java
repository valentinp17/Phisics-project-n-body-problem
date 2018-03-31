package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Wave {
     private final int POINT_PER_PIXEL = 10;

     private double waveAmplitude;
     private double waveFrequency;
     private double phase;
     private ArrayList<Vector2D> wavePoints;
     private Color waveColor;
     private Orientation orientation;

     Wave(double waveAmplitude, double waveFrequency, double phase, Color waveColor, Orientation orientation) {
          this.waveAmplitude = waveAmplitude;
          this.waveFrequency = waveFrequency;
          this.phase = phase;
          this.waveColor = waveColor;
          this.orientation = orientation;
          wavePoints = new ArrayList<>();
          initWavePoints(this.orientation);
     }

     public void drawWave(GraphicsContext gc, double trans) {
          for(int i = 0; i < wavePoints.size() - 1; i++) {
               gc.setStroke(Color.color(waveColor.getRed(), waveColor.getGreen(), waveColor.getBlue(), trans));
               gc.setLineWidth(2);
               gc.strokeLine(wavePoints.get(i).getX(), wavePoints.get(i).getY(),
                       wavePoints.get(i + 1).getX(), wavePoints.get(i + 1).getY());
          }
     }

     public void updatePoints(double dp) {
          for(int i = 0; i < wavePoints.size(); i++) {
               double x = wavePoints.get(i).getX();
               double y = wavePoints.get(i).getY();

               phase = (phase + dp);

               if(orientation == Orientation.HORIZONTAL) {
                    wavePoints.get(i).replace(x, 300 + calcFunction(x));
               } else {
                    wavePoints.get(i).replace(300 + calcFunction(-y), y); // -y чтобы она двигалась вниз
               }
          }
     }

     private double calcFunction(double t) {
          return waveAmplitude * Math.sin(waveFrequency * t - phase);
     }

     private void initWavePoints(Orientation orientation) {
          if(orientation == Orientation.HORIZONTAL) {
               for(int i = 0; i < 600 * POINT_PER_PIXEL; i++) {
                    wavePoints.add(new Vector2D(i / POINT_PER_PIXEL, 300 + calcFunction(i)));
               }
          } else {
               for(int i = 0; i < 600 * POINT_PER_PIXEL; i++) {
                    wavePoints.add(new Vector2D(300 + calcFunction(i), i / POINT_PER_PIXEL));
               }
          }

     }

     public ArrayList<Vector2D> getWavePoints() {
          return wavePoints;
     }

     public void setWaveAmplitude(double waveAmplitude) {
          this.waveAmplitude = waveAmplitude;
     }

     public void setWaveFrequency(double waveFrequency) {
          this.waveFrequency = waveFrequency;
     }

     public double getWaveFrequency() {
          return waveFrequency;
     }
}
