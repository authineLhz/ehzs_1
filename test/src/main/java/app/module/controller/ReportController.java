package app.module.controller;

import com.authine.cloudpivot.engine.api.model.application.AppPackageModel;
import com.authine.cloudpivot.engine.api.model.report.ReportPageModel;
import com.authine.cloudpivot.web.api.controller.base.BaseController;
import com.authine.cloudpivot.web.api.service.EngineService;
import com.authine.cloudpivot.web.api.view.ResponseResult;
import com.authine.cloudpivot.web.api.view.app.AppPackageVO;
import com.authine.cloudpivot.web.api.view.app.ReportPageVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportcreate")
public class ReportController extends BaseController {
    @Autowired
    EngineService engineService;
    @PostMapping("/create")
    public ResponseResult<Void> create(@RequestBody ReportPageVo reportPageVo) {
        validateNotEmpty(reportPageVo.getCode(), CODE_INVALID_MSG);
        validateNotEmpty(reportPageVo.getName(), NAME_INVALID_MSG);
        validateCode28(reportPageVo.getCode());
        validateChineseLength(reportPageVo.getName(), 50);

        ReportPageModel reportPageModel=new ReportPageModel();
        reportPageModel.setAppCode(reportPageVo.getAppCode());
        reportPageModel.setCode(reportPageVo.getCode());
        reportPageModel.setIcon(reportPageVo.getIcon());
        reportPageModel.setMobileAble(reportPageVo.getMobileAble());
        reportPageModel.setName(reportPageVo.getName());
        reportPageModel.setName_i18n(reportPageVo.getName_i18n());
        reportPageModel.setPcAble(reportPageVo.getPcAble());

        engineService.getReportPageFacade().save(reportPageModel);
        return getOkResponseResult("成功创建报表");
    }
}
