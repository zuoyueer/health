package com.zuoyueer.service;

import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/16
 * Time: 17:31
 * @projectName health_parent
 * @description: 统计报表接口
 */
public interface ReportService {
    /**
     * 获取运营统计数据
     * @return
     */
    Map<String, Object> getBusinessReportData() throws Exception;
}
