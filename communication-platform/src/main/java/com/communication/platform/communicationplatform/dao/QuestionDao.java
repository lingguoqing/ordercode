package com.communication.platform.communicationplatform.dao;

import com.communication.platform.communicationplatform.entity.Question;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface QuestionDao {
    // 插入新问题
    int insert(Question question);
    
    // 查询所有问题（带分页）
    List<Question> findAll();
    
    // 根据ID查询问题
    Question findById(Long id);
    
    // 更新浏览次数
    int updateViewCount(@Param("id") Long id, @Param("viewCount") Integer viewCount);
    
    // 根据用户ID查询问题
    List<Question> findByUserId(Long userId);
} 