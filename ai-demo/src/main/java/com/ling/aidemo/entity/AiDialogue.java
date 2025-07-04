package com.ling.aidemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * @TableName ai_dialogue
 */
@TableName(value = "ai_dialogue")
@Data
public class AiDialogue implements Serializable {
    @Serial
    private static final long serialVersionUID = -3045260340938826089L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String conversationId;

    private String content;

    private String properties;

    private String messageType;

    private String toolCalls;

    private String textContent;

    private String metadata;

    private LocalDateTime createTime;

    private Long userId;

    @TableLogic
    private Integer isDelete;
}