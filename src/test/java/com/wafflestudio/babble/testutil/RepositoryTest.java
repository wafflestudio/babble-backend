package com.wafflestudio.babble.testutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.wafflestudio.babble.common.config.DatabaseConfig;
import com.wafflestudio.babble.common.config.TimeConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfig.class, TimeConfig.class, DatabaseCleaner.class})
@ExtendWith(DataClearExtension.class)
public @interface RepositoryTest {
}
