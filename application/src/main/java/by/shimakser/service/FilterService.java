package by.shimakser.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

    public Pageable buildPageable(PageRequest request) {
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        Sort sortFieldName = request.getSort();

        return PageRequest.of(pageNumber, pageSize, sortFieldName);
    }
}
