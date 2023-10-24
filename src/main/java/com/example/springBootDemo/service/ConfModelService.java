package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.ConfModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


/**
 * 模式，同样一个环境可能会出现多种模式叠加，比如小盘、多概念、消息面、国资委控股(ConfModel)表服务接口
 *
 * @author makejava
 * @since 2023-10-06 15:52:24
 */
public interface ConfModelService extends IService<ConfModel>{

    void imporConfModel(InputStream is) throws Exception;

    void queryModel(HttpServletResponse response) throws IOException;
}
