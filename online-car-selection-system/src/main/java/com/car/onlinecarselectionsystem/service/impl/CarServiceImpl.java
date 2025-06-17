package com.car.onlinecarselectionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.car.onlinecarselectionsystem.entity.Car;
import com.car.onlinecarselectionsystem.mapper.CarMapper;
import com.car.onlinecarselectionsystem.service.CarService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
    // ServiceImpl已经提供了基本的CRUD方法，如果需要可以添加额外的业务逻辑

    @Override
    public IPage<Car> getCarDetailsPage(IPage<Car> page, Wrapper<Car> queryWrapper) {
        return baseMapper.selectCarDetailsPage(page, queryWrapper);
    }

    @Override
    public Car getCarDetailsById(Integer carId) {
        return baseMapper.selectCarDetailsById(carId);
    }
} 