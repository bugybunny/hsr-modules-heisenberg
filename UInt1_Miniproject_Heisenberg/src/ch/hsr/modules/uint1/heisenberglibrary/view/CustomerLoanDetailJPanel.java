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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.IModelChangeType;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.ModelChangeTypeEnums;
import ch.hsr.modules.uint1.heisenberglibrary.model.ObservableModelChangeEvent;
import ch.hsr.modules.uint1.heisenberglibrary.view.CustomerComboboxModel.DisplayableCustomer;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.CustomerLoanTableModel;
import ch.hsr.modules.uint1.heisenberglibrary.view.model.IDisposable;

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
    private JLabel                                               customerActiveLoansLabel;
    private GhostHintJTextField                                  addLoanIdTextfield;
    private ToolTipJButton                                       addNewLoanButton;
    private ToolTipJButton                                       returnLoanButton;

    // actions
    private AddLoanAction                                        addLoanAction;
    private ReturnLoanAction                                     returnLoanAction;

    private static final Pattern                                 MATCH_NUMBER     = Pattern
                                                                                          .compile("^[0-9]+$");

    public CustomerLoanDetailJPanel(Customer aCustomer, Library aLibrary) {
        super(aCustomer);
        library = aLibrary;
        initComponents();
        initCommonHandlers();
        // display existing loans for a customer
        if (aCustomer != null) {
            setCustomer(aCustomer);
            selectCustomerComboBox.setEnabled(false);
            initHandlersForExistingLoan();
        }
        // let the user select the customer to add a loan to
        else {
            /*
             * selectCustomerComboBox.getSelectedItem and
             * selectCustomerComboBox.getElementAt both return a
             * DisplayableCustomer (because if I use a "normal" customer in the
             * ComboBoxModel I would have many other problems. And setCustomer()
             * would not find an equal customer in the library even after
             * casting the DisplayableCustomer to a "normal" customer because
             * the equals method fails for this.getClass() == other.getClass().
             * So we need to use the library since the indices are the same.
             * 
             * If I use selectCustomerComboBox.setSelectedItem then the item is
             * found and the correct index set (found via debugging but since
             * JComboBox is generic I can���t see the code while debugging) but
             * after setting the correct one, other methods are called that I
             * cannot see that change the setIndex back to -1. So
             * selectCustomerComboBox.setSelectedItem(aCustomer);
             * selectCustomerComboBox.getSelectedIndex(); called immediately
             * after each other return -1. So we need to do something else that
             * does not seem the best way since I cannot find my or the Swing
             * bug.
             */
            setCustomer(library.getCustomers().get(
                    selectCustomerComboBox.getSelectedIndex()));
            selectCustomerComboBox.setEnabled(true);
            initHandlersForNewLoan();
        }
        checkCustomerLendabilty();
        addObserverForObservable(library, this);
    }

    private void setCustomer(Customer aNewCustomer) {
        setDisplayedObject(aNewCustomer);
        if (displayedObject != null) {
            // setSelectedItem does not work because we use a
            // CustomerComboboxModel.DisplayableCustomer. I've overwritten the
            // equals method there but it still does not work. so we get the
            // index of this customer and set this as selected index. stupid
            // comboboxmodel implementation with old methods without generics
            // and so on
            int indexOfCustomer = library.getCustomers().indexOf(aNewCustomer);
            selectCustomerComboBox.setSelectedIndex(indexOfCustomer);
            loanDetailTable.setModel(new CustomerLoanTableModel(
                    displayedObject, library));
            checkCustomerLendabilty();
        }
        updateDisplay();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel customerJPanel = new JPanel(new BorderLayout());

        JPanel customerPictureJPanel = new JPanel();
        FlowLayout flCustomerPictureJPanel = (FlowLayout) customerPictureJPanel
                .getLayout();
        flCustomerPictureJPanel.setAlignment(FlowLayout.LEFT);

        JLabel pokemonLabel = new JLabel();
        pokemonLabel.setIcon(new ImageIcon(CustomerLoanDetailJPanel.class
                .getResource("/images/kuser.png"))); //$NON-NLS-1$
        customerJPanel.add(pokemonLabel, BorderLayout.WEST);

        JPanel customerDetailJpanel = new JPanel();
        GridBagLayout gblCustomerDetailJpanel = new GridBagLayout();
        gblCustomerDetailJpanel.columnWidths = new int[] { 10, 0, 10, 0, 0 };
        gblCustomerDetailJpanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0 };
        gblCustomerDetailJpanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                1.0, Double.MIN_VALUE };
        gblCustomerDetailJpanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        customerDetailJpanel.setLayout(gblCustomerDetailJpanel);

        selectCustomerComboBox = new JComboBox<>(new CustomerComboboxModel(
                library));
        GridBagConstraints gbcSelectCustomerComboBox = new GridBagConstraints();
        gbcSelectCustomerComboBox.gridwidth = 3;
        gbcSelectCustomerComboBox.insets = new Insets(0, 0, 5, 0);
        gbcSelectCustomerComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbcSelectCustomerComboBox.gridx = 1;
        gbcSelectCustomerComboBox.gridy = 0;
        customerDetailJpanel.add(selectCustomerComboBox,
                gbcSelectCustomerComboBox);

        JLabel nameLabel = new JLabel(
                UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.label.name.text")); //$NON-NLS-1$
        GridBagConstraints gbcNameLabel = new GridBagConstraints();
        gbcNameLabel.anchor = GridBagConstraints.EAST;
        gbcNameLabel.insets = new Insets(0, 0, 5, 5);
        gbcNameLabel.gridx = 1;
        gbcNameLabel.gridy = 2;
        customerDetailJpanel.add(nameLabel, gbcNameLabel);

        customerNameLabel = new JLabel(UiComponentStrings.getString("empty")); //$NON-NLS-1$
        GridBagConstraints gbcCustomerNameLabel = new GridBagConstraints();
        gbcCustomerNameLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerNameLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerNameLabel.gridx = 3;
        gbcCustomerNameLabel.gridy = 2;
        customerDetailJpanel.add(customerNameLabel, gbcCustomerNameLabel);

        JLabel surnameLabel = new JLabel(
                UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.label.surname.text")); //$NON-NLS-1$
        GridBagConstraints gbcSurnameLabel = new GridBagConstraints();
        gbcSurnameLabel.anchor = GridBagConstraints.EAST;
        gbcSurnameLabel.insets = new Insets(0, 0, 5, 5);
        gbcSurnameLabel.gridx = 1;
        gbcSurnameLabel.gridy = 3;
        customerDetailJpanel.add(surnameLabel, gbcSurnameLabel);

        customerSurnameLabel = new JLabel(UiComponentStrings.getString("empty")); //$NON-NLS-1$
        GridBagConstraints gbcCustomerSurnameLabel = new GridBagConstraints();
        gbcCustomerSurnameLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerSurnameLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerSurnameLabel.gridx = 3;
        gbcCustomerSurnameLabel.gridy = 3;
        customerDetailJpanel.add(customerSurnameLabel, gbcCustomerSurnameLabel);

        JLabel addressLabel = new JLabel(
                UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.label.address.text")); //$NON-NLS-1$
        GridBagConstraints gbcAddressLabel = new GridBagConstraints();
        gbcAddressLabel.anchor = GridBagConstraints.EAST;
        gbcAddressLabel.insets = new Insets(0, 0, 5, 5);
        gbcAddressLabel.gridx = 1;
        gbcAddressLabel.gridy = 4;
        customerDetailJpanel.add(addressLabel, gbcAddressLabel);

        customerAddressLabel = new JLabel(UiComponentStrings.getString("empty")); //$NON-NLS-1$
        GridBagConstraints gbcCustomerAddressLabel = new GridBagConstraints();
        gbcCustomerAddressLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerAddressLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerAddressLabel.gridx = 3;
        gbcCustomerAddressLabel.gridy = 4;
        customerDetailJpanel.add(customerAddressLabel, gbcCustomerAddressLabel);

        JLabel zipLabel = new JLabel(
                UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.label.zip.text")); //$NON-NLS-1$
        GridBagConstraints gbcZipLabel = new GridBagConstraints();
        gbcZipLabel.anchor = GridBagConstraints.EAST;
        gbcZipLabel.insets = new Insets(0, 0, 5, 5);
        gbcZipLabel.gridx = 1;
        gbcZipLabel.gridy = 5;
        customerDetailJpanel.add(zipLabel, gbcZipLabel);

        customerZipLabel = new JLabel(UiComponentStrings.getString("empty")); //$NON-NLS-1$
        GridBagConstraints gbcCustomerZipLabel = new GridBagConstraints();
        gbcCustomerZipLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerZipLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerZipLabel.gridx = 3;
        gbcCustomerZipLabel.gridy = 5;
        customerDetailJpanel.add(customerZipLabel, gbcCustomerZipLabel);

        JLabel cityLabel = new JLabel(
                UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.label.city.text")); //$NON-NLS-1$
        GridBagConstraints gbcCityLabel = new GridBagConstraints();
        gbcCityLabel.anchor = GridBagConstraints.EAST;
        gbcCityLabel.insets = new Insets(0, 0, 5, 5);
        gbcCityLabel.gridx = 1;
        gbcCityLabel.gridy = 6;
        customerDetailJpanel.add(cityLabel, gbcCityLabel);

        cityNameLabel = new JLabel(UiComponentStrings.getString("empty")); //$NON-NLS-1$
        GridBagConstraints gbcCityNameLabel = new GridBagConstraints();
        gbcCityNameLabel.insets = new Insets(0, 0, 5, 0);
        gbcCityNameLabel.anchor = GridBagConstraints.WEST;
        gbcCityNameLabel.gridx = 3;
        gbcCityNameLabel.gridy = 6;
        customerDetailJpanel.add(cityNameLabel, gbcCityNameLabel);

        JLabel activeLoansLabel = new JLabel(
                UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.lblActiveLoans.text")); //$NON-NLS-1$
        GridBagConstraints gbcAblActiveLoans = new GridBagConstraints();
        gbcAblActiveLoans.insets = new Insets(0, 0, 5, 5);
        gbcAblActiveLoans.gridx = 1;
        gbcAblActiveLoans.gridy = 7;
        customerDetailJpanel.add(activeLoansLabel, gbcAblActiveLoans);

        customerActiveLoansLabel = new JLabel(
                UiComponentStrings.getString("empty")); //$NON-NLS-1$
        GridBagConstraints gbcCustomerActiveLoansLabel = new GridBagConstraints();
        gbcCustomerActiveLoansLabel.insets = new Insets(0, 0, 5, 0);
        gbcCustomerActiveLoansLabel.anchor = GridBagConstraints.WEST;
        gbcCustomerActiveLoansLabel.gridx = 3;
        gbcCustomerActiveLoansLabel.gridy = 7;
        customerDetailJpanel.add(customerActiveLoansLabel,
                gbcCustomerActiveLoansLabel);
        customerJPanel.add(customerDetailJpanel, BorderLayout.CENTER);
        add(customerJPanel, BorderLayout.NORTH);

        JPanel loanDetailJpanel = new JPanel(new BorderLayout());
        loanDetailJpanel.setBorder(new TitledBorder(null, UiComponentStrings
                .getString("CustomerLoanDetailJPanel.border.loandetails.text"), //$NON-NLS-1$
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        add(loanDetailJpanel, BorderLayout.CENTER);

        JPanel loanButtonJPanel = new JPanel();
        loanDetailJpanel.add(loanButtonJPanel, BorderLayout.NORTH);
        loanButtonJPanel.setLayout(new BoxLayout(loanButtonJPanel,
                BoxLayout.X_AXIS));

        addLoanIdTextfield = new GhostHintJTextField(
                "Enter Copy-Id to lend out");
        addLoanIdTextfield.setToolTipText(UiComponentStrings
                .getString("CustomerLoanDetailJPanel.textField.toolTipText")); //$NON-NLS-1$
        loanButtonJPanel.add(addLoanIdTextfield);
        addLoanIdTextfield.setText("");
        addLoanIdTextfield.setColumns(10);

        String addNewLoanButtonText = MessageFormat
                .format(UiComponentStrings
                        .getString("CustomerLoanDetailJPanel.button.addloan.text"), Integer.valueOf(Loan.DAYS_TO_RETURN_BOOK)); //$NON-NLS-1$
        String addNewLoanButtonEnabledTooltip = UiComponentStrings
                .getString("CustomerLoanDetailJPanel.button.addloan.enabled.tooltip"); //$NON-NLS-1$;
        addNewLoanButton = new ToolTipJButton(addNewLoanButtonText,
                addNewLoanButtonEnabledTooltip,
                UiComponentStrings.getString("empty")); //$NON-NLS-1$

        loanButtonJPanel.add(addNewLoanButton);

        String returnLoanButtonText = UiComponentStrings
                .getString("CustomerLoanDetailJPanel.button.returnloan.text"); //$NON-NLS-1$
        String returnLoanButtonEnabledTooltip = UiComponentStrings
                .getString("CustomerLoanDetailJPanel.button.addloan.enabled.tooltip"); //$NON-NLS-1$;
        String returnLoanButtonDisabledTooltip = UiComponentStrings
                .getString("CustomerLoanDetailJPanel.button.addloan.disabled.tooltip"); //$NON-NLS-1$;
        returnLoanButton = new ToolTipJButton(returnLoanButtonText,
                returnLoanButtonEnabledTooltip, returnLoanButtonDisabledTooltip);
        loanButtonJPanel.add(returnLoanButton);

        loanDetailTable = new JTable();
        loanDetailTable.getTableHeader().setReorderingAllowed(false);
        loanDetailTable.setAutoCreateRowSorter(true);
        loanDetailTable.setCellSelectionEnabled(true);
        loanDetailTable.setFillsViewportHeight(true);
        loanDetailTable.setColumnSelectionAllowed(false);
        JScrollPane jsp = new JScrollPane(loanDetailTable);
        jsp.setPreferredSize(new Dimension((int) jsp.getPreferredSize()
                .getWidth(), 200));
        loanDetailJpanel.add(jsp, BorderLayout.CENTER);
    }

    private void initCommonHandlers() {
        addLoanAction = new AddLoanAction(addNewLoanButton.getText());
        addNewLoanButton.setAction(addLoanAction);

        returnLoanAction = new ReturnLoanAction(returnLoanButton.getText());
        returnLoanButton.setAction(returnLoanAction);
    }

    private void initHandlersForExistingLoan() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent aComponentShownEvent) {
                loanDetailTable.requestFocus();
            }
        });
    }

    private void initHandlersForNewLoan() {
        selectCustomerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent anActionEvent) {
                DisplayableCustomer selectedDisplayableCustomer = (DisplayableCustomer) selectCustomerComboBox
                        .getSelectedItem();
                Customer customerToSet = ((CustomerComboboxModel) selectCustomerComboBox
                        .getModel())
                        .getCustomerForDisplayableCustomer(selectedDisplayableCustomer);
                setCustomer(customerToSet);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent aComponentShownEvent) {
                selectCustomerComboBox.requestFocus();
            }
        });
    }

    @Override
    public void update(Observable anObservable, Object anArgument) {
        if (anArgument instanceof ObservableModelChangeEvent) {
            ObservableModelChangeEvent modelChange = (ObservableModelChangeEvent) anArgument;
            IModelChangeType type = modelChange.getChangeType();
            if (type == ModelChangeTypeEnums.Loan.RETURNED
                    || type == ModelChangeTypeEnums.Loan.ADDED) {
                checkCustomerLendabilty();
            }
        }
    }

    private void checkCustomerLendabilty() {
        if (!library.isCustomerAllowedToLendOut(displayedObject)) {
            addLoanAction.setEnabled(false);
            if (!library.getOverdueLoansForCustomer(displayedObject).isEmpty()) {
                addNewLoanButton
                        .setDisabledToolTip(UiComponentStrings
                                .getString("CustomerLoanDetailJPanel.button.addloan.disabled.tooltip.overdueloans")); //$NON-NLS-1$
            } else {
                DisplayableCustomer selectedDisplayableCustomer = ((CustomerComboboxModel) selectCustomerComboBox
                        .getModel())
                        .getDisplayableCustomerForCustomer(displayedObject);
                String disabledToolTip = MessageFormat
                        .format(UiComponentStrings
                                .getString("CustomerLoanDetailJPanel.button.addloan.disabled.tooltip.maxloans"),
                                Integer.valueOf(selectedDisplayableCustomer
                                        .getActiveLoanCount()));
                addNewLoanButton.setDisabledToolTip(disabledToolTip);
            }
        } else {
            addLoanAction.setEnabled(true);
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
            DisplayableCustomer selectedDisplayableCustomer = ((CustomerComboboxModel) selectCustomerComboBox
                    .getModel())
                    .getDisplayableCustomerForCustomer(displayedObject);
            customerSurnameLabel.setText(displayedObject.getSurname());
            customerNameLabel.setText(displayedObject.getName());
            customerZipLabel.setText(String.valueOf(displayedObject.getZip()));
            customerAddressLabel.setText(displayedObject.getStreet());
            cityNameLabel.setText(displayedObject.getCity());
            customerActiveLoansLabel
                    .setText(Integer.toString(selectedDisplayableCustomer
                            .getActiveLoanCount()));

        }
    }

    private class AddLoanAction extends AbstractAction {
        private static final long serialVersionUID = -4973712671800673981L;

        private AddLoanAction(String anActionName) {
            super(anActionName);
        }

        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            DisplayableCustomer selectedDisplayableCustomer = ((CustomerComboboxModel) selectCustomerComboBox
                    .getModel())
                    .getDisplayableCustomerForCustomer(displayedObject);

            if (selectedDisplayableCustomer.getActiveLoanCount() < 4) {
                // Check valid number
                if (MATCH_NUMBER.matcher(addLoanIdTextfield.getText())
                        .matches()) {
                    long loanID = Long.parseLong(addLoanIdTextfield.getText());
                    for (Copy tempCopy : library.getAvailableCopies()) {
                        if (tempCopy.getInventoryNumber() == loanID) {
                            library.createAndAddLoan(displayedObject, tempCopy);

                        }
                    }
                }
            }

        }
    }

    private class ReturnLoanAction extends AbstractAction {
        private static final long serialVersionUID = -2413488724848750967L;

        private ReturnLoanAction(String anActionName) {
            super(anActionName);
        }

        @Override
        public void actionPerformed(ActionEvent anActionEvent) {
            List<Copy> returnedCopies = new ArrayList<>(
                    loanDetailTable.getSelectedRowCount());
            for (int tempLoan : loanDetailTable.getSelectedRows()) {
                returnedCopies.add(library
                        .getActiveCustomerLoans(displayedObject)
                        .get(loanDetailTable.convertRowIndexToModel(tempLoan))
                        .getCopy());
            }

            for (Copy returnLoan : returnedCopies) {
                library.returnCopy(returnLoan);
            }
        }
    }

    @Override
    public void cleanUpBeforeDispose() {
        removeAllObservers();
        if (loanDetailTable.getModel() instanceof IDisposable) {
            ((IDisposable) loanDetailTable.getModel()).cleanUpBeforeDispose();
        }
        if (selectCustomerComboBox.getModel() instanceof IDisposable) {
            ((IDisposable) selectCustomerComboBox.getModel())
                    .cleanUpBeforeDispose();
        }
    }
}
