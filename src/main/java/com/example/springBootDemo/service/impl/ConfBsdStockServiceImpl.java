package com.example.springBootDemo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfBsdStockDao;
import com.example.springBootDemo.entity.ConfBsdStock;
import com.example.springBootDemo.service.ConfBsdStockService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 辨识度股票(ConfBsdStock)表服务实现类
 *
 * @author makejava
 * @since 2023-08-17 18:35:09
 */
@Slf4j
@Service("confBsdStockService")
public class ConfBsdStockServiceImpl extends ServiceImpl<ConfBsdStockDao, ConfBsdStock> implements ConfBsdStockService {
    @Resource
    private ConfBsdStockDao confBsdStockDao;
    @Autowired
    private ConfBsdStockService confBsdStockService;

    @Override
    public List<ConfBsdStock> genConfBsdStock(String date) {
            List checkList = Lists.newArrayList("死亡换手","大长腿","负反馈","加速","A杀","重点监控","高度板","日内龙","回封龙");
            List checkAttrList = Lists.newArrayList("小盘","低位","低价","次新");


        List<ConfBsdStock> list = confBsdStockDao.queryStockMonth(date);
//        辨识度标的
//        走势：加速、天地板、大长腿、地天、A杀、重点监控、高度板
//        频率；7(5)/3
//        地位：日内龙、回封龙

        list = list.stream().filter(po -> {
            String mainBusiness = po.getMainBusiness();
            String[] mArr = mainBusiness.replaceAll("最-", "").split(";");
            Set mSet = Sets.newHashSet(mArr);
            po.setMainBusiness(mSet.toString());

            String nicheBusiness = po.getNicheBusiness();
            if (StringUtils.isBlank(nicheBusiness)) return false;
            Set nSet = Sets.newHashSet(nicheBusiness.replaceAll("\\+", ";").split(";"));
//            log.info(nSet.toString());
            po.setNicheBusiness(nSet.toString());

            String instructions = po.getInstructions();
//            log.info(instructions);
            if (StringUtils.isBlank(instructions)) return false;
            Set<String> iSet = Sets.newHashSet(instructions.split(";"));

            List attrList = Lists.newArrayList();
            List iList = Lists.newArrayList();
            for (String str : iSet) {
                if (checkList.contains(str)) {
                    iList.add(str);
                } else if (checkAttrList.contains(str)) {
                    attrList.add(str);
                }
            }
            po.setAttr(attrList.toString());
            po.setInstructions(iList.toString());

            if (mArr.length > 4) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) confBsdStockService.insertBatch(list, list.size());

        return list;
    }
}
