package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;


import java.util.ArrayList;
import java.util.List;

public class Branchs {
    private List<Branch> branches = new ArrayList<>();
    private String currentBranchName;


    public Branchs() {
    }

    public Branchs(List<Branch> branches, String currentBranchName) {
        this.branches = branches;
        this.currentBranchName = currentBranchName;
    }

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

	    public boolean isLocal() {
		    return local;
	    }

	    public void setLocal(boolean local) {
		    this.local = local;
	    }

	    public String getName() {
		    return name;
	    }

	    public void setName(String name) {
		    this.name = name;
	    }

	    public String getObjectId() {
		    return objectId;
	    }

	    public void setObjectId(String objectId) {
		    this.objectId = objectId;
	    }

	    public String getBranchName() {
		    return branchName;
	    }

	    public void setBranchName(String branchName) {
		    this.branchName = branchName;
	    }
    }
}
