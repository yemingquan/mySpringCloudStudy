package com.example.springBootDemo.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.AOP.LogAnnotation;
import com.example.springBootDemo.config.system.session.RespBean;
import com.example.springBootDemo.entity.test.Student;
import com.example.springBootDemo.service.StudentService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//MVC层  json格式
@RestController
@RequestMapping("/ssm")
@Api(tags = {"测试模块(功能测试)"})
@EnableCaching
@Component
@Slf4j
public class TestController {

    @Autowired
    StudentService studentService;

    @Resource
    private ThreadPoolTaskExecutor executor;


    @ApiOperation("缓存测试")
//    @Cacheable(cacheNames="sampleCache", value = "c", key = "123")
    @Cacheable(value = "c", key = "123")
    @GetMapping("c")
    public String hello(String name) {
        System.out.println("name - " + name);
        return "hello " + name;
    }

//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("sampleCache")));//注册名为sampleCache的缓存
//        return cacheManager;
//    }

    @LogAnnotation(module = "123", name = "学生测试")
    @GetMapping("/save")
    public String save(Student po) {
        int result = 10 / 0;//TODO 测试用
        studentService.insert(po);
        return "save success";
    }

    @GetMapping("/del")
    public String del(Long id) {
//        studentService.delete(id);
        return "del success";
    }

    @GetMapping("/update")
    public String update(Student stu) {
//        Student stu = new Student();
//        stu.setId((long)Math.random());
//        stu.setName("人名"+Math.random());
        studentService.updateById(stu);
        return "update success";
    }

    @GetMapping("/get")
    public Student get(Long id) {
        Student stu = studentService.selectById(id);
        return stu;
    }

    @LogAnnotation(module = "123", name = "学生清单")
    @GetMapping("/list")
    public List<Student> list(Student po) {
        //根据某年级查询并列举所有数据
        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        wrapper.eq("id", po.getId());
        List<Student> list = studentService.selectList(wrapper);
        return list;
    }

    @ApiOperationSupport(order = 1)
    @GetMapping("/test")
    @ApiOperation("1-0 测试接口")
    public void test(HttpServletResponse response) throws Exception {
//        log.info("1-1{}", 'Ａ' + 1);
//        log.info("1-1{}", ('Ａ' + 1 < 128));
//
//        for (int i = 65313; ; i++) {
//            char c = (char) i;
//            log.info("char:{},字符:{}", i, c);
//        }
    }

    @ApiOperationSupport(order = 1)
    @GetMapping("/testThreadPoolTaskExecutor")
    @ApiOperation("1-0 测试线程池")
    public void testThreadPoolTaskExecutor(HttpServletResponse response) throws Exception {
        log.info("-----------------------------------开始");
        CompletableFuture.runAsync(() -> {
            try {
//                for (int i = 65313; ; i++) {
//                    char c = (char) i;
//                    log.info("char:{},字符:{}", i, c);
//                }
                Thread.sleep(2000);
                log.info("异步线程执行完毕 ");
            } catch (Exception e) {
                log.error("异常 ", e);
            }
        }, executor);
        log.info("-----------------------------------结束");
    }


    @ApiOperationSupport(order = 9999)
    @ApiOperation("Excel导出测试")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String fileName = "excel测试.xlsx";
        String sheetName = "excel测试";
//        ExportParams params = new ExportParams(title, "sheet1", ExcelType.XSSF);

        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        List<Student> list = studentService.selectList(wrapper);

        ExcelUtil<Student> excelUtil = new ExcelUtil<>(Student.class);
        excelUtil.exportCustomExcel_bak(list, fileName, sheetName, response);
    }

    @ApiOperationSupport(order = 9999)
    @ApiOperation("Excel导入测试")
    @PostMapping("/import")
    public RespBean importExcel(@RequestPart MultipartFile multipartFile) {
        try {
            List<Student> list = ExcelUtil.excelToList(multipartFile, Student.class);
            if (studentService.insertBatch(list)) {
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}
