package com.cong.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cong.example.domain.Task;
import com.cong.example.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SqlSlowMirrorSampleApplicationTests {
    @Resource
    private TaskService taskService;

    @Test
    void testSqlSlowMirror() {
        assertNotNull(taskService.getById(1));
        System.out.println(taskService.getById(1));
    }

    @Test
    void testSqlSlowMirror2() {
        assertNotNull(taskService.getOne(new LambdaQueryWrapper<Task>().eq(Task::getId,1)));
//        System.out.println(taskService.getById(1));
    }

    @Test
    void testSqlSlowMirror3() {
        assertNotNull(taskService.list(new LambdaQueryWrapper<Task>().eq(Task::getContent,1)));
//        System.out.println(taskService.getById(1));
    }

}
