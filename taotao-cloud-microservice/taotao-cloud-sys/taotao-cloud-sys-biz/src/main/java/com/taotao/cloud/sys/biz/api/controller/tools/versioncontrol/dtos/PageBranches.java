package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos.Branches;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageBranches {
    private List<Branches.Branch> branches = new ArrayList<>();
    private int total;
    private String currentBranchName;

    public PageBranches() {
    }

    public PageBranches(List<Branches.Branch> branches, int total, String currentBranchName) {
        this.branches = branches;
        this.total = total;
        this.currentBranchName = currentBranchName;
    }
}
