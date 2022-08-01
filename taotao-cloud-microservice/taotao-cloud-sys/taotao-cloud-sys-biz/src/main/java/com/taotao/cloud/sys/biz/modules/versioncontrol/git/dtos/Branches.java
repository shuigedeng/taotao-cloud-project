package com.taotao.cloud.sys.biz.modules.versioncontrol.git.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Branches {
    private List<Branch> branches = new ArrayList<>();
    private String currentBranchName;


    public Branches() {
    }

    public Branches(List<Branch> branches, String currentBranchName) {
        this.branches = branches;
        this.currentBranchName = currentBranchName;
    }

    @Data
    public static final class Branch {
        private boolean local;
        private String name;
        private String objectId;
        private String branchName;

        public Branch() {
        }

        public Branch(String name,String objectId) {
            this.name = name;
            this.objectId = objectId;

            if (name.contains("refs/heads")){
                local = true;
                this.branchName = name.substring("refs/heads/".length());
            }else{
                this.branchName = name.substring("refs/remotes/".length());
            }
        }

    }
}
