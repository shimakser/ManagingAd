package by.shimakser.filter;

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

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
