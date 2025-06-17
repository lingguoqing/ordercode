package com.car.onlinecarselectionsystem.controller;

import com.car.onlinecarselectionsystem.entity.Car;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.car.onlinecarselectionsystem.entity.CarModel;
import com.car.onlinecarselectionsystem.service.CarModelService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarModelService carModelService;

    @PostMapping
    public ResponseEntity<ApiResponse<Car>> createCar(@RequestBody Car car) {
        carService.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Car created successfully!", car));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<IPage<Car>>> getAllCars(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        IPage<Car> carPage = carService.getCarDetailsPage(new Page<>(page, size), new QueryWrapper<>());
        return ResponseEntity.ok(ApiResponse.success(carPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Car>> getCarById(@PathVariable Integer id) {
        Car car = carService.getCarDetailsById(id);
        if (car != null) {
            return ResponseEntity.ok(ApiResponse.success(car));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Car not found!"));
        }
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<IPage<Car>>> getCarsByBrandId(
            @PathVariable Integer brandId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        QueryWrapper<CarModel> modelQueryWrapper = new QueryWrapper<>();
        modelQueryWrapper.eq("brand_id", brandId);
        List<CarModel> carModels = carModelService.list(modelQueryWrapper);

        if (carModels.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(new Page<>())); // 返回空分页数据
        }

        List<Integer> modelIds = carModels.stream()
                .map(CarModel::getModelId)
                .collect(Collectors.toList());

        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.in("c.model_id", modelIds);
        IPage<Car> carPage = carService.getCarDetailsPage(new Page<>(page, size), carQueryWrapper);

        return ResponseEntity.ok(ApiResponse.success(carPage));
    }

    @GetMapping("/model/{modelId}")
    public ResponseEntity<ApiResponse<IPage<Car>>> getCarsByModelId(
            @PathVariable Integer modelId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        QueryWrapper<Car> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("c.model_id", modelId);
        IPage<Car> carPage = carService.getCarDetailsPage(new Page<>(page, size), queryWrapper);

        return ResponseEntity.ok(ApiResponse.success(carPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Car>> updateCar(@PathVariable Integer id, @RequestBody Car car) {
        car.setCarId(id);
        if (carService.updateById(car)) {
            return ResponseEntity.ok(ApiResponse.success("Car updated successfully!", car));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Car not found for update!"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCar(@PathVariable Integer id) {
        if (carService.removeById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("Car deleted successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Car not found for deletion!"));
        }
    }
} 