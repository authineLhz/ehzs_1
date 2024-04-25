package com.authine.cloudpivot.service.impl;

import com.authine.cloudpivot.util.UserUtil;
import com.authine.cloudpivot.service.SubAdminService;


import com.authine.cloudpivot.engine.api.model.organization.UserModel;
import com.authine.cloudpivot.engine.api.model.permission.AppPackageScopeModel;
import com.authine.cloudpivot.engine.api.model.system.AdminModel;
import com.authine.cloudpivot.engine.enums.type.AdminType;
import com.authine.cloudpivot.web.api.service.EngineService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.authine.cloudpivot.web.api.util.AuthUtils.getUserId;
@Controller
@Service
public class SubAdminServiceImpl implements SubAdminService {

    @Autowired
    EngineService engineService;
    @Autowired
    UserUtil userUtil;

    public String createSubAdmin(String account) {
        //获取用户信息
        UserModel user = userUtil.getUserInfoFromAccount(account);
        //构造管理的应用
        List<AppPackageScopeModel> appPackages=new ArrayList<>();
        AppPackageScopeModel appPackageScopeModel=new AppPackageScopeModel();
        appPackageScopeModel.setName(user.getName());
        String appCode = userUtil.getAppCodeFromAccount(account);
        appPackageScopeModel.setCode(appCode);
        appPackages.add(appPackageScopeModel);
        //获取应用管理员
        List<AdminModel> admins = engineService.getAdminFacade().getAdminByUserId(user.getId());
        List<AdminModel> appAdmins = admins.stream().filter(item -> "应用管理员".equals(item.getAdminType().getName())).collect(Collectors.toList());
        List<AdminModel> managers = new ArrayList<>();

        AdminModel managerModel= new AdminModel();

        if (!(appAdmins.size() > 0)) {
            //如果不是管理员，新建管理员模型
            managerModel.setUserId(user.getId());
            managerModel.setAdminType(AdminType.APP_MNG);
            managerModel.setAppPackages(appPackages);
            managerModel.setCreatedBy(getUserId());
            managerModel.setDataManage(false);
            managerModel.setAppManage(false);
            managerModel.setRoleManage(false);
            managerModel.setDataDictionaryManage(false);
            //  managerModel.setAdminGroups(appPackageMangerVO.getAdminGroups());
            //  managerModel.setDepartments(appPackageMangerVO.getDepartments());
        }else {
            //是管理员添加相应应用权限
            managerModel= appAdmins.get(0);
            List<AppPackageScopeModel> exisAppPackages = managerModel.getAppPackages();
            //判断exisAppPackages中有没有当前的应用管理权限
            List<AppPackageScopeModel> collect = exisAppPackages.stream().filter(item -> appCode.equals(item.getCode())).collect(Collectors.toList());
            if(collect.size()==0){
                exisAppPackages.add(appPackageScopeModel);
                managerModel.setAppPackages(exisAppPackages);
            }
        }
        managers.add(managerModel);
        //添加管理权限
        updateAppAdmins(managers);
        return "null";
    }
    /**
     * 创建或者更新应用管理员
     * @param managers 当前提交的数据组装后的管理员数据
     */
    private void updateAppAdmins(List<AdminModel> managers) {
        if (CollectionUtils.isNotEmpty(managers.get(0).getAppPackages())) {
            //去重
            List<AppPackageScopeModel> appPackages = managers.get(0).getAppPackages();
            List<AppPackageScopeModel> collect = appPackages.stream().collect(Collectors.toMap(AppPackageScopeModel::getCode, Function.identity(), (k1, k2) -> k1)).values().stream().collect(Collectors.toList());
            managers.get(0).setAppPackages(collect);
        }

        List<AdminModel> adminModels = engineService.getAdminFacade().updateAdmins(managers);
    }
}
//    public ResponseResult<Void> updateAppPackageAdmin(@RequestBody AppPackageMangerVO appPackageMangerVO) {
//        List<Map<String, String>> users = appPackageMangerVO.getUsers();
//        if (CollectionUtils.isEmpty(users)) {
//            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), "用户id至少有一个");
//        }
//
//        String managerId = appPackageMangerVO.getId();
//        validateNotEmpty(managerId, "管理员id不能为空");
//
//        boolean loginUserHasAppPackageOrDeptScope = removeLoginUserNoHasAppPackageOrDeptScope(appPackageMangerVO.getAppPackages(), appPackageMangerVO.getDepartments(), users.get(0).get("id"), false);
//        if (!loginUserHasAppPackageOrDeptScope) {
//            throw new PortalException(40002, "存在没有权限的应用范围或者组织范围");
//        }
//        if (!new AppPermHelper(this).isHasMixedByLoginUserPermScopeAndSelectUserDepts(users.get(0).get("id"), appPackageMangerVO.getDepartments())) {
//            throw new PortalException(40003, "当前管理员的组织权限范围和所选管理员所在部门及子部门没有交集，请重新选择有权限范围的管理员");
//        }
//
//        //处理编辑时组织管理范围只是用于展示的组织、展示的组织跳过权限校验
//        List<DepartmentScopeModel> departments = appPackageMangerVO.getDepartments();
//        List<DepartmentScopeModel> notAuthDepartments = appPackageMangerVO.getNotAuthDepartments();
//        List<DepartmentScopeModel> totalDepartments = Lists.newArrayList();
//        totalDepartments.addAll(departments);
//        totalDepartments.addAll(notAuthDepartments);
//        appPackageMangerVO.setDepartments(totalDepartments);
//        List<DepartmentScopeModel> tempModels = Lists.newArrayList(totalDepartments);
//
//        List<AdminModel> managers = Lists.newArrayListWithExpectedSize(users.size());
//        //将没有权限的部分补回来
//        addUserNoHasPerm(appPackageMangerVO);
//        appPackageMangerVO.setDepartments(tempModels);
//
//
//
//
//
//
//        updateAppAdmins(managers);
//
//    }
//        boolean loginUserHasAppPackageOrDeptScope = removeLoginUserNoHasAppPackageOrDeptScope(appPackageMangerVO.getAppPackages(), appPackageMangerVO.getDepartments(), users.get(0).get("id"), false);
//        if (!loginUserHasAppPackageOrDeptScope) {
//            throw new PortalException(40002, "存在没有权限的应用范围或者组织范围");
//        }
//        AppPermHelper appPermHelper = new AppPermHelper(this);
//        if (!appPermHelper.isHasMixedByLoginUserPermScopeAndSelectUserDepts(users.get(0).get("id"), appPackageMangerVO.getDepartments())) {
//            throw new PortalException(40003, "当前管理员的组织权限范围和所选管理员所在部门及子部门没有交集，请重新选择有权限范围的管理员");
//        }

//判断当前子管理员是否已存在
//        List<String> ids = users.stream().map(map -> map.get("id")).collect(Collectors.toList());
//        PageRequest pageable = PageRequest.of(0, 1);
//        Page<AdminModel> adminsByUserIds = engineService.getAdminFacade().getAdminsByUserIds(ids, AdminType.APP_MNG, pageable);