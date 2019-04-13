package org.bdilab.grrs.bic.controller;

import org.bdilab.grrs.bic.entity.UserInfo;
import org.bdilab.grrs.bic.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author caytng@163.com
 * @date 2019/4/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    private UserInfo userInfo;

    @Before
    public void setUp() {
        userRepository.insert("admin", "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG", "system", "system");
        userInfo = new UserInfo("admin", "password");
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void modifySelfPswd() {
        ResponseEntity loginResult = userController.login(userInfo);
        Assert.assertNotNull(loginResult);
        Assert.assertTrue(HttpStatus.OK.equals(loginResult.getStatusCode()));

        ResponseEntity modifyResult = userController.modifySelfPswd(userInfo, "password", "123456");
        Assert.assertNotNull(modifyResult);
        Assert.assertTrue(modifyResult.getBody() instanceof Boolean);
        Assert.assertTrue((Boolean) modifyResult.getBody());

        loginResult = userController.login(userInfo);
        Assert.assertNotNull(loginResult);
        Assert.assertTrue(HttpStatus.METHOD_FAILURE.equals(loginResult.getStatusCode()));

        userInfo.setUserPswd("123456");
        loginResult = userController.login(userInfo);
        Assert.assertNotNull(loginResult);
        Assert.assertTrue(HttpStatus.OK.equals(loginResult.getStatusCode()));
    }
}
