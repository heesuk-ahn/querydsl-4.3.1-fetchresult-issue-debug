//package com.heesuk.querydsl.debug.demo.repository;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//import org.springframework.stereotype.Repository;
//
//import com.heesuk.querydsl.debug.demo.entity.Book;
//import com.heesuk.querydsl.debug.demo.vo.BookName;
//import com.mysema.commons.lang.CloseableIterator;
//import com.querydsl.core.QueryResults;
//import com.querydsl.core.types.Expression;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//
//import static com.heesuk.querydsl.debug.demo.entity.QBook.book;
//
//@Repository
//public class BookRepository extends QuerydslRepositorySupport {
//
//    private final JPAQueryFactory queryFactory;
//
//    public BookRepository(JPAQueryFactory queryFactory) {
//        super(Book.class);
//        this.queryFactory = queryFactory;
//    }
//
//    //TODO: get fetch results
//    public QueryResults<Book> getBooks(int offset, int limit) {
//        return queryFactory
//            .selectFrom(book)
//            .offset(offset)
//            .limit(limit)
//            .fetchResults();
//    }
//
//    public QueryResults<BookName> getBooksWithProjection(int offset, int limit) {
//        Map<String, Expression<?>> bindings = new HashMap<>();
//        bindings.put("name", book.name);
//
//        return queryFactory
//            .select(Projections.bean(BookName.class, bindings))
//            .from(book)
//            .offset(offset)
//            .limit(limit)
//            .fetchResults();
//    }
//
//    public CloseableIterator<Book> getBooksByIterate() {
//        return queryFactory.selectFrom(book).iterate();
//    }
//
//    //TODO: get fetch iterate
//    //TODO: get fetch transform
//
//
//}

package com.heesuk.querydsl.debug.demo.repository;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.stereotype.Repository;

import com.heesuk.querydsl.debug.demo.entity.Book;
import com.heesuk.querydsl.debug.demo.util.JPAHelper;
import com.heesuk.querydsl.debug.demo.vo.BookName;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLTemplates;

import static com.heesuk.querydsl.debug.demo.entity.QBook.book;

@Repository
public class BookRepository {

    private final JPAQueryFactory queryFactory;
    private EntityManagerFactory emf;

    public BookRepository(JPAQueryFactory queryFactory, EntityManagerFactory emf) {
        this.queryFactory = queryFactory;
        this.emf = emf;
    }

    public QueryResults<Book> getBooks(int offset, int limit) {
        return queryFactory
            .selectFrom(book)
            .offset(offset)
            .limit(limit)
            .fetchResults();
    }

    public QueryResults<Book> getBooksFromJPASQL(int offset, int limit) {
        SQLTemplates oracleTemplate = OracleTemplates.builder().printSchema().build();
        JPAHelper jpaHelper = new JPAHelper(emf.createEntityManager(), oracleTemplate);

        return jpaHelper.query().select(book).from(book).offset(offset).limit(limit).fetchResults();
    }

    public QueryResults<BookName> getBooksWithProjection(int offset, int limit) {
        Map<String, Expression<?>> bindings = new HashMap<>();
        bindings.put("name", book.name);

        return queryFactory
            .select(Projections.bean(BookName.class, bindings))
            .from(book)
            .offset(offset)
            .limit(limit)
            .fetchResults();
    }

    public QueryResults<BookName> getBooksWithProjectionFromJPASQL(int offset, int limit) {
        SQLTemplates oracleTemplate = OracleTemplates.builder().printSchema().build();
        JPAHelper jpaHelper = new JPAHelper(emf.createEntityManager(), oracleTemplate);

        Map<String, Expression<?>> bindings = new HashMap<>();
        bindings.put("name", book.name);

        return jpaHelper.query()
                        .select(Projections.bean(BookName.class, bindings))
                        .from(book)
                        .offset(offset)
                        .limit(limit)
                        .fetchResults();
    }

    public CloseableIterator<Book> getBooksByIterateFromJPASQL() {
        SQLTemplates oracleTemplate = OracleTemplates.builder().printSchema().build();
        JPAHelper jpaHelper = new JPAHelper(emf.createEntityManager(), oracleTemplate);

        return jpaHelper.query().select(book).from(book).iterate();
    }

    public CloseableIterator<Book> getBooksByIterate() {
        return queryFactory.selectFrom(book).iterate();
    }

}