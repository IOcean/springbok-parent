package fr.iocean.framework.core.resource.service;

import fr.iocean.framework.core.resource.exception.ResourceNotFoundException;
import fr.iocean.framework.core.resource.model.PersistentResource;
import fr.iocean.framework.core.resource.repository.ResourceRepository;
import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Provides CRUD operations and pagination for a resource.
 * @param <E> resource type
 * @param <ID> resource id type
 * @param <R> resource repository 
 */
@Service
@Transactional
public abstract class ResourceService<E extends PersistentResource, ID extends Serializable, R extends ResourceRepository<E, ID>> implements FullCrudService<E, ID>  {
    
    private static final String MESSAGE_RESOURCE_NOT_FOUND = "Resource was not found";
    
    @Autowired
    protected R repository;

    /**
     * Creates a resource.
     * @param resource resource to create
     * @return created resource
     */
    @Override
    public E create(E resource) {
        return repository.save(resource);
    }
    
    /**
     * Creates multiple resources.
     * @param iterable resources to create
     * @return created resources
     */
    @Override
    public Iterable<E> create(Iterable<E> iterable) {
        return repository.save(iterable);
    }
    
    /**
     * Returns true if the resource exists, false otherwise
     * @param id resource id
     * @return true of the resource exists, false otherwise
     */
    @Override
    public boolean exists(ID id) { 
        if(id == null) {
            return false;
        }
        
        return repository.exists(id);
    }

    /**
     * Returns the total number of resources.
     * @return total number of resources
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Finds a resource by its id.
     * @param id resource id
     * @return resource
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Override
    public E findOne(ID id) {
        E resource = null;
        
        if (id != null) {
            resource = repository.findOne(id);
        }
        
        if (resource == null) {
            throw new ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND);
        }
        
        return resource;
    }

    /**
     * Finds every resources.
     * @return every resources
     */
    @Override
    public Iterable<E> findAll() {
        return repository.findAll();
    }

    /**
     * Finds every resources, and applies a sort.
     * @param sort sort to apply
     * @return every resources sorted according to the sort
     */
    @Override
    public Iterable<E> findAll(Sort sort) {
        return repository.findAll(sort);
    }
    
    /**
     * Finds a page of resources.
     * @param pageable resource page request
     * @return resource page
     */
    @Override
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Finds multiple resources by their ids.
     * @param iterable resources ids
     * @return resources
     */
    @Override
    public Iterable<E> findAll(Iterable<ID> iterable) {
        return repository.findAll(iterable);
    }
    
    /**
     * Updates a resource.
     * @param id resource id
     * @param resource resource to update
     * @return updated resource
     * @throws ResourceNotFoundException if the resource id is not found or if the resource id doesnt match its actual id
     */
    @Override
    public E update(ID id, E resource) {
        if (id == null || !repository.exists(id) || resource == null || !resource.getId().equals(id)) {
            throw new ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND);
        }
        
        return repository.save(resource);
    }

    /**
     * Updates multiple resources.
     * @param iterable resources to update
     * @return updated resources
     */
    @Override
    public Iterable<E> update(Iterable<E> iterable) {
        return repository.save(iterable);
    }
    
    /**
     * Delete a resource by its id.
     * @param id id of the resource to delete
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Override
    public void delete(ID id) {
        if (!repository.exists(id)) {
            throw new ResourceNotFoundException(MESSAGE_RESOURCE_NOT_FOUND);
        }
        
        repository.delete(id);
    }
    
    /**
     * Delete resources by their ids.
     * @param ids resources ids
     */
    @Override
    public void delete(List<ID> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            Iterable<E> resources = repository.findAll(ids);

            if (resources != null) {
                resources.forEach(repository::delete);
            }
        }
    }

    /**
     * Deletes a resource.
     * @param resource resource to delete
     */
    @Override
    public void delete(E resource) {
        repository.delete(resource);
    }
    
    /**
     * Deletes multiple resources.
     * @param iterable resources to delete
     */
    @Override
    public void delete(Iterable<? extends E> iterable) {
        repository.delete(iterable);
    }

    /**
     * Deletes all resources.
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
