package com.example.university.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.university.entity.Course;
import com.example.university.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public IPage<Course> getCoursePage(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name) {
        Page<Course> page = new Page<>(current, size);
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        return courseService.page(page, wrapper);
    }

    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.list();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Integer id) {
        return courseService.getById(id);
    }

    @PostMapping
    public boolean createCourse(@RequestBody Course course) {
        return courseService.save(course);
    }

    @PutMapping("/{id}")
    public boolean updateCourse(@PathVariable Integer id, @RequestBody Course course) {
        course.setId(id);
        return courseService.updateById(course);
    }

    @DeleteMapping("/{id}")
    public boolean deleteCourse(@PathVariable Integer id) {
        return courseService.removeById(id);
    }

    @GetMapping("/available/{studentId}")
    public List<Course> getAvailableCourses(@PathVariable Integer studentId) {
        return courseService.findAvailableCoursesForStudent(studentId);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public List<Course> getCoursesByTeacherId(@PathVariable Integer teacherId) {
        return courseService.findCoursesByTeacherId(teacherId);
    }
} 