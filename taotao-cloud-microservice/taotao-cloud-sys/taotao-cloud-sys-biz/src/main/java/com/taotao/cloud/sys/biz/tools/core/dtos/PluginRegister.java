package com.taotao.cloud.sys.biz.tools.core.dtos;


import com.taotao.cloud.sys.biz.tools.core.utils.Version;
import java.util.ArrayList;
import java.util.List;

public class PluginRegister {
    private String id;
    private String name;
    private String author;
    private String desc;
    private List<String> dependencies = new ArrayList<>();
    private String help;
    private Version version;

    public PluginRegister() {
    }

    public PluginRegister(String id) {
        this.id = id;
    }

    public String getVersionString(){
        return version.toString();
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
}
