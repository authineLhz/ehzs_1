package test;

//权限模式修改
///api/system/permission/update_apppackage
//如果为1所有人可见；如果为2则根据权限组可见
//{
//  "appCode": "test_app",
//  "visibleType": 2
//}

//权限组新增
///api/system/permission/update_group
//{
//  "appCode": "test_app",
//  "name": "test",
//  "externalUser": false,
//  "departments": "[{\"id\":\"8a2816c98f09f7b8018f09fc188718e2\",\"name\":\"lhz\",\"unitType\":3,\"code\":\"lhz\",\"corpid\":\"RELEVANCE-94eec13d9ab948f0895373ca93a9f400\"}]",
//  "id": ""
//}

//权限组查询
///api/system/permission/get_apppackage?appCode=test_app
//{
//  "id": "8a2816c98f09f7b8018f09fd4af118ec",
//  "remarks": null,
//  "createdTime": "2024-04-23 16:05:46",
//  "modifiedTime": "2024-04-23 16:34:10",
//  "deleted": false,
//  "createdBy": null,
//  "modifiedBy": null,
//  "appPackage": {
//    "id": "8a2816c98f09f7b8018f09fd4ae118ea",
//    "remarks": null,
//    "createdTime": "2024-04-23 16:05:46",
//    "modifiedTime": "2024-04-23 16:05:46",
//    "deleted": false,
//    "createdBy": "8a2816c98f09f7b8018f09fc188718e2",
//    "modifiedBy": null,
//    "groupId": "2c928fe6785dbfbb01785dc6277a0000",
//    "appGroupName": null,
//    "code": "test_app",
//    "name": "测试权限组信息",
//    "name_i18n": "{\"zh\":\"测试权限组信息\"}",
//    "logoUrlId": null,
//    "logoUrl": null,
//    "sortKey": 0,
//    "enabled": true,
//    "appKey": null,
//    "appSecret": null,
//    "agentId": null,
//    "appNameSpace": "fg2c",
//    "fromAppMarket": false,
//    "favorites": false,
//    "builtInApp": false,
//    "appDescription": null,
//    "parentApp": null,
//    "source": null
//  },
//  "departments": "[{\"id\":\"8a2816c98f09f7b8018f09fc188718e2\",\"name\":\"lhz\",\"unitType\":3}]",
//  "roles": null,
//  "permissionGroups": [
//    {
//      "id": "8a2816c98f0a0a5e018f0a174b1f0003",
//      "remarks": null,
//      "createdTime": "2024-04-23 16:34:10",
//      "modifiedTime": "2024-04-23 16:34:10",
//      "deleted": false,
//      "createdBy": "8a2816c98f09f7b8018f09fc188718e2",
//      "modifiedBy": "8a2816c98f09f7b8018f09fc188718e2",
//      "name": "test",
//      "appCode": "test_app",
//      "departments": "[{\"fullDepartment\":\"本地测试\",\"id\":\"8a2816c98f09f7b8018f09fc188718e2\",\"name\":\"lhz\",\"unitType\":3}]",
//      "roles": null,
//      "externalUser": false,
//      "authorType": 0,
//      "dataPermissionGroups": null,
//      "sortKey": null
//    }
//  ],
//  "visibleType": 2,
//  "visiableTypeName": "部分可见"
//}

//权限组信息查询
///api/system/permission/get_group?id=8a2816c98f0a0a5e018f0a174b1f0003&appCode=
//{
//  "id": "8a2816c98f0a0a5e018f0a174b1f0003",
//  "remarks": null,
//  "createdTime": "2024-04-23 16:34:10",
//  "modifiedTime": "2024-04-23 16:34:10",
//  "deleted": false,
//  "createdBy": "8a2816c98f09f7b8018f09fc188718e2",
//  "modifiedBy": "8a2816c98f09f7b8018f09fc188718e2",
//  "name": "test",
//  "appCode": "test_app",
//  "departments": "[{\"unitType\":3,\"name\":\"lhz\",\"id\":\"8a2816c98f09f7b8018f09fc188718e2\",\"fullDepartment\":\"本地测试\"}]",
//  "roles": null,
//  "externalUser": false,
//  "authorType": 0,
//  "dataPermissionGroups": [
//    {
//      "id": null,
//      "remarks": null,
//      "createdTime": "2024-04-23 16:36:27",
//      "modifiedTime": null,
//      "deleted": false,
//      "createdBy": null,
//      "modifiedBy": null,
//      "permissionGroupId": "8a2816c98f0a0a5e018f0a174b1f0003",
//      "functionName": "测试报表1",
//      "functionCode": "test_1",
//      "schemaName": "测试报表1",
//      "schemaCode": "test_1",
//      "visible": false,
//      "creatable": true,
//      "importable": true,
//      "exportable": true,
//      "editable": true,
//      "deletable": false,
//      "printAble": false,
//      "batchPrintAble": false,
//      "batchUpdateAble": false,
//      "editOwnerAble": false,
//      "nodeType": "Report",
//      "sortKey": 1,
//      "conditions": [],
//      "dataPermissionType": 3,
//      "filterType": 1,
//      "propertityPermissionGroups": null,
//      "modelType": "LIST",
//      "attribute": null,
//      "filterTypeName": "全部",
//      "managerTypeName": "仅自己"
//    }
//  ],
//  "sortKey": null
//}

//修改具体权限组信息
///api/system/permission/update_group
//{
//  "appCode": "test_app",
//  "id": "8a2816c98f0a0a5e018f0a174b1f0003",
//  "dataPermissionGroups": [
//    {
//      "id": null,
//      "name": "测试报表1",
//      "functionCode": "test_1",
//      "schemaCode": "test_1",
//      "visible": true,
//      "filterType": 1,
//      "conditions": [],
//      "attribute": null,
//      "dataPermissionType": 3,
//      "nodeType": "Report",
//      "modelType": "LIST",
//      "propertityPermissionGroups": null,
//      "setPermission": true,
//      "bizPermType": "1",
//      "creatable": true,
//      "deletable": false,
//      "editable": true,
//      "exportable": true,
//      "importable": true,
//      "printAble": false,
//      "editOwnerAble": false,
//      "batchPrintAble": false,
//      "batchUpdateAble": false
//    }
//  ],
//  "departments": "[{\"unitType\":3,\"name\":\"lhz\",\"id\":\"8a2816c98f09f7b8018f09fc188718e2\",\"fullDepartment\":\"本地测试\"}]",
//  "name": "test",
//  "externalUser": false
//}

//修改权限组配置
///api/system/permission/update_group
//{
//  "appCode": "lhztest111",
//  "name": "测试测试",
//  "externalUser": false,
//  "departments": "[{\"id\":\"2c928e507651e1880176523ca944293f\",\"name\":\"测试组织\",\"unitType\":1}]",
//  "id": "2c928e508d110208018f0a1d4c860279",
//  "dataPermissionGroups": [
//    {
//      "id": "2c928e508d110208018f0a1d8c79028b",
//      "remarks": null,
//      "createdTime": "2024-04-23 16:41:00",
//      "modifiedTime": "2024-04-23 16:41:00",
//      "deleted": false,
//      "createdBy": null,
//      "modifiedBy": "2c928e508d110208018e73ad36fa3887",
//      "permissionGroupId": "2c928e508d110208018f0a1d4c860279",
//      "functionName": "testeaikuhfdie",
//      "functionCode": "fsERWFTGWFTGWSER",
//      "schemaName": "testeaikuhfdie",
//      "schemaCode": "fsERWFTGWFTGWSER",
//      "visible": true,
//      "creatable": true,
//      "importable": true,
//      "exportable": true,
//      "editable": true,
//      "deletable": false,
//      "printAble": false,
//      "batchPrintAble": false,
//      "batchUpdateAble": false,
//      "editOwnerAble": false,
//      "nodeType": "Report",
//      "sortKey": 1,
//      "conditions": [],
//      "dataPermissionType": 3,
//      "filterType": 1,
//      "propertityPermissionGroups": null,
//      "modelType": "LIST",
//      "attribute": null,
//      "managerTypeName": "仅自己",
//      "filterTypeName": "全部"
//    }
//  ]
//}

//{
//  "id": null,
//  "name": "test",
//  "functionCode": "test",
//  "schemaCode": "test",
//  "visible": true,
//  "filterType": 1,
//  "conditions": [],
//  "attribute": null,
//  "dataPermissionType": 3,
//  "nodeType": "Report",
//  "modelType": "LIST",
//  "propertityPermissionGroups": null,
//  "setPermission": true,
//  "bizPermType": "1",
//  "creatable": true,
//  "deletable": false,
//  "editable": true,
//  "exportable": true,
//  "importable": true,
//  "printAble": false,
//  "editOwnerAble": false,
//  "batchPrintAble": false,
//  "batchUpdateAble": false
//}

//{
//  "id": null,
//  "name": "name",
//  "functionCode": "code",
//  "schemaCode": "code",
//  "visible": true,
//  "filterType": 1,
//  "conditions": [],
//  "attribute": null,
//  "dataPermissionType": 3,
//  "nodeType": "Report",
//  "modelType": "LIST",
//  "propertityPermissionGroups": null,
//  "setPermission": true,-----------------------------------
//  "bizPermType": "1",
//  "creatable": true,
//  "deletable": false,
//  "editable": true,
//  "exportable": true,
//  "importable": true,
//  "printAble": false,
//  "editOwnerAble": false,
//  "batchPrintAble": false,
//  "batchUpdateAble": false
//}


public class test
{

}
