package com.authine.cloudpivot.service.impl;

import com.authine.cloudpivot.service.AppService;
import com.authine.cloudpivot.service.ReportService;
import com.authine.cloudpivot.util.UserUtil;


import com.authine.cloudpivot.app.api.facade.ApplicationFacade;
import com.authine.cloudpivot.engine.api.exceptions.ServiceException;
import com.authine.cloudpivot.engine.api.model.application.AppGroupModel;
import com.authine.cloudpivot.engine.api.model.application.AppPackageModel;
import com.authine.cloudpivot.engine.api.model.organization.UserModel;
import com.authine.cloudpivot.web.api.exception.PortalException;
import com.authine.cloudpivot.web.api.exception.ResultEnum;
import com.authine.cloudpivot.web.api.service.EngineService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.authine.cloudpivot.web.api.controller.base.BaseController.*;
import static com.authine.cloudpivot.web.api.util.AuthUtils.getUserId;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    EngineService engineService;
    @Autowired
    ApplicationFacade applicationFacade;
    @Autowired
    ReportService reportService;
    @Autowired
    UserUtil userUtil;
    @Override
    public String saveApp(String account,String reportCode,String reportName) {

        AppGroupModel ehaigroup = engineService.getApplicationFacade().getAppGroupByGroupCode("ehaigroup");
        if ((ehaigroup==null)){
            createGroup("ehaigroup");
        }
        AppGroupModel ehaigroup2 = engineService.getApplicationFacade().getAppGroupByGroupCode("ehaigroup");
        UserModel user = userUtil.getUserInfoFromAccount(account);

        String groupId=ehaigroup2.getId();
        String appNameSpace=userUtil.getNameSpace();
        String userId=user.getId();
        String peopleName=user.getName();

        String appName = peopleName+"的报表";
        String appCode = userUtil.getAppCodeFromAccount(account);
        AppPackageModel appPackageFromDb = applicationFacade.getAppPackageByCodeIgnoreDelete(appCode);

        if(appPackageFromDb!=null){
            if (appPackageFromDb.getDeleted().equals(true)) {
                //还原
                List<String> ids=new ArrayList<>();
                ids.add(appPackageFromDb.getId());
                engineService.getAppManagementFacade().restoreFromRecycle(ids);
            }
        }else{
            validateNotEmpty(appCode, CODE_INVALID_MSG);
            validateNotEmpty(appName, NAME_INVALID_MSG);
            validateCode28(appCode);
            validateChineseLength(appName, 50);

            AppPackageModel appPackageModel = new AppPackageModel();
            appPackageModel.setCode(appCode);
            appPackageModel.setName(appName);
            appPackageModel.setGroupId(groupId);
            appPackageModel.setAppNameSpace(appNameSpace);
            appPackageModel.setAppDescription(null);
            appPackageModel.setLogoUrl(null);
            appPackageModel.setLogoUrlId(null);
            appPackageModel.setEnabled(Boolean.TRUE);
            appPackageModel.setCreatedBy(userId);

            AppPackageModel saved = engineService.getAppManagementFacade().createAppPackage(appPackageModel);
        }
        if(engineService.getReportPageFacade().getReportPageByCode(reportCode)==null){
            reportService.createReport(appCode,reportCode,reportName);
        }
        return "成功创建应用和报表";

    }

    public void createGroup(String groupCode) {
        AppGroupModel appGroupModel = new AppGroupModel();
        appGroupModel.setCode(groupCode);
        appGroupModel.setName("e海智数分组");
        appGroupModel.setCreatedBy(getUserId());
        //appGroupModel.setChildren(VoUtils.toModel(appGroupVO.getChildren(), AppPackageModel.class));

        engineService.getApplicationFacade().createAppGroup(appGroupModel);
    }

    public void validateNotEmpty(String value, String errMsg) {
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
//appPackageModel.setName_i18n(appPackageVO.getName_i18n());
//reportPageModel.setName_i18n(reportPageVo.getName_i18n());
//    "reportfor\""+ PinyinUtil.getPinyin(peopleName,"")+"\"";