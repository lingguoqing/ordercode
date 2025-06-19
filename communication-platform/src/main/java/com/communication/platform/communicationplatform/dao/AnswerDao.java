package com.communication.platform.communicationplatform.dao;

import com.communication.platform.communicationplatform.entity.Answer;
import java.util.List;

public interface AnswerDao {
    // 插入新回答
    int insert(Answer answer);
    
    // 根据问题ID查询所有回答
    List<Answer> findByQuestionId(Long questionId);
    
    // 根据用户ID查询所有回答
    List<Answer> findByUserId(Long userId);
    
    // 根据ID查询回答
    Answer findById(Long id);
} 