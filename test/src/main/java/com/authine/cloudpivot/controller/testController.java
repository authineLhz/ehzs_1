package com.authine.cloudpivot.controller;

import com.authine.cloudpivot.service.ReportService;
import com.authine.cloudpivot.service.SubAdminService;
import com.authine.cloudpivot.service.impl.AppServiceImpl;
import com.authine.cloudpivot.web.api.controller.base.BaseController;
import com.authine.cloudpivot.web.api.service.EngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.authine.cloudpivot.web.api.view.ResponseResult;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequestMapping("/api/appcreate2")
public class testController extends BaseController {
    @Autowired
    EngineService engineService;
    @Autowired
    AppServiceImpl appService;
    @Autowired
    ReportService reportService;
    @Autowired
    SubAdminService subAdminService;
    @PostConstruct
    public void init(){
        log.info("lhzTestTestReportOpenApiController");
    }
    @PostMapping("/create")
    public ResponseResult<Void> create(String account) {

        appService.saveApp(account,"testreportcode","测试报表的名称");
        subAdminService.createSubAdmin(account);
        return getOkResponseResult("成功创建应用");

    }
}
