package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.enums.IntConstants;
import com.edu.ulab.app.enums.LongConstants;
import com.edu.ulab.app.enums.StringConstatnts;
import com.edu.ulab.app.exception.MyException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.web.request.UserBookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserServiceImpl userService;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           UserServiceImpl userService, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        if (book.getPageCount() <= IntConstants.BOOK_UNREAL_PAGE_QTY_INT.getDigits())
            throw new MyException(StringConstatnts.INPUTED_BOOK_PAGE_QTY_IS_UNREAL.getText());
        log.info("Mapped book: {}", book);
        Book savedBook = getEmptyBook();
        try {
            savedBook = bookRepository.save(book);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("Saved book: {}", savedBook);
        return (savedBook == null) ? bookMapper.bookToBookDto(getEmptyBook()) : bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public void updateBookById(BookDto bookDto) {
        log.info("updateBookById begins: {}", bookDto);
        try {
            bookRepository.updateBookById
                    (bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPageCount());
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("updateBookById finished");
    }

    @Override
    public BookDto getBookById(int id) {
        log.info("getBookById begins - id: {}", id);
        if (id <= IntConstants.BOOK_ID_NOT_FOUND_INT.getDigits()){
            throw new MyException(StringConstatnts.INPUTED_BOOK_ID_IS_UNREAL.getText());
        }
        Book book = getEmptyBook();
        try {
            book = bookRepository.findById((long) id).orElse(getEmptyBook());
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("getBookById finished - book: {}", book);
        if (book == null || book.getId() == LongConstants.BOOK_ID_NOT_FOUND_LONG.getDigits()) {
            throw new MyException(StringConstatnts.INPUTED_BOOK_ID_WAS_NOT_FOUND_IN_DB.getText());
        }
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public void deleteBookById(int id) {
        log.info("deleteBookById begins: {}", id);
        if (id <= LongConstants.BOOK_ID_NOT_FOUND_LONG.getDigits()){
            throw new MyException(StringConstatnts.INPUTED_BOOK_ID_IS_UNREAL.getText());
        }
        try {
            bookRepository.deleteById((long) id);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("deleteBookById finished");
    }

    @Override
    public void deleteAllBooksByUserId(int userId){
        log.info("\n-- deleteAllBooksByPersonId userId: {}", userId);
        if (userId <= LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()){
            throw new MyException(StringConstatnts.INPUTED_USER_ID_IS_UNREAL.getText());
        }
        log.info("\n-- deleteAllBooksByPersonId deleting begins");
        try {
            bookRepository.deleteAllBooksByPersonId(userId);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("\n-- deleteAllBooksByPersonId deleting finished");
    }

    @Override
    public List<Integer> getListBooksIdsByUserIdCheckedAndGotFmDB(int userId){
        List<Book> books = getAllBooksByUserId(userId);

        List<Integer> booksIds = List.of(IntConstants.SMTHNG_GOES_WRONG_CONNECTION_TO_DB.getDigits());
        log.info("\n---- getListBooksIdsByUserIdCheckedAndGotFmDB - books: " + books);
        if (!books.isEmpty() || books != null){
            booksIds = books.stream().map(Book::getId).toList();
        }
        log.info("\n---- getListBooksIdsByUserIdCheckedAndGotFmDB - books: " + books);
        return booksIds;
    }

    @Override
    public List<Book> getAllBooksByUserId(int userId){
        log.info("\n---- getAllBooksByUserId - userId: " + userId);
        List<Book> books = new ArrayList<>();
        try {
            books = bookRepository.findAllByPersonId(userId);
        } catch (Exception e){
            log.error(e.toString());
            throw new MyException(e.toString());
        }
        log.info("\n---- getAllBooksByUserId - books: " + books);
        if (books == null) {
            throw new MyException(StringConstatnts.SMTHNG_GOES_WRONG_WITH_CONNECTING_DB.getText());
        }
        if (books.isEmpty()) {
            throw new MyException(StringConstatnts.USER_HAS_NO_ANY_BOOK_IN_HIS_OWN_CASE.getText());
        }
        return books;
    }

    @Override
    public List<Integer> getListBooksIdsForUserAndAddBooksInRepo(UserBookRequest userBookRequest, UserDto createdUser){
        return userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(this::createBook) // repo save
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
    }

    public Book getEmptyBook() {
        log.info("\n---- getEmptyBook");
        return Book.builder()
                .id(IntConstants.BOOK_ID_NOT_FOUND_INT.getDigits())
                .title("Это пустышка Book")
                .author("Нет данных author Book")
                .pageCount(IntConstants.BOOK_UNREAL_PAGE_QTY_INT.getDigits())
                .person(userService.getEmptyPerson())
                .build();
    }
}
