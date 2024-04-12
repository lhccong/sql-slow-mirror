package com.cong.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cong.example.service.TaskService;
import com.cong.example.domain.Task;
import com.cong.example.mapper.TaskMapper;
import org.springframework.stereotype.Service;

/**
* @author liuhuaicong
* @description 针对表【task】的数据库操作Service实现
* @createDate 2024-04-12 10:39:07
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService {

}




