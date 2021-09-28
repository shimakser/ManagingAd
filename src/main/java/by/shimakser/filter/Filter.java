package by.shimakser.filter;

import java.util.Set;

public class Filter {

    private Set<String> age;

    private String title;

    private String deletedDate;

    private String createdDateFrom;
    private String createdDateTo;

    public Set<String> getAge() {
        return age;
    }

    public void setAge(Set<String> age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(String createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public String getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(String createdDateTo) {
        this.createdDateTo = createdDateTo;
    }
}
