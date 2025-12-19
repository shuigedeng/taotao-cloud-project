package com.taotao.cloud.idea.plugin.generateDBEntity;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * DatabaseConfigDialog
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class DatabaseConfigDialog implements Configurable {

    private JPanel myMainPanel;
    private JBTextField hostField;
    private JBTextField portField;
    private JBTextField databaseField;
    private JBTextField usernameField;
    private JPasswordField passwordField;
    private JButton testConnectionButton;
    private JBTable typeMappingTable;
    private JComboBox<String> annotationOptionsComboBox;
    private JTextField baseClassField;
    private JComboBox<String> packageSelectionComboBox;
    private JCheckBox useLombokCheckBox;
    private final DatabaseSettings settings;
    private final Project project;
    private JTextArea excludeFieldsTextArea;

    private static final String[] SQL_TYPES = {
            "CHAR", "VARCHAR", "NVARCHAR", "TEXT", "NTEXT", "TINYTEXT", "MEDIUMTEXT", "LONGTEXT",
            "BINARY", "VARBINARY", "BLOB", "TINYBLOB", "MEDIUMBLOB", "LONGBLOB",
            "TINYINT", "SMALLINT", "MEDIUMINT", "INT", "INTEGER", "BIGINT",
            "FLOAT", "DOUBLE", "DECIMAL", "NUMERIC",
            "DATE", "TIME", "DATETIME", "TIMESTAMP",
            "BIT", "BOOLEAN",
    };

    private static final String[] JAVA_TYPES = {
            "String", "byte[]", "Byte", "Short", "Integer", "Long",
            "Float", "Double", "BigDecimal", "LocalDate", "LocalTime", "LocalDateTime",
            "Boolean"
    };

    private static final String[] ANNOTATION_OPTIONS = {"No annotations", "JPA annotations",
            "MyBatis-Plus annotations"};

    public DatabaseConfigDialog( Project project ) {
        this.project = project;
        settings = DatabaseSettings.getInstance(project);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        hostField = new JBTextField();
        portField = new JBTextField();
        databaseField = new JBTextField();
        usernameField = new JBTextField();
        passwordField = new JPasswordField();
        testConnectionButton = new JButton("Test Connection");
        testConnectionButton.addActionListener(e -> testConnection());

        annotationOptionsComboBox = new JComboBox<>(ANNOTATION_OPTIONS);

        baseClassField = new JTextField();
        packageSelectionComboBox = new JComboBox<>(getProjectPackages());

        useLombokCheckBox = new JCheckBox("Use Lombok annotations");

        String[] columnNames = {"SQL Type", "Java Type"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable( int row, int column ) {
                return true;
            }
        };
        typeMappingTable = new JBTable(model);

        TableColumn sqlTypeColumn = typeMappingTable.getColumnModel().getColumn(0);
        JComboBox<String> sqlTypeComboBox = new JComboBox<>(SQL_TYPES);
        sqlTypeColumn.setCellEditor(new DefaultCellEditor(sqlTypeComboBox));

        TableColumn javaTypeColumn = typeMappingTable.getColumnModel().getColumn(1);
        JComboBox<String> javaTypeComboBox = new JComboBox<>(JAVA_TYPES);
        javaTypeColumn.setCellEditor(new DefaultCellEditor(javaTypeComboBox));

        JPanel typeMappingPanel = new JPanel(new BorderLayout());
        typeMappingPanel.add(new JScrollPane(typeMappingTable), BorderLayout.CENTER);
        JButton addMappingButton = new JButton("Add Mapping");
        addMappingButton.addActionListener(e -> addNewMapping());
        typeMappingPanel.add(addMappingButton, BorderLayout.SOUTH);

        excludeFieldsTextArea = new JTextArea(5, 20);
        JScrollPane excludeFieldsScrollPane = new JScrollPane(excludeFieldsTextArea);

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Host:", hostField)
                .addLabeledComponent("Port:", portField)
                .addLabeledComponent("Database:", databaseField)
                .addLabeledComponent("Username:", usernameField)
                .addLabeledComponent("Password:", passwordField)
                .addComponent(testConnectionButton)
                .addSeparator()
                .addLabeledComponent("Annotation Option:", annotationOptionsComboBox)
                .addLabeledComponent("Base Class:", baseClassField)
                .addLabeledComponent("Package:", packageSelectionComboBox)
                .addComponent(useLombokCheckBox)
                .addSeparator()
                .addLabeledComponent("Type Mappings:", typeMappingPanel)
                .addSeparator()
                .addLabeledComponent("Exclude Common Fields (one per line):", excludeFieldsScrollPane)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();

        loadSettings();

        return myMainPanel;
    }

    private void addNewMapping() {
        DefaultTableModel model = (DefaultTableModel) typeMappingTable.getModel();
        model.addRow(new Object[]{SQL_TYPES[0], JAVA_TYPES[0]});
    }

    private void testConnection() {
        String url = String.format("jdbc:mysql://%s:%s/%s",
                hostField.getText(),
                portField.getText(),
                databaseField.getText());

        try (Connection conn = DatabaseUtil.connectToDatabase(url,
                usernameField.getText(),
                new String(passwordField.getPassword()))) {
            Messages.showInfoMessage(project, "Connection successful!", "Database Connection");
        } catch (SQLException ex) {
            Messages.showErrorDialog(project, "Connection failed: " + ex.getMessage(), "Database Connection Error");
        }
    }

    private String[] getProjectPackages() {
        Set<String> packages = new HashSet<>();
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        PsiManager psiManager = PsiManager.getInstance(project);

        for (Module module : moduleManager.getModules()) {
            ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
            for (VirtualFile sourceRoot : rootManager.getSourceRoots()) {
                PsiDirectory directory = psiManager.findDirectory(sourceRoot);
                if (directory != null) {
                    addPackagesRecursively(directory, "", packages);
                }
            }
        }

        List<String> sortedPackages = new ArrayList<>(packages);
        Collections.sort(sortedPackages);
        sortedPackages.add(0, "NO SELECT PACKAGE");
        return sortedPackages.toArray(new String[0]);
    }

    private void addPackagesRecursively( PsiDirectory directory, String parentPackage, Set<String> packages ) {
        PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(directory);
        if (psiPackage != null) {
            String packageName = psiPackage.getQualifiedName();
            if (!packageName.isEmpty()) {
                packages.add(packageName);
            }

            for (PsiDirectory subDir : directory.getSubdirectories()) {
                addPackagesRecursively(subDir, packageName, packages);
            }
        }
    }

    private void loadSettings() {
        hostField.setText(settings.getHost());
        portField.setText(settings.getPort());
        databaseField.setText(settings.getDatabase());
        usernameField.setText(settings.getUsername());
        passwordField.setText(settings.getPassword());
        annotationOptionsComboBox.setSelectedItem(settings.getAnnotationOption());
        baseClassField.setText(settings.getBaseClass());
        packageSelectionComboBox.setSelectedItem(settings.getSelectedPackage());
        useLombokCheckBox.setSelected(settings.isUseLombok());
        loadTypeMappings();
        excludeFieldsTextArea.setText(String.join("\n", settings.getExcludedFields()));
    }

    private void loadTypeMappings() {
        DefaultTableModel model = (DefaultTableModel) typeMappingTable.getModel();
        model.setRowCount(0);
        Map<String, String> mappings = settings.getTypeMappings();
        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void saveSettings() {
        settings.setHost(hostField.getText());
        settings.setPort(portField.getText());
        settings.setDatabase(databaseField.getText());
        settings.setUsername(usernameField.getText());
        settings.setPassword(new String(passwordField.getPassword()));
        settings.setAnnotationOption((String) annotationOptionsComboBox.getSelectedItem());
        settings.setBaseClass(baseClassField.getText());
        settings.setSelectedPackage((String) packageSelectionComboBox.getSelectedItem());
        settings.setUseLombok(useLombokCheckBox.isSelected());
        saveTypeMappings();
        settings.setExcludedFields(Arrays.asList(excludeFieldsTextArea.getText().split("\n")));
    }

    private void saveTypeMappings() {
        Map<String, String> mappings = new HashMap<>();
        DefaultTableModel model = (DefaultTableModel) typeMappingTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String sqlType = (String) model.getValueAt(i, 0);
            String javaType = (String) model.getValueAt(i, 1);
            if (sqlType != null && javaType != null && !sqlType.isEmpty() && !javaType.isEmpty()) {
                mappings.put(sqlType, javaType);
            }
        }
        settings.setTypeMappings(mappings);
    }

    @Override
    public boolean isModified() {
        return !hostField.getText().equals(settings.getHost()) ||
                !portField.getText().equals(settings.getPort()) ||
                !databaseField.getText().equals(settings.getDatabase()) ||
                !usernameField.getText().equals(settings.getUsername()) ||
                !new String(passwordField.getPassword()).equals(settings.getPassword()) ||
                !annotationOptionsComboBox.getSelectedItem().equals(settings.getAnnotationOption()) ||
                !baseClassField.getText().equals(settings.getBaseClass()) ||
                !packageSelectionComboBox.getSelectedItem().equals(settings.getSelectedPackage()) ||
                useLombokCheckBox.isSelected() != settings.isUseLombok() ||
                isTypeMappingsModified() ||
                isExcludedFieldsModified();
    }

    private boolean isExcludedFieldsModified() {
        List<String> currentExcludedFields = Arrays.asList(excludeFieldsTextArea.getText().split("\n"));
        return !currentExcludedFields.equals(settings.getExcludedFields());
    }

    private boolean isTypeMappingsModified() {
        Map<String, String> currentMappings = getCurrentTypeMappings();
        return !currentMappings.equals(settings.getTypeMappings());
    }

    private Map<String, String> getCurrentTypeMappings() {
        Map<String, String> currentMappings = new HashMap<>();
        DefaultTableModel model = (DefaultTableModel) typeMappingTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String sqlType = (String) model.getValueAt(i, 0);
            String javaType = (String) model.getValueAt(i, 1);
            if (sqlType != null && javaType != null && !sqlType.isEmpty() && !javaType.isEmpty()) {
                currentMappings.put(sqlType, javaType);
            }
        }
        return currentMappings;
    }

    @Override
    public void apply() throws ConfigurationException {
        saveSettings();
    }

    @Override
    public void reset() {
        loadSettings();
    }

    @Override
    public String getDisplayName() {
        return "Database Configuration";
    }
}
