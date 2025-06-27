package com.example.library.service;

import com.example.library.dao.SubscribeDao;
import com.example.library.model.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscribeService {

    @Autowired
    private SubscribeDao subscribeDao;

    public boolean subscribeBook(Long userId, Long bookId) {
        // 检查是否已订阅
        if (subscribeDao.findByUserIdAndBookId(userId, bookId) != null) {
            return false; // 已订阅
        }
        Subscribe subscribe = new Subscribe();
        subscribe.setUserId(userId);
        subscribe.setBookId(bookId);
        subscribe.setSubscribeTime(LocalDateTime.now());
        subscribeDao.save(subscribe);
        return true;
    }

    public void unsubscribeBook(Long userId, Long bookId) {
        subscribeDao.delete(userId, bookId);
    }

    public List<Subscribe> findSubscriptionsByUserId(Long userId) {
        return subscribeDao.findByUserId(userId);
    }
} 