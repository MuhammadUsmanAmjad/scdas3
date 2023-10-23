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

