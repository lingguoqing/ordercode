package com.ling.generalsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ling.generalsystem.entity.AiDialogue;
import com.ling.generalsystem.mapper.AiDialogueMapper;
import com.ling.generalsystem.service.AiDialogueService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Collections;
import java.util.List;

/**
 * @author guoqing.ling
 * @description 针对表【ai_dialogue】的数据库操作Service实现
 * @createDate 2025-07-02 09:59:15
 */
@Service
public class AiDialogueServiceImpl extends ServiceImpl<AiDialogueMapper, AiDialogue> implements AiDialogueService {

    @Override
    public List<AiDialogue> getByConversationId(String conversationId, int lastN) {
        LambdaQueryWrapper<AiDialogue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiDialogue::getConversationId, conversationId)
                .orderByDesc(AiDialogue::getCreateTime)
                .last("limit " + lastN);
        List<AiDialogue> list = this.list(wrapper);
        Collections.reverse(list); // 转为正序
        return list;
    }
}




