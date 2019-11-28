package com.api.exercise.services;

import com.api.exercise.config.JwtConfig;
import com.api.exercise.dto.PhoneInput;
import com.api.exercise.dto.UserInput;
import com.api.exercise.entities.Phone;
import com.api.exercise.entities.User;
import com.api.exercise.repository.UserRepository;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(userRepository, bCryptPasswordEncoder, jwtConfig);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("password-encoded");
        when(jwtConfig.getSecret()).thenReturn("super-sercret");
        when(jwtConfig.getExpirationTime()).thenReturn(3);
        when(jwtConfig.getIssuer()).thenReturn("me");
    }

    @Test
    public void test_create_user_ok() {
        UserInput userInput = this.createUserInput();
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(this.userMock(userInput));
        User actualUser = userService.saveUser(userInput);
        Assert.assertNotNull(actualUser);
        Assert.assertNotNull(actualUser.getId());
        Assert.assertEquals(userInput.getEmail(), actualUser.getEmail());
        Assert.assertEquals(userInput.getName(), actualUser.getName());
        Assert.assertEquals(userInput.getPhones().size(), actualUser.getPhones().size());
    }

    @Test(expected = EntityExistsException.class)
    public void test_create_user_return_error_when_user_exists() {
        UserInput userInput = this.createUserInput();
        when(userRepository.findByEmail(anyString())).thenReturn(this.userMock(userInput));
        userService.saveUser(userInput);
    }

    private UserInput createUserInput() {
        return UserInput
                .builder()
                .name("Name")
                .password("pass")
                .email("email@email.com")
                .phone(
                        PhoneInput
                                .builder()
                                .cityCode("1")
                                .countryCode("56")
                                .number("1233456")
                                .build()
                )
                .build();
    }

    private User userMock(UserInput userInput) {
        Date date = new Date();
        User user = User
                .builder()
                .active(true)
                .created(date)
                .modified(date)
                .email(userInput.getEmail())
                .id(UUID.randomUUID().toString())
                .token("token123456")
                .lastLogin(date)
                .name(userInput.getName())
                .password("password")
                .build();


        if (userInput.getPhones() != null) {
            List<Phone> phones = Lists.newArrayList();
            long phoneId = 1;
            for (PhoneInput phoneInput : userInput.getPhones()) {
                phones
                        .add(
                                Phone
                                        .builder()
                                        .id(phoneId)
                                        .number(phoneInput.getNumber())
                                        .cityCode(phoneInput.getCityCode())
                                        .countryCode(phoneInput.getCountryCode())
                                        .user(user)
                                        .build()
                        );
            }
            user.setPhones(phones);
        }
        return user;
    }
}
