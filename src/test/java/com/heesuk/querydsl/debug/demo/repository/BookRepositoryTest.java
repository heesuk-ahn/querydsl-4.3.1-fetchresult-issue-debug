package com.heesuk.querydsl.debug.demo.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.heesuk.querydsl.debug.demo.entity.Book;
import com.heesuk.querydsl.debug.demo.vo.BookName;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryResults;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManagerFactory emf;

    private final String bookName = "FIRST_BOOK";
    private final Book firstBook = Book.builder().name(bookName).build();

    private static <T> void executeQueryForInsert(EntityManager entityManager, T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void should_Get_Books_By_Using_Fetch_Results() {
        final int offset = 0;
        final int limit = 10;

        executeQueryForInsert(emf.createEntityManager(), firstBook);

        QueryResults<Book> books = bookRepository.getBooks(offset, limit);

        assertThat(books.getTotal()).isEqualTo(1L);
        assertThat(books.getLimit()).isEqualTo(10L);
        assertThat(books.getOffset()).isEqualTo(0L);
    }

    @Test
    public void should_Get_Books_From_JPASQL_By_Using_Fetch_Results() {
        final int offset = 0;
        final int limit = 10;

        executeQueryForInsert(emf.createEntityManager(), firstBook);

        QueryResults<Book> books = bookRepository.getBooksFromJPASQL(offset, limit);

        assertThat(books.getLimit()).isEqualTo(10L);
        assertThat(books.getOffset()).isEqualTo(0L);
    }

    @Test
    public void should_Get_Books_With_Projection() {
        final int offset = 0;
        final int limit = 10;

        executeQueryForInsert(emf.createEntityManager(), firstBook);

        QueryResults<BookName> bookNames = bookRepository.getBooksWithProjection(offset, limit);

        assertThat(bookNames.getResults().get(0).getName()).isEqualTo(bookName);
    }

    @Test
    public void should_Get_Books_With_Projection_From_JPASQL() {
        final int offset = 0;
        final int limit = 10;

        executeQueryForInsert(emf.createEntityManager(), firstBook);

        QueryResults<BookName> bookNames = bookRepository.getBooksWithProjectionFromJPASQL(offset, limit);

        assertThat(bookNames.getResults().get(0).getName()).isEqualTo(bookName);
    }

    @Test
    public void should_Get_Books_By_Using_Iterate_From_JPASQL() {
        CloseableIterator<Book> books = bookRepository.getBooksByIterateFromJPASQL();

        executeQueryForInsert(emf.createEntityManager(), firstBook);

        assertThat(books.hasNext()).isTrue();
        assertThat(books.next().getName()).isEqualTo(firstBook.getName());
    }

    @Test
    public void should_Get_Books_By_Using_Iterate() {
        CloseableIterator<Book> books = bookRepository.getBooksByIterate();

        executeQueryForInsert(emf.createEntityManager(), firstBook);

        assertThat(books.hasNext()).isTrue();
        assertThat(books.next().getName()).isEqualTo(firstBook.getName());
    }

}