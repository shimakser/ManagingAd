package by.shimakser.service.jooq;

import by.shimakser.filter.Request;
import by.shimakser.model.jooq.Car;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.shimakser.model.Tables.CAR;

@Service
public class CarService {

    private final DSLContext context;

    @Autowired
    public CarService(DSLContext context) {
        this.context = context;
    }

    @Transactional
    public List<Car> getAllCars(Request request) {
        return context
                .select(
                        CAR.CAR_ID.as("id"),
                        CAR.CAR_NAME.as("title"),
                        CAR.CAR_CREATION_DATE.as("date"),
                        CAR.CAR_COUNT.as("num")
                )
                .from(CAR)
                .orderBy(CAR.CAR_ID.asc())
                .limit(request.getSize())
                .offset(request.getPage())
                .fetchInto(Car.class);

    }
}
