package ch.hsr.modules.uint1.heisenberglibrary.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.hsr.modules.uint1.heisenberglibrary.model.BookDO;
import ch.hsr.modules.uint1.heisenberglibrary.model.Copy;
import ch.hsr.modules.uint1.heisenberglibrary.model.Customer;
import ch.hsr.modules.uint1.heisenberglibrary.model.IllegalLoanOperationException;
import ch.hsr.modules.uint1.heisenberglibrary.model.Library;
import ch.hsr.modules.uint1.heisenberglibrary.model.Loan;
import ch.hsr.modules.uint1.heisenberglibrary.model.Shelf;
import ch.hsr.modules.uint1.heisenberglibrary.util.NimbusLookAndFeelInitalizer;
import ch.hsr.modules.uint1.heisenberglibrary.view.LibraryMasterJFrame;

public class LibraryApp {
    public static void main(String[] args) {
        try {
            // WANNA SPEAK GERMAN O_O
            // Locale.setDefault(new Locale("de_ch"));
            Library library = new Library();
            initLibrary(library);

            try {
                for (LookAndFeelInfo info : UIManager
                        .getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
            catch (Exception e) {
                // do nothing and just use the default look and feel for the os
            }
            // some properties need to be changed before the components are
            // drawn some after oO
            NimbusLookAndFeelInitalizer.changeColors();
            new LibraryMasterJFrame(library);
            NimbusLookAndFeelInitalizer.changeColors();
        }
        catch (Throwable aThrowable) {
            // do nothing :p
        }
    }

    private static void initLibrary(Library library)
            throws ParserConfigurationException, SAXException, IOException,
            IllegalLoanOperationException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();

        loadCustomersFromXml(library, builder, new File("data/customers.xml"));
        loadBooksFromXml(library, builder, new File("data/books.xml"));

        // create pseudo random books and loans
        createBooksAndLoans(library);
        Collections.sort(library.getCustomers());

        System.out.println("Initialisation of the library was successful!\n");
        System.out.println("Books in library: " + library.getBooks().size());
        System.out
                .println("Customers: " + library.getCustomers().size() + "\n");
        System.out.println("Copies in library: " + library.getCopies().size());
        System.out.println("Copies currently on loan: "
                + library.getLentOutBooks().size());
        int lentBooksPercentage = (int) (((double) library.getLentOutBooks()
                .size()) / library.getCopies().size() * 100);
        System.out.println("Percent copies on loan: " + lentBooksPercentage
                + "%");
        System.out.println("Copies currently overdue: "
                + library.getOverdueLoans().size());
    }

    private static void createBooksAndLoans(Library library)
            throws IllegalLoanOperationException {
        for (int i = 0; i < library.getBooks().size(); i++) {
            switch (i % 4) {
                case 0:
                    Copy c1 = library.createAndAddCopy(library.getBooks()
                            .get(i));
                    c1.setCondition(Copy.Condition.GOOD);
                    createLoansForCopy(library, c1, i, 5);
                    Copy c2 = library.createAndAddCopy(library.getBooks()
                            .get(i));
                    c2.setCondition(Copy.Condition.DAMAGED);
                    createLoansForCopy(library, c2, i, 2);
                    Copy c3 = library.createAndAddCopy(library.getBooks()
                            .get(i));
                    c3.setCondition(Copy.Condition.WASTE);
                    break;
                case 1:
                    Copy c4 = library.createAndAddCopy(library.getBooks()
                            .get(i));
                    createLoansForCopy(library, c4, i, 4);
                    library.createAndAddCopy(library.getBooks().get(i));
                    break;
                case 2:
                    Copy c5 = library.createAndAddCopy(library.getBooks()
                            .get(i));
                    createLoansForCopy(library, c5, i, 2);
                    break;
                case 3:
                    Copy c6 = library.createAndAddCopy(library.getBooks()
                            .get(i));
                    createOverdueLoanForCopy(library, c6, i);
                    break;
            }
        }
    }

    private static void loadBooksFromXml(Library library,
            DocumentBuilder builder, File file) throws SAXException,
            IOException {
        Document doc2 = builder.parse(file);
        NodeList titles = doc2.getElementsByTagName("title");
        for (int i = 0; i < titles.getLength(); i++) {
            Node title = titles.item(i);
            BookDO b = library
                    .createAndAddBook(getTextContentOf(title, "name"));
            b.setAuthor(getTextContentOf(title, "author"));
            b.setPublisher(getTextContentOf(title, "publisher"));
            Shelf shelf = Shelf.values()[(int) (Math.random() * Shelf.values().length)];
            b.setShelf(shelf);
        }
    }

    private static void loadCustomersFromXml(Library library,
            DocumentBuilder builder, File file) throws SAXException,
            IOException {
        Document doc = builder.parse(file);
        NodeList customers = doc.getElementsByTagName("customer");
        for (int i = 0; i < customers.getLength(); i++) {
            Node customer = customers.item(i);

            Customer c = library.createAndAddCustomer(
                    getTextContentOf(customer, "surname"),
                    getTextContentOf(customer, "name"));
            c.setAdress(getTextContentOf(customer, "street"),
                    Integer.parseInt(getTextContentOf(customer, "zip")),
                    getTextContentOf(customer, "city"));
        }
    }

    private static void createLoansForCopy(Library library, Copy copy,
            int position, int count) throws IllegalLoanOperationException {
        // Create Loans in the past
        for (int i = count; i > 1; i--) {
            Loan l = library.createAndAddLoan(
                    getCustomer(library, position + i), copy);
            GregorianCalendar pickup = l.getPickupDate();
            pickup.add(GregorianCalendar.MONTH, -i);
            pickup.add(GregorianCalendar.DAY_OF_MONTH, position % 10);
            l.setPickupDate(pickup);
            GregorianCalendar ret = (GregorianCalendar) pickup.clone();
            ret.add(GregorianCalendar.DAY_OF_YEAR, position % 10 + i * 2);
            library.returnCopy(copy, ret);
        }
        // Create actual open loans
        if (position % 2 == 0) {
            Loan l = library.createAndAddLoan(getCustomer(library, position),
                    copy);
            GregorianCalendar pickup = l.getPickupDate();
            pickup.add(GregorianCalendar.DAY_OF_MONTH, -position % 10);
            l.setPickupDate(pickup);
        }
    }

    private static void createOverdueLoanForCopy(Library library, Copy copy,
            int position) throws IllegalLoanOperationException {
        Loan l = library.createAndAddLoan(getCustomer(library, position), copy);
        GregorianCalendar pickup = l.getPickupDate();
        pickup.add(GregorianCalendar.MONTH, -1);
        pickup.add(GregorianCalendar.DAY_OF_MONTH, -position % 15);
        l.setPickupDate(pickup);
    }

    private static Customer getCustomer(Library library, int position) {
        return library.getCustomers().get(
                position % library.getCustomers().size());
    }

    private static String getTextContentOf(Node element, String name) {
        NodeList attributes = element.getChildNodes();
        for (int r = 0; r < attributes.getLength(); r++) {
            if (attributes.item(r).getNodeName().equals(name)) {
                return attributes.item(r).getTextContent();
            }
        }
        return "";
    }
}
