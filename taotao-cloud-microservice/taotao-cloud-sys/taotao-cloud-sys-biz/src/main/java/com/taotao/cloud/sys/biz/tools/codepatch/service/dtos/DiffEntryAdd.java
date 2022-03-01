package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;

import org.eclipse.jgit.diff.DiffEntry;

public class DiffEntryAdd extends DiffEntry {
    public DiffEntryAdd(String path) {
        this.changeType = ChangeType.ADD;
        this.newPath = path;
    }
}
