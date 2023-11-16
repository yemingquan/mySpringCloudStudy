package com.example.springBootDemo.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.springBootDemo.config.components.constant.DateConstant;
import com.example.springBootDemo.config.components.constant.StockConstant;
import com.example.springBootDemo.dao.mapper.BaseStockMonitorDao;
import com.example.springBootDemo.entity.BaseStockMonitor;
import com.example.springBootDemo.entity.ConfStock;
import com.example.springBootDemo.service.BaseStockMonitorService;
import com.example.springBootDemo.service.ConfDateService;
import com.example.springBootDemo.service.ConfStockService;
import com.example.springBootDemo.util.excel.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * 股票监管池(BaseStockMonitor)表服务实现类
 *
 * @author makejava
 * @since 2023-10-21 19:50:08
 */
@Service("baseStockMonitorService")
public class BaseStockMonitorServiceImpl extends ServiceImpl<BaseStockMonitorDao, BaseStockMonitor> implements BaseStockMonitorService {
    @Resource
    private BaseStockMonitorDao baseStockMonitorDao;
    @Resource
    private BaseStockMonitorService baseStockMonitorService;
    @Resource
    private ConfStockService confStockService;
    @Resource
    private ConfDateService confDateService;

    @Override
    public void queryBaseStockMonitor(HttpServletResponse response) throws IOException {
        EntityWrapper ew = new EntityWrapper<>();
        ew.orderBy("monitor_start", false);
        List<BaseStockMonitor> list = baseStockMonitorDao.selectList(ew);

        //生成excel文档
        ExportParams params = ExcelUtil.getSimpleExportParams(list, BaseStockMonitor.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, BaseStockMonitor.class, list);
        ExcelUtil.exportExel(response, "BaseStockMonitor", workbook);
    }

    @Override
    public void imporBaseStockMonitor(InputStream is) throws Exception {
        //如果是全量导出，那么可以删除后再导入
        EntityWrapper ew = new EntityWrapper<>();
        baseStockMonitorService.delete(ew);

        List<BaseStockMonitor> list = ExcelUtil.excelToList(is, BaseStockMonitor.class);
        is.close();

        for (BaseStockMonitor vo : list) {
            if (StringUtils.isEmpty(vo.getReason())) {
                vo.setReason(StockConstant.MonitorReasnEnum.MAIN_1.getName());
            }

            String stockName = vo.getStockName();
            if (StringUtils.isEmpty(vo.getStockCode()) && StringUtils.isNotBlank(stockName)) {
                ConfStock confStock = confStockService.getStockCodeByStockName(stockName);
                vo.setStockCode(confStock.getStockCode());
            }

            if (vo.getCycle() == null) {
                Integer cycle = confDateService.queryTypeDayLimit(vo.getMonitorStart(),vo.getMonitorEnd(), DateConstant.DEAL_LIST);
                vo.setCycle(cycle);
            }
        }
        baseStockMonitorService.insertOrUpdateBatch(list, list.size());
    }

    @Override
    public List<BaseStockMonitor> getStockMonitorListByDate(String date) {
        return baseStockMonitorDao.getStockMonitorListByDate(date);
    }

    @Override
    public List<BaseStockMonitor> getNewStockMonitor(String date) {
        return baseStockMonitorDao.getNewStockMonitor(date);
    }

    @Override
    public List<BaseStockMonitor> getCloseToRemoveStockMonitor(String date) {
        return baseStockMonitorDao.getCloseToRemoveStockMonitor(date);
    }

}
