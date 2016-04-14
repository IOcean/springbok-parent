package fr.iocean.framework.core.util;

import fr.iocean.framework.core.exception.PageRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {

    public static Pageable newPageable(String page, String size, String direction, String... properties) throws PageRequestException {
        if (StringUtils.isNotBlank(direction)) {
            return new PageRequest(
                    buildPageNumber(page),
                    buildPageSize(size),
                    buildDirection(direction), properties);
        } else {
            return new PageRequest(buildPageNumber(page),
                    buildPageSize(size));
        }
    }

    private static int buildPageNumber(final String pageNumberParameter) throws PageRequestException {
        try {
            int pageNumberValue = Integer.parseInt(pageNumberParameter);

            if (pageNumberValue < 0) {
                throw new PageRequestException("Page number must equal to or greater than 0");
            }
            
            return pageNumberValue;
        } catch (NumberFormatException e) {
            throw new PageRequestException("Page number must be an integer");
        }

    }

    private static int buildPageSize(final String pageSizeParameter) throws PageRequestException {

        try {
            int pageSizeValue = Integer.parseInt(pageSizeParameter);

            if (pageSizeValue < 0) {
                throw new PageRequestException("Page size must be equal to or greater than 0");
            }

            return pageSizeValue;
        } catch (NumberFormatException e) {
            throw new PageRequestException("Page size must be an integer");
        }

    }

    private static Sort.Direction buildDirection(final String directionParameter) throws PageRequestException {
        if ("asc".equalsIgnoreCase(directionParameter)) {
            return Sort.Direction.ASC;
        } else if ("desc".equalsIgnoreCase(directionParameter)) {
            return Sort.Direction.DESC;
        } else {
            throw new PageRequestException("Page direction must be 'asc' or 'desc'");
        }
    }
    
}
