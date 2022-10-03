package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.enums.LongConstants;
import com.edu.ulab.app.enums.StringConstatnts;
import com.edu.ulab.app.exception.MyException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import com.edu.ulab.app.service.impl.BookServiceImplTemplate;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import com.edu.ulab.app.service.impl.UserServiceImplTemplate;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserServiceImpl userService;
    private final BookServiceImpl bookService;
//    private final UserServiceImplTemplate userService;
//    private final BookServiceImplTemplate bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserServiceImpl userService,
                          BookServiceImpl bookService,
//                          UserServiceImplTemplate userService,
//                          BookServiceImplTemplate bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto); // repo save
        log.info("Created user: {}", createdUser);

        List<Integer> bookIdList = bookService.getListBooksIdsForUserAndAddBooksInRepo(userBookRequest, createdUser);
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    @Transactional
    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest) {
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("updateUserWithBooks userDto: {}", userDto);
        int userId = userService.updateUser(userDto);
        log.info("updateUserWithBooks userId: {}", userId);
        userDto.setId(Math.toIntExact(userId));
        log.info("updateUserWithBooks userDto: {}", userDto);
        bookService.deleteAllBooksByUserId(Math.toIntExact(userId));

        List<Integer> bookIdList = bookService.getListBooksIdsForUserAndAddBooksInRepo(userBookRequest, userDto);
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(bookIdList)
                .build();
    }

    @Transactional
    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("getUserWithBooks - userId: {}", userId);
        UserDto userDto = userService.getUserById(Math.toIntExact(userId));
        log.info("getUserWithBooks: got user from DB - userDto: {}", userDto);
        List<Integer> bookIdListFmDB = bookService.getListBooksIdsByUserIdCheckedAndGotFmDB(userDto.getId());
        log.info("getUserWithBooks - bookIdListFmDB: {}", bookIdListFmDB);
        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(bookIdListFmDB)
                .build();
    }

    @Transactional
    public void deleteUserWithBooks(Long userId) {
        log.info("deleteUserWithBooks - userId: {}", userId);
        if (userId <= LongConstants.USER_ID_NOT_FOUND_LONG.getDigits()) {
            throw new MyException(StringConstatnts.INPUTED_USER_ID_IS_UNREAL.getText());
        }
        log.info("deleteUserWithBooks - deleting all books fm DB");
        bookService.deleteAllBooksByUserId(Math.toIntExact(userId));
        log.info("deleteUserWithBooks - delete user fm DB");
        userService.deleteUserByUserIdCheckedIsInDB(Math.toIntExact(userId));
    }
}
