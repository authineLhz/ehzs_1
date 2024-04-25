package com.authine.cloudpivot.service.impl;

import com.authine.cloudpivot.engine.api.model.report.ReportPageModel;
import com.authine.cloudpivot.engine.enums.type.*;
import com.authine.cloudpivot.engine.enums.type.bizmodel.ModelType;
import com.authine.cloudpivot.service.PermissionService;
import com.authine.cloudpivot.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.authine.cloudpivot.engine.api.model.application.AppPackageModel;
import com.authine.cloudpivot.engine.api.model.organization.UserModel;
import com.authine.cloudpivot.engine.api.model.permission.AppFunctionPermissionConditionModel;
import com.authine.cloudpivot.engine.api.model.permission.AppFunctionPermissionModel;
import com.authine.cloudpivot.engine.api.model.permission.AppPackagePermissionModel;
import com.authine.cloudpivot.engine.api.model.permission.PermissionGroupModel;
import com.authine.cloudpivot.web.api.exception.PortalException;
import com.authine.cloudpivot.web.api.exception.ResultEnum;
import com.authine.cloudpivot.web.api.service.EngineService;
import com.authine.cloudpivot.web.api.view.runtime.DepartmentUnitVO;
import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Resource
    EngineService engineService;

    @Resource
    UserUtil userUtil;

    @Override
    @Transactional
    public void addPermission(String creator, List<String> users, String eId) {
        //判断参数是否异常
        if (Objects.isNull(users) || users.isEmpty()) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), "权限组人员为空");
        }
        //获取创建人对应的应用编码
        String appCode = userUtil.getAppCodeFromAccount(creator);
        //获取创建人对应的userInfo
        UserModel createUser = userUtil.getUserInfoFromAccount(creator);
        //判断该应用是否部分人可见,若不是，将其修改
        checkAndSetPartPermission(creator, appCode);
        //users转成对应所需的数据格式,即权限范围
        Stream<UserModel> usersInfoStream = users.stream().map(user -> userUtil.getUserInfoFromAccount(user));
        List<UserModel> usersInfo = usersInfoStream.collect(Collectors.toList());
        List<DepartmentUnitVO> permissionRange = usersInfo.stream().map(user -> new DepartmentUnitVO(UnitType.USER.getIndex(), user.getName(), user.getId())).collect(Collectors.toList());
        String rangeString = JSON.toJSONString(permissionRange);
        //获取权限组，若之前并未创建，则创建
        PermissionGroupModel groupInfo = getPermissionGroupModel(creator, appCode, rangeString, eId);
        //设置创建人、修改人信息
        updateCreateAndModifyInfo(groupInfo, createUser);
        //创建或者保存权限组
        engineService.getPermissionManagementFacade().updatePermissionGroup(groupInfo);
    }

    private List<AppFunctionPermissionModel> getAppFunctionPermissionModels(String eId) {
        List<AppFunctionPermissionModel> appFunctionPermissionModels = new ArrayList<>();
        AppFunctionPermissionModel appFunctionPermissionModel = new AppFunctionPermissionModel();
        appFunctionPermissionModel.setVisible(Boolean.TRUE);
        //获取报表名称，将其设置为权限组名称
        ReportPageModel report = engineService.getReportPageFacade().getReportPageByCode(eId);
        appFunctionPermissionModel.setFunctionName(report.getName());
        appFunctionPermissionModel.setSchemaName(report.getName());
        appFunctionPermissionModel.setFunctionCode(eId);
        appFunctionPermissionModel.setSchemaCode(eId);
        appFunctionPermissionModel.setFilterType(FilterType.ALL);
        appFunctionPermissionModel.setConditions(new ArrayList<>());
        appFunctionPermissionModel.setDataPermissionType(DataPermissionType.ALL);
        appFunctionPermissionModel.setNodeType(NodeType.Report);
        appFunctionPermissionModel.setModelType(ModelType.LIST);
        appFunctionPermissionModel.setCreatable(Boolean.TRUE);
        appFunctionPermissionModel.setDeletable(Boolean.FALSE);
        appFunctionPermissionModel.setEditable(Boolean.TRUE);
        appFunctionPermissionModel.setExportable(Boolean.TRUE);
        appFunctionPermissionModel.setImportable(Boolean.TRUE);
        appFunctionPermissionModel.setPrintAble(Boolean.FALSE);
        appFunctionPermissionModel.setEditOwnerAble(Boolean.FALSE);
        appFunctionPermissionModel.setBatchPrintAble(Boolean.FALSE);
        appFunctionPermissionModel.setBatchUpdateAble(Boolean.FALSE);
        appFunctionPermissionModel.setPropertityPermissionGroups(new ArrayList<>());
        appFunctionPermissionModels.add(appFunctionPermissionModel);
        return appFunctionPermissionModels;
    }

    private void updateCreateAndModifyInfo(PermissionGroupModel groupInfo, UserModel createUser) {
        //id为空，则处于创建,设置其创建人信息
        if (StringUtils.isEmpty(groupInfo.getId())) {
            groupInfo.setCreatedBy(createUser.getId());
        }
        groupInfo.setModifiedBy(createUser.getId());
        //为每一个模型或者报表的权限设置修改人信息
        List<AppFunctionPermissionModel> appFunctionPermissionModels = groupInfo.getDataPermissionGroups();
        if (CollectionUtils.isNotEmpty(appFunctionPermissionModels)) {
            for (AppFunctionPermissionModel appFunctionPermissionModel : appFunctionPermissionModels) {
                appFunctionPermissionModel.setModifiedBy(createUser.getId());
                appFunctionPermissionModel.setCreatedBy(createUser.getId());
                List<List<AppFunctionPermissionConditionModel>> conditions = appFunctionPermissionModel.getConditions();
                if (CollectionUtils.isNotEmpty(conditions)) {
                    conditions.forEach(item -> {
                        if (CollectionUtils.isEmpty(item)) {
                            return;
                        }
                        item.forEach(condition -> condition.setModifiedBy(createUser.getId()));
                    });
                }
            }
        }
    }

    private PermissionGroupModel getPermissionGroupModel(String creator, String appCode, String rangeString, String eId) {
        //判断是否已经存在权限组
        List<PermissionGroupModel> permissionGroups = engineService.getPermissionManagementFacade().getPermissionGroupListByAppPackageCode(appCode);

        PermissionGroupModel groupInfo;

        //如果为空，则创建
        if (permissionGroups.isEmpty()) {
            //数据格式转化
            groupInfo = new PermissionGroupModel();
            //以creatorAccount为权限组名称
            groupInfo.setName(creator);
            //设置应用编码
            groupInfo.setAppCode(appCode);
            //设置权限组范围
            groupInfo.setDepartments(rangeString);
            //角色组为空
            groupInfo.setRoles(Strings.EMPTY);
            //外部用户拒绝
            groupInfo.setExternalUser(Boolean.FALSE);
            //按组织
            groupInfo.setAuthorType(AuthorType.ORGANIZATION);
            //排序值设置默认为0
            groupInfo.setSortKey(0);
            groupInfo.setDataPermissionGroups(getAppFunctionPermissionModels(eId));
        } else {
            groupInfo = permissionGroups.get(0);
            groupInfo.setDepartments(rangeString);
            //修改权限组配置为可见
            if (Objects.nonNull(groupInfo.getDataPermissionGroups())) {
                groupInfo.getDataPermissionGroups().forEach(permission -> permission.setVisible(Boolean.TRUE));
            } else {
                groupInfo.setDataPermissionGroups(getAppFunctionPermissionModels(eId));
            }
        }
        return groupInfo;
    }

    private void checkAndSetPartPermission(String creator, String appCode) {
        //获取应用部分人可见权限
        AppPackagePermissionModel permission = engineService.getPermissionManagementFacade().getAppPackagePermission(appCode);
        if (!permission.getVisibleType().equals(VisibleType.PART_VISIABLE)) {
            //修改权限组模式为部分人可见
            VisibleType visibleType = VisibleType.PART_VISIABLE;
            //填充应用编码
            AppPackageModel appPackageModel = new AppPackageModel();
            appPackageModel.setCode(appCode);
            //获取创建人的id并填充
            AppPackagePermissionModel appPackagePermissionModel = new AppPackagePermissionModel();
            appPackagePermissionModel.setAppPackage(appPackageModel);
            appPackagePermissionModel.setVisibleType(visibleType);
            appPackagePermissionModel.setModifiedBy(userUtil.getUserInfoFromAccount(creator).getId());
            //修改权限组模式为部分人可见
            engineService.getPermissionManagementFacade().updateAppPackagePermission(appPackagePermissionModel);
        }
    }
}
