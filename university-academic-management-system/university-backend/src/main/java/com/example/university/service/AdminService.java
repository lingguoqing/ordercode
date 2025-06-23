package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.university.entity.Admin;
import com.example.university.mapper.AdminMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {
} 