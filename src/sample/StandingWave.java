package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class StandingWave {
     private final int POINT_PER_PIXEL = 10;

     private ArrayList<Vector2D> wavePoints;
     private Wave wave1;
     private Wave wave2;
     private Color waveColor;

     StandingWave(Wave wave1, Wave wave2, Color color) {
          this.wave1 = wave1;
          this.wave2 = wave2;
          this.waveColor = color;
          wavePoints = new ArrayList<>();
          initWavePoints();
     }

     private void initWavePoints() {
          for(int i = 0; i < 600 * POINT_PER_PIXEL; i++) {
               wavePoints.add(new Vector2D(i / POINT_PER_PIXEL, 300));
          }
     }

     public void updatePoints() {
          ArrayList<Vector2D> wavePoints1 = wave1.getWavePoints();
          ArrayList<Vector2D> wavePoints2 = wave2.getWavePoints();
          for(int i = 0; i < wavePoints.size(); i++) {
               wavePoints.get(i).replace(wavePoints.get(i).getX(),  wavePoints1.get(i).getY()
                       + wavePoints2.get(i).getY() - 300);
          }
     }

     public void drawWave(GraphicsContext gc) {
          for(int i = 0; i < wavePoints.size() - 1; i++) {
               gc.setStroke(waveColor);
               gc.setLineWidth(2);
               gc.strokeLine(wavePoints.get(i).getX(), wavePoints.get(i).getY(),
                       wavePoints.get(i + 1).getX(), wavePoints.get(i + 1).getY());
          }
     }
}
