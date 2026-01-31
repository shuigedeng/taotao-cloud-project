package com.taotao.cloud.maven.plugin;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * DummyMojo
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Mojo(name = "test-dummy")
public class DummyMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Hello World.
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}")
    private File outputDirectory;
}
