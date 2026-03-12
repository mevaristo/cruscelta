package com.cruscelta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfig.class)
public class CrusceltaApplicationTest {

    @Test
    void contextLoads() {

    }
}
