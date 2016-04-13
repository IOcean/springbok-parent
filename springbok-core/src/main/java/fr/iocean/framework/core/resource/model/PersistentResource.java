package fr.iocean.framework.core.resource.model;

import java.io.Serializable;

/**
 * Resource persisted in a RDBMS.
 * @param <ID> id type
 */
public interface PersistentResource<ID extends Serializable> extends Serializable {

    /**
     * Gets the resource id.
     * @return resource id
     */
    ID getId();

    /**
     * Sets the resource id.
     * @param id new resource id
     */
    void setId(ID id);
}
