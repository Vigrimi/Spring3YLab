package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.web.request.UserBookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return bookDto;
    }

    @Override
    public void updateBookById(BookDto bookDto) {

    }

    @Override
    public BookDto getBookById(int id) {
        // ?????????????????????? ???????????????????? ????????????
        return null;
    }

    @Override
    public void deleteBookById(int id) {
        // ?????????????????????? ???????????????????? ????????????
    }

    @Override
    public void deleteAllBooksByUserId(int userId) {

    }

    @Override
    public List<Integer> getListBooksIdsByUserIdCheckedAndGotFmDB(int userId) {
        return null;
    }

    @Override
    public List<Book> getAllBooksByUserId(int userId) {
        return null;
    }

    @Override
    public List<Integer> getListBooksIdsForUserAndAddBooksInRepo(UserBookRequest userBookRequest, UserDto createdUser) {
        return null;
    }
}
