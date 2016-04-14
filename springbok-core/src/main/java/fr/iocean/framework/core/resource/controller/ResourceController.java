package fr.iocean.framework.core.resource.controller;

import fr.iocean.framework.core.exception.PageRequestException;
import fr.iocean.framework.core.resource.model.PersistentResource;
import fr.iocean.framework.core.resource.service.FullCrudService;
import fr.iocean.framework.core.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.io.Serializable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/***
 * Exposes CRUD and pagination for a resource.
 * @param <E> resource type
 * @param <ID> resource id type
 * @param <S> resource service
 */
@RestController
public abstract class ResourceController<E extends PersistentResource, ID extends Serializable, S extends FullCrudService<E, ID>> 
    implements CruController<E, ID> {
    
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String DEFAULT_PAGE_DIRECTION = "asc";
    public static final String DEFAULT_PAGE_PROPERTIES = "id";
    
    @Autowired
    protected S service;

    /**
     * Exposes a create entry point, returns the created resource and a 201 if the resource is valid and a 400 otherwise.
     * @param resource resource to create
     * @return created resource
     */
    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Override
    public E create(@RequestBody @Valid E resource) {
        return service.create(resource);
    }
    
    /**
     * Exposes a search by id entry point, returns the resource and a 200 if a resource exist for this id
     * and a 404 otherwise.
     * @param id resource id
     * @return resource
     */
    @RequestMapping(value="{id}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Override
    public E getOne(@PathVariable ID id) {
        return service.findOne(id);
    }
    
    /**
     * Exposes a pagination entry point, returns the resource page and a 200, or a 400 if one of the parameters is not valid.
     * @param pageNumber page number 0-based
     * @param pageSize page size
     * @param direction sort direction
     * @param properties resource properties to sort on
     * @return resource page
     * @throws PageRequestException if pageNumber &lt; 0, pageSize &lt; 0, direction doesnt equal "asc" or "desc  
     */
    @RequestMapping(method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Override
    public Page<E> getAll(
        @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) String pageNumber, 
        @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) String pageSize, 
        @RequestParam(value = "direction", defaultValue = DEFAULT_PAGE_DIRECTION) String direction, 
        @RequestParam(value = "properties", defaultValue = DEFAULT_PAGE_PROPERTIES) String... properties) throws PageRequestException {
        
        Pageable pageRequest = PageUtils.newPageable(pageNumber, pageSize, direction, properties);
        
        return service.findAll(pageRequest);
    }
    
    /**
     * Exposes an update by id entry point, returns :
     * - the updated resource and a 200 if the resource is valid and found
     * - a 400 if the resource is not valid
     * - a 404 if the resource is not found.
     * @param resource resource to update
     * @return updated resource 
     */
    @RequestMapping(value="{id}", method = PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Override
    public E update(@PathVariable ID id, @RequestBody @Valid E resource) {
        return service.update(id, resource);
    }
}
