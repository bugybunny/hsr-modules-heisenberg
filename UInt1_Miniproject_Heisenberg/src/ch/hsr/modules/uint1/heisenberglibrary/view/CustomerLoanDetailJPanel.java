/*
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.hsr.modules.uint1.heisenberglibrary.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;

/**
 * TODO COMMENT ME!
 * 
 * @author twinter
 */
public class CustomerLoanDetailJPanel extends JPanel {
    private JTable   loanDetailTable;
    private Customer displayedCustomer;
    private Library  library;

    /**
     * Create the panel.
     */
    public CustomerLoanDetailJPanel(Customer aCustomer, Library aLibrary) {
        displayedCustomer = aCustomer;
        library = aLibrary;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));

        JPanel customerPictureJPanel = new JPanel();
        add(customerPictureJPanel, BorderLayout.WEST);
        FlowLayout fl_customerPictureJPanel = (FlowLayout) customerPictureJPanel
                .getLayout();
        fl_customerPictureJPanel.setAlignment(FlowLayout.LEFT);

        JLabel pokemonLable = new JLabel("");
        pokemonLable.setIcon(new ImageIcon(CustomerLoanDetailJPanel.class
                .getResource("/images/kuser.png")));
        customerPictureJPanel.add(pokemonLable);

        JPanel customerDetailJpanel = new JPanel();
        add(customerDetailJpanel, BorderLayout.CENTER);
        GridBagLayout gbl_customerDetailJpanel = new GridBagLayout();
        gbl_customerDetailJpanel.columnWidths = new int[] { 10, 0, 10, 0, 0 };
        gbl_customerDetailJpanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0,
                0 };
        gbl_customerDetailJpanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                1.0, Double.MIN_VALUE };
        gbl_customerDetailJpanel.rowWeights = new double[] { 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        customerDetailJpanel.setLayout(gbl_customerDetailJpanel);

        JComboBox<Customer> selectCustomerComboBox = new JComboBox<Customer>(
                new CustomerComboboxModel(library));
        GridBagConstraints gbc_selectCustomerComboBox = new GridBagConstraints();
        gbc_selectCustomerComboBox.gridwidth = 3;
        gbc_selectCustomerComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_selectCustomerComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_selectCustomerComboBox.gridx = 1;
        gbc_selectCustomerComboBox.gridy = 0;
        customerDetailJpanel.add(selectCustomerComboBox,
                gbc_selectCustomerComboBox);

        JLabel nameLable = new JLabel("Name:");
        GridBagConstraints gbc_nameLable = new GridBagConstraints();
        gbc_nameLable.anchor = GridBagConstraints.EAST;
        gbc_nameLable.insets = new Insets(0, 0, 5, 5);
        gbc_nameLable.gridx = 1;
        gbc_nameLable.gridy = 2;
        customerDetailJpanel.add(nameLable, gbc_nameLable);

        JLabel customerNameLable = new JLabel("Hans");
        GridBagConstraints gbc_customerNameLable = new GridBagConstraints();
        gbc_customerNameLable.anchor = GridBagConstraints.WEST;
        gbc_customerNameLable.insets = new Insets(0, 0, 5, 0);
        gbc_customerNameLable.gridx = 3;
        gbc_customerNameLable.gridy = 2;
        customerDetailJpanel.add(customerNameLable, gbc_customerNameLable);

        JLabel surnameLable = new JLabel("Surname:");
        GridBagConstraints gbc_surnameLable = new GridBagConstraints();
        gbc_surnameLable.anchor = GridBagConstraints.EAST;
        gbc_surnameLable.insets = new Insets(0, 0, 5, 5);
        gbc_surnameLable.gridx = 1;
        gbc_surnameLable.gridy = 3;
        customerDetailJpanel.add(surnameLable, gbc_surnameLable);

        JLabel customerSurnameLable = new JLabel("Müller-Meier");
        GridBagConstraints gbc_customerSurnameLable = new GridBagConstraints();
        gbc_customerSurnameLable.anchor = GridBagConstraints.WEST;
        gbc_customerSurnameLable.insets = new Insets(0, 0, 5, 0);
        gbc_customerSurnameLable.gridx = 3;
        gbc_customerSurnameLable.gridy = 3;
        customerDetailJpanel
                .add(customerSurnameLable, gbc_customerSurnameLable);

        JLabel addressLable = new JLabel("Address:");
        GridBagConstraints gbc_addressLable = new GridBagConstraints();
        gbc_addressLable.anchor = GridBagConstraints.EAST;
        gbc_addressLable.insets = new Insets(0, 0, 5, 5);
        gbc_addressLable.gridx = 1;
        gbc_addressLable.gridy = 4;
        customerDetailJpanel.add(addressLable, gbc_addressLable);

        JLabel customerAddressLable = new JLabel("Exemplarstrasse 33");
        GridBagConstraints gbc_customerAddressLable = new GridBagConstraints();
        gbc_customerAddressLable.anchor = GridBagConstraints.WEST;
        gbc_customerAddressLable.insets = new Insets(0, 0, 5, 0);
        gbc_customerAddressLable.gridx = 3;
        gbc_customerAddressLable.gridy = 4;
        customerDetailJpanel
                .add(customerAddressLable, gbc_customerAddressLable);

        JLabel zipLable = new JLabel("Zip Code:");
        GridBagConstraints gbc_zipLable = new GridBagConstraints();
        gbc_zipLable.anchor = GridBagConstraints.EAST;
        gbc_zipLable.insets = new Insets(0, 0, 5, 5);
        gbc_zipLable.gridx = 1;
        gbc_zipLable.gridy = 5;
        customerDetailJpanel.add(zipLable, gbc_zipLable);

        JLabel customerZipLable = new JLabel("8645");
        GridBagConstraints gbc_customerZipLable = new GridBagConstraints();
        gbc_customerZipLable.anchor = GridBagConstraints.WEST;
        gbc_customerZipLable.insets = new Insets(0, 0, 5, 0);
        gbc_customerZipLable.gridx = 3;
        gbc_customerZipLable.gridy = 5;
        customerDetailJpanel.add(customerZipLable, gbc_customerZipLable);

        JLabel cityLable = new JLabel("City:");
        GridBagConstraints gbc_cityLable = new GridBagConstraints();
        gbc_cityLable.anchor = GridBagConstraints.EAST;
        gbc_cityLable.insets = new Insets(0, 0, 0, 5);
        gbc_cityLable.gridx = 1;
        gbc_cityLable.gridy = 6;
        customerDetailJpanel.add(cityLable, gbc_cityLable);

        JLabel cityNameLable = new JLabel("Rapperswil-Jona");
        GridBagConstraints gbc_cityNameLable = new GridBagConstraints();
        gbc_cityNameLable.anchor = GridBagConstraints.WEST;
        gbc_cityNameLable.gridx = 3;
        gbc_cityNameLable.gridy = 6;
        customerDetailJpanel.add(cityNameLable, gbc_cityNameLable);

        JPanel loanDetailJpanel = new JPanel();
        loanDetailJpanel.setBorder(new TitledBorder(null, "Loan details",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(loanDetailJpanel, BorderLayout.SOUTH);
        loanDetailJpanel.setLayout(new BorderLayout(0, 0));

        JPanel loanButtonJpanel = new JPanel();
        loanDetailJpanel.add(loanButtonJpanel, BorderLayout.NORTH);
        loanButtonJpanel.setLayout(new BoxLayout(loanButtonJpanel,
                BoxLayout.X_AXIS));

        JButton addNewLoanButton = new JButton("Add new loan");
        loanButtonJpanel.add(addNewLoanButton);

        JButton removeLoanButton = new JButton("Remove loan");
        loanButtonJpanel.add(removeLoanButton);

        loanDetailTable = new JTable();
        loanDetailJpanel.add(loanDetailTable);
    }

    /**
     * Registers a collection of actions to a given keystroke on this panel and
     * all subcomponents.
     * 
     * @param someActions
     *            map with keystrokes and their associated actions<br>
     *            {@code key}: keystroke to bind the action to<br> {@code value}
     *            : action that should be fired when keystroke has been pressed
     * 
     */
    void addAncestorActions(Map<KeyStroke, Action> someActions) {
        for (Map.Entry<KeyStroke, Action> tempAction : someActions.entrySet()) {
            Object actionName = tempAction.getValue().getValue(Action.NAME);
            getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                    tempAction.getKey(), actionName);
            getActionMap().put(actionName, tempAction.getValue());
        }
    }

    public Customer getDisplayedCustomer() {
        return displayedCustomer;
    }
}
