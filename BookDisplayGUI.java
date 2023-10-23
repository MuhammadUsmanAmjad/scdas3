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

