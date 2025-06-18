package com.car.onlinecarselectionsystem.controller;

import com.car.onlinecarselectionsystem.entity.CarModel;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-models")
public class CarModelController {

    @Autowired
    private CarModelService carModelService;

    @PostMapping
    public ResponseEntity<ApiResponse<CarModel>> createCarModel(@RequestBody CarModel carModel) {
        carModelService.save(carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("车型创建成功", carModel));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarModel>>> getAllCarModels() {
        List<CarModel> carModels = carModelService.list();
        return ResponseEntity.ok(ApiResponse.success(carModels));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarModel>> getCarModelById(@PathVariable Integer id) {
        CarModel carModel = carModelService.getById(id);
        if (carModel != null) {
            return ResponseEntity.ok(ApiResponse.success(carModel));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("未找到该车型"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarModel>> updateCarModel(@PathVariable Integer id, @RequestBody CarModel carModel) {
        carModel.setModelId(id);
        if (carModelService.updateById(carModel)) {
            return ResponseEntity.ok(ApiResponse.success("更新成功", carModel));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("更新失败"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCarModel(@PathVariable Integer id) {
        if (carModelService.removeById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("删除成功"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("删除失败"));
        }
    }
} 