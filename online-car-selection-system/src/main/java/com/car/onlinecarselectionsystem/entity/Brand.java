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
@TableName("brand")
public class Brand {
    @TableId(value = "brand_id", type = IdType.AUTO)
    private Integer brandId;
    private String brandName;
} 