package view;

import animation.XAnimatorTask;
import animation.YAnimatorTask;
import bezier.BezierCurve;
import bezier.BezierDrawer;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class Graph extends JDialog {
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 880;

    private JPanel contentPane;
    private JPanel innerPane;
    private JLabel coordinates;
    private JButton drawGraph;
    private JButton animate;
    private JCheckBox bearingPointsCheckBox;
    private List<BezierCurve> bezierCurves;
    private boolean drawPoints;

    public Graph() {
        setContentPane(contentPane);
        setModal(true);
        initListeners();
    }

    private void initListeners() {
        innerPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                drawCoordinates(e);
            }
        });
        innerPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                tryMoveBearingPoint(e);
            }
        });
        drawGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearSurface();
                drawGraph();
            }
        });
        bearingPointsCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                drawPoints = bearingPointsCheckBox.isSelected();
                if (drawPoints) {
                    drawBearingPoints();
                } else {
                    clearSurface();
                    drawGraph();
                }
            }
        });
        animate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addAnimationWithSound();
            }
        });
        innerPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (drawPoints) {
                    drawBearingPoints();
                }
            }
        });

    }

    private void addAnimationWithSound() {
        String soundName = "C:\\Users\\dmytro.antonkin\\Univ\\untitled5\\resources\\sound.wav";
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile())) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
            e1.printStackTrace();
        }
        if (bezierCurves != null) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new XAnimatorTask(innerPane.getGraphics(), bezierCurves.get(9), 1, 7, Graph.this, drawPoints), 50, 50);
            timer.scheduleAtFixedRate(new XAnimatorTask(innerPane.getGraphics(), bezierCurves.get(6), 1, 2, Graph.this, drawPoints), 100, 100);
            timer.scheduleAtFixedRate(new XAnimatorTask(innerPane.getGraphics(), bezierCurves.get(7), 3, 3, Graph.this, drawPoints), 100, 100);
            timer.scheduleAtFixedRate(new YAnimatorTask(innerPane.getGraphics(), bezierCurves.get(3), 2, 7, Graph.this, drawPoints), 50, 100);
            timer.scheduleAtFixedRate(new YAnimatorTask(innerPane.getGraphics(), bezierCurves.get(4), 1, 7, Graph.this, drawPoints), 50, 100);
        }
    }

    private void tryMoveBearingPoint(MouseEvent e) {
        List<Integer> possibleFaults = Arrays.asList(1, 2, -1, -2, 3, -3, 4, -4, 5, -5, 0);
        List<BezierCurve> idleCurves = Collections.emptyList();
        if (bezierCurves != null) {
            for (BezierCurve bezierCurve : bezierCurves) {
                List<Point> bearingPoints = bezierCurve.getBearingPoints();
                for (Point bearingPoint : bearingPoints) {
                    int x = e.getX() - bearingPoint.x;
                    int y = e.getY() - bearingPoint.y;
                    if (possibleFaults.contains(x) && possibleFaults.contains(y)) {
                        bezierCurve.setChanged(true);
                        List<Point> changedPoints = new ArrayList<>(bearingPoints);
                        int index = changedPoints.indexOf(bearingPoint);
                        clearSurface(bezierCurve.getMinXCoordinate(), bezierCurve.getMinYCoordinate(), bezierCurve.getMinMaxXDistance(), bezierCurve.getMinMaxYDistance());
                        changedPoints.remove(index);
                        changedPoints.add(index, new Point(e.getX(), e.getY()));
                        bezierCurve.redraw(innerPane.getGraphics(), changedPoints, drawPoints, index);
                        idleCurves = getCurvesToRedraw(bezierCurve);
                    }
                }
            }

            for (BezierCurve curve : idleCurves) {
                curve.draw(innerPane.getGraphics());
                if (drawPoints) {
                    curve.drawBearingPoints(innerPane.getGraphics());
                }
            }
        }
    }

    public List<BezierCurve> getCurvesToRedraw(BezierCurve bezierCurve) {
        ArrayList<BezierCurve> curves = new ArrayList<>(bezierCurves);
        curves.remove(bezierCurve);
        for (BezierCurve curve : curves) {
            curve.setChanged(true);
        }
        return curves;
    }

    private void drawCoordinates(MouseEvent e) {
        coordinates.setText("X: " + e.getX() + " Y: " + e.getY());
    }

    private void drawGraph() {
        bezierCurves = BezierDrawer.initCurves();
        for (BezierCurve bezierCurve : bezierCurves) {
            bezierCurve.draw(innerPane.getGraphics());
        }
        drawBearingPoints();
    }

    private void drawBearingPoints() {
        if (drawPoints) {
            for (BezierCurve bezierCurve : bezierCurves) {
                bezierCurve.drawBearingPoints(innerPane.getGraphics());
            }
        }
    }

    public JPanel getInnerPane() {
        return innerPane;
    }

    public void clearSurface() {
        Rectangle bounds = innerPane.getBounds();
        clearSurface(0, 0, bounds.width, bounds.height);
    }

    public void clearSurface(int x, int y, int width, int height) {
        Graphics graphics = innerPane.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.clearRect(x, y, width, height);
        graphics.fillRect(x, y, width, height);
    }
}
