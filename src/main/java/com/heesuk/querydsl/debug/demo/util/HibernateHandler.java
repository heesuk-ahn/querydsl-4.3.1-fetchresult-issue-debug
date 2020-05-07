package com.heesuk.querydsl.debug.demo.util;

import java.util.Iterator;

import javax.persistence.Query;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.ResultTransformer;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.FactoryExpressionTransformer;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.jpa.ScrollableResultsIterator;
import com.querydsl.jpa.TransformingIterator;

public class HibernateHandler implements QueryHandler {

    @Override
    public void addScalar(Query query, String alias, Class<?> type) {
        query.unwrap(NativeQuery.class).addScalar(alias);
    }

    @Override
    public void addEntity(Query query, String alias, Class<?> type) {
        query.unwrap(NativeQuery.class).addEntity(alias, type);
    }
    @Override
    public <T> CloseableIterator<T> iterate(Query query, FactoryExpression<?> projection) {
        if (query instanceof org.hibernate.query.Query) {
            org.hibernate.query.Query hQuery = (org.hibernate.query.Query) query;
            ScrollableResults results = hQuery.scroll(ScrollMode.FORWARD_ONLY);
            CloseableIterator<T> iterator = new ScrollableResultsIterator<T>(results);
            if (projection != null) {
                iterator = new TransformingIterator<T>(iterator, projection);
            }
            return iterator;
        } else {
            Iterator<T> iterator = query.getResultList().iterator();
            if (projection != null) {
                return new TransformingIterator<T>(iterator, projection);
            } else {
                return new IteratorAdapter<T>(iterator);
            }
        }
    }

    @Override
    public boolean transform(Query query, FactoryExpression<?> projection) {
        if (query instanceof org.hibernate.query.Query) {
            ResultTransformer transformer = new FactoryExpressionTransformer(projection);
            ((org.hibernate.query.Query) query).setResultTransformer(transformer);
            query.unwrap(org.hibernate.query.Query.class).setResultTransformer(transformer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean wrapEntityProjections() {
        return true;
    }

    @Override
    public boolean createNativeQueryTyped() {
        return false;
    }

}
