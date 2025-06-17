package com.car.onlinecarselectionsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.car.onlinecarselectionsystem.entity.CarModel;
import com.car.onlinecarselectionsystem.mapper.CarModelMapper;
import com.car.onlinecarselectionsystem.service.CarModelService;
import org.springframework.stereotype.Service;

@Service
public class CarModelServiceImpl extends ServiceImpl<CarModelMapper, CarModel> implements CarModelService {
    // ServiceImpl已经提供了基本的CRUD方法，如果需要可以添加额外的业务逻辑
} 