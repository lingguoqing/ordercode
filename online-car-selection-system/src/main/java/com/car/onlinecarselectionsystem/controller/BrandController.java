package com.car.onlinecarselectionsystem.controller;

import com.car.onlinecarselectionsystem.entity.Brand;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Brand>> createBrand(@RequestBody Brand brand) {
        brandService.save(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("品牌创建成功", brand));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Brand>>> getAllBrands() {
        List<Brand> brands = brandService.list();
        return ResponseEntity.ok(ApiResponse.success(brands));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Brand>> getBrandById(@PathVariable Integer id) {
        Brand brand = brandService.getById(id);
        if (brand != null) {
            return ResponseEntity.ok(ApiResponse.success(brand));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("未找到该品牌"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Brand>> updateBrand(@PathVariable Integer id, @RequestBody Brand brand) {
        brand.setBrandId(id);
        if (brandService.updateById(brand)) {
            return ResponseEntity.ok(ApiResponse.success("品牌更新成功", brand));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("未找到可更新的品牌"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBrand(@PathVariable Integer id) {
        if (brandService.removeById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("删除成功"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("删除失败"));
        }
    }
} 