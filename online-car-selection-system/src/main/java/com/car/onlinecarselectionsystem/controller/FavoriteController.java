package com.car.onlinecarselectionsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.car.onlinecarselectionsystem.entity.Favorite;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<Favorite>> createFavorite(@RequestBody Favorite favorite) {
        // 检查车辆是否已经被当前用户收藏
        if (favoriteService.isCarAlreadyFavorited(favorite.getUserId(), favorite.getCarId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("该车辆已被当前用户收藏"));
        }
        // 设置收藏时间为当前时间
        favorite.setFavoriteTime(LocalDateTime.now());

        favoriteService.save(favorite);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("收藏成功", favorite));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Favorite>>> getAllFavorites() {
        List<Favorite> favorites = favoriteService.list();
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Favorite>> getFavoriteById(@PathVariable Integer id) {
        Favorite favorite = favoriteService.getById(id);
        if (favorite != null) {
            return ResponseEntity.ok(ApiResponse.success(favorite));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("未找到该收藏"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFavorite(@PathVariable Integer id) {
        if (favoriteService.removeById(id)) {
            return ResponseEntity.ok(ApiResponse.success("取消收藏成功"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("未找到可删除的收藏"));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Favorite>>> getFavoritesByUserId(@PathVariable Integer userId) {
        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Favorite> favorites = favoriteService.list(queryWrapper);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    @GetMapping("/user/{userId}/car/{carId}")
    public ResponseEntity<ApiResponse<Boolean>> isCarFavorited(@PathVariable Integer userId, @PathVariable Integer carId) {
        boolean isFavorited = favoriteService.isCarAlreadyFavorited(userId, carId);
        return ResponseEntity.ok(ApiResponse.success(isFavorited));
    }

    @DeleteMapping("/user/{userId}/car/{carId}")
    public ResponseEntity<ApiResponse<String>> deleteByUserIdAndCarId(@PathVariable Integer userId, @PathVariable Integer carId) {
        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("car_id", carId);
        boolean removed = favoriteService.remove(queryWrapper);
        if (removed) {
            return ResponseEntity.ok(ApiResponse.success("取消收藏成功"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("未找到收藏记录"));
        }
    }
} 