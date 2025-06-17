package com.car.onlinecarselectionsystem.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.car.onlinecarselectionsystem.entity.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CarMapper extends BaseMapper<Car> {

    @Select("SELECT c.car_id, c.production_year, c.price, c.color, c.mileage, c.inventory_status, c.model_id, c.image_url, cm.model_name AS modelName, b.brand_name AS brandName " +
            "FROM car c " +
            "JOIN car_model cm ON c.model_id = cm.model_id " +
            "JOIN brand b ON cm.brand_id = b.brand_id ${ew.customSqlSegment}")
    IPage<Car> selectCarDetailsPage(IPage<Car> page, @Param("ew") Wrapper<Car> queryWrapper);

    @Select("SELECT c.car_id, c.production_year, c.price, c.color, c.mileage, c.inventory_status, c.model_id, c.image_url, cm.model_name AS modelName, b.brand_name AS brandName " +
            "FROM car c " +
            "JOIN car_model cm ON c.model_id = cm.model_id " +
            "JOIN brand b ON cm.brand_id = b.brand_id " +
            "WHERE c.car_id = #{carId}")
    Car selectCarDetailsById(@Param("carId") Integer carId);
} 