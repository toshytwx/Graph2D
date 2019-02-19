import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.text.NumberFormat;

public class Params extends JDialog {
    private JPanel contentPane;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JFormattedTextField formattedTextField3;
    private JFormattedTextField formattedTextField4;
    private JFormattedTextField formattedTextField5;
    private JFormattedTextField formattedTextField6;
    private JFormattedTextField formattedTextField7;
    private JFormattedTextField formattedTextField8;
    private JFormattedTextField formattedTextField9;
    private JFormattedTextField formattedTextField10;
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
                GraphConfig.DIAMETER_1 = (int) formattedTextField1.getValue();
                GraphConfig.DIAMETER_2 = (int) formattedTextField2.getValue();
                GraphConfig.HEIGHT_1 = (int) formattedTextField3.getValue();
                GraphConfig.HEIGHT_2 = (int) formattedTextField4.getValue();
                GraphConfig.HEIGHT_3 = (int) formattedTextField5.getValue();
                GraphConfig.WIDTH_1 = (int) formattedTextField6.getValue();
                GraphConfig.WIDTH_2 = (int) formattedTextField7.getValue();
                GraphConfig.START_ANGLE = (int) formattedTextField8.getValue();
                GraphConfig.END_ANGLE = (int) formattedTextField9.getValue();
                GraphConfig.FACT_ANGLE = (int) formattedTextField10.getValue();
            }
        });
    }

    private void setupFormat() {
        formattedTextField1.setValue(GraphConfig.DIAMETER_1);
        formattedTextField2.setValue(GraphConfig.DIAMETER_2);
        formattedTextField3.setValue(GraphConfig.HEIGHT_1);
        formattedTextField4.setValue(GraphConfig.HEIGHT_2);
        formattedTextField5.setValue(GraphConfig.HEIGHT_3);
        formattedTextField6.setValue(GraphConfig.WIDTH_1);
        formattedTextField7.setValue(GraphConfig.WIDTH_2);
        formattedTextField8.setValue(GraphConfig.START_ANGLE);
        formattedTextField9.setValue(GraphConfig.END_ANGLE);
        formattedTextField10.setValue(GraphConfig.FACT_ANGLE);
    }
}
