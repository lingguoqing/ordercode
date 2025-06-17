package com.car.onlinecarselectionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.car.onlinecarselectionsystem.entity.Favorite;
import com.car.onlinecarselectionsystem.mapper.FavoriteMapper;
import com.car.onlinecarselectionsystem.service.FavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    // ServiceImpl已经提供了基本的CRUD方法，如果需要可以添加额外的业务逻辑

    @Override
    public boolean isCarAlreadyFavorited(Integer userId, Integer carId) {
        QueryWrapper<Favorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("car_id", carId);
        return count(queryWrapper) > 0;
    }
} 