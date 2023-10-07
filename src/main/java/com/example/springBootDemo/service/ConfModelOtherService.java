package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfModelOther;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


/**
 * 模式——其他(ConfModelOther)表服务接口
 *
 * @author makejava
 * @since 2023-10-06 22:15:46
 */
public interface ConfModelOtherService extends IService<ConfModelOther>{

    void queryModelOther(HttpServletResponse response) throws IOException;

    void imporConfModelOther(InputStream inputStream) throws Exception;
}
