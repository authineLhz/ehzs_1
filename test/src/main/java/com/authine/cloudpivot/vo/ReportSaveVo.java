package com.authine.cloudpivot.vo;

import lombok.Data;

import java.util.List;

@Data
public class ReportSaveVo {
    private String creatorAccount;
    private String reportName;
    private String id;
    private List<String> usersAccount;
}
