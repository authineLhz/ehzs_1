package com.authine.cloudpivot.service;

import java.util.List;

public interface PermissionService {

    void addPermission(String creator, List<String> users, String eId);

}
