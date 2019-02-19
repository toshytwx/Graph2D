import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Graph extends JDialog {
    private static final int CELL_WIDTH = 10;
    private static final int CELL_HEIGHT = 10;
    static final int WIDTH = 1040;
    static final int HEIGHT = 640;
    private static final Color CUSTOM_GRAY = new Color(128, 128, 128, 64);
    private static final Color CUSTOM_BLACK = new Color(0,0,0,128);

    private JPanel contentPane;
    private JPanel innerPane;
    private JPanel controlPane;
    private JLabel coordinates;
    private JButton drawGrid;
    private JButton adjustParams;
    private JButton drawGraph;
    private TDCoordinate graphCentre;

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
        drawGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                initCenter();
                drawGrid();
                drawAxis();
            }
        });
        drawGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
        graphics.drawOval(graphCentre.getX() - c1Radius, graphCentre.getY() - c1Radius, GraphConfig.DIAMETER_1, GraphConfig.DIAMETER_1);
        int c2Radius = GraphConfig.DIAMETER_2 / 2;
        graphics.drawArc(graphCentre.getX() - c2Radius, graphCentre.getY() - c2Radius, GraphConfig.DIAMETER_2, GraphConfig.DIAMETER_2, GraphConfig.START_ANGLE, GraphConfig.END_ANGLE);
        drawGraphLines(graphics);
    }

    private void drawGraphLines(Graphics graphics) {
        TDCoordinate A = new TDCoordinate(graphCentre.getX() - GraphConfig.WIDTH_1, graphCentre.getY() - GraphConfig.HEIGHT_3);
        TDCoordinate B = new TDCoordinate(graphCentre.getX() - GraphConfig.WIDTH_1, A.getY() + GraphConfig.HEIGHT_1);
        TDCoordinate C = new TDCoordinate(graphCentre.getX() - GraphConfig.WIDTH_1 + GraphConfig.WIDTH_2, graphCentre.getY() + GraphConfig.HEIGHT_2 + GraphConfig.HEIGHT_3);
        TDCoordinate D = new TDCoordinate(graphCentre.getX() - GraphConfig.WIDTH_1 + GraphConfig.WIDTH_2, graphCentre.getY() + GraphConfig.HEIGHT_3);
        TDCoordinate E = new TDCoordinate(getArcIntersection(), graphCentre.getY() + GraphConfig.HEIGHT_3);
        TDCoordinate F = new TDCoordinate(getArcIntersection(), graphCentre.getY() - GraphConfig.HEIGHT_3);

        graphics.drawLine(A.getX(), A.getY(), B.getX(), B.getY());
        graphics.drawLine(B.getX(), B.getY(), C.getX(), C.getY());
        graphics.drawLine(C.getX(), C.getY(), D.getX(), D.getY());
        graphics.drawLine(D.getX(), D.getY(), E.getX(), E.getY());
        graphics.drawLine(A.getX(), A.getY(), F.getX(), F.getY());
    }

    private int getArcIntersection() {
        int radius = GraphConfig.DIAMETER_2 / 2;
        return (int) (graphCentre.getX() - radius * Math.cos(Math.toRadians((double) GraphConfig.FACT_ANGLE)));
    }

    private void initCenter() {
        Dimension size = innerPane.getSize();
        this.graphCentre = new TDCoordinate(size.width / 2, size.height / 2);
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
        Dimension size = innerPane.getSize();
        graphics.setPaintMode();
        graphics.setColor(CUSTOM_GRAY);
        for (int i = 0; i < size.width / CELL_WIDTH; i++) {
            graphics.drawLine(0, graphCentre.getY() - i * CELL_WIDTH, size.width, graphCentre.getY() - i * CELL_WIDTH);
            graphics.drawLine(0, graphCentre.getY() + i * CELL_WIDTH, size.width, graphCentre.getY() + i * CELL_WIDTH);
            graphics.drawLine(graphCentre.getX() - i * CELL_HEIGHT, 0, graphCentre.getX() - i * CELL_HEIGHT, size.height);
            graphics.drawLine(graphCentre.getX() + i * CELL_HEIGHT, 0, graphCentre.getX() + i * CELL_HEIGHT, size.height);
        }
    }
}
