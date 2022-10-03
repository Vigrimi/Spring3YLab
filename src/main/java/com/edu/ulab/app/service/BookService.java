package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.web.request.UserBookRequest;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDto);

    void updateBookById(BookDto bookDto);

    BookDto getBookById(int id);

    void deleteBookById(int id);

    void deleteAllBooksByUserId(int userId);

    List<Integer> getListBooksIdsByUserIdCheckedAndGotFmDB(int userId);

    List<Book> getAllBooksByUserId(int userId);

    List<Integer> getListBooksIdsForUserAndAddBooksInRepo(UserBookRequest userBookRequest, UserDto createdUser);
}
