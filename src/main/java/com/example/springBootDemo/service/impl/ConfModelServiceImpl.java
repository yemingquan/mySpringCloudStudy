package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.dao.mapper.ConfModelDao;
import com.example.springBootDemo.entity.ConfModel;
import com.example.springBootDemo.entity.ConfModelDetail;
import com.example.springBootDemo.entity.input.Model;
import com.example.springBootDemo.service.ConfModelDetailService;
import com.example.springBootDemo.service.ConfModelService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 模式，同样一个环境可能会出现多种模式叠加，比如小盘、多概念、消息面、国资委控股(ConfModel)表服务实现类
 *
 * @author makejava
 * @since 2023-10-06 15:52:24
 */
@Service("confModelService")
public class ConfModelServiceImpl extends ServiceImpl<ConfModelDao, ConfModel> implements ConfModelService {
    @Resource
    private ConfModelDao confModelDao;
    @Resource
    private ConfModelService confModelService;
    @Resource
    private ConfModelDetailService confModelDetailService;

    @Override
    public void imporConfModel(InputStream is) throws Exception {
        List<Model> modelList = ExcelUtil.excelToList(is, Model.class);
        is.close();

        Map<String,Model> map = modelList.stream().collect(Collectors.toMap(Model::getModelType, Function.identity(), (item1, item2) -> item2));
        List<ConfModel> confModelList = Lists.newArrayList();
        for (String str : map.keySet()){
            Model m = map.get(str);
            ConfModel po = ConfModel.builder()
                    .modelType(m.getModelType())
                    .modelName(m.getModelName())
                    .abbr(m.getAbbr())
                    .behaviour(m.getBehaviour())
                    .instructions(m.getInstructions())
                    .build();
//            po.setCreateBy("增量概念刷新-修改概念");
//            po.setCreateDate(new Date());
            confModelList.add(po);
        }
        confModelService.insertOrUpdateBatch(confModelList, confModelList.size());

        List<ConfModelDetail> confModelDetailList = Lists.newArrayList();
        for (Model m :modelList){
            if(StringUtils.isEmpty(m.getModelDetailType()))continue;

            ConfModelDetail po = ConfModelDetail.builder()
                    .modelType(m.getModelType())
                    .modelDetailType(m.getModelDetailType())
                    .name(m.getModelDetailName())
                    .abbr(m.getDetailAbbr())
                    .behaviour(m.getDetailBehaviour())
                    .instructions(m.getDetailInstructions())
                    .build();
//            po.setCreateBy("增量概念刷新-修改概念");
//            po.setCreateDate(new Date());
            confModelDetailList.add(po);
        }
        confModelDetailService.insertOrUpdateBatch(confModelDetailList, confModelDetailList.size());
    }

    @Override
    public void queryModel(HttpServletResponse response) throws IOException {
        List<Model> list = confModelDao.queryModel();

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(list, Model.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Model.class, list);
        ExcelUtil.exportExel(response, "ConfModelExcel", workbook);
    }
}
