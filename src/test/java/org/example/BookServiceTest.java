package org.example;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private List<Book> bookDatabase;

    @InjectMocks
    private BookService bookService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.out.println("Starting tests for method with @Before");
    }
    @BeforeClass
    public static void setClass() {
        System.out.println("Setting up @BeforeClass");
    }
    @AfterClass
    public static void tearDownClass() {
        System.out.println("Compiling class after testing with @AfterClass");
    }
    @After
    public void tearDown() {
        System.out.println("Executing clean up operations after each test with @After");
    }

    //test searchBook()
    @Test
    public void searchBookTitleMatch() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        Book book2 = new Book("Harry Potter", "J.K. Rowling", "Fantasy", 24.99);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());
        List<Book> results = bookService.searchBook("Atomic Habits");
        assertEquals(1, results.size());
        assertTrue(results.contains(book1));
    }

    @Test
    public void searchBookAuthorMatch() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        Book book2 = new Book("Harry Potter", "J.K. Rowling", "Fantasy", 24.99);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());
        List<Book> results = bookService.searchBook("J.K. Rowling");
        assertEquals(1, results.size());
        assertTrue(results.contains(book2));
    }

    @Test
    public void searchBookGenreMatch() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        Book book2 = new Book("Harry Potter", "J.K. Rowling", "Fantasy", 24.99);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());
        List<Book> results = bookService.searchBook("Self-Help");
        assertEquals(1, results.size());
        assertTrue(results.contains(book1));
    }

    @Test
    public void searchBookNoMatch() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        Book book2 = new Book("Harry Potter", "J.K. Rowling", "Fantasy", 24.99);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());
        List<Book> searchResult = bookService.searchBook("Nonexisting Book");
        assertTrue(searchResult.isEmpty());
    }

    //test purchaseBook()
    @Test
    public void purchaseBookExists() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        User user = new User("Mary", "mary123", "mary@example.com");
        when(bookDatabase.contains(book1)).thenReturn(true);
        boolean result = bookService.purchaseBook(user, book1);
        assertTrue(result);
    }

    @Test
    public void purchaseBookDoesNotExist() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        User user = new User("Mary", "mary123", "mary@example.com");
        when(bookDatabase.contains(book1)).thenReturn(false);
        boolean result = bookService.purchaseBook(user, book1);
        assertFalse(result);
    }

    @Test
    public void purchaseBookInsufficientBalance() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        User user = new User("Mary", "mary123", "mary@example.com", 15.30);
        when(bookDatabase.contains(book1)).thenReturn(true);
        boolean result = bookService.purchaseBook(user, book1);
        assertTrue(result);
    }

    //test addBook()
    @Test
    public void addBookPositive() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        when(bookDatabase.contains(book1)).thenReturn(false);
        boolean result = bookService.addBook(book1);
        assertTrue(result);
        verify(bookDatabase).add(book1);
    }

    @Test
    public void addBookNegativeBookInDatabaseAlready() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        when(bookDatabase.contains(book1)).thenReturn(true);
        boolean result = bookService.addBook(book1);
        assertFalse(result);
        verify(bookDatabase, never()).add(book1);
    }

    @Test
    public void addBookEdge() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        when(bookDatabase.contains(book1)).thenReturn(true);
        boolean result = bookService.addBook(book1);
        assertFalse(result);
        verify(bookDatabase, never()).add(book1);

        // Test adding a new book to the empty database
        Book book2 = new Book("Twilight", "Stephenie Meyer", "Fantasy", 8.99);
        when(bookDatabase.contains(book2)).thenReturn(false);
        boolean result2 = bookService.addBook(book2);
        assertTrue(result2);
        verify(bookDatabase).add(book2);
    }

    //test removeBook()
    @Test
    public void removeBookPositive() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        when(bookDatabase.remove(book1)).thenReturn(true);
        boolean result = bookService.removeBook(book1);
        assertTrue(result);
        verify(bookDatabase).remove(book1);
    }

    @Test
    public void removeBookNegativeBookNotInDatabase() {
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        when(bookDatabase.remove(book1)).thenReturn(false);
        boolean result = bookService.removeBook(book1);
        assertFalse(result);
        verify(bookDatabase).remove(book1);
    }

    @Test
    public void removeBookEdge() {
        // Test removing a book that is not in the database
        Book book1 = new Book("Atomic Habits", "James Clear", "Self-Help", 15.99);
        when(bookDatabase.remove(book1)).thenReturn(false);
        boolean result = bookService.removeBook(book1);
        assertFalse(result);
        verify(bookDatabase).remove(book1);

        // Test removing a book that is in the database
        Book book2 = new Book("Twilight", "Stephenie Meyer", "Fantasy", 8.99);
        when(bookDatabase.remove(book2)).thenReturn(true);
        boolean result2 = bookService.removeBook(book2);
        assertTrue(result2);
        verify(bookDatabase).remove(book2);
    }
}