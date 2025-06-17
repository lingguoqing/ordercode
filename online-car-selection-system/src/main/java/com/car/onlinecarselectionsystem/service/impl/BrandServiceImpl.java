package com.car.onlinecarselectionsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.car.onlinecarselectionsystem.entity.Brand;
import com.car.onlinecarselectionsystem.mapper.BrandMapper;
import com.car.onlinecarselectionsystem.service.BrandService;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    // ServiceImpl已经提供了基本的CRUD方法，如果需要可以添加额外的业务逻辑
} 