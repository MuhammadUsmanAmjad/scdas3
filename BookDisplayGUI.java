import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

class Book {
    private String title;
    private String author;
    private String publishDate;
    private String content;
    private int popularityCount;
    private String contentFileName;

    public Book(String title, String author, String publishDate, String content, int popularityCount) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.content = content;
        this.popularityCount = popularityCount;
    }

    // Getters and setters for Book attributes
    public int getPopularityCount() {
        return popularityCount;
    }

    public void setPopularityCount(int popularityCount) {
        this.popularityCount = popularityCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentFileName(String ContentFile) {
        this.contentFileName = ContentFile;
    }

    public String getContentFileName() {
        return this.contentFileName;
    }
}

public class BookDisplayGUI {
    private JFrame mainFrame;
    private JTable bookTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JFrame contentFrame;
    private JButton popularityButton;
    // private String Filename;
    List<Book> books = new ArrayList<>();

    public BookDisplayGUI() {
        ReadBooksFromFile(books);
        mainFrame = new JFrame("Book Display");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 400);

        createButtons();

        String[] columnNames = { "Title", "Author", "Publication Date", "Read" };
        Object[][] data = new Object[books.size()][4];

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = book.getAuthor();
            data[i][2] = book.getPublishDate();
            data[i][3] = "Read"; // Placeholder for button
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        bookTable = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 3) {
                    return getValueAt(0, column).getClass();
                }
                return super.getColumnClass(column);
            }
        };

        bookTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int row = bookTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    bookTable.setSelectionBackground(Color.RED);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bookTable.setSelectionBackground(bookTable.getBackground());
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainFrame.add(scrollPane);

        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                String content = books.get(selectedRow).getContent();
                showBookContent(content);
            }
        });

        mainFrame.setVisible(true);

    }

    private void createButtons() {
        // Create a JPanel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout()); // Buttons will be horizontally aligned

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        popularityButton = new JButton("Popularity Graph");

        // Add action listeners to the buttons
        addButton.addActionListener(new AddButtonActionListener());
        editButton.addActionListener(new EditButtonActionListener());
        deleteButton.addActionListener(new DeleteButtonActionListener());
        popularityButton.addActionListener(new PopularityButtonActionListener()); // Update the action listener

        // Add buttons to the buttonPanel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(popularityButton); // Add the "Popularity Graph" button

        // Add the buttonPanel to the mainFrame at the top (North) position
        mainFrame.add(buttonPanel, BorderLayout.SOUTH); // Place the buttons at the Bottom
    }

    private void showBookContent(String content) {
        if (contentFrame == null) {
            contentFrame = new JFrame("Book Content");
            contentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Add a window listener to handle window closing
            contentFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int result = JOptionPane.showConfirmDialog(contentFrame, "Do you want to exit?", "Confirm Exit",
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        contentFrame.dispose(); // Close the content frame if the user confirms
                    }
                }
            });

            contentFrame.setSize(600, 400);

            JTextArea contentTextArea = new JTextArea(content);
            contentTextArea.setEditable(false);

            JScrollPane contentScrollPane = new JScrollPane(contentTextArea);
            contentFrame.add(contentScrollPane);

            contentFrame.setVisible(true);
        } else {
            // If the contentFrame is already open, just update the content
            JTextArea contentTextArea = (JTextArea) ((JScrollPane) contentFrame.getContentPane().getComponent(0))
                    .getViewport().getView();
            contentTextArea.setText(content);
            contentFrame.setVisible(true);
        }
    }

    private void drawBarGraph(Graphics g, List<Book> topBooks) {
        int x = 50; // Starting X-coordinate
        int y = 300; // Starting Y-coordinate
        int barWidth = 50; // Width of each bar

        // Maximum popularity count to scale the graph
        int maxPopularity = topBooks.get(0).getPopularityCount();

        // Draw the bars
        for (Book book : topBooks) {
            int popularity = book.getPopularityCount();
            int barHeight = (int) (popularity * 300.0 / maxPopularity); // Scale to fit the graph

            g.setColor(Color.BLUE);
            g.fillRect(x, y - barHeight, barWidth, barHeight);

            g.setColor(Color.BLACK);
            g.drawRect(x, y - barHeight, barWidth, barHeight);

            // Display the book title below the bar
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(book.getTitle(), x, y + 20);

            x += 100; // Adjust the X-coordinate for the next bar
        }
    }

    private void createPopularityGraph() {
        JFrame graphFrame = new JFrame("Popularity Count Bar Graph");
        graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        graphFrame.setSize(600, 400);

        // Sort books by popularity count in descending order
        Collections.sort(books, (b1, b2) -> Integer.compare(b2.getPopularityCount(), b1.getPopularityCount()));

        // Take the top 3 books (most popular)
        List<Book> topBooks = books.subList(0, Math.min(3, books.size()));

        JPanel graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarGraph(g, topBooks);
            }
        };

        graphFrame.add(graphPanel);
        graphFrame.setVisible(true);
    }

    private Book searchBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
        model.setRowCount(0); // Clear the table

        for (Book book : books) {
            String title = book.getTitle();
            String author = book.getAuthor();
            String publishDate = book.getPublishDate();

            JButton readButton = new JButton("Read");

            // Set the action for the "Read" button
            readButton.addActionListener(e -> {
                int row = bookTable.getSelectedRow();
                if (row >= 0) {
                    String content = books.get(row).getContent();
                    showBookContent(content);
                }
            });

            // Set the button as the cell value for the "Read" column
            model.addRow(new Object[] { title, author, publishDate, readButton });
        }

        // After updating the table model, you can reset the cell editor and renderer
        // for the "Read" column
        bookTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        bookTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = bookTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    bookTable.setRowSelectionInterval(row, row);
                }
            }
        });

    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create an input dialog to get book details
            String title = JOptionPane.showInputDialog(mainFrame, "Enter Title:");
            String author = JOptionPane.showInputDialog(mainFrame, "Enter Author:");
            String publishDate = JOptionPane.showInputDialog(mainFrame, "Enter Publication Date:");
            String content = JOptionPane.showInputDialog(mainFrame, "Enter Content:");
            String popularityCount = JOptionPane.showInputDialog(mainFrame, "Enter Popularity Count:");
            int popularityCount1 = Integer.parseInt(popularityCount);

            // Check if the user canceled the input
            if (title != null && author != null && publishDate != null && content != null) {
                Book newBook = new Book(title, author, publishDate, content, popularityCount1);
                // Add the new book to the list of books
                books.add(newBook);
                writetofile();
                // Update the table to reflect the changes
                updateTable(); // Call this to update the table
            }
        }
    }

    private class EditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = JOptionPane.showInputDialog(mainFrame, "Enter Title to Edit:");
            if (title != null) {
                Book bookToEdit = searchBookByTitle(title);
                if (bookToEdit != null) {
                    // Create input dialogs to edit book details
                    String newTitle = JOptionPane.showInputDialog(mainFrame, "Edit Title:", bookToEdit.getTitle());
                    String author = JOptionPane.showInputDialog(mainFrame, "Edit Author:", bookToEdit.getAuthor());
                    String publishDate = JOptionPane.showInputDialog(mainFrame, "Edit Publication Date:",
                            bookToEdit.getPublishDate());
                    String content = JOptionPane.showInputDialog(mainFrame, "Edit Content:", bookToEdit.getContent());
                    String popularityCount = JOptionPane.showInputDialog(mainFrame, "Edit Popularity Count:",
                            bookToEdit.getPopularityCount());
                    int popularityCount1 = Integer.parseInt(popularityCount);
                    // Check if the user canceled the input
                    if (newTitle != null && author != null && publishDate != null && content != null
                            && popularityCount1 != 0) {
                        // Update the book's details
                        bookToEdit.setTitle(newTitle);
                        bookToEdit.setAuthor(author);
                        bookToEdit.setPublishDate(publishDate);
                        bookToEdit.setContent(content);
                        bookToEdit.setPopularityCount(popularityCount1);
                        writetofile();
                        // Update the table to reflect the changes
                        updateTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = JOptionPane.showInputDialog(mainFrame, "Enter Title to Delete:");
            if (title != null) {
                Book bookToDelete = searchBookByTitle(title);
                if (bookToDelete != null) {
                    // Remove the book from the list
                    File file = new File(bookToDelete.getContentFileName());
                    if (file.exists()) {
                        file.delete();
                    }
                    books.remove(bookToDelete);
                    writetofile();
                    // Update the table to reflect the changes
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void writetofile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("book_data.txt"))) {
            for (Book book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getPublishDate() + ","
                        + book.getPopularityCount() + "," + book.getContentFileName());
                writer.newLine();

                // Write content to the specified file
                try (BufferedWriter contentWriter = new BufferedWriter(new FileWriter(book.getContentFileName()))) {
                    contentWriter.write(book.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        books.clear();
        ReadBooksFromFile(books);
    }

    private class PopularityButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            createPopularityGraph();
        }
    }

    class ReadAction extends AbstractAction {
        private String content;

        public ReadAction(String content) {
            super("Read");
            this.content = content;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showBookContent(content);
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true); // Fix the button appearance
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            button.setText("Read");
            return button;
        }

        public Object getCellEditorValue() {
            return "Read";
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            // Set the default text for the button
            setText("READ");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            // Display the text "READ" for the button
            return this;
        }
    }

    private void ReadBooksFromFile(List<Book> books) {
        try (BufferedReader reader = new BufferedReader(new FileReader("book_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 5) { // Assuming popularity count is the fourth element
                    String title = bookData[0].trim();
                    String author = bookData[1].trim();
                    String publishDate = bookData[2].trim();
                    int popularityCount = Integer.parseInt(bookData[3].trim());
                    String contentFileName = bookData[4].trim();

                    // Read content from the file
                    String content = readContentFromFile(contentFileName);

                    Book book = new Book(title, author, publishDate, content, popularityCount);
                    book.setContentFileName(contentFileName);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read content from a file
    private String readContentFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader contentReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = contentReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            new BookDisplayGUI();
        });
    }

}
