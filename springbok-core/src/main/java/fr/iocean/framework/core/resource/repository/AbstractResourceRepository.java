package fr.iocean.framework.core.resource.repository;

import fr.iocean.framework.core.resource.model.PersistentResource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides entity manager and hibernate session access for custom repositories implementation
 * Provides paginated search support methods
 *
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

    public ProjectionList createProjectionList(Pageable pageable) {
        ProjectionList projectionList = Projections.projectionList()
                .add(Projections.property("id"));
        if (pageable != null && pageable.getSort() != null) {
            pageable.getSort().forEach(sort -> {
                if (!"id".equals(sort.getProperty())) {
                    projectionList.add(Projections.property(sort.getProperty()));
                }
            });
        }
        return projectionList;
    }

    public List<T> createContentList(PageImpl<Object> ids, Criteria query, Pageable pageable) {
        if (ids.hasContent()) {
            query.add(Restrictions.in("id", extractIds(ids)));

            if (pageable.getSort() != null) {
                addOrder(query, pageable);
            }

            return query.list();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Object> extractIds(Page<Object> page) {
        return page.getContent().stream().map(object -> {
            if (object instanceof Object[]) {
                return ((Object[]) object)[0];
            }
            return object;
        }).collect(Collectors.toList());
    }

}
