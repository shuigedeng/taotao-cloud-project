package com.taotao.cloud.idea.plugin.generateDBEntity;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.webSymbols.utils.NameCaseUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * GenerateEntityAction
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class GenerateEntityAction extends AnAction {

    private static final Map<String, String> DEFAULT_TYPE_MAPPINGS = initDefaultTypeMappings();

    @Override
    public void actionPerformed( AnActionEvent e ) {
        Project project = e.getProject();
        if (project == null)
            return;

        DatabaseSettings settings = DatabaseSettings.getInstance(project);
        String url = String.format("jdbc:mysql://%s:%s/%s", settings.getHost(), settings.getPort(),
                settings.getDatabase());

        try (Connection conn = DatabaseUtil.connectToDatabase(url, settings.getUsername(), settings.getPassword())) {
            String selectedTable = selectTable(conn, settings);
            if (selectedTable != null) {
                String className = NameCaseUtils.toCamelCase(selectedTable);
                className = Character.toUpperCase(className.charAt(0)) + className.substring(1);

                StringBuilder classCode = generateEntityClass(conn.getMetaData(), selectedTable, className, settings);
                createAndOpenFile(project, settings, className, classCode);
            }
        } catch (SQLException ex) {
            Messages.showErrorDialog(project, "Failed to connect to database: " + ex.getMessage(), "Database Error");
        }
    }

    private String selectTable( Connection conn, DatabaseSettings settings ) throws SQLException {
        List<String> tableNames = getTableNames(conn, settings.getDatabase());
        return Messages.showEditableChooseDialog(
                "Select a table to generate entity",
                "Generate Entity",
                Messages.getQuestionIcon(),
                tableNames.toArray(new String[0]),
                tableNames.get(0),
                null
        );
    }

    private List<String> getTableNames( Connection conn, String database ) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        try (ResultSet tablesRs = conn.getMetaData().getTables(database, null, "%", new String[]{"TABLE"})) {
            while (tablesRs.next()) {
                tableNames.add(tablesRs.getString("TABLE_NAME"));
            }
        }
        return tableNames;
    }

    private void createAndOpenFile( Project project, DatabaseSettings settings, String className,
            StringBuilder classCode ) {
        PsiPackage selectedPackage = getSelectedPackage(project, settings);
        if (selectedPackage != null) {
            PsiDirectory targetDirectory = selectedPackage.getDirectories()[0];
            WriteCommandAction.runWriteCommandAction(project, () -> {
                PsiFile psiFile = PsiFileFactory.getInstance(project)
                        .createFileFromText(className + ".java", JavaFileType.INSTANCE, classCode.toString());
                PsiFile addedFile = (PsiFile) targetDirectory.add(psiFile);
                openFileInEditor(project, addedFile);
            });
        } else {
            Messages.showErrorDialog(project, "No package selected. Cannot generate entity.",
                    "Package Selection Error");
        }
    }

    private PsiPackage getSelectedPackage( Project project, DatabaseSettings settings ) {
        if (settings.getSelectedPackage() != null && !settings.getSelectedPackage().isEmpty()
                && !"NO SELECT PACKAGE".equals(settings.getSelectedPackage())) {
            return JavaPsiFacade.getInstance(project).findPackage(settings.getSelectedPackage());
        } else {
            PackageChooserDialog packageChooser = new PackageChooserDialog("Select Target Package", project);
            packageChooser.show();
            return packageChooser.getSelectedPackage();
        }
    }

    private void openFileInEditor( Project project, PsiFile file ) {
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openTextEditor(
                    new OpenFileDescriptor(project, virtualFile), true
            );
        }
    }

    private StringBuilder generateEntityClass( DatabaseMetaData metaData, String tableName, String className,
            DatabaseSettings settings ) throws SQLException {
        StringBuilder classCode = new StringBuilder();
        addImports(classCode, settings);
        addClassLevelAnnotations(classCode, tableName, settings);
        classCode.append("public class ").append(className);
        addBaseClass(classCode, settings);
        classCode.append(" implements Serializable {\n\n");

        List<FieldInfo> fields = generateFields(metaData, tableName, classCode, settings);

        if (!settings.isUseLombok()) {
            generateGettersAndSetters(classCode, fields);
        }

        classCode.append("}\n");
        return classCode;
    }

    private void addImports( StringBuilder classCode, DatabaseSettings settings ) {
        classCode.append("import java.io.Serializable;\n");
        if (settings.isUseLombok()) {
            classCode.append("import lombok.Data;\n");
        }
        if ("JPA annotations".equals(settings.getAnnotationOption())) {
            classCode.append("import javax.persistence.*;\n");
        } else if ("MyBatis-Plus annotations".equals(settings.getAnnotationOption())) {
            classCode.append("import com.baomidou.mybatisplus.annotation.*;\n");
        }
        classCode.append("\n");
    }

    private void addClassLevelAnnotations( StringBuilder classCode, String tableName, DatabaseSettings settings ) {
        if (settings.isUseLombok()) {
            classCode.append("@Data\n");
        }
        if ("JPA annotations".equals(settings.getAnnotationOption())) {
            classCode.append("@Entity\n");
            classCode.append("@Table(name = \"").append(tableName).append("\")\n");
        } else if ("MyBatis-Plus annotations".equals(settings.getAnnotationOption())) {
            classCode.append("@TableName(\"").append(tableName).append("\")\n");
        }
    }

    private void addBaseClass( StringBuilder classCode, DatabaseSettings settings ) {
        if (!settings.getBaseClass().isEmpty()) {
            classCode.append(" extends ").append(settings.getBaseClass());
        }
    }

    private List<FieldInfo> generateFields( DatabaseMetaData metaData, String tableName, StringBuilder classCode,
            DatabaseSettings settings ) throws SQLException {
        List<FieldInfo> fields = new ArrayList<>();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String fieldName = NameCaseUtils.toCamelCase(columnName);

                if (settings.getExcludedFields().contains(fieldName)) {
                    continue;
                }

                String dataType = columns.getString("TYPE_NAME");
                String javaType = mapSqlTypeToJavaType(dataType, settings);
                String remarks = columns.getString("REMARKS");
                boolean isAutoIncrement = columns.getString("IS_AUTOINCREMENT").equals("YES");

                addFieldComment(classCode, remarks);
                addFieldAnnotations(classCode, columnName, isAutoIncrement, settings);

                classCode.append("    private ").append(javaType).append(" ").append(fieldName).append(";\n\n");
                fields.add(new FieldInfo(fieldName, javaType));
            }
        }
        return fields;
    }

    private void addFieldComment( StringBuilder classCode, String remarks ) {
        if (remarks != null && !remarks.isEmpty()) {
            classCode.append("    /**\n");
            classCode.append("     * ").append(remarks).append("\n");
            classCode.append("     */\n");
        }
    }

    private void addFieldAnnotations( StringBuilder classCode, String columnName, boolean isAutoIncrement,
            DatabaseSettings settings ) {
        if ("JPA annotations".equals(settings.getAnnotationOption())) {
            classCode.append("    @Column(name = \"").append(columnName).append("\")\n");
            if (isAutoIncrement) {
                classCode.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                classCode.append("    @Id\n");
            }
        } else if ("MyBatis-Plus annotations".equals(settings.getAnnotationOption())) {
            classCode.append("    @TableField(\"").append(columnName).append("\")\n");
            if (isAutoIncrement) {
                classCode.append("    @TableId(type = IdType.AUTO)\n");
            }
        }
    }

    private String mapSqlTypeToJavaType( String sqlType, DatabaseSettings settings ) {
        Map<String, String> userTypeMappings = settings.getTypeMappings();
        String upperCaseSqlType = sqlType.toUpperCase();
        return userTypeMappings.getOrDefault(upperCaseSqlType,
                DEFAULT_TYPE_MAPPINGS.getOrDefault(upperCaseSqlType, "Object"));
    }

    private void generateGettersAndSetters( StringBuilder classCode, List<FieldInfo> fields ) {
        for (FieldInfo field : fields) {
            String capitalizedName = capitalize(field.name);
            // Getter
            classCode.append("    public ").append(field.type).append(" get").append(capitalizedName).append("() {\n");
            classCode.append("        return ").append(field.name).append(";\n");
            classCode.append("    }\n\n");
            // Setter
            classCode.append("    public void set").append(capitalizedName).append("(").append(field.type).append(" ")
                    .append(field.name).append(") {\n");
            classCode.append("        this.").append(field.name).append(" = ").append(field.name).append(";\n");
            classCode.append("    }\n\n");
        }
    }

    private String capitalize( String str ) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static Map<String, String> initDefaultTypeMappings() {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("CHAR", "String");
        mappings.put("VARCHAR", "String");
        mappings.put("LONGVARCHAR", "String");
        mappings.put("NUMERIC", "BigDecimal");
        mappings.put("DECIMAL", "BigDecimal");
        mappings.put("BIT", "Boolean");
        mappings.put("BOOLEAN", "Boolean");
        mappings.put("TINYINT", "Byte");
        mappings.put("SMALLINT", "Short");
        mappings.put("INTEGER", "Integer");
        mappings.put("INT", "Integer");
        mappings.put("BIGINT", "Long");
        mappings.put("REAL", "Float");
        mappings.put("FLOAT", "Double");
        mappings.put("DOUBLE", "Double");
        mappings.put("BINARY", "byte[]");
        mappings.put("VARBINARY", "byte[]");
        mappings.put("LONGVARBINARY", "byte[]");
        mappings.put("DATE", "Date");
        mappings.put("TIME", "Time");
        mappings.put("TIMESTAMP", "Date");
        mappings.put("DATETIME", "Date");
        mappings.put("CLOB", "Clob");
        mappings.put("BLOB", "Blob");
        mappings.put("ARRAY", "List");
        return mappings;
    }


}
