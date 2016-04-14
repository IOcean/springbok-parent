package fr.iocean.framework.security.controller;

import fr.iocean.framework.core.resource.controller.ResourceController;
import fr.iocean.framework.core.exception.PageRequestException;
import fr.iocean.framework.core.util.PageUtils;
import fr.iocean.framework.security.model.credential.Credential;
import fr.iocean.framework.security.service.CredentialService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static fr.iocean.framework.security.SpringbokSecurityApplication.API_ROOT_PATH;

@RestController
@RequestMapping(API_ROOT_PATH + "/credentials")
public class CredentialController extends ResourceController<Credential, Long, CredentialService> {

    @RequestMapping("search")
    @ResponseBody
    public Iterable<Credential> search(
            @RequestParam(value = "accountId", required = false) Long accountId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "profileId", required = false) Long profileId,
            @RequestParam(value = "pageNumber", required = false, defaultValue = DEFAULT_PAGE_NUMBER) String pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) String pageSize,
            @RequestParam(value = "direction", required = false, defaultValue = DEFAULT_PAGE_DIRECTION) String direction,
            @RequestParam(value = "properties", required = false, defaultValue = DEFAULT_PAGE_PROPERTIES) String... properties
    ) throws PageRequestException {
        Pageable pageRequest = PageUtils.newPageable(pageNumber, pageSize, direction, properties);
        return service.search(pageRequest, accountId, username, profileId);
    }

    @Override
    @PreAuthorize("hasRole('admin')")
    public Credential update(@PathVariable Long id, @Valid @RequestBody Credential resource) {
        return super.update(id, resource);
    }

    @Override
    @PreAuthorize("hasRole('admin')")
    public Credential create(@Valid @RequestBody Credential resource) {
        return super.create(resource);
    }
    
}
