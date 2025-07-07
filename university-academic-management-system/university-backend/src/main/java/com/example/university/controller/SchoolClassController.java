package com.example.university.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.entity.SchoolClass;
import com.example.university.service.SchoolClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
public class SchoolClassController {

    @Autowired
    private SchoolClassService schoolClassService;

    @GetMapping
    public IPage<SchoolClass> getClassPage(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String name) {
        Page<SchoolClass> page = new Page<>(current, size);
        QueryWrapper<SchoolClass> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        return schoolClassService.page(page, wrapper);
    }

    @GetMapping("/all")
    public List<SchoolClass> getAllClasses() {
        return schoolClassService.list();
    }

    @GetMapping("/{id}")
    public SchoolClass getClassById(@PathVariable Integer id) {
        return schoolClassService.getById(id);
    }

    @PostMapping
    public boolean createClass(@RequestBody SchoolClass schoolClass) {
        return schoolClassService.save(schoolClass);
    }

    @PutMapping("/{id}")
    public boolean updateClass(@PathVariable Integer id, @RequestBody SchoolClass schoolClass) {
        schoolClass.setId(id);
        return schoolClassService.updateById(schoolClass);
    }

    @DeleteMapping("/{id}")
    public boolean deleteClass(@PathVariable Integer id) {
        return schoolClassService.removeById(id);
    }
} 