import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Params extends JDialog {
    private JPanel contentPane;
    private JFormattedTextField formattedTextField1;
    private JButton applyParams;

    public Params() {
        setContentPane(contentPane);
        setModal(true);
        setupFormat();
        configureListeners();
    }

    private void configureListeners() {
        applyParams.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                GraphConfig.A = (int) formattedTextField1.getValue();
            }
        });
    }

    private void setupFormat() {
        formattedTextField1.setValue(GraphConfig.A);
    }
}
