package by.shimakser.service.jooq;

import by.shimakser.filter.Request;
import by.shimakser.model.jooq.Car;
import by.shimakser.model.tables.records.CarRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<CarRecord> carRecords = context
                .select(CAR.fields())
                .from(CAR)
                .orderBy(CAR.ID.asc())
                .limit(request.getSize())
                .offset(request.getPage())
                .fetchInto(CarRecord.class);

        return carConverter(carRecords);
    }

    private List<Car> carConverter(List<CarRecord> carRecords) {
        List<Car> cars = new ArrayList<>();

        for (CarRecord carRecord: carRecords) {
            Car car = new Car();
            car.setId(carRecord.field1().getValue(carRecord));
            car.setTitle(carRecord.field2().getValue(carRecord));
            car.setDate(carRecord.field3().getValue(carRecord));
            car.setNum(Integer.parseInt(carRecord.field4().getValue(carRecord)));
            cars.add(car);
        }
        return cars;
    }
}
