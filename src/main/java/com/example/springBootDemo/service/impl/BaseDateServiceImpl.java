package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.BaseDateDao;
import com.example.springBootDemo.entity.BaseDate;
import com.example.springBootDemo.service.BaseDateService;
import com.example.springBootDemo.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 日期(BaseDate)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 00:27:46
 */
@Service("baseDateService")
public class BaseDateServiceImpl extends ServiceImpl<BaseDateDao, BaseDate> implements BaseDateService {
    @Resource
    private BaseDateDao baseDateDao;

    @Override
    public void updateBatchByDate(List<BaseDate> list) {
        baseDateDao.updateBatchByDate(list);
    }

    @Override
    public Date getBeforeTypeDate(Date date, List<String> typeList) {
        return baseDateDao.getBeforeTypeDate(date, typeList);
    }

    @Override
    public String getBeforeTypeDate(String dateStr, List<String> typeList) {
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        }
        Date date = DateUtil.parseDate(dateStr);
        Date resultDate = baseDateDao.getBeforeTypeDate(date, typeList);
        return DateUtil.format(resultDate, "yyyy-MM-dd");
    }
}
