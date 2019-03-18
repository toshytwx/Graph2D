package animation;

import bezier.BezierCurve;
import view.Graph;

import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

public abstract class AnimatorTask extends TimerTask {
    protected long t0;
    private Graphics graphics;
    private BezierCurve curveToAnimate;
    private Graph area;
    private int pointIndex;
    protected int increment;
    private boolean drawPoints;

    public AnimatorTask(Graphics graphics, BezierCurve curveToAnimate, int pointIndex, int increment, Graph area, boolean drawPoints) {
        this.t0 = System.currentTimeMillis();
        this.graphics = graphics;
        this.curveToAnimate = curveToAnimate;
        this.area = area;
        this.pointIndex = pointIndex;
        this.increment = increment;
        this.drawPoints = drawPoints;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - t0 > 2000) {
            cancel();
        } else {
            java.util.List<Point> bearingPoints = curveToAnimate.getBearingPoints();
            Point point = bearingPoints.get(pointIndex);
            increment(point);
            area.clearSurface(curveToAnimate.getMinXCoordinate(), curveToAnimate.getMinYCoordinate(), curveToAnimate.getMinMaxXDistance(), curveToAnimate.getMinMaxYDistance());
            curveToAnimate.setChanged(true);
            curveToAnimate.redraw(graphics, bearingPoints, drawPoints, pointIndex);
        }
        for (BezierCurve curve : area.getCurvesToRedraw(curveToAnimate)) {
            curve.draw(graphics);
        }
        for(MouseListener a: area.getInnerPane().getListeners(MouseListener.class)) {
            a.mouseReleased(new MouseEvent(area, 0, 0, 0,0,0,0, false) {
                //Nothing need go here, the actionPerformed method (with the
                //above arguments) will trigger the respective listener
            });
        }
    }

    protected abstract void increment(Point point);
}
