package by.shimakser.service;

import by.shimakser.filter.Request;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FilterService {

    public Pageable buildPageable(Request request) {
        Integer pageNumber = request.getPage();
        Integer pageSize = request.getSize();
        String sortFieldName = request.getSortBy();

        return PageRequest.of(pageNumber, pageSize, Sort.by(sortFieldName));
    }

    public LocalDateTime convertToLocalDateTime(String date) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, pattern);
    }
}
