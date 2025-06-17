package com.car.onlinecarselectionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.car.onlinecarselectionsystem.entity.Favorite;

public interface FavoriteService extends IService<Favorite> {
    // 可以添加自定义的收藏服务方法，如果需要的话
    boolean isCarAlreadyFavorited(Integer userId, Integer carId);
} 