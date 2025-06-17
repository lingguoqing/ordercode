package com.car.onlinecarselectionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("car")
public class Car {
    @TableId(value = "car_id", type = IdType.AUTO)
    private Integer carId;
    private Integer productionYear;
    private Double price;
    private String color;
    private Integer mileage;
    private String inventoryStatus;
    private Integer modelId;
    private String imageUrl;

    // 新增字段用于显示品牌名称和车型名称
    private String brandName;
    private String modelName;
} 