package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.RelationConfDao;
import com.example.springBootDemo.entity.ConfModel;
import com.example.springBootDemo.entity.ConfModelDetail;
import com.example.springBootDemo.entity.ConfModelOther;
import com.example.springBootDemo.entity.RelationConf;
import com.example.springBootDemo.entity.report.ModelReport;
import com.example.springBootDemo.service.ConfModelDetailService;
import com.example.springBootDemo.service.ConfModelOtherService;
import com.example.springBootDemo.service.ConfModelService;
import com.example.springBootDemo.service.RelationConfService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 模式关系表(RelationConf)表服务实现类
 *
 * @author makejava
 * @since 2023-10-07 09:11:30
 */
@Service("relationConfService")
public class RelationConfServiceImpl extends ServiceImpl<RelationConfDao, RelationConf> implements RelationConfService {
    @Resource
    private RelationConfDao relationConfDao;
    @Resource
    private RelationConfService relationConfService;
    @Resource
    private ConfModelService confModelService;
    @Resource
    private ConfModelDetailService confModelDetailService;
    @Resource
    private ConfModelOtherService confModelOtherService;

    @Override
    public void queryRelationConf(HttpServletResponse response) throws IOException {
        EntityWrapper relationConfEw = new EntityWrapper<>();
        relationConfEw.orderBy("date", false);
        List<RelationConf> relationConfList = relationConfDao.selectList(relationConfEw);
        oprRelationConf(relationConfList);

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(relationConfList, RelationConf.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, RelationConf.class, relationConfList);
        ExcelUtil.exportExel(response, "RelationConf", workbook);
    }

    public void oprRelationConf(List<RelationConf> relationConfList) {
        // 转码处理
        Map<String, String> modelRelationMap = getModelRelation(false);
//        Map<String, String> modelDetailRelation = getModelDetailRelation(false);
        Map<String, String> modelOtherRelation = getModelOtherRelation(false);
        for (RelationConf rc : relationConfList) {
            String model = rc.getModel();
            if(StringUtils.isNotBlank(model)){
                String modelName = modelRelationMap.get(model);
                rc.setModel(modelName);
            }

            String modelOther = rc.getModelOther();
            if(StringUtils.isNotBlank(modelOther)){
                String modelOtherName = modelOtherRelation.get(modelOther);
                rc.setModelOther(modelOtherName);
            }

            String stockName = rc.getStockName();
            if(StringUtils.isNotBlank(stockName)){
                //TODO 需要改成方法
                stockName = stockName.replaceAll("，", ",");
            }
        }
    }

    public Map<String, String> getModelRelation(Boolean flag) {
        EntityWrapper ew = new EntityWrapper<ConfModel>();
        List<ConfModel> list = confModelService.selectList(ew);
        Map<String, String> map = list.stream().collect(Collectors.toMap(ConfModel::getModelType, ConfModel::getModelName, (item1, item2) -> item2));
        if (flag) {
            map = MapUtils.invertMap(map);
        }
        return map;
    }

    public Map<String, String> getModelDetailRelation(Boolean flag) {
        EntityWrapper ew = new EntityWrapper<ConfModelDetail>();
        List<ConfModelDetail> list = confModelDetailService.selectList(ew);
        Map<String, String> map = list.stream().collect(Collectors.toMap(ConfModelDetail::getModelDetailType, ConfModelDetail::getName, (item1, item2) -> item2));
        if (flag) {
            map = MapUtils.invertMap(map);
        }
        return map;
    }

    public Map<String, String> getModelOtherRelation(Boolean flag) {
        EntityWrapper ew = new EntityWrapper<ConfModelOther>();
        List<ConfModelOther> list = confModelOtherService.selectList(ew);
        Map<String, String> map = list.stream().collect(Collectors.toMap(ConfModelOther::getConf, ConfModelOther::getName, (item1, item2) -> item2));
        if (flag) {
            map = MapUtils.invertMap(map);
        }
        return map;
    }

    @Override
    public void imporRelationConf(InputStream is) throws Exception {
        List<RelationConf> list = ExcelUtil.excelToList(is, RelationConf.class);
        is.close();
        oprRelationConf(list);
        relationConfService.insertOrUpdateBatch(list, list.size());
    }

    @Override
    public List<ModelReport> exportModelReport(String date) {
        return relationConfDao.exportModelReport(date);
    }

}
