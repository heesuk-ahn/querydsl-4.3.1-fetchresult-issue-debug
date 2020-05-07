package com.heesuk.querydsl.debug.demo.util;

import javax.persistence.EntityManager;

import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;

public class JPAHelper {

    private final EntityManager entityManager;
    private final SQLTemplates sqlTemplates;

    public JPAHelper(EntityManager entityManager, SQLTemplates sqlTemplates) {
        this.entityManager = entityManager;
        this.sqlTemplates = sqlTemplates;
    }

    public JPASQLQuery<?> query() {
        return new JPASQLQuery<Void>(entityManager, new Configuration(sqlTemplates));
    }

}
