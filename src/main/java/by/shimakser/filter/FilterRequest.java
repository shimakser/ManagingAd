package by.shimakser.filter;

public class FilterRequest {

    private Filter filter;
    private int numOfPage;
    private int pageSize;
    private String sortBy;

    public FilterRequest() {}

    public FilterRequest(Filter filter, int numOfPage, int pageSize, String sortBy) {
        this.filter = filter;
        this.numOfPage = numOfPage;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public int getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(int numOfPage) {
        this.numOfPage = numOfPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
