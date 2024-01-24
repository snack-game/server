package com.snackgame.server.support.database;

import javax.sql.DataSource;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class CleanDatabaseBeforeEach implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        DataSource dataSource = getDataSourceFrom(context);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        new DatabaseCleaner(jdbcTemplate).clean();
    }

    private DataSource getDataSourceFrom(ExtensionContext context) {
        return SpringExtension.getApplicationContext(context)
                .getBean(DataSource.class);
    }
}
