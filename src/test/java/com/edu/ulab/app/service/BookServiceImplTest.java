package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.MyException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;
    @Mock
    UserServiceImpl userService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание и сохранение книги в БД. Должно пройти успешно.")
    void createBookTest() {
        //given
        Person person  = new Person();
        person.setId(1);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1);
        result.setUserId(1);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1, bookDtoResult.getId());
    }

    // get
    @Test
    @DisplayName("Получение книги по айди. Должно пройти успешно.")
    void getBookByIdTest() {
        //Given
        Person person  = new Person();
        person.setId(1);

        BookDto resultBookDto = new BookDto();
        resultBookDto.setId(2);
        resultBookDto.setUserId(1);
        resultBookDto.setAuthor("test author");
        resultBookDto.setTitle("test title");
        resultBookDto.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(2);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //When do
        when(bookRepository.findById(2L)).thenReturn(Optional.of(savedBook));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(resultBookDto);

        //Then result
        BookDto bookDtoResult = bookService.getBookById(2);
        assertEquals(2, bookDtoResult.getId());
    }

    // * failed
    @Test
    @DisplayName("Получение книги по айди - выкинет эксепшн, что нет такого айди. Должно пройти успешно.")
    void getBookByIdTestException() {
        //Given
        Person person  = new Person();
        person.setId(1);

        BookDto resultBookDto = new BookDto();
        resultBookDto.setId(2);
        resultBookDto.setUserId(1);
        resultBookDto.setAuthor("test author");
        resultBookDto.setTitle("test title");
        resultBookDto.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(2);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //When do
        when(bookRepository.findById(2L)).thenReturn(Optional.of(savedBook));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(resultBookDto);

        //Then result
        MyException thrown = Assertions.assertThrows(
                MyException.class, () -> {
                    bookService.getBookById(1);
                }, "MyException: INPUTED_BOOK_ID_WAS_NOT_FOUND_IN_DB was expected"
        );
        Assertions.assertEquals("INPUTED_BOOK_ID_WAS_NOT_FOUND_IN_DB", thrown.getMessage());
    }

    // get all
    @Test
    @DisplayName("Получение всех книг по айди юзера. Должно пройти успешно.")
    void getAllBooksByUserIdTest() {
        //Given
        Person person  = new Person();
        person.setId(1);

        Book savedBook1 = new Book();
        savedBook1.setId(1);
        savedBook1.setPageCount(101);
        savedBook1.setTitle("test title1");
        savedBook1.setAuthor("test author1");
        savedBook1.setPerson(person);

        Book savedBook2 = new Book();
        savedBook2.setId(2);
        savedBook2.setPageCount(102);
        savedBook2.setTitle("test title2");
        savedBook2.setAuthor("test author2");
        savedBook2.setPerson(person);

        List<Book> resultBooks = new ArrayList<>();
        resultBooks.add(savedBook1);
        resultBooks.add(savedBook2);

        //When do
        when(bookRepository.findAllByPersonId(1)).thenReturn(resultBooks);

        //Then result
        List<Book> expectedBooks = bookService.getAllBooksByUserId(1);
        Assertions.assertEquals(expectedBooks, resultBooks); // exp act
    }

    // get * failed
    @Test
    @DisplayName("Получение всех книг по айди юзера - должно выкинуть эксепшн - нет книг по такому юзер айди. " +
            "Должно пройти успешно.")
    void getAllBooksByUserIdTestException() {
        //Given
        Person person  = new Person();
        person.setId(1);

        Book savedBook1 = new Book();
        savedBook1.setId(1);
        savedBook1.setPageCount(101);
        savedBook1.setTitle("test title1");
        savedBook1.setAuthor("test author1");
        savedBook1.setPerson(person);

        Book savedBook2 = new Book();
        savedBook2.setId(2);
        savedBook2.setPageCount(102);
        savedBook2.setTitle("test title2");
        savedBook2.setAuthor("test author2");
        savedBook2.setPerson(person);

        List<Book> resultBooks = new ArrayList<>();
        resultBooks.add(savedBook1);
        resultBooks.add(savedBook2);

        //When do
        when(bookRepository.findAllByPersonId(1)).thenReturn(resultBooks);

        //Then result
        MyException thrown = Assertions.assertThrows(
                MyException.class, () -> {
                    bookService.getAllBooksByUserId(2);
                }, "MyException: USER_HAS_NO_ANY_BOOKS_IN_HIS_OWN_CASE was expected"
        );
        Assertions.assertEquals("USER_HAS_NO_ANY_BOOKS_IN_HIS_OWN_CASE", thrown.getMessage());
    }

    // update
    @Test
    @DisplayName("Обновить данные по книге в БД по айди. Должно пройти успешно.")
    void updateBookByIdTest() {
        //Given
        Person person  = new Person();
        person.setId(1);

        BookDto bookDtoWithNewData = new BookDto();
        bookDtoWithNewData.setId(1);
        bookDtoWithNewData.setUserId(1);
        bookDtoWithNewData.setTitle("test title1");
        bookDtoWithNewData.setAuthor("test author1");
        bookDtoWithNewData.setPageCount(999); // new data, old=101

        Book updatedBook = new Book();
        updatedBook.setId(1);
        updatedBook.setPerson(person);
        updatedBook.setTitle("test title1");
        updatedBook.setAuthor("test author1");
        updatedBook.setPageCount(101);

        //When do
        Mockito.doAnswer(new Answer<Book>() {
            public Book answer(InvocationOnMock invocation) {
                updatedBook.setPageCount(bookDtoWithNewData.getPageCount());
                return updatedBook;
            }
        }).when(bookRepository).updateBookById(bookDtoWithNewData.getId(), bookDtoWithNewData.getTitle(),
                bookDtoWithNewData.getAuthor(), bookDtoWithNewData.getPageCount());

        //Then result
        bookService.updateBookById(bookDtoWithNewData);
        Assertions.assertEquals(999, updatedBook.getPageCount()); // exp act
    }

    // delete
    @Test
    @DisplayName("Удалить книгу из БД по айди. Должно пройти успешно.")
    void deleteBookByIdTest() {
        //Given
        Person person  = new Person();
        person.setId(1);

        Book savedBook1 = new Book();
        savedBook1.setId(1);
        savedBook1.setPageCount(101);
        savedBook1.setTitle("test title1");
        savedBook1.setAuthor("test author1");
        savedBook1.setPerson(person);

        Book savedBook2 = new Book();
        savedBook2.setId(2);
        savedBook2.setPageCount(102);
        savedBook2.setTitle("test title2");
        savedBook2.setAuthor("test author2");
        savedBook2.setPerson(person);

        List<Book> resultBooks = new ArrayList<>();
        resultBooks.add(savedBook1);
        resultBooks.add(savedBook2);

        //When do
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                resultBooks.remove(savedBook2);
                return null;
            }
        }).when(bookRepository).deleteById(2L);

        //Then result
        bookService.deleteBookById(2);
        Assertions.assertEquals(1, resultBooks.size()); // exp act
    }
}
//Given
//When do
//Then result