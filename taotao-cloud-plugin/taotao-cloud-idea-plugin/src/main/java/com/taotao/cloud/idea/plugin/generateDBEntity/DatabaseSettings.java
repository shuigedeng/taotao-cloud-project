package com.taotao.cloud.idea.plugin.generateDBEntity;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DatabaseSettings
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@State(
        name = "DatabaseSettings",
        storages = {@Storage("databaseSettings.xml")}
)
public class DatabaseSettings implements PersistentStateComponent<DatabaseSettings> {

    private String host = "localhost";
    private String port = "3306";
    private String database = "";
    private String username = "";
    private String password = "";
    private Map<String, String> typeMappings = new HashMap<>();
    private String annotationOption = "No annotations";
    private String baseClass = "";
    private boolean useLombok = false;
    private List<String> excludedFields = new ArrayList<>();
    private String selectedPackage = ""; // New field for storing the selected package

    public static DatabaseSettings getInstance( Project project ) {
        return ServiceManager.getService(project, DatabaseSettings.class);
    }

    @Nullable
    @Override
    public DatabaseSettings getState() {
        return this;
    }

    @Override
    public void loadState( @NotNull DatabaseSettings state ) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // Getters and setters for all fields
    public String getHost() {
        return host;
    }

    public void setHost( String host ) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort( String port ) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase( String database ) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public Map<String, String> getTypeMappings() {
        return typeMappings;
    }

    public void setTypeMappings( Map<String, String> typeMappings ) {
        this.typeMappings = typeMappings;
    }

    public String getAnnotationOption() {
        return annotationOption;
    }

    public void setAnnotationOption( String annotationOption ) {
        this.annotationOption = annotationOption;
    }

    public String getBaseClass() {
        return baseClass;
    }

    public void setBaseClass( String baseClass ) {
        this.baseClass = baseClass;
    }

    public boolean isUseLombok() {
        return useLombok;
    }

    public void setUseLombok( boolean useLombok ) {
        this.useLombok = useLombok;
    }

    public List<String> getExcludedFields() {
        return excludedFields;
    }

    public void setExcludedFields( List<String> excludedFields ) {
        this.excludedFields = excludedFields;
    }

    // New getter and setter for the selected package
    public String getSelectedPackage() {
        return selectedPackage;
    }

    public void setSelectedPackage( String selectedPackage ) {
        this.selectedPackage = selectedPackage;
    }
}
