package com.cong.example;

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

}
