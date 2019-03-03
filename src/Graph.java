import util.TransformationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph extends JDialog {
    private static final int CELL_WIDTH = 10;
    private static final int CELL_HEIGHT = 10;
    static final int WIDTH = 1080;
    static final int HEIGHT = 780;
    private static final Color CUSTOM_GRAY = new Color(128, 128, 128, 64);
    private static final Color CUSTOM_BLACK = new Color(0, 0, 0, 128);

    private JPanel contentPane;
    private JPanel innerPane;
    private JPanel controlPane;
    private JLabel coordinates;
    private JButton adjustParams;
    private JButton drawGraph;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JButton redrawEuclid;
    private JSpinner spinner6;
    private JSpinner spinner5;
    private JSpinner spinner4;
    private JSpinner spinner7;
    private JSpinner spinner8;
    private JSpinner spinner9;
    private JButton redrawAffinn;
    private JButton redrawProjective;
    private JSpinner spinner10;
    private JSpinner spinner11;
    private JSpinner spinner12;
    private JSpinner spinner13;
    private JSpinner spinner14;
    private JSpinner spinner15;
    private JSpinner spinner16;
    private JSpinner spinner17;
    private JSpinner spinner18;
    private Point graphCentre;
    private Map<String, Point> graphPoints;
    private Map<Point, Point> gridPoints;
    private Map<Point, Point> axisPoints;
    private List<Point> arcPoints;

    Graph() {
        setContentPane(contentPane);
        setModal(true);
        initListeners();
        graphPoints = new HashMap<>();
        gridPoints = new HashMap<>();
        axisPoints = new HashMap<>();
        arcPoints = new ArrayList<>();
    }

    private void initListeners() {
        innerPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                drawCoordinates(e);
            }
        });
        drawGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                initCenter();
                clearSurface();
                drawGraph();
            }
        });
        adjustParams.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Params dialog = new Params();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        redrawEuclid.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearSurface();
                graphPoints = TransformationUtil.transformEuclid(graphPoints, (Integer) spinner3.getValue(), new Point((Integer) spinner1.getValue(), (Integer) spinner2.getValue()));
                drawGraph();
            }
        });
        redrawAffinn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearSurface(false);
                graphPoints = TransformationUtil.transformGraphAffin(graphPoints,
                        new Point((Integer) spinner4.getValue(), (Integer) spinner7.getValue()),
                        new Point((Integer) spinner5.getValue(), (Integer) spinner8.getValue()),
                        new Point((Integer) spinner6.getValue(), (Integer) spinner9.getValue()));
                gridPoints = TransformationUtil.transformGridAffin(gridPoints,
                        new Point((Integer) spinner4.getValue(), (Integer) spinner7.getValue()),
                        new Point((Integer) spinner5.getValue(), (Integer) spinner8.getValue()),
                        new Point((Integer) spinner6.getValue(), (Integer) spinner9.getValue()));
                axisPoints = TransformationUtil.transformGridAffin(axisPoints,
                        new Point((Integer) spinner4.getValue(), (Integer) spinner7.getValue()),
                        new Point((Integer) spinner5.getValue(), (Integer) spinner8.getValue()),
                        new Point((Integer) spinner6.getValue(), (Integer) spinner9.getValue()));
                drawGrid();
                drawAxis();
                drawGraph();
            }
        });
        redrawProjective.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearSurface(false);
                graphPoints = TransformationUtil.transformGraphProjective(graphPoints,
                        new Point((Integer) spinner10.getValue(), (Integer) spinner11.getValue()), (Integer) spinner12.getValue(),
                        new Point((Integer) spinner13.getValue(), (Integer) spinner14.getValue()), (Integer) spinner15.getValue(),
                        new Point((Integer) spinner16.getValue(), (Integer) spinner17.getValue()), (Integer) spinner18.getValue());
                gridPoints = TransformationUtil.transformGridProjective(gridPoints,
                        new Point((Integer) spinner10.getValue(), (Integer) spinner11.getValue()), (Integer) spinner12.getValue(),
                        new Point((Integer) spinner13.getValue(), (Integer) spinner14.getValue()), (Integer) spinner15.getValue(),
                        new Point((Integer) spinner16.getValue(), (Integer) spinner17.getValue()), (Integer) spinner18.getValue());
                axisPoints = TransformationUtil.transformGridProjective(axisPoints,
                        new Point((Integer) spinner10.getValue(), (Integer) spinner11.getValue()), (Integer) spinner12.getValue(),
                        new Point((Integer) spinner13.getValue(), (Integer) spinner14.getValue()), (Integer) spinner15.getValue(),
                        new Point((Integer) spinner16.getValue(), (Integer) spinner17.getValue()), (Integer) spinner18.getValue());
                drawGrid();
                drawAxis();
                drawGraph();
            }
        });
    }

    private void initCirclePoints(int radius, int centerX, int centerY, String key) {
        Point E = arcPoints.get(0);
        Point F = arcPoints.get(1);
        for (int i = 0; i < 360; i++) {
            for (double j = 1; j < 2; j += 0.5) {
                int x = (int) (centerX + radius * Math.cos(i / j));
                int y = (int) (centerY + radius * Math.sin(i / j));
                if ((x >= E.x && x >= F.x) || (y >= E.y && y <= F.y)) {
                    this.graphPoints.put(key + i / j, new Point(x, y));
                }
            }
        }
    }

    private void drawCoordinates(MouseEvent e) {
        coordinates.setText("X: " + e.getX() + " Y: " + e.getY());
    }

    private void drawGraph() {
        Graphics graphics = innerPane.getGraphics();
        graphics.setColor(CUSTOM_BLACK);
        drawGraphLines(graphics);
        drawCircle(graphics);
        drawCircle(graphics);
    }

    private void initLinePoints() {
        Point A = new Point(graphCentre.x - GraphConfig.WIDTH_1, graphCentre.y - GraphConfig.HEIGHT_3);
        Point B = new Point(graphCentre.x - GraphConfig.WIDTH_1, A.y + GraphConfig.HEIGHT_1);
        Point C = new Point(graphCentre.x - GraphConfig.WIDTH_1 + GraphConfig.WIDTH_2, graphCentre.y + GraphConfig.HEIGHT_2 + GraphConfig.HEIGHT_3);
        Point D = new Point(graphCentre.x - GraphConfig.WIDTH_1 + GraphConfig.WIDTH_2, graphCentre.y + GraphConfig.HEIGHT_3);
        Point E = new Point(getArcIntersection(), graphCentre.y + GraphConfig.HEIGHT_3);
        Point F = new Point(getArcIntersection(), graphCentre.y - GraphConfig.HEIGHT_3);

        graphPoints.put("A", A);
        graphPoints.put("B", B);
        graphPoints.put("C", C);
        graphPoints.put("D", D);
        graphPoints.put("E", E);
        graphPoints.put("F", F);

        arcPoints.add(E);
        arcPoints.add(F);
    }

    private void drawCircle(Graphics graphics) {
        for (Map.Entry<String, Point> entry : graphPoints.entrySet()) {
            if (entry.getKey().contains("ARC") || entry.getKey().contains("CIRCLE")) {
                Point value = entry.getValue();
                graphics.drawLine(value.x, value.y, value.x, value.y);
            }
        }
    }

    private void drawGraphLines(Graphics graphics) {
        Point a = graphPoints.get("A");
        Point b = graphPoints.get("B");
        Point c = graphPoints.get("C");
        Point d = graphPoints.get("D");
        Point e = graphPoints.get("E");
        Point f = graphPoints.get("F");

        graphics.drawLine(a.x, a.y, b.x, b.y);
        graphics.drawLine(b.x, b.y, c.x, c.y);
        graphics.drawLine(c.x, c.y, d.x, d.y);
        graphics.drawLine(d.x, d.y, e.x, e.y);
        graphics.drawLine(a.x, a.y, f.x, f.y);
    }

    private int getArcIntersection() {
        int radius = GraphConfig.DIAMETER_2 / 2;
        return (int) (graphCentre.x - radius * Math.cos(Math.toRadians((double) GraphConfig.FACT_ANGLE)));
    }

    private void initCenter() {
        Dimension size = innerPane.getSize();
        this.graphCentre = new Point(size.width / 2, size.height / 2);
    }

    private void drawAxis() {
        Graphics graphics = innerPane.getGraphics();
        graphics.setColor(Color.BLUE);

        for (Map.Entry<Point, Point> entry : axisPoints.entrySet()) {
            graphics.drawLine(entry.getValue().x, entry.getValue().y, entry.getKey().x, entry.getKey().y);
        }

        Point xAxeKeyPoint = graphPoints.get("xAxe");
        Point yAxeKeyPoint = graphPoints.get("yAxe");
        graphics.drawString("X", xAxeKeyPoint.x, xAxeKeyPoint.y);
        graphics.drawString("Y", yAxeKeyPoint.x, yAxeKeyPoint.y);
    }

    private void drawGrid() {
        Graphics graphics = innerPane.getGraphics();
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaintMode();
        graphics.setColor(CUSTOM_GRAY);
        for (Map.Entry<Point, Point> entry : gridPoints.entrySet()) {
            graphics.drawLine(entry.getKey().x, entry.getKey().y, entry.getValue().x, entry.getValue().y);
        }
    }

    private void clearSurface(boolean needTo) {
        Graphics graphics = innerPane.getGraphics();
        Rectangle bounds = innerPane.getBounds();
        graphics.setColor(Color.WHITE);
        graphics.clearRect(bounds.x - CELL_WIDTH, bounds.y - CELL_HEIGHT, bounds.width, bounds.height);
        graphics.fillRect(bounds.x - CELL_WIDTH, bounds.y - CELL_HEIGHT, bounds.width, bounds.height);
        if (needTo) {
            gridPoints.clear();
            axisPoints.clear();
            graphPoints.clear();
            initPoints();
            drawGrid();
            drawAxis();
        }
    }

    private void clearSurface() {
        clearSurface(true);
    }

    private void initPoints() {
        initGridPoints();
        initAxisPoints();
        initLinePoints();
        initCirclePoints(GraphConfig.DIAMETER_1 / 2, graphCentre.x, graphCentre.y, "CIRCLE");
        initCirclePoints(GraphConfig.DIAMETER_2 / 2, graphCentre.x, graphCentre.y, "ARC");
    }

    private void initAxisPoints() {
        Dimension size = innerPane.getSize();
        int heightHalf = size.height / 2;
        int widthHalf = size.width / 2;
        int cellWidthHalf = CELL_WIDTH / 2;
        int cellHeightHalf = CELL_HEIGHT / 2;

        axisPoints.put(new Point(size.width, heightHalf), new Point(0, heightHalf));
        axisPoints.put(new Point(size.width - cellWidthHalf, heightHalf - cellHeightHalf), new Point(size.width, heightHalf));
        axisPoints.put(new Point(size.width - cellWidthHalf, heightHalf + cellHeightHalf), new Point(size.width, heightHalf));
        graphPoints.put("xAxe", new Point((int) (size.width - CELL_WIDTH * 1.25), (int) (heightHalf + CELL_HEIGHT * 1.25)));

        axisPoints.put(new Point(widthHalf, size.height), new Point(widthHalf, 0));
        axisPoints.put(new Point(widthHalf - cellWidthHalf, cellWidthHalf), new Point(widthHalf, 0));
        axisPoints.put(new Point(widthHalf + cellWidthHalf, cellWidthHalf), new Point(widthHalf, 0));
        graphPoints.put("yAxe", new Point((int) (widthHalf + CELL_WIDTH * 1.25), (int) (CELL_HEIGHT * 1.25)));
    }

    private void initGridPoints() {
        Dimension size = innerPane.getSize();
        for (int i = 0; i < size.width / CELL_WIDTH; i++) {
            gridPoints.put(new Point(0, graphCentre.y - i * CELL_WIDTH), new Point(size.width, graphCentre.y - i * CELL_WIDTH));
            gridPoints.put(new Point(0, graphCentre.y + i * CELL_WIDTH), new Point(size.width, graphCentre.y + i * CELL_WIDTH));
            gridPoints.put(new Point(graphCentre.x - i * CELL_HEIGHT, 0), new Point(graphCentre.x - i * CELL_HEIGHT, size.height));
            gridPoints.put(new Point(graphCentre.x + i * CELL_HEIGHT, 0), new Point(graphCentre.x + i * CELL_HEIGHT, size.height));
        }
    }
}
