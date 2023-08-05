package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.domain.StudentPo;
import org.springframework.stereotype.Service;

/**
 * (BStudent)表服务接口
 *
 * @author xiaoye
 * @since 2023-08-05 12:46:33
 */
@Service
public interface StudentService extends IService<StudentPo> {

}