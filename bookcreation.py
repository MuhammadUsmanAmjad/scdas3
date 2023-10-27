# List of book data
book_data = [
    ("C++", "Ali Ahmad", 2004, 9, "book1.txt"),
    ("PHP", "John Smith", 2001, 22, "book2.txt"),
    ("Python", "Emma Johnson", 2010, 15, "book3.txt"),
    ("Java", "Michael Davis", 2015, 18, "book4.txt"),
    ("C", "Sarah Miller", 1999, 11, "book5.txt"),
    ("C++", "David Wilson", 2005, 10, "book6.txt"),
    ("Ruby", "Laura Brown", 2009, 14, "book7.txt"),
    ("JavaScript", "Robert Jones", 2011, 20, "book8.txt"),
    ("C#", "Olivia Lee", 2016, 12, "book9.txt"),
    ("Perl", "James Wilson", 1998, 8, "book10.txt")
]

# Create and write data to text files
for i, book_info in enumerate(book_data, start=1):
    language, author, year, chapters, filename = book_info
    description = f"""**Book {i} ({language})**
"{language} Programming: {language} Programming Guide" by {author}
Published in {year}, this book delves into the depths of {language} programming, covering {chapters} chapters. It's a must-read for {language} enthusiasts.
"""

    with open(filename, 'w') as file:
        file.write(description)

print("Text files have been created and populated with book descriptions.")
