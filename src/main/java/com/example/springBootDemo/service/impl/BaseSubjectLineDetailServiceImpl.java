package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.dao.mapper.BaseSubjectLineDetailDao;
import com.example.springBootDemo.entity.BaseSubjectLineDetail;
import com.example.springBootDemo.entity.report.SubjectReport;
import com.example.springBootDemo.service.BaseSubjectLineDetailService;
import com.example.springBootDemo.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 题材线明细(BaseSubjectLineDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-08-25 17:39:59
 */
@Service("baseSubjectLineDetailService")
public class BaseSubjectLineDetailServiceImpl extends ServiceImpl<BaseSubjectLineDetailDao, BaseSubjectLineDetail> implements BaseSubjectLineDetailService {
    @Resource
    private BaseSubjectLineDetailDao baseSubjectLineDetailDao;

    @Override
    public List<SubjectReport> getSubjectReport(String date, String startDate) {
        return baseSubjectLineDetailDao.getSubjectReport(date, startDate);
    }

    @Override
    public List<BaseSubjectLineDetail> getBaseSubjectLineDetailList(BaseSubjectLineDetail detail) {
        EntityWrapper<BaseSubjectLineDetail> detailWr = new EntityWrapper<>();
        String subLineName = detail.getSubLineName();
        Date createDate = detail.getCreateDate();
        if (StringUtils.isNoneBlank(subLineName)) {
            detailWr.eq("SUB_LINE_NAME", subLineName);
        }

        if (createDate != null) {
            detailWr.eq("create_date", createDate);
        }
        detailWr.eq("STATE", "1");
        return selectList(detailWr);
    }

    @Override
    public void deleteBaseSubjectLineDetailByDateList(String date, String startDate) {
        EntityWrapper<BaseSubjectLineDetail> detailWr = new EntityWrapper();
        if (StringUtils.isNotBlank(startDate)) {
            detailWr.between("create_date", startDate, date);
        } else {
            detailWr.eq("create_date", date);
        }
        delete(detailWr);
    }

    @Override
    public List<String> getHotBusiness(Integer dayCnt) {
        if(dayCnt ==null){
            dayCnt =-10;
        }
        String date = DateUtil.format(new Date(), DateConstant.DATE_FORMAT_10);
        String startDate = DateUtil.format(DateUtil.getDayDiff(new Date(), dayCnt), DateConstant.DATE_FORMAT_10);
        List<SubjectReport> subList = getSubjectReport(date, startDate);
        return subList.stream()
                .sorted(Comparator.comparing(SubjectReport::getCountZt, Comparator.reverseOrder()))
                .sorted(Comparator.comparing(SubjectReport::getCreateDate, Comparator.reverseOrder()))
                .map(SubjectReport::getMainBusiness).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getActiveBusinessList(Date date){
        return baseSubjectLineDetailDao.getActiveBusinessList(date);
    }

    @Override
    public void clearRepetitionDate() {
        baseSubjectLineDetailDao.clearRepetitionDate();
    }
}
