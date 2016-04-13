package fr.iocean.framework.security.repository.credential;

import fr.iocean.framework.core.resource.repository.AbstractResourceRepository;
import fr.iocean.framework.security.model.credential.Credential;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CredentialRepositoryImpl extends AbstractResourceRepository<Credential> implements CredentialRepositoryCustom {
    @Override
    protected Class<Credential> getEntityClass() {
        return Credential.class;
    }

    public PageImpl<Credential> search(Pageable pageable, Long accountId, String username, Long profileId) {
        Criteria query = createSearchCriteria(pageable);
        constructQuerySearch(query, accountId, username, profileId);
        
        query.setProjection(Projections.distinct(entityProjectionList()));
        query.setResultTransformer(new AliasToBeanResultTransformer(entityClass));

        Long count = count(accountId, username, profileId);
        return createSearchResult(pageable, query, count);
    }

    private Projection entityProjectionList() {
        String[] properties = getSession().getSessionFactory().getClassMetadata(entityClass).getPropertyNames();
        ProjectionList list = Projections.projectionList();
        for (String property : properties) {
            list.add(Projections.property(property), property);
        }
        list.add(Projections.id(), Projections.id().toString());
        return list;
    }

    private Long count(Long userId, String login, Long profileId) {
        Criteria query = getSession().createCriteria(entityClass).setProjection(Projections.countDistinct("id"));
        constructQuerySearch(query, userId, login, profileId);
        return (Long) query.uniqueResult();
    }

    private void constructQuerySearch(Criteria query, Long accountId, String username, Long profileId) {
        if (profileId != null || accountId != null || StringUtils.isNotBlank(username)) {
            query.createAlias("profileCredentials", "pc");
            query.createAlias("pc.profile", "p");
        }

        if (profileId != null) {
            query.add(Restrictions.eq("p.id", profileId));
        }

        if (accountId != null) {
            query.createAlias("p.profileAccounts", "pa");
            query.add(Restrictions.eq("pa.account.id", accountId));
        }

        if (StringUtils.isNotBlank(username)) {
            query.createAlias("p.profileAccounts", "pa");
            query.createAlias("pa.account", "a");
            query.add(Restrictions.eq("a.username", username));
        }
    }
}
