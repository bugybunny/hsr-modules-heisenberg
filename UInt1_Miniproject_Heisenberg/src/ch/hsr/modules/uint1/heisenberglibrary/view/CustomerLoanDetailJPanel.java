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
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;

/**
 * Panel to display a customer and his currently lent out books and to enter new
 * loans.
 * 
 * @author twinter
 * @author msyfrig
 */
public class CustomerLoanDetailJPanel extends
        AbstractObservableObjectJPanel<Customer> {
    private static final long                                    serialVersionUID = 1811625089328376836L;
    private JTable                                               loanDetailTable;
    private Library                                              library;
    private JLabel                                               customerAddressLabel;
    private JLabel                                               customerZipLabel;
    private JLabel                                               cityNameLabel;
    private JLabel                                               customerSurnameLabel;
    private JLabel                                               customerNameLabel;
    private JComboBox<CustomerComboboxModel.DisplayableCustomer> selectCustomerComboBox;

    public CustomerLoanDetailJPanel(Customer aCustomer, Library aLibrary) {
        super(aCustomer);
        library = aLibrary;
        initComponents();
        setCustomer(aCustomer);
        library.addObserver(this);
    }

    private void setCustomer(Customer aNewCustomer) {
        setDisplayedObject(aNewCustomer);
        if (displayedObject != null) {
            selectCustomerComboBox.setSelectedItem(displayedObject);
            selectCustomerComboBox.setEnabled(false);
        }
        updateDisplay();
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

        selectCustomerComboBox = new JComboBox<>(new CustomerComboboxModel(
                library));
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

        customerNameLabel = new JLabel("Hans");
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

        customerSurnameLabel = new JLabel(UiComponentStrings.getString("empty"));
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

        customerAddressLabel = new JLabel(UiComponentStrings.getString("empty"));
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

        customerZipLabel = new JLabel(UiComponentStrings.getString("empty"));
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

        cityNameLabel = new JLabel(UiComponentStrings.getString("empty"));
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

    @Override
    public void update(Observable anObesrvable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            ModelChangeType type = modelChange.getChangeType();
            // TODO implementieren
        }
    }

    @Override
    protected boolean save() {
        // nothing to do here since we cannot change customers
        return false;
    }

    private void updateDisplay() {
        // this case should never happen, that the book is null but it still
        // prevents us from a npe
        if (displayedObject == null) {
            customerSurnameLabel.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            customerNameLabel.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            customerZipLabel.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$
            customerAddressLabel.setText(UiComponentStrings.getString("empty")); //$NON-NLS-1$

        } else {
            customerSurnameLabel.setText(displayedObject.getSurname());
            customerNameLabel.setText(displayedObject.getName());
            customerZipLabel.setText(String.valueOf(displayedObject.getZip()));
            customerAddressLabel.setText(displayedObject.getStreet());
            cityNameLabel.setText(displayedObject.getCity());

        }
    }
}
