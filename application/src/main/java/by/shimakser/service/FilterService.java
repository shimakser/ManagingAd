package by.shimakser.service;

import by.shimakser.filter.Request;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

    public Pageable buildPageable(Request request) {
        Integer pageNumber = request.getPage();
        Integer pageSize = request.getSize();
        String sortFieldName = request.getSortBy();

        return PageRequest.of(pageNumber, pageSize, Sort.by(sortFieldName));
    }
}
