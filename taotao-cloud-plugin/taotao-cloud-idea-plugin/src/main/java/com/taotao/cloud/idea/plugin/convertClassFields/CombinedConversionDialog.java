package com.taotao.cloud.idea.plugin.convertClassFields;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CombinedConversionDialog extends DialogWrapper {
    private final Project project;
    private final PsiClass sourceClass;
    private final ClassChooserPanel targetClassChooser;
    private final JBRadioButton singleObjectButton;
    private final JBRadioButton listButton;
    private final JBTextField sourceNameField;
    private final JBTextField targetNameField;
    private final JButton fieldMappingButton;
    private Map<String, String> fieldMapping;

    public CombinedConversionDialog(Project project, String sourceVariableName, PsiClass sourceClass) {
        super(project);
        this.project = project;
        this.sourceClass = sourceClass;
        this.targetClassChooser = new ClassChooserPanel(project, "Target Class:");
        this.singleObjectButton = new JBRadioButton("Single Object");
        this.listButton = new JBRadioButton("List of Objects");
        this.sourceNameField = new JBTextField(sourceVariableName);
        this.targetNameField = new JBTextField();
        this.fieldMappingButton = new JButton("Field Mapping");
        this.fieldMapping = new HashMap<>();

        // 添加目标类选择监听器，当选择新的目标类时自动初始化字段映射
        targetClassChooser.addTargetClassChangeListener(this::initializeFieldMapping);

        setTitle("Class Conversion Configuration");
        init();
    }

    private void initializeFieldMapping() {
        PsiClass targetClass = getTargetClass();
        if (targetClass == null) return;

        fieldMapping.clear();
        String[] sourceFieldNames = Arrays.stream(sourceClass.getFields())
                                         .map(PsiField::getName)
                                         .toArray(String[]::new);

        for (PsiField targetField : targetClass.getFields()) {
            String targetFieldName = targetField.getName();
            // 如果源类中存在同名字段，自动映射
            if (Arrays.asList(sourceFieldNames).contains(targetFieldName)) {
                fieldMapping.put(targetFieldName, targetFieldName);
            }
        }
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = JBUI.insets(5);

        // Source class display
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(new JBLabel("Source Class:"), gbc);

        gbc.gridx = 1;
        JBTextField sourceClassField = new JBTextField(sourceClass.getQualifiedName());
        sourceClassField.setEditable(false);
        panel.add(sourceClassField, gbc);

        // Target class chooser
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(targetClassChooser, gbc);

        // Conversion type
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JBLabel("Conversion Type:"), gbc);

        JPanel conversionTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup group = new ButtonGroup();
        group.add(singleObjectButton);
        group.add(listButton);
        singleObjectButton.setSelected(true);
        
        // Add listeners for radio buttons
        singleObjectButton.addActionListener(e -> updateTargetNameSuffix());
        listButton.addActionListener(e -> updateTargetNameSuffix());
        
        conversionTypePanel.add(singleObjectButton);
        conversionTypePanel.add(listButton);

        gbc.gridx = 1;
        panel.add(conversionTypePanel, gbc);

        // Variable names
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JBLabel("Source Variable Name:"), gbc);

        gbc.gridx = 1;
        panel.add(sourceNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JBLabel("Target Variable Name:"), gbc);

        gbc.gridx = 1;
        panel.add(targetNameField, gbc);

        // Add Field Mapping button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        fieldMappingButton.addActionListener(e -> showFieldMappingDialog());
        panel.add(fieldMappingButton, gbc);

        return panel;
    }

    private void updateTargetNameSuffix() {
        String currentName = targetNameField.getText();
        if (currentName.isEmpty()) return;
        
        String baseName = currentName;
        // Remove "List" suffix if present
        if (baseName.endsWith("List")) {
            baseName = baseName.substring(0, baseName.length() - 4);
        }
        
        // Add or remove "List" suffix based on selection
        if (listButton.isSelected()) {
            targetNameField.setText(baseName + "List");
        } else {
            targetNameField.setText(baseName);
        }
    }

    private void showFieldMappingDialog() {
        PsiClass targetClass = getTargetClass();
        if (targetClass == null) {
            JOptionPane.showMessageDialog(getContentPanel(),
                "Please select a target class first.",
                "Target Class Not Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FieldMappingDialog dialog = new FieldMappingDialog(project, sourceClass, targetClass, fieldMapping);
        if (dialog.showAndGet()) {
            fieldMapping = dialog.getFieldMapping();
        }
    }


    public PsiClass getTargetClass() {
        return targetClassChooser.getSelectedClass();
    }

    public boolean isList() {
        return listButton.isSelected();
    }

    public String getSourceName() {
        return sourceNameField.getText();
    }

    public String getTargetName() {
        return targetNameField.getText();
    }

    public Map<String, String> getFieldMapping() {
        return fieldMapping;
    }

    @Override
    protected void doOKAction() {
        if (getTargetClass() == null) {
            JOptionPane.showMessageDialog(getContentPanel(),
                "Please select a target class.",
                "Invalid Selection", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (getTargetName().isEmpty()) {
            JOptionPane.showMessageDialog(getContentPanel(),
                "Please enter a target variable name.",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        super.doOKAction();
    }

    private class ClassChooserPanel extends JPanel {
        private final Project project;
        private ClassChooserUtil.ClassChooserDialog classChooser;
        private final JBTextField selectedClassField;
        private final String dialogTitle;
        private Runnable targetClassChangeListener;

        public ClassChooserPanel(Project project, String labelText) {
            super(new BorderLayout());
            this.project = project;
            this.dialogTitle = "";
            this.selectedClassField = new JBTextField();
            selectedClassField.setEditable(false);

            JButton chooseButton = new JButton("Choose");
            chooseButton.addActionListener(e -> showClassChooserDialog());

            JPanel selectionPanel = new JPanel(new BorderLayout());
            selectionPanel.add(selectedClassField, BorderLayout.CENTER);
            selectionPanel.add(chooseButton, BorderLayout.EAST);

            add(new JBLabel(labelText), BorderLayout.WEST);
            add(selectionPanel, BorderLayout.CENTER);
        }

        public void addTargetClassChangeListener(Runnable listener) {
            this.targetClassChangeListener = listener;
        }

        private void showClassChooserDialog() {
            classChooser = new ClassChooserUtil.ClassChooserDialog(project, dialogTitle) {
                @Override
                protected void doOKAction() {
                    super.doOKAction();
                    updateSelectedClassField();
                    if (targetClassChangeListener != null) {
                        targetClassChangeListener.run();
                    }
                }
            };
            
            if (classChooser.showAndGet()) {
                updateSelectedClassField();
                if (targetClassChangeListener != null) {
                    targetClassChangeListener.run();
                }
            }
        }

        public PsiClass getSelectedClass() {
            return classChooser != null ? classChooser.getSelectedClass() : null;
        }

        private void updateSelectedClassField() {
            PsiClass selectedClass = getSelectedClass();
            if (selectedClass != null) {
                selectedClassField.setText(selectedClass.getQualifiedName());
                updateTargetNameField();
            }
        }

        private void updateTargetNameField() {
            PsiClass selectedClass = getSelectedClass();
            if (selectedClass != null) {
                String className = selectedClass.getName();
                if (className != null && !className.isEmpty()) {
                    String targetName = Character.toLowerCase(className.charAt(0)) + className.substring(1);
                    if (listButton.isSelected()) {
                        targetName += "List";
                    }
                    targetNameField.setText(targetName);
                }
            }
        }
    }

    private class FieldMappingDialog extends DialogWrapper {
        private final PsiClass sourceClass;
        private final PsiClass targetClass;
        private final Map<String, JComboBox<String>> mappingFields;
        private final Map<String, JBCheckBox> fieldCheckboxes;
        private final Map<String, String> fieldMapping;

        public FieldMappingDialog(Project project, PsiClass sourceClass, PsiClass targetClass, Map<String, String> initialMapping) {
            super(project);
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
            this.mappingFields = new HashMap<>();
            this.fieldCheckboxes = new HashMap<>();
            this.fieldMapping = new HashMap<>(initialMapping);

            setTitle("Field Mapping Configuration");
            init();
        }

        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = JBUI.insets(5);

            // Headers
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JBLabel("Include"), gbc);

            gbc.gridx = 1;
            panel.add(new JBLabel("Target Field"), gbc);

            gbc.gridx = 2;
            panel.add(new JBLabel("Source Field"), gbc);

            String[] sourceFieldNames = Arrays.stream(sourceClass.getFields())
                                             .map(PsiField::getName)
                                             .toArray(String[]::new);

            for (PsiField targetField : targetClass.getFields()) {
                String targetFieldName = targetField.getName();
                gbc.gridx = 0;
                gbc.gridy++;

                JBCheckBox checkBox = new JBCheckBox();
                boolean shouldBeSelected = fieldMapping.containsKey(targetFieldName);
                checkBox.setSelected(shouldBeSelected);
                fieldCheckboxes.put(targetFieldName, checkBox);
                panel.add(checkBox, gbc);

                gbc.gridx = 1;
                panel.add(new JBLabel(targetFieldName), gbc);

                gbc.gridx = 2;
                JComboBox<String> sourceFieldCombo = new JComboBox<>(sourceFieldNames);
                if (fieldMapping.containsKey(targetFieldName)) {
                    sourceFieldCombo.setSelectedItem(fieldMapping.get(targetFieldName));
                }
                mappingFields.put(targetFieldName, sourceFieldCombo);
                panel.add(sourceFieldCombo, gbc);
            }

            return panel;
        }

        @Override
        protected void doOKAction() {
            fieldMapping.clear();  // Clear existing mappings
            for (Map.Entry<String, JComboBox<String>> entry : mappingFields.entrySet()) {
                String targetField = entry.getKey();
                JBCheckBox checkBox = fieldCheckboxes.get(targetField);
                if (checkBox.isSelected()) {
                    String sourceField = (String) entry.getValue().getSelectedItem();
                    if (sourceField != null && !sourceField.isEmpty()) {
                        fieldMapping.put(targetField, sourceField);
                    }
                }
            }
            super.doOKAction();
        }

        public Map<String, String> getFieldMapping() {
            return fieldMapping;
        }
    }
}
