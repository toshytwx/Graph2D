import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String[] args) {
        displayGraph();
        System.exit(0);
    }

    private static void displayGraph() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Graph graph = new Graph();
        graph.setPreferredSize(new Dimension(Graph.WIDTH, Graph.HEIGHT));
        graph.pack();
        graph.setVisible(true);
    }
}
