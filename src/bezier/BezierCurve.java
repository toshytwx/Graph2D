package bezier;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BezierCurve {
    private static final int POINT_R = 4;

    private String id;
    private List<Point> bearingPoints;
    private boolean changed;
    private List<Point> coordinates;

    BezierCurve(Integer id, List<Point> bearingPoints) {
        this.id = String.valueOf(id);
        this.bearingPoints = bearingPoints;
        this.changed = true;
    }

    public String getId() {
        return id;
    }

    public List<Point> getBearingPoints() {
        return bearingPoints;
    }

    public boolean isChanged() {
        return changed;
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    void setCoordinates(List<Point> coordinates) {
        this.coordinates = coordinates;
    }

    public void draw(Graphics graphics) {
        if (changed) {
            for (int i = 1; i < coordinates.size(); i++) {

                Point prev = coordinates.get(i - 1);
                Point next = coordinates.get(i);

                graphics.drawLine(prev.x, prev.y, next.x, next.y);
            }
            changed = false;
        }
    }


    public void drawBearingPoints(Graphics graphics, int... indexes) {
        for (int i = 0; i < bearingPoints.size(); i++) {
            Point bearingPoint = bearingPoints.get(i);
            if (indexes.length == 0 || i != indexes[0]) {
                int diameter = POINT_R * 2;
                graphics.drawOval(bearingPoint.x - POINT_R, bearingPoint.y - POINT_R, diameter, diameter);
            }
        }
    }

    public void redraw(Graphics graphics, List<Point> changedBearingPoints, boolean drawPoints, int index) {
        this.coordinates = BezierDrawer.getCurvePointsByBearingPoints(changedBearingPoints);
        this.bearingPoints = changedBearingPoints;
        draw(graphics);
        if (drawPoints) {
            drawBearingPoints(graphics, index);
        }
    }

    public int getMinXCoordinate() {
        List<Integer> xCoordinates = new ArrayList<>();
        for (Point coordinate : coordinates) {
            xCoordinates.add(coordinate.x);
        }
        for (Point bearingPoint : bearingPoints) {
            xCoordinates.add(bearingPoint.x + POINT_R);
        }
        return Collections.min(xCoordinates);
    }

    public int getMaxXCoordinate() {
        List<Integer> xCoordinates = new ArrayList<>();
        for (Point coordinate : coordinates) {
            xCoordinates.add(coordinate.x);
        }
        for (Point bearingPoint : bearingPoints) {
            xCoordinates.add(bearingPoint.x + POINT_R);
        }
        return Collections.max(xCoordinates);
    }

    public int getMaxYCoordinate() {
        List<Integer> yCoordinates = new ArrayList<>();
        for (Point coordinate : coordinates) {
            yCoordinates.add(coordinate.y);
        }
        for (Point bearingPoint : bearingPoints) {
            yCoordinates.add(bearingPoint.y + POINT_R);
        }
        return Collections.max(yCoordinates);
    }

    public int getMinYCoordinate() {
        List<Integer> yCoordinates = new ArrayList<>();
        for (Point coordinate : coordinates) {
            yCoordinates.add(coordinate.y);
        }
        for (Point bearingPoint : bearingPoints) {
            yCoordinates.add(bearingPoint.y + POINT_R);
        }
        return Collections.min(yCoordinates);
    }

    public int getMinMaxXDistance() {
        return getMaxXCoordinate() - getMinXCoordinate();
    }

    public int getMinMaxYDistance() {
        return getMaxYCoordinate() - getMinYCoordinate();
    }
}
