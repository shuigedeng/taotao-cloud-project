package com.taotao.cloud.sys.biz.modules.versioncontrol.dtos;

import com.sanri.tools.modules.versioncontrol.git.dtos.Branches;
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
