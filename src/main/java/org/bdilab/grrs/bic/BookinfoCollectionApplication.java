package org.bdilab.grrs.bic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author caytng@163.com
 * @date 2019/4/10
 */
@SpringBootApplication
@EnableJpaRepositories
@EntityScan
public class BookinfoCollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookinfoCollectionApplication.class, args);
    }
}
