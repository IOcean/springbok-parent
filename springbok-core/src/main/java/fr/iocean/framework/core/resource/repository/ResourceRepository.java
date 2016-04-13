package fr.iocean.framework.core.resource.repository;

import fr.iocean.framework.core.resource.model.PersistentResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Provides CRUD operations and pagination for a resource.
 * @param <E> resource type
 * @param <ID> resource id type  
 */
@NoRepositoryBean
public interface ResourceRepository<E extends PersistentResource, ID extends Serializable> extends JpaRepository<E, ID> {
}
