package animation;

import bezier.BezierCurve;
import view.Graph;

import java.awt.*;

public class XAnimatorTask extends AnimatorTask {
    public XAnimatorTask(Graphics graphics, BezierCurve curveToAnimate, int pointIndex, int increment, Graph area, boolean drawPoints) {
        super(graphics, curveToAnimate, pointIndex, increment, area, drawPoints);
    }

    @Override
    protected void increment(Point point) {
        if (System.currentTimeMillis() - t0 <= 500) {
            point.x += increment;
        } else {
            point.x -= increment;
        }
    }
}
