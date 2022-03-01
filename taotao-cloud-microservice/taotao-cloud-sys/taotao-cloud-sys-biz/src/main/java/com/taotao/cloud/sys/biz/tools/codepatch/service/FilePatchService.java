package com.taotao.cloud.sys.biz.tools.codepatch.service;

import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 从编译后目录中提取文件
 */
@Service
public class FilePatchService {

    public void loadClassFiles(String compilePath, List<DiffEntry> diffEntries){

    }
}
