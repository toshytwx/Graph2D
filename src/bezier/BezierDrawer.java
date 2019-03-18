package bezier;

import util.GraphConfig;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BezierDrawer {

    public static List<BezierCurve> initCurves() {
        List<BezierCurve> curves = new ArrayList<>();
        Map<Integer, List<Point>> pointsByFive = GraphConfig.getPointsBySix();
        for (Map.Entry<Integer, List<Point>> entry : pointsByFive.entrySet()) {
            List<Point> bearingPoints = entry.getValue();
            BezierCurve bezierCurve = new BezierCurve(entry.getKey(), bearingPoints);
            bezierCurve.setCoordinates(getCurvePointsByBearingPoints(bearingPoints));
            curves.add(bezierCurve);
        }
        return curves;
    }

    static List<Point> getCurvePointsByBearingPoints(List<Point> bearingPoints) {
        List<Point> curvePoints = new ArrayList<>();

        for (double t = 0; t <= 1; t += 0.01) {
            int x = (int) (Math.pow(1 - t, 5) * bearingPoints.get(0).x +
                    5 * t * Math.pow(1 - t, 4) * bearingPoints.get(1).x +
                    10 * Math.pow(t, 2) * Math.pow(1 - t, 3) * bearingPoints.get(2).x +
                    10 * Math.pow(t, 3) * Math.pow(1 - t, 2) * bearingPoints.get(3).x +
                    5 * Math.pow(t, 4) * (1 - t) * bearingPoints.get(4).x +
                    Math.pow(t, 5) * bearingPoints.get(5).x);
            int y = (int) (Math.pow(1 - t, 5) * bearingPoints.get(0).y +
                    5 * t * Math.pow(1 - t, 4) * bearingPoints.get(1).y +
                    10 * Math.pow(t, 2) * Math.pow(1 - t, 3) * bearingPoints.get(2).y +
                    10 * Math.pow(t, 3) * Math.pow(1 - t, 2) * bearingPoints.get(3).y +
                    5 * Math.pow(t, 4) * (1 - t) * bearingPoints.get(4).y +
                    Math.pow(t, 5) * bearingPoints.get(5).y);

            curvePoints.add(new Point(x, y));
        }

        return curvePoints;
    }
}
