package org.bdilab.grrs.bic.controller;

import org.bdilab.grrs.bic.entity.User;
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

import java.util.List;

/**
 * @author caytng@163.com
 * @date 2019/4/12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    private UserInfo adminInfo;

    private UserInfo userInfo;

    @Before
    public void setUp() {
        userRepository.insert("admin", "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG", "system", "system");
        adminInfo = new UserInfo("admin", "password");
        userInfo = new UserInfo("caytng", "123456");
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void test() {
        loginAdmin();
        addOrEnableUser();
        disableUser();
        resetPswd();
    }

    private void loginAdmin() {
        ResponseEntity loginResult = userController.login(adminInfo);
        Assert.assertNotNull(loginResult);
        Assert.assertNotNull(loginResult.getStatusCode());
        Assert.assertTrue(HttpStatus.OK.equals(loginResult.getStatusCode()));
    }

    private void addOrEnableUser() {
        ResponseEntity insertResult = adminController.addOrEnableUser(adminInfo, userInfo);
        Assert.assertNotNull(insertResult);
        Assert.assertTrue(HttpStatus.CREATED.equals(insertResult.getStatusCode()));

        ResponseEntity listResult = adminController.listUsers(adminInfo);
        Assert.assertNotNull(listResult);
        Assert.assertTrue(listResult.getBody() instanceof List);
        List<User> userList = (List<User>) listResult.getBody();
        Assert.assertNotNull(userList);
        Assert.assertEquals(userList.size(), 1L);
        User userFromDB = userList.get(0);
        Assert.assertTrue(userInfo.getUserName().equals(userFromDB.getUserName()));
        Assert.assertFalse(userFromDB.getDeleted());
    }

    private void disableUser() {
        ResponseEntity result1 = adminController.disableUser(adminInfo, adminInfo.getUserName());
        Assert.assertNotNull(result1);
        Assert.assertTrue(HttpStatus.NOT_ACCEPTABLE.equals(result1.getStatusCode()));

        ResponseEntity result2 = adminController.disableUser(adminInfo, userInfo.getUserName());
        Assert.assertNotNull(result2);
        Assert.assertTrue(HttpStatus.OK.equals(result2.getStatusCode()));

        ResponseEntity loginResult = userController.login(userInfo);
        Assert.assertNotNull(loginResult);
        Assert.assertNotNull(loginResult.getStatusCode());
        Assert.assertTrue(HttpStatus.METHOD_FAILURE.equals(loginResult.getStatusCode()));
    }

    private void resetPswd() {
        ResponseEntity insertResult = adminController.addOrEnableUser(adminInfo, userInfo);
        Assert.assertNotNull(insertResult);
        Assert.assertTrue(HttpStatus.ACCEPTED.equals(insertResult.getStatusCode()));

        userInfo.setUserPswd("123");
        ResponseEntity resetResult = adminController.resetPswd(adminInfo, userInfo);
        Assert.assertNotNull(resetResult);
        Assert.assertTrue(HttpStatus.OK.equals(resetResult.getStatusCode()));
        Assert.assertTrue(resetResult.getBody() instanceof Boolean);
        Assert.assertTrue((Boolean) resetResult.getBody());

        ResponseEntity listResult = adminController.listUsers(adminInfo);
        Assert.assertNotNull(listResult);
        Assert.assertTrue(listResult.getBody() instanceof List);
        List<User> userList = (List<User>) listResult.getBody();
        Assert.assertNotNull(userList);
        Assert.assertEquals(userList.size(), 1L);
        User userFromDB = userList.get(0);
        Assert.assertTrue(userInfo.getUserName().equals(userFromDB.getUserName()));
        Assert.assertFalse(userFromDB.getDeleted());

        ResponseEntity loginResult = userController.login(userInfo);
        Assert.assertNotNull(loginResult);
        Assert.assertNotNull(loginResult.getStatusCode());
        Assert.assertTrue(HttpStatus.OK.equals(loginResult.getStatusCode()));
    }
}
