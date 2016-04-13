package fr.iocean.framework.core.resource.repository;

import fr.iocean.framework.core.resource.model.PersistentResource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * Provides entity manager and hibernate session access for custom repositories implementation
 * Provides paginated search support methods
 * @param <T>
 */
public abstract class AbstractResourceRepository<T extends PersistentResource> {

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

    @PostConstruct
    public void init() {
        entityClass = getEntityClass();
    }

    protected abstract Class<T> getEntityClass();

    protected Session getSession() {
        return em.unwrap(Session.class);
    }

    public Criteria createSearchCriteria(Pageable pageable) {
        return getSession().createCriteria(entityClass)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
    }

    @SuppressWarnings("unchecked")
    public PageImpl<T> createSearchResult(Pageable pageable, Criteria query, long count) {
        if (pageable.getSort() == null) {
            return new PageImpl<>(query.list(), pageable, count);
        } else {
            addOrder(query, pageable);
            return new PageImpl<>(query.list(), pageable, count);
        }
    }

    public void addOrder(Criteria query, Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (order.isAscending()) {
                query.addOrder(Order.asc(order.getProperty()));
            } else {
                query.addOrder(Order.desc(order.getProperty()));
            }
        }
    }

}
