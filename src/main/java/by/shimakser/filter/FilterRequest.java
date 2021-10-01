package by.shimakser.filter;

import lombok.Data;

@Data
public class FilterRequest {

    private Filter filter;
    private Integer page;
    private Integer size;
    private String sortBy;

    public FilterRequest() {}

    public FilterRequest(Filter filter, Integer page, Integer size, String sortBy) {
        this.filter = filter;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
    }
}
