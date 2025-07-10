package com.ling.generalsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ling.generalsystem.entity.AiDialogue;

import java.util.List;

/**
* @author guoqing.ling
* @description 针对表【ai_dialogue】的数据库操作Service
* @createDate 2025-07-02 09:59:15
*/
public interface AiDialogueService extends IService<AiDialogue> {


    /**
     * 查询某个会话最近N条对话
     */
    List<AiDialogue> getByConversationId(String conversationId, int lastN);
}
