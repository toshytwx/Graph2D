package util;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TransformationUtil {

    public static Map<String, Point> transformEuclid(Map<String, Point> rawPoints, int rawAngle, Point transformationPoint) {
        Map<String, Point> transformedPoints = new HashMap<>();
        double ang = (-(rawAngle * Math.PI) / 180);
        for (Map.Entry<String, Point> entry : rawPoints.entrySet()) {
            int rawPointX = entry.getValue().x;
            int rawPointY = entry.getValue().y;
            int transformedX;
            int transformedY;
            if (rawAngle != 0) {
                transformedX = (int) (rawPointX * Math.cos(ang) + rawPointY * (-Math.sin(ang)) - transformationPoint.x * (Math.cos(ang) - 1) + transformationPoint.y * Math.sin(ang));
                transformedY = (int) (rawPointX * Math.sin(ang) + rawPointY * Math.cos(ang) - transformationPoint.y * (Math.cos(ang) - 1) - transformationPoint.x * Math.sin(ang));
            } else {
                transformedX = rawPointX + transformationPoint.x;
                transformedY = rawPointY + transformationPoint.y;
            }
            Point transformedPoint = new Point(transformedX, transformedY);
            transformedPoints.put(entry.getKey(), transformedPoint);
            System.out.print(rawPointX == transformedX);
        }
        return transformedPoints;
    }

    public static Map<String, Point> transformGraphAffin(Map<String, Point> rawPoints, Point newCenter, Point xDirection, Point yDirection) {
        Map<String, Point> transformedPoints = new HashMap<>();
        for (Map.Entry<String, Point> entry : rawPoints.entrySet()) {
            int rawX = entry.getValue().x;
            int rawY = entry.getValue().y;
            int transformedX = rawX * xDirection.x + rawY * xDirection.y + newCenter.x;
            int transformedY = rawX * yDirection.x + rawY * yDirection.y + newCenter.y;
            Point transformedPoint = new Point(transformedX, transformedY);
            transformedPoints.put(entry.getKey(), transformedPoint);
        }
        return transformedPoints;
    }
}
