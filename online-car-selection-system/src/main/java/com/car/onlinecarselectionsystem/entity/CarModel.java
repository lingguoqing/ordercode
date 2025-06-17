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
@TableName("car_model")
public class CarModel {
    @TableId(value = "model_id", type = IdType.AUTO)
    private Integer modelId;
    private String modelName;
    private String bodyType;
    private String powerType;
    private Integer seatsNumber;
    private Integer brandId;
} 