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
 * @author msyfrig
 */
public class CustomerLoanDetailJPanel extends JPanel {
    private static final long serialVersionUID = 1811625089328376836L;
    private JTable            loanDetailTable;
    private Customer          displayedCustomer;
    private Library           library;

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
        FlowLayout flCustomerPictureJPanel = (FlowLayout) customerPictureJPanel
                .getLayout();
        flCustomerPictureJPanel.setAlignment(FlowLayout.LEFT);

        JLabel pokemonLabel = new JLabel("");
        pokemonLabel.setIcon(new ImageIcon(CustomerLoanDetailJPanel.class
                .getResource("/images/kuser.png")));
        customerPictureJPanel.add(pokemonLabel);

        JPanel customerDetailJpanel = new JPanel();
        add(customerDetailJpanel, BorderLayout.CENTER);
        GridBagLayout gblCustomerDetailJpanel = new GridBagLayout();
        gblCustomerDetailJpanel.columnWidths = new int[] { 10, 0, 10, 0, 0 };
        gblCustomerDetailJpanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        gblCustomerDetailJpanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                1.0, Double.MIN_VALUE };
        gblCustomerDetailJpanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, Double.MIN_VALUE };
        customerDetailJpanel.setLayout(gblCustomerDetailJpanel);

        JComboBox<Customer> selectCustomerComboBox = new JComboBox<>(
                new CustomerComboboxModel(library));
        GridBagConstraints gbcSelectCustomerComboBox = new GridBagConstraints();
        gbcSelectCustomerComboBox.gridwidth = 3;
        gbcSelectCustomerComboBox.insets = new Insets(0, 0, 5, 5);
        gbcSelectCustomerComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbcSelectCustomerComboBox.gridx = 1;
        gbcSelectCustomerComboBox.gridy = 0;
        customerDetailJpanel.add(selectCustomerComboBox,
                gbcSelectCustomerComboBox);

        JLabel nameLabel = new JLabel("Name:");
        GridBagConstraints gbcNameLabel = new GridBagConstraints();
        gbcNameLabel.anchor = GridBagConstraints.EAST;
        gbcNameLabel.insets = new Insets(0, 0, 5, 5);
        gbcNameLabel.gridx = 1;
        gbcNameLabel.gridy = 2;
        customerDetailJpanel.add(nameLabel, gbcNameLabel);

        JLabel customerNameLabel = new JLabel("Hans");
        GridBagConstraints gbcCustomerNameLabel = new GridBagConstraints();
        gbcCustomerNameLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerNameLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerNameLabel.gridx = 3;
        gbcCustomerNameLabel.gridy = 2;
        customerDetailJpanel.add(customerNameLabel, gbcCustomerNameLabel);

        JLabel surnameLabel = new JLabel("Surname:");
        GridBagConstraints gbcSurnameLabel = new GridBagConstraints();
        gbcSurnameLabel.anchor = GridBagConstraints.EAST;
        gbcSurnameLabel.insets = new Insets(0, 0, 5, 5);
        gbcSurnameLabel.gridx = 1;
        gbcSurnameLabel.gridy = 3;
        customerDetailJpanel.add(surnameLabel, gbcSurnameLabel);

        JLabel customerSurnameLabel = new JLabel("Müller-Meier");
        GridBagConstraints gbcCustomerSurnameLabel = new GridBagConstraints();
        gbcCustomerSurnameLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerSurnameLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerSurnameLabel.gridx = 3;
        gbcCustomerSurnameLabel.gridy = 3;
        customerDetailJpanel.add(customerSurnameLabel, gbcCustomerSurnameLabel);

        JLabel addressLabel = new JLabel("Address:");
        GridBagConstraints gbcAddressLabel = new GridBagConstraints();
        gbcAddressLabel.anchor = GridBagConstraints.EAST;
        gbcAddressLabel.insets = new Insets(0, 0, 5, 5);
        gbcAddressLabel.gridx = 1;
        gbcAddressLabel.gridy = 4;
        customerDetailJpanel.add(addressLabel, gbcAddressLabel);

        JLabel customerAddressLabel = new JLabel("Exemplarstrasse 33");
        GridBagConstraints gbcCustomerAddressLabel = new GridBagConstraints();
        gbcCustomerAddressLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerAddressLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerAddressLabel.gridx = 3;
        gbcCustomerAddressLabel.gridy = 4;
        customerDetailJpanel.add(customerAddressLabel, gbcCustomerAddressLabel);

        JLabel zipLabel = new JLabel("Zip Code:");
        GridBagConstraints gbcZipLabel = new GridBagConstraints();
        gbcZipLabel.anchor = GridBagConstraints.EAST;
        gbcZipLabel.insets = new Insets(0, 0, 5, 5);
        gbcZipLabel.gridx = 1;
        gbcZipLabel.gridy = 5;
        customerDetailJpanel.add(zipLabel, gbcZipLabel);

        JLabel customerZipLabel = new JLabel("8645");
        GridBagConstraints gbcCustomerZipLabel = new GridBagConstraints();
        gbcCustomerZipLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerZipLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerZipLabel.gridx = 3;
        gbcCustomerZipLabel.gridy = 5;
        customerDetailJpanel.add(customerZipLabel, gbcCustomerZipLabel);

        JLabel cityLabel = new JLabel("City:");
        GridBagConstraints gbcCityLabel = new GridBagConstraints();
        gbcCityLabel.anchor = GridBagConstraints.EAST;
        gbcCityLabel.insets = new Insets(0, 0, 0, 5);
        gbcCityLabel.gridx = 1;
        gbcCityLabel.gridy = 6;
        customerDetailJpanel.add(cityLabel, gbcCityLabel);

        JLabel cityNameLabel = new JLabel("Rapperswil-Jona");
        GridBagConstraints gbcCityNameLabel = new GridBagConstraints();
        gbcCityNameLabel.anchor = GridBagConstraints.WEST;
        gbcCityNameLabel.gridx = 3;
        gbcCityNameLabel.gridy = 6;
        customerDetailJpanel.add(cityNameLabel, gbcCityNameLabel);

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

    private void initHandlersForExistingLoan() {

    }

    private void initHandlersForNewLoan() {

    }

    /**
     * Registers a collection of actions to a given keystroke on this panel and
     * all subcomponents.
     * 
     * @param someActions
     *            map with keystrokes and their associated actions<br>
     *            {@code key}: keystroke to bind the action to<br> {@code value}
     *            : action that should be fired when keystroke has been pressed
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
