package by.shimakser.filter;

import lombok.Data;

@Data
public class Request {

    protected Integer page = 0;
    protected Integer size = 10;
    protected String sortBy = "id";
}
