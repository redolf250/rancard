package com.redolf.rancard.services.impls;

import org.testcontainers.containers.MySQLContainer;

abstract class AbstractContainerBaseTest {
    static final MySQLContainer MY_SQL_CONTAINER;
    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:5.7")
                .withDatabaseName("test");
        MY_SQL_CONTAINER.start();
    }
}
