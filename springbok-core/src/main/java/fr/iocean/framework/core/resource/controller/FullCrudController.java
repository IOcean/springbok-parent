package fr.iocean.framework.core.resource.controller;

import java.io.Serializable;
import java.util.List;

/**
 * Exposes CRUD operations and pagination for a resource.
 * @author baptiste
 * @param <E> resource type
 * @param <ID> resource id type
 */
public interface FullCrudController<E extends Serializable, ID extends Serializable> extends CruController<E, ID> {
    
    /**
     * Exposes a delete by id entry point.
     * @param id resource id to delete
     */
    void delete(ID id);
    
    
    /**
     * Exposes a delete entry point for a list of ids.
     * @param ids resources ids to delete
     */
    void delete(List<ID> ids);
    
    /**
     * Exposes a delete entry point.
     */
    void deleteAll();
}
