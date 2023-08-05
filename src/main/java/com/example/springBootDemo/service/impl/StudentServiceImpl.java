package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.StudentMapper;
import com.example.springBootDemo.domain.StudentPo;
import com.example.springBootDemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * (BStudent)表服务实现类
 *
 * @author xiaoye
 * @since 2023-08-05 12:46:33
 */
@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentPo> implements StudentService {

}