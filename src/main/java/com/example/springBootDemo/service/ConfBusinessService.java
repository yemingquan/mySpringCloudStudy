package com.example.springBootDemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.springBootDemo.entity.input.ConfBusiness;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 行业配置化(ConfBusiness)表服务接口
 *
 * @author makejava
 * @since 2023-09-13 16:37:28
 */
public interface ConfBusinessService extends IService<ConfBusiness>{

    boolean imporConfBusiness(MultipartFile inputStream) throws Exception;

    void exportConfBusiness(HttpServletResponse response, List<ConfBusiness> addList) throws Exception;

    void refushConfBusiness();

    void exportConfBusinessWithTHS(HttpServletResponse response) throws Exception;
}
