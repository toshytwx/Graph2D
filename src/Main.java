import java.awt.*;


public class Main {

    public static void main(String[] args) {
        displayGraph();
        System.exit(0);
    }

    private static void displayGraph() {
        Graph graph = new Graph();
        graph.setPreferredSize(new Dimension(Graph.WIDTH, Graph.HEIGHT));
        graph.pack();
        graph.setVisible(true);
    }
}
