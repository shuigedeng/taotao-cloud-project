package com.taotao.cloud.idea.plugin.convertClassFields;

import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.tree.*;
import java.awt.*;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.*;

/**
 * ClassChooserUtil
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ClassChooserUtil {

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final Queue<Project> projectInitQueue = new ConcurrentLinkedQueue<>();
    private static final Map<Project, SoftReference<List<PsiClass>>> projectClassCache = new ConcurrentHashMap<>();
    private static final Map<Project, ScheduledFuture<?>> refreshTasks = new ConcurrentHashMap<>();

    public static void initialize( Project project ) {
        projectInitQueue.offer(project);
        if (projectInitQueue.size() == 1) {
            scheduleNextInitialization();
        }
        setupVirtualFileListener(project);
    }

    private static void scheduleNextInitialization() {
        executorService.schedule(() -> {
            Project project = projectInitQueue.poll();
            if (project != null && !project.isDisposed()) {
                refreshProjectClassCache(project);
                scheduleNextInitialization();
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    private static void refreshProjectClassCache( Project project ) {
        DumbService.getInstance(project).runWhenSmart(() -> {
            List<PsiClass> newClasses = new ArrayList<>();
            PsiManager psiManager = PsiManager.getInstance(project);
            ProjectRootManager.getInstance(project).getFileIndex().iterateContent(fileOrDir -> {
                if (fileOrDir.isValid() && !fileOrDir.isDirectory() && JavaFileType.INSTANCE.equals(
                        fileOrDir.getFileType())) {
                    PsiFile psiFile = psiManager.findFile(fileOrDir);
                    if (psiFile instanceof PsiJavaFile) {
                        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                        newClasses.addAll(Arrays.asList(psiJavaFile.getClasses()));
                    }
                }
                return true;
            });

            cacheClasses(project, newClasses);
        });
    }

    private static void cacheClasses( Project project, List<PsiClass> classes ) {
        projectClassCache.put(project, new SoftReference<>(classes));
    }

    private static List<PsiClass> getCachedClasses( Project project ) {
        SoftReference<List<PsiClass>> ref = projectClassCache.get(project);
        return ref != null ? ref.get() : null;
    }

    private static void setupVirtualFileListener( Project project ) {
        project.getMessageBus().connect().subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after( @NotNull List<? extends VFileEvent> events ) {
                for (VFileEvent event : events) {
                    if (event instanceof VFileContentChangeEvent || event instanceof VFileCreateEvent
                            || event instanceof VFileDeleteEvent) {
                        VirtualFile file = event.getFile();
                        if (file != null && file.getFileType() instanceof JavaFileType) {
                            debounceRefresh(project);
                            break;
                        }
                    }
                }
            }
        });
    }

    private static void debounceRefresh( Project project ) {
        ScheduledFuture<?> existingTask = refreshTasks.get(project);
        if (existingTask != null) {
            existingTask.cancel(false);
        }
        ScheduledFuture<?> newTask = executorService.schedule(
                () -> refreshProjectClassCache(project),
                500, TimeUnit.MILLISECONDS
        );
        refreshTasks.put(project, newTask);
    }


    private static void ensureProjectClassesLoaded( Project project ) {
        if (getCachedClasses(project) == null) {
            refreshProjectClassCache(project);
        }
    }

    private static Map<String, List<PsiClass>> findMatchingClasses( Project project, String className,
            boolean fuzzyMatch ) {
        List<PsiClass> allClasses = getCachedClasses(project);
        if (allClasses == null) {
            return Collections.emptyMap();
        }

        Map<String, List<PsiClass>> packageToClassesMap = new TreeMap<>();

        allClasses.stream()
                .filter(psiClass -> psiClass.getName() != null && psiClass.getQualifiedName() != null)
                .filter(psiClass -> fuzzyMatch
                        ? psiClass.getName().toLowerCase().contains(className.toLowerCase())
                        : psiClass.getName().equalsIgnoreCase(className))
                .forEach(psiClass -> {
                    String packageName = psiClass.getQualifiedName()
                            .substring(0, psiClass.getQualifiedName().lastIndexOf('.'));
                    packageToClassesMap.computeIfAbsent(packageName, k -> new ArrayList<>()).add(psiClass);
                });

        return packageToClassesMap;
    }

    static class ClassChooserDialog extends DialogWrapper {

        private final String instructionText;
        private final JBTextField searchField;
        private final JTree classTree;
        private final DefaultTreeModel treeModel;
        private final Project project;
        private final JComboBox<String> matchTypeComboBox;

        protected ClassChooserDialog( @NotNull Project project, String instructionText ) {
            super(project);
            this.project = project;
            this.instructionText = instructionText;
            this.searchField = new JBTextField();
            this.treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
            this.classTree = new Tree(treeModel);
            this.matchTypeComboBox = new ComboBox<>(new String[]{"Fuzzy", "Exact"});
            ensureProjectClassesLoaded(project);
            setupSearchField();
            init();
            setTitle("Choose Class");
        }

        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(JBUI.Borders.empty(10));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.insets = JBUI.insets(5);

            setupMatchTypeComboBox(panel, gbc);
            setupSearchComponents(panel, gbc);
            setupClassTreeComponents(panel, gbc);
            setupInstructionLabel(panel, gbc);

            panel.setPreferredSize(new Dimension(600, 400));

            return panel;
        }

        private void setupMatchTypeComboBox( JPanel panel, GridBagConstraints gbc ) {
            JPanel matchTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            matchTypePanel.add(new JBLabel("Match Type:"));
            matchTypePanel.add(matchTypeComboBox);

            matchTypeComboBox.addActionListener(e -> updateClassTree());

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(matchTypePanel, gbc);
        }

        private void setupSearchComponents( JPanel panel, GridBagConstraints gbc ) {
            gbc.gridy++;
            gbc.gridwidth = 1;
            panel.add(createStyledLabel("Search:"), gbc);

            gbc.gridy++;
            panel.add(createSearchPanel(), gbc);
        }

        private void setupClassTreeComponents( JPanel panel, GridBagConstraints gbc ) {
            setupClassTree();
            gbc.gridy++;
            gbc.weighty = 1.0;
            panel.add(new JBScrollPane(classTree), gbc);
        }

        private void setupInstructionLabel( JPanel panel, GridBagConstraints gbc ) {
            gbc.gridy++;
            gbc.weighty = 0.0;
            JLabel instructionLabel = new JLabel(instructionText);
            instructionLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(instructionLabel, gbc);
        }

        private JComponent createStyledLabel( String text ) {
            JBLabel label = new JBLabel(text);
            label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
            label.setForeground(UIUtil.getLabelForeground());
            return label;
        }

        private JPanel createSearchPanel() {
            JPanel searchPanel = new JPanel(new BorderLayout());
            searchPanel.setBorder(JBUI.Borders.empty(5));
            searchPanel.add(searchField, BorderLayout.CENTER);
            searchField.putClientProperty("JTextField.Search.Gap", 0);
            searchField.putClientProperty("JTextField.Search.Icon", AllIcons.Actions.Search);
            return searchPanel;
        }

        private void setupSearchField() {
            searchField.getDocument().addDocumentListener(new DocumentAdapter() {
                @Override
                protected void textChanged( @NotNull DocumentEvent e ) {
                    updateClassTree();
                }
            });
        }

        private void setupClassTree() {
            classTree.setCellRenderer(new ClassTreeCellRenderer());
            classTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            classTree.setRootVisible(false);
            classTree.setShowsRootHandles(true);
            UIUtil.setLineStyleAngled(classTree);
        }

        private void updateClassTree() {
            String searchText = searchField.getText();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
            root.removeAllChildren();

            if (!searchText.isEmpty()) {
                boolean fuzzyMatch = matchTypeComboBox.getSelectedItem().equals("Fuzzy");
                Map<String, List<PsiClass>> packageToClassesMap = findMatchingClasses(project, searchText, fuzzyMatch);

                for (Map.Entry<String, List<PsiClass>> entry : packageToClassesMap.entrySet()) {
                    DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(entry.getKey());
                    for (PsiClass psiClass : entry.getValue()) {
                        packageNode.add(new DefaultMutableTreeNode(psiClass));
                    }
                    root.add(packageNode);
                }
            }

            treeModel.reload();

            int maxExpandedNodes = 20;
            int expandedCount = 0;
            for (int i = 0; i < root.getChildCount() && expandedCount < maxExpandedNodes; i++) {
                TreeNode node = root.getChildAt(i);
                TreePath path = new TreePath(( (DefaultMutableTreeNode) node ).getPath());
                classTree.expandPath(path);
                expandedCount++;
            }

            if (root.getChildCount() <= 5) {
                for (int i = 0; i < classTree.getRowCount(); i++) {
                    classTree.expandRow(i);
                }
            }
        }

        public PsiClass getSelectedClass() {
            TreePath selectionPath = classTree.getSelectionPath();
            if (selectionPath != null) {
                Object lastPathComponent = selectionPath.getLastPathComponent();
                if (lastPathComponent instanceof DefaultMutableTreeNode) {
                    Object userObject = ( (DefaultMutableTreeNode) lastPathComponent ).getUserObject();
                    if (userObject instanceof PsiClass) {
                        return (PsiClass) userObject;
                    }
                }
            }
            return null;
        }


        @Override
        protected void doOKAction() {
            if (getSelectedClass() == null) {
                JOptionPane.showMessageDialog(getContentPanel(),
                        "Please select a class.",
                        "Invalid Selection", JOptionPane.ERROR_MESSAGE);
                return;
            }
            super.doOKAction();
        }
    }

    private static class ClassTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus ) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                Object userObject = ( (DefaultMutableTreeNode) value ).getUserObject();
                if (userObject instanceof PsiClass) {
                    PsiClass psiClass = (PsiClass) userObject;
                    setText(psiClass.getName());
                    setIcon(AllIcons.Nodes.Class);
                } else if (userObject instanceof String) {
                    setIcon(AllIcons.Nodes.Package);
                }
            }
            return this;
        }
    }
}
