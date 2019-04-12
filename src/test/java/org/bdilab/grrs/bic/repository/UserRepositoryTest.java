package org.bdilab.grrs.bic.repository;

import org.bdilab.grrs.bic.entity.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class UserRepositoryTest {
    private static final String SYSTEM_NAME = "system";
    private static final String ADMIN_NAME = "admin";
    private static final String USER_NAME = "caytng";
    private static final String PSWD1 = "password";
    private static final String PSWD2 = "123456";
    
    @Autowired
    private UserRepository repository;

    @Before
    public void setup() {
        repository.insert(ADMIN_NAME, PSWD1, SYSTEM_NAME, SYSTEM_NAME);
    }

    @After
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void test() {
        findAllByCreator();
        findByUserName();
        insert();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        update();
        remove();
        enable();
    }

    private void findAllByCreator() {
        List<User> users = repository.findAllByCreator(SYSTEM_NAME);
        Assert.assertEquals(users.size(), 1);

        User admin = users.get(0);
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin.getUserName(), ADMIN_NAME);
    }

    private void findByUserName() {
        User admin = repository.findByUserName(ADMIN_NAME);
        Assert.assertNotNull(admin);
        Assert.assertFalse(admin.getDeleted());
        Assert.assertEquals(admin.getUserPswd(), "password");
        Assert.assertEquals(admin.getCreator(), admin.getModifier());
    }

    private void insert() {
        Integer result = repository.insert(USER_NAME, PSWD2, SYSTEM_NAME, SYSTEM_NAME);
        Assert.assertEquals((long)result, 1);

        User user = repository.findByUserName(USER_NAME);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        Assert.assertTrue(user.getCreateTime().equals(user.getModifyTime()));
    }

    private void update() {
        Integer result = repository.update(ADMIN_NAME, PSWD2, SYSTEM_NAME);
        Assert.assertEquals((long)result, 1);

        User admin = repository.findByUserName(ADMIN_NAME);
        Assert.assertNotNull(admin);
        Assert.assertEquals(admin.getUserPswd(), PSWD2);
        Assert.assertFalse(admin.getCreateTime().equals(admin.getModifyTime()));
    }

    private void remove() {
        Integer result = repository.remove(USER_NAME);
        Assert.assertEquals((long)result, 1);

        User user = repository.findByUserName(USER_NAME);
        Assert.assertNull(user);

        List<User> users = repository.findAllByCreator(SYSTEM_NAME);
        Assert.assertEquals(users.size(), 2);
        Assert.assertFalse(users.get(0).getDeleted());
        Assert.assertTrue(users.get(1).getDeleted());
    }

    private void enable() {
        Integer result = repository.insert(USER_NAME, PSWD2, ADMIN_NAME, ADMIN_NAME);
        Assert.assertEquals((long)result, 2);

        User user = repository.findByUserName(USER_NAME);
        Assert.assertNotNull(user);
        Assert.assertFalse(user.getDeleted());
        Assert.assertFalse(user.getCreator().equals(user.getModifier()));
        Assert.assertFalse(user.getCreateTime().equals(user.getModifyTime()));
    }
}
