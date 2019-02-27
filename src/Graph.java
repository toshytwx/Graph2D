import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class Graph extends JDialog {
    private static final int CELL_WIDTH = 10;
    private static final int CELL_HEIGHT = 10;
    static final int WIDTH = 1056;
    static final int HEIGHT = 520;
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
    private Point graphCentre;
    private Point E;
    private Point F;

    Graph() {
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

    private void drawCoordinates(MouseEvent e) {
        coordinates.setText("X: " + e.getX() + " Y: " + e.getY());
    }

    private void drawGraph() {
        Graphics graphics = innerPane.getGraphics();

        graphics.setColor(CUSTOM_BLACK);
        int c1Radius = GraphConfig.DIAMETER_1 / 2;
        drawGraphLines(graphics);
        drawCircle(graphics, c1Radius, graphCentre.x, graphCentre.y, 0, 360);
        int c2Radius = GraphConfig.DIAMETER_2 / 2;
        drawCircle(graphics, c2Radius, graphCentre.x, graphCentre.y, 0, 360);
    }

    private List<Point> drawCircle(Graphics graphics, int radius, int centerX, int centerY, int startAngle, int endAngle) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = startAngle; i < endAngle; i++) {
            int x = (int) (centerX + radius * Math.cos(i));
            int y = (int) (centerY + radius * Math.sin(i));
            if ((x >= E.x && x >= F.x) || (y >= E.y && y <= F.y)) {
                points.add(new Point(x, y));
                graphics.drawLine(x, y, x, y);
            }
        }
        return points;
    }

    private void drawGraphLines(Graphics graphics) {
        Point A = new Point(graphCentre.x - GraphConfig.WIDTH_1, graphCentre.y - GraphConfig.HEIGHT_3);
        Point B = new Point(graphCentre.x - GraphConfig.WIDTH_1, A.y + GraphConfig.HEIGHT_1);
        Point C = new Point(graphCentre.x - GraphConfig.WIDTH_1 + GraphConfig.WIDTH_2, graphCentre.y + GraphConfig.HEIGHT_2 + GraphConfig.HEIGHT_3);
        Point D = new Point(graphCentre.x - GraphConfig.WIDTH_1 + GraphConfig.WIDTH_2, graphCentre.y + GraphConfig.HEIGHT_3);
        Point E = new Point(getArcIntersection(), graphCentre.y + GraphConfig.HEIGHT_3);
        Point F = new Point(getArcIntersection(), graphCentre.y - GraphConfig.HEIGHT_3);

        graphics.drawLine(A.x, A.y, B.x, B.y);
        graphics.drawLine(B.x, B.y, C.x, C.y);
        graphics.drawLine(C.x, C.y, D.x, D.y);
        graphics.drawLine(D.x, D.y, E.x, E.y);
        graphics.drawLine(A.x, A.y, F.x, F.y);

        this.E = E;
        this.F = F;
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
        Dimension size = innerPane.getSize();
        graphics.setColor(Color.BLUE);
        int heightHalf = size.height / 2;
        int widthHalf = size.width / 2;
        int cellWidthHalf = CELL_WIDTH / 2;
        int cellHeightHalf = CELL_HEIGHT / 2;

        graphics.drawLine(0, heightHalf, size.width, heightHalf);
        graphics.drawLine(size.width, heightHalf, size.width - cellWidthHalf, heightHalf - cellHeightHalf);
        graphics.drawLine(size.width, heightHalf, size.width - cellWidthHalf, heightHalf + cellHeightHalf);
        graphics.drawString("X", (int) (size.width - CELL_WIDTH * 1.25), (int) (heightHalf + CELL_HEIGHT * 1.25));
        graphics.setColor(Color.RED);

        graphics.drawLine(widthHalf, 0, widthHalf, size.height);
        graphics.drawLine(widthHalf, 0, widthHalf - cellWidthHalf, cellWidthHalf);
        graphics.drawLine(widthHalf, 0, widthHalf + cellWidthHalf, cellWidthHalf);
        graphics.drawString("Y", (int) (widthHalf + CELL_WIDTH * 1.25), (int) (CELL_HEIGHT * 1.25));
    }

    private void drawGrid() {
        Graphics graphics = innerPane.getGraphics();
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension size = innerPane.getSize();
        graphics.setPaintMode();
        graphics.setColor(CUSTOM_GRAY);
        for (int i = 0; i < size.width / CELL_WIDTH; i++) {
            graphics.drawLine(0, graphCentre.y - i * CELL_WIDTH, size.width, graphCentre.y - i * CELL_WIDTH);
            graphics.drawLine(0, graphCentre.y + i * CELL_WIDTH, size.width, graphCentre.y + i * CELL_WIDTH);
            graphics.drawLine(graphCentre.x - i * CELL_HEIGHT, 0, graphCentre.x - i * CELL_HEIGHT, size.height);
            graphics.drawLine(graphCentre.x + i * CELL_HEIGHT, 0, graphCentre.x + i * CELL_HEIGHT, size.height);
        }
    }

    private void clearSurface() {
        Graphics graphics = innerPane.getGraphics();
        Rectangle bounds = innerPane.getBounds();
        graphics.setColor(Color.WHITE);
        graphics.clearRect(bounds.x - CELL_WIDTH, bounds.y - CELL_HEIGHT, bounds.width, bounds.height);
        graphics.fillRect(bounds.x - CELL_WIDTH, bounds.y - CELL_HEIGHT, bounds.width, bounds.height);
        drawGrid();
        drawAxis();
    }
}
