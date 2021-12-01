package by.shimakser.controller.jooq;

import by.shimakser.filter.Request;
import by.shimakser.model.jooq.Car;
import by.shimakser.service.jooq.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(value = "/jooq")
    public List<Car> getCars(@RequestBody Request request) {
        return carService.getAllCars(request);
    }
}
