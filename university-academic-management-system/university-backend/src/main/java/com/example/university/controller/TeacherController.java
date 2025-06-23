package com.example.university.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.entity.Teacher;
import com.example.university.entity.Course;
import com.example.university.service.CourseService;
import com.example.university.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;

    @GetMapping
    public IPage<Teacher> getTeacherPage(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String name) {
        Page<Teacher> page = new Page<>(current, size);
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        return teacherService.page(page, wrapper);
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Integer id) {
        return teacherService.getById(id);
    }

    @PostMapping
    public boolean createTeacher(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    @PutMapping("/{id}")
    public boolean updateTeacher(@PathVariable Integer id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        return teacherService.updateById(teacher);
    }

    @DeleteMapping("/{id}")
    public boolean deleteTeacher(@PathVariable Integer id) {
        return teacherService.removeById(id);
    }

    @GetMapping("/{id}/courses")
    public List<Course> getTeacherCourses(@PathVariable Integer id) {
        return courseService.findCoursesByTeacherId(id);
    }
} 