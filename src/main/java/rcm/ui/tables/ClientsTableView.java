package rcm.ui.tables;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

import rcm.model.Application;
import rcm.model.Client;

import rcm.ui.BaseTopBar;
import rcm.ui.popup.CreateClientView;
import rcm.ui.popup.Dialog;

class ClientsTopBar extends BaseTopBar {

    public ClientsTopBar(Application app) {
        super(app, "searchClients");
    }

    private static final long serialVersionUID = -455443009885225672L;

    @Override
    public JPanel buildLeftSide() {
        JPanel leftSide = new JPanel(new FlowLayout());

        // New Client button
        JButton newClient = new JButton("New Client");
        newClient.setPreferredSize(new Dimension(150, 30));
        newClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateClientView popup = new CreateClientView(app);
                popup.setLocationRelativeTo(null);
                popup.setVisible(true);
            }
        });
        leftSide.add(newClient);

        return leftSide;
    }
}

public class ClientsTableView extends BaseTableView {

    private static final long serialVersionUID = -319420806707922265L;
    private List<Client> clients;

    public ClientsTableView(Application app) {
        super(app, new ClientsTopBar(app));
    }

    @Override
    public void updateTableModel() {

        if (app.getLoggedInCompany() != null) {

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            String[] columnNames = { "ID", "Name", "Address", "Reference person", "Email" };
            tableModel.setColumnIdentifiers(columnNames);

            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                int dataId = c.getId();
                String dataName = c.getName();
                String dataAddress = c.getAddress();
                String dataRefPerson = c.getRefPerson();
                String dataEmail = c.getEmail();
                Object[] rowData = { dataId, dataName, dataAddress, dataRefPerson, dataEmail };
                tableModel.addRow(rowData);
            }

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem itemViewContainer = new JMenuItem("View Client");
            popupMenu.add(itemViewContainer);
            table.setComponentPopupMenu(popupMenu);
            table.setModel(tableModel);
            table.setEnabled(false);

            itemViewContainer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    try {
                        int id = (int) table.getValueAt(table.getSelectedRow(), 0);
                        app.fireChange("showClient", id);
                    } catch (Exception e) {
                        Dialog.WarningDialog("Please choose a client first", "No client chosen");
                    }
                }
            });

            tableModel.fireTableDataChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
        case "companyTabChanged":
        case "companyLoggedIn":
        case "newClientCreated":
        case "startJourney":
        case "endJourney":
            clients = app.requestClients();
            updateTableModel();
            break;
        case "searchClients":
            clients = app.searchForClients((String) evt.getNewValue());
            updateTableModel();
            break;
        case "advSearchClients":
            @SuppressWarnings("unchecked")
            Map<String, Object> filters = (Map<String, Object>) evt.getNewValue();
            String query = (String) filters.get("query");
            clients = app.searchForClients(query, (boolean) filters.getOrDefault("name", false),
                    (boolean) filters.getOrDefault("address", false),
                    (boolean) filters.getOrDefault("refPerson", false), (boolean) filters.getOrDefault("email", false));
            updateTableModel();
            break;
        default:
            break;
        }
    }
}
