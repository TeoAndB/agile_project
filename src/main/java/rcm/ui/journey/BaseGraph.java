package rcm.ui.journey;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import rcm.model.Application;

public abstract class BaseGraph extends JPanel {

    private static final long serialVersionUID = 9059820726277444630L;
    protected Application app;
    protected int id;
    private ChartPanel chartPanel;

    public BaseGraph(Application app, int id) {
        this.app = app;
        this.id = id;
        initUI();

    }

    private void initUI() {
        chartPanel = new ChartPanel(null);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(new Color(220, 220, 220));
        chartPanel.setPreferredSize(new Dimension(650, 400));
        add(chartPanel);
    }

    public void updateGraph(int id) {
        this.id = id;
        JFreeChart chart = createChart();
        chartPanel.setChart(chart);
    }

    public abstract JFreeChart createChart();

}