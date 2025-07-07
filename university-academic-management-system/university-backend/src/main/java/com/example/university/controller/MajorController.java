package com.example.university.controller;

import com.example.university.entity.Major;
import com.example.university.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    public List<Major> getAllMajors() {
        return majorService.list();
    }

    @PostMapping
    public boolean createMajor(@RequestBody Major major) {
        return majorService.save(major);
    }

    @PutMapping("/{id}")
    public boolean updateMajor(@PathVariable Integer id, @RequestBody Major major) {
        major.setId(id);
        return majorService.updateById(major);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMajor(@PathVariable Integer id) {
        return majorService.removeById(id);
    }
} 