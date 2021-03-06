package rcm.ui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

import rcm.model.Application;
import rcm.model.Journey;
import rcm.ui.BaseTopBar;
import rcm.ui.popup.Dialog;

class SharedJourneysTopBar extends BaseTopBar {

    public SharedJourneysTopBar(Application app) {
        super(app, "searchSharedJourney");
    }

    private static final long serialVersionUID = -2448342061117967555L;

    @Override
    public JPanel buildLeftSide() {
        return new JPanel();
    }
}

public class SharedJourneysTableView extends BaseTableView {

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private static final long serialVersionUID = 1156877669628672936L;
    protected JMenuItem itemViewSharedJourney;
    protected JPopupMenu popupMenu;
    private List<Journey> journeys;

    public SharedJourneysTableView(Application app) {
        super(app, new SharedJourneysTopBar(app));
        app.addObserver(this);
    }

    @Override
    public void updateTableModel() {
        if (app.getLoggedInClient() != null) {

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            String[] columnNames = { "ID", "Origin", "Destination", "Content", "Start Date", "End Date", "Owner" };
            tableModel.setColumnIdentifiers(columnNames);

            for (int i = 0; i < journeys.size(); i++) {
                Journey j = journeys.get(i);
                int dataId = j.getId();
                String dataOrigin = j.getOriginPort();
                String dataDestination = j.getDestinationPort();
                String dataContent = j.getContent();
                String dataStartDate = ((j.getStartTimestamp() != null) ? j.getStartTimestamp().format(formatter)
                        : "Not Started yet");
                String dataEndDate = ((j.getEndTimestamp() != null) ? j.getEndTimestamp().format(formatter)
                        : "Not Ended yet");
                String owner = j.getClient().getName();
                Object[] rowData = { dataId, dataOrigin, dataDestination, dataContent, dataStartDate, dataEndDate,
                        owner };
                tableModel.addRow(rowData);
            }

            popupMenu = new JPopupMenu();
            itemViewSharedJourney = new JMenuItem("View Shared Journey");

            popupMenu.add(itemViewSharedJourney);

            table.setComponentPopupMenu(popupMenu);
            table.setModel(tableModel);
            table.setEnabled(false);

            itemViewSharedJourney.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                        int id = (int) table.getValueAt(table.getSelectedRow(), 0);
                        app.fireChange("showSharedJourney", id);
                    } catch (Exception e) {
                        Dialog.WarningDialog("Please choose a journey first", "No journey chosen");
                    }
                }
            });

            tableModel.fireTableDataChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
        case "clientTabChanged":
        case "clientLoggedIn":
        case "newJourney":
            journeys = app.requestSharedJourneys();
            updateTableModel();
            break;
        case "searchSharedJourney":
            journeys = app.searchForSharedJourneys((String) evt.getNewValue());
            updateTableModel();
            break;
        case "advSearchSharedJourneys":
            @SuppressWarnings("unchecked")
            Map<String, Object> filters = (Map<String, Object>) evt.getNewValue();
            String query = (String) filters.get("query");
            journeys = app.searchForSharedJourneys(query, (boolean) filters.getOrDefault("origin", false),
                    (boolean) filters.getOrDefault("destination", false),
                    (boolean) filters.getOrDefault("content", false));
            updateTableModel();
            break;
        default:
            break;
        }
    }
}
