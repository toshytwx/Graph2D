package animation;

import bezier.BezierCurve;
import view.Graph;

import java.awt.*;

public class YAnimatorTask extends AnimatorTask {
    public YAnimatorTask(Graphics graphics, BezierCurve curveToAnimate, int pointIndex, int increment, Graph area, boolean drawPoints) {
        super(graphics, curveToAnimate, pointIndex, increment, area, drawPoints);
    }

    @Override
    protected void increment(Point point) {
        if (System.currentTimeMillis() - t0 <= 1000) {
            point.y += increment;
        } else {
            point.y -= increment;
        }
    }
}
