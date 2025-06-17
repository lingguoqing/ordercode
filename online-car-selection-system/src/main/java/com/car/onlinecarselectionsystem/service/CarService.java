package com.car.onlinecarselectionsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.car.onlinecarselectionsystem.entity.Car;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

public interface CarService extends IService<Car> {
    // 可以添加自定义的车辆服务方法，如果需要的话

    /**
     * 分页查询车辆详细信息，包含品牌和车型名称
     * @param page 分页对象
     * @param queryWrapper 查询条件包装器
     * @return 包含车辆详细信息的MyBatis Plus分页对象
     */
    IPage<Car> getCarDetailsPage(IPage<Car> page, Wrapper<Car> queryWrapper);

    /**
     * 根据车辆ID获取车辆详细信息，包含品牌和车型名称
     * @param carId 车辆ID
     * @return 车辆详细信息，如果未找到则返回null
     */
    Car getCarDetailsById(Integer carId);
} 