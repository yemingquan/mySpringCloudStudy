package com.example.springBootDemo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.springBootDemo.config.components.system.session.RespBean;
import com.example.springBootDemo.dao.mapper.ConfBsdStockDao;
import com.example.springBootDemo.entity.ConfBsdStock;
import com.example.springBootDemo.entity.ConfCxStock;
import com.example.springBootDemo.entity.ConfMySotck;
import com.example.springBootDemo.entity.input.BaseBond;
import com.example.springBootDemo.service.BaseBondService;
import com.example.springBootDemo.service.ConfBsdStockService;
import com.example.springBootDemo.service.ConfCxStockService;
import com.example.springBootDemo.service.ConfMySotckService;
import com.example.springBootDemo.util.excel.ExcelChangeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置表控制层
 *
 * @author makejava
 * @since 2023-08-17 18:37:58
 */
@RestController
@RequestMapping("conf")
@Api(tags = "基础数据-配置")
public class ConfController {

    @Resource
    private ConfBsdStockService confBsdStockService;

    @Resource
    private ConfCxStockService confCxStockService;
    @Resource
    private ConfMySotckService confMySotckService;
    @Resource
    private BaseBondService baseBondService;
    @Resource
    private ConfBsdStockDao confBsdStockDao;

    @ApiOperation("根据历史数据生成辨识度股票池")
    @PostMapping("/genConfBsdStock")
    public RespBean genConfBsdStock(@RequestParam(value = "date", required = false) String date) {
        try {
            //检索10天以内的数据
            confBsdStockService.genConfBsdStock(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.success("处理成功");
    }


    @ApiOperation("文件导入次新信息")
    @PostMapping("/imporCX")
    public RespBean imporCX() {
        try {
            String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
            File file = new File(basePath + "Table_cx.xls");
            File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

            //设置导入参数
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1); //表头占1行，默认1
            //导入前先删除当天的数据
            EntityWrapper<ConfCxStock> wrapper = new EntityWrapper<>();
            confCxStockService.delete(wrapper);

            List<ConfCxStock> list = ExcelImportUtil.importExcel(new FileInputStream(tempFile), ConfCxStock.class, importParams);
//            is.close();
            confCxStockService.insertBatch(list, list.size());
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("文件导入可转债信息")
    @PostMapping("/imporKZZ")
    public RespBean imporKZZ() {
        try {
            String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
            File file = new File(basePath + "Table_kzz.xls");
            File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

            //设置导入参数
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1); //表头占1行，默认1
            //导入前先删除当天的数据
            EntityWrapper<BaseBond> wrapper = new EntityWrapper<>();
            baseBondService.delete(wrapper);

            List<BaseBond> list = ExcelImportUtil.importExcel(new FileInputStream(tempFile), BaseBond.class, importParams);
//            is.close();
            list.stream().forEach(po -> {
                //如果没有涨幅说明转债有问题
                if (po.getGains() == null) {

                } else {
                    StringBuffer instructions = new StringBuffer("");
                    //转股起始日
                    Date convStartDate = po.getConvStartDate();
                    //债券余额(万元)
                    Double remainSize = po.getRemainSize() / 10000;
                    //转股溢价
                    Double cbOverRate = po.getCbOverRate() * 100;

                    if (cbOverRate > 30) {
                        instructions.append("溢价率:" + new BigDecimal(cbOverRate).setScale(2, BigDecimal.ROUND_UP) + "%;");
                    }
                    if (new Date().after(convStartDate) && remainSize < 3) {
                        instructions.append("规模:" + new BigDecimal(remainSize).setScale(2, BigDecimal.ROUND_UP) + "亿;");
                    } else if (new Date().before(convStartDate)) {
                        instructions.append("次新债;");
                    }
                    po.setInstructions(instructions.toString());
                }
            });
            baseBondService.insertBatch(list, list.size());
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("文件导入股票配置信息")
    @PostMapping("/imporMyStock")
    public RespBean imporMyStock() {
        try {
            String basePath = "C:\\Users\\xiaoYe\\Desktop\\同花顺output\\";
            File file = new File(basePath + "Table_stock.xls");
            File tempFile = ExcelChangeUtil.csvToXlsxConverter(file, file.getName());

            //设置导入参数
            ImportParams importParams = new ImportParams();
            importParams.setHeadRows(1); //表头占1行，默认1
            //导入前先删除当天的数据
            EntityWrapper<ConfMySotck> wrapper = new EntityWrapper<>();
            confMySotckService.delete(wrapper);

            List<ConfMySotck> list = ExcelImportUtil.importExcel(new FileInputStream(tempFile), ConfMySotck.class, importParams);
//            is.close();


            List<ConfBsdStock> bsdList = confBsdStockDao.queryStockMonth(null);
            Map<String, String> map = Maps.newHashMap();
            Map<String, List<ConfBsdStock>> stockMap = bsdList.stream().collect(Collectors.groupingBy(ConfBsdStock::getStockCode));
            for (String stockCode : stockMap.keySet()) {
                List<ConfBsdStock> mapList = stockMap.get(stockCode);
                Set set = Sets.newHashSet();

                for (ConfBsdStock po : mapList) {
                    String mainBusiness = po.getMainBusiness();
                    String nicheBusiness = po.getNicheBusiness();
                    if (StringUtils.isNotBlank(mainBusiness))
                        set.addAll(Sets.newHashSet(mainBusiness.replaceAll("\\+", ";").replaceAll("次新", "").replaceAll("最-", "").split(";")));
                    if (StringUtils.isNotBlank(nicheBusiness))
                        set.addAll(Sets.newHashSet(nicheBusiness.replaceAll("\\+", ";").replaceAll("次新", "").split(";")));
                }
                set.remove(";");
                set.remove("");

                List<String> tempList = Lists.newArrayList(set);
                String mainBusiness = tempList.stream().collect(Collectors.joining((",")));
                map.put(stockCode, mainBusiness);
            }

            for (ConfMySotck myStock : list) {
                String mainBusiness = map.get(myStock.getStockCode());
                if (StringUtils.isNotBlank(mainBusiness)) {
                    myStock.setMainBusiness(mainBusiness);
                }
            }

            confMySotckService.insertBatch(list, list.size());
            return RespBean.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


    @ApiOperation("刷新股票配置信息")
    @PostMapping("/reflshMyStock")
    public RespBean reflshMyStock() {
        try {
            List<ConfMySotck> list = confMySotckService.selectList(new EntityWrapper<ConfMySotck>());

            List<ConfBsdStock> bsdList = confBsdStockDao.queryStockMonth(null);
            Map<String, String> map = Maps.newHashMap();
            Map<String, List<ConfBsdStock>> stockMap = bsdList.stream().collect(Collectors.groupingBy(ConfBsdStock::getStockCode));
            for (String stockCode : stockMap.keySet()) {
                List<ConfBsdStock> mapList = stockMap.get(stockCode);
                Set set = Sets.newHashSet();

                for (ConfBsdStock po : mapList) {
                    String mainBusiness = po.getMainBusiness();
                    String nicheBusiness = po.getNicheBusiness();
                    if (StringUtils.isNotBlank(mainBusiness))
                        set.addAll(Sets.newHashSet(mainBusiness.replaceAll("\\+", ";").replaceAll("最-", "").split(";")));
                    if (StringUtils.isNotBlank(nicheBusiness))
                        set.addAll(Sets.newHashSet(nicheBusiness.replaceAll("\\+", ";").replaceAll("最-", "").split(";")));
                }
                set.remove(";");
                set.remove("");

                List<String> tempList = Lists.newArrayList(set);
                String mainBusiness = tempList.stream().collect(Collectors.joining((",")));
                map.put(stockCode, mainBusiness);
            }

            for (ConfMySotck myStock : list) {
                String mainBusiness = map.get(myStock.getStockCode());
                if (StringUtils.isNotBlank(mainBusiness)) {
                    myStock.setMainBusiness(mainBusiness);
                }
            }

            confMySotckService.updateBatchById(list, list.size());
            return RespBean.success("刷新成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("刷新失败");
    }
}

