package com.car.onlinecarselectionsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("favorite")
public class Favorite {
    @TableId(value = "favorite_id", type = IdType.AUTO)
    private Integer favoriteId;
    private Integer userId;
    private Integer carId;
    private LocalDateTime favoriteTime;
} 