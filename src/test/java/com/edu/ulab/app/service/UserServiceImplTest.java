package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.MyException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание и сохранение пользователя в БД. Должно пройти успешно.")
    void createUserTest() {
        //given
        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    // get
    @Test
    @DisplayName("Получение юзера по айди. Должно пройти успешно.")
    void getUserByIdTest() {
        //Given
        Person savedPerson  = new Person();
        savedPerson.setId(1);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");

        //When do
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedPerson));
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //Then result
        UserDto testResultUserDto = userService.getUserById(1);
        assertEquals(result, testResultUserDto);
    }

    // * failed
    @Test
    @DisplayName("Получение юзера по айди - выкинет эксепшн, что нет такого айди. Должно пройти успешно.")
    void getUserByIdTestException() {
        //Given
        Person savedPerson  = new Person();
        savedPerson.setId(1);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");

        //When do
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedPerson));
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //Then result
        MyException thrown = Assertions.assertThrows(
                MyException.class, () -> {
                    userService.getUserById(2);
                }, "MyException: INPUTED_USER_ID_WAS_NOT_FOUND_IN_DB was expected"
        );
        Assertions.assertEquals("INPUTED_USER_ID_WAS_NOT_FOUND_IN_DB", thrown.getMessage());
    }

    // update
    @Test
    @DisplayName("Обновить юзера в БД. Должно пройти успешно.")
    void updateUserTest() {
        //Given
        UserDto userDtoWithNewData = new UserDto();
        userDtoWithNewData.setAge(22);
        userDtoWithNewData.setFullName("test name11");
        userDtoWithNewData.setTitle("test title11");

        Person savedPerson = new Person();
        savedPerson.setId(1);
        savedPerson.setFullName("test name11");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title11");

        //When do
        when(userRepository.findByFullNameAndTitle(userDtoWithNewData.getFullName(),
                        userDtoWithNewData.getTitle())).thenReturn(Optional.of(savedPerson));
        Mockito.doAnswer(new Answer<Person>() {
            public Person answer(InvocationOnMock invocation) {
                savedPerson.setAge(userDtoWithNewData.getAge());
                return savedPerson;
            }
        }).when(userRepository).save(savedPerson);

        //Then result
        int idOfUpdatedPerson = userService.updateUser(userDtoWithNewData);
        assertEquals(1, idOfUpdatedPerson);
        assertEquals(22, savedPerson.getAge());
    }

    // delete
    @Test
    @DisplayName("Удалить юзера из БД по айди. Должно пройти успешно.")
    void deleteUserByUserIdCheckedIsInDBTest() {
        //Given
        Person savedPerson1 = new Person();
        savedPerson1.setId(1);
        savedPerson1.setFullName("test name11");
        savedPerson1.setAge(11);
        savedPerson1.setTitle("reader");

        Person savedPerson2 = new Person();
        savedPerson2.setId(2);
        savedPerson2.setFullName("test name22");
        savedPerson2.setAge(22);
        savedPerson2.setTitle("reader");

        List<Person> personList = new ArrayList<>();
        personList.add(savedPerson1);
        personList.add(savedPerson2);

        //When do
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                personList.remove(savedPerson2);
                return null;
            }
        }).when(userRepository).deleteById(2L);

        //Then result
        userService.deleteUserByUserIdCheckedIsInDB(2);
        Assertions.assertEquals(1, personList.size()); // exp act
    }
}
//Given
//When do
//Then result