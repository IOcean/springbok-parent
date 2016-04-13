package fr.iocean.framework.core.resource.controller;

import fr.iocean.framework.core.resource.model.PersistentResource;
import fr.iocean.framework.core.resource.service.FullCrudService;
import java.io.Serializable;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/***
 * Exposes CRUD and pagination for a resource.
 * @param <E> resource type
 * @param <ID> resource id type
 * @param <S> resource service
 */
@RestController
public abstract class DeletableResourceController<E extends PersistentResource, ID extends Serializable, S extends FullCrudService<E, ID>> 
    extends ResourceController<E, ID, S>
    implements FullCrudController<E, ID> {
    
    /**
     * Exposes a delete by id entry point, returns a 410 if the resource is found, a 404 otherwise.
     * @param id resource id to delete
     */
    @RequestMapping(value="{id}", method = DELETE)
    @ResponseStatus(HttpStatus.GONE)
    @Override
    public void delete(@PathVariable ID id) {
        service.delete(id);
    }
    
    /**
     * Exposes a delete entry point for a list of ids, returns a 410,
     * it's exposed as a POST and not a DELETE because if the list of ids is passed as a request parameters,
     * which is the only way of passing data for a DELETE request since it has no body,
     * the URL maximum size limits the number of ids we can pass.
     * @param ids resource ids to delete
     */
    @RequestMapping(value="deleteSeveral", method = POST)
    @ResponseStatus(HttpStatus.GONE)
    @Override
    public void delete(@RequestBody List<ID> ids) {
        service.delete(ids);
    }
    
    /**
     * Exposes a delete entry point, returns a 410.
     */
    @RequestMapping(method = DELETE)
    @ResponseStatus(HttpStatus.GONE)
    @Override
    public void deleteAll() {
        service.deleteAll();
    }
}
