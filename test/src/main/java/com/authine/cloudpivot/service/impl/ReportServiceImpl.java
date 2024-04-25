package com.authine.cloudpivot.service.impl;

import com.authine.cloudpivot.service.ReportService;
import com.authine.cloudpivot.engine.api.model.report.ReportPageModel;
import com.authine.cloudpivot.web.api.exception.PortalException;
import com.authine.cloudpivot.web.api.exception.ResultEnum;
import com.authine.cloudpivot.web.api.service.EngineService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.authine.cloudpivot.web.api.controller.base.BaseController.*;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    EngineService engineService;

    @Override
    public String createReport(String appCode,String reportCode,String reportName) {


        validateNotEmpty(reportCode, CODE_INVALID_MSG);
        validateNotEmpty(reportName, NAME_INVALID_MSG);
        validateCode28(reportCode);
        validateChineseLength(reportName, 50);

        ReportPageModel reportPageModel=new ReportPageModel();
        reportPageModel.setAppCode(appCode);
        reportPageModel.setCode(reportCode);
        reportPageModel.setName(reportName);
        reportPageModel.setPcAble(true);
        reportPageModel.setMobileAble(false);
        reportPageModel.setIcon(null);

        engineService.getReportPageFacade().save(reportPageModel);
        return "成功创建报表";
    }
    protected void validateNotEmpty(String value, String errMsg) {
        if (Strings.isNullOrEmpty(value)) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), errMsg);
        }
    }
    protected void validateCode28(String code) {
        boolean isMatches = StringUtils.isNotBlank(code) && code.matches("^[a-zA-Z][a-zA-Z0-9_]{0,27}");
        if (!isMatches) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), CODE_INVALID_MSG);
        }
    }
    protected void validateChineseLength(String value, int maxLen) {
        if (getChineseLength(value) > maxLen) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), "字符长度超过最大长度[" + maxLen + "]了");
        }
    }

}