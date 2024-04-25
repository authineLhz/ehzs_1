package com.authine.cloudpivot.web.api.controller.openapi.v3;

import com.authine.cloudpivot.service.PermissionService;
import com.authine.cloudpivot.vo.ReportSaveVo;
import com.authine.cloudpivot.engine.api.model.report.ReportPageModel;
import com.authine.cloudpivot.web.api.controller.base.BaseController;
import com.authine.cloudpivot.web.api.service.EngineService;
import com.authine.cloudpivot.web.api.view.ResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/openapi/v3/report")
@Slf4j
public class ReportOpenApiController extends BaseController {

    @Resource
    PermissionService permissionService;

    @Resource
    EngineService engineService;

    @ApiOperation(value = "保存报表", notes = "保存子管理员、应用、报表、权限组")
    @PostMapping("/save")
    public ResponseResult<Void> save(@RequestBody ReportSaveVo reportSaveVo) {
        permissionService.addPermission(reportSaveVo.getCreatorAccount(), reportSaveVo.getUsersAccount(), reportSaveVo.getId());
        return getOkResponseResult("成功保存报表");
    }

    @ApiOperation(value = "删除报表", notes = "删除报表、权限组")
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@RequestParam("id") String id) {
        engineService.getReportPageFacade().delete(engineService.getReportPageFacade().getReportPageByCode(id));
        return getOkResponseResult("成功删除报表");
    }
}
