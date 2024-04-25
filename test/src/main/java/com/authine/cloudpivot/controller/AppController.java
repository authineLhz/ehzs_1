package com.authine.cloudpivot.controller;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.authine.cloudpivot.engine.api.model.application.AppPackageModel;
import com.authine.cloudpivot.web.api.controller.base.BaseController;
import com.authine.cloudpivot.web.api.handler.CustomizedOrigin;
import com.authine.cloudpivot.web.api.service.EngineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.authine.cloudpivot.web.api.view.ResponseResult;
import com.authine.cloudpivot.web.api.view.app.AppPackageVO;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/api/appcreate")
@Slf4j
public class AppController extends BaseController {
    @Autowired
    EngineService engineService;
    @ApiOperation(value = "创建应用", notes = "创建一个应用")
    @PostMapping("/create")
    public ResponseResult<Void> create(@RequestBody AppPackageVO appPackageVO) {

        String name=null;
        String nameSpace=null;
        String groupId=null;

        String appName = name+"的报表";
        String appCode = "reportfor\""+PinyinUtil.getPinyin(name,"")+"\"";



        validateNotEmpty(appCode, CODE_INVALID_MSG);
        validateNotEmpty(appName, NAME_INVALID_MSG);
        validateCode28(appCode);
        validateChineseLength(appName, 50);


        AppPackageModel appPackageModel = new AppPackageModel();

        appPackageModel.setCode(appCode);
        appPackageModel.setName(appName);
        //appPackageModel.setName_i18n(appPackageVO.getName_i18n());
        appPackageModel.setLogoUrlId(null);
        appPackageModel.setGroupId(groupId);
        appPackageModel.setAppNameSpace(nameSpace);
        appPackageModel.setAppDescription(appPackageVO.getAppDescription());


        appPackageModel.setLogoUrl(appPackageVO.getLogoUrl());
        appPackageModel.setEnabled(Boolean.TRUE);
        appPackageModel.setCreatedBy(getUserId());


        AppPackageModel saved = engineService.getAppManagementFacade().createAppPackage(appPackageModel);
        return getOkResponseResult("成功创建应用");
    }
}
