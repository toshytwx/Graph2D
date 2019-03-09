import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph extends JDialog {
    private static final int CELL_WIDTH = 10;
    private static final int CELL_HEIGHT = 10;
    static final int WIDTH = 1080;
    static final int HEIGHT = 880;
    private static final Color CUSTOM_GRAY = new Color(128, 128, 128, 64);
    private static final Color CUSTOM_BLACK = new Color(0, 0, 0, 128);

    private JPanel contentPane;
    private JPanel innerPane;
    private JPanel controlPane;
    private JLabel coordinates;
    private JButton adjustParams;
    private JButton drawGraph;
    private Point graphCentre;
    private Map<String, Point> graphPoints;
    private Map<Point, Point> gridPoints;
    private Map<Point, Point> axisPoints;
    private ArrayList<Point> points;

    Graph() {
        setContentPane(contentPane);
        setModal(true);
        initListeners();
        graphPoints = new HashMap<>();
        gridPoints = new HashMap<>();
        axisPoints = new HashMap<>();
        points = new ArrayList<>();
    }

    private void initListeners() {
        innerPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                drawCoordinates(e);
                drawTangent(e);
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
    }

    private void drawTangent(MouseEvent e) {
        Point mousePoint = e.getPoint();
        if (points.contains(mousePoint)) {
            clearSurface();
            drawGraph();
            int x = mousePoint.x - innerPane.getWidth() / 2;
            int y = mousePoint.y - innerPane.getHeight() / 2;
            double tangentValueInPoint =
                    (GraphConfig.A * y + x * x) /
                            (double) (-GraphConfig.A * x + y * y);
            Graphics graphics = innerPane.getGraphics();
            graphics.setColor(Color.red);
            Point point = new Point((x + innerPane.getWidth() / 2 + 50), (int) (y + 50 * tangentValueInPoint + innerPane.getHeight() / 2));
            Point point1 = new Point((x + innerPane.getWidth() / 2 - 50), (int) (y - 50 * tangentValueInPoint + innerPane.getHeight() / 2));
            graphics.drawLine(point.x, point.y, point1.x, point1.y);
            graphics.setColor(Color.green);
            point = new Point((x + innerPane.getWidth() / 2 - 50), (int) (y + 50 / tangentValueInPoint + innerPane.getHeight() / 2));
            point1 = new Point((x + innerPane.getWidth() / 2 + 50), (int) (y - 50 / tangentValueInPoint + innerPane.getHeight() / 2));
            graphics.drawLine(point.x, point.y, point1.x, point1.y);
        }
    }

    private void drawCoordinates(MouseEvent e) {
        coordinates.setText("X: " + (e.getX() - innerPane.getWidth() / 2) + " Y: " + (e.getY() - innerPane.getHeight() / 2));
    }

    private void drawGraph() {
        Graphics graphics = innerPane.getGraphics();
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(CUSTOM_BLACK);
        for (int i = 1; i < points.size(); i++) {
            Point prevPoint = points.get(i - 1);
            Point nextPoint = points.get(i);
            graphics.setColor(CUSTOM_BLACK);
            if (nextPoint.y - prevPoint.y <= HEIGHT) {
                graphics.drawLine(prevPoint.x, prevPoint.y, nextPoint.x, nextPoint.y);
            }
        }
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

    private void clearSurface() {
        Graphics graphics = innerPane.getGraphics();
        Rectangle bounds = innerPane.getBounds();
        graphics.setColor(Color.WHITE);
        graphics.clearRect(bounds.x - CELL_WIDTH, bounds.y - CELL_HEIGHT, bounds.width, bounds.height);
        graphics.fillRect(bounds.x - CELL_WIDTH, bounds.y - CELL_HEIGHT, bounds.width, bounds.height);
        gridPoints.clear();
        axisPoints.clear();
        graphPoints.clear();
        points.clear();
        initPoints();
        drawGrid();
        drawAxis();
    }

    private void initPoints() {
        initGridPoints();
        initAxisPoints();
        for (double i = 0; i <= 180; i += 0.5) {
            int x = (int) (3 * GraphConfig.A * Math.tan((i * Math.PI) / 180) / (1 + Math.pow(Math.tan((i * Math.PI) / 180), 3))) + graphCentre.x;
            int y = graphCentre.y - (int) (3 * GraphConfig.A * Math.tan((i * Math.PI) / 180) * Math.tan((i * Math.PI) / 180) / (1 + Math.pow(Math.tan((i * Math.PI) / 180), 3)));
            points.add(new Point(x, y));
        }
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
