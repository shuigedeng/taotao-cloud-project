package com.taotao.cloud.sys.biz.tools.core.service.classloader;

import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PomFileParser {
    private FileManager fileManager;

    /**
     * 本地仓库地址 /$dataDir/localRepository
     */
    private final File localRepositoryDir;

    /**
     * 远程仓库列表, 可添加公司仓库列表
     */
    @Autowired
    private RemoteRepositoryConfig remoteRepositoryConfig;

    private MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();

    private RepositorySystem repositorySystem;

    private LocalRepository localRepository;

    @Autowired
    public PomFileParser(FileManager fileManager) {
        this.fileManager = fileManager;

        localRepositoryDir = fileManager.mkDataDir("localRepository");
        localRepository = new LocalRepository(localRepositoryDir);

        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        repositorySystem = locator.getService(RepositorySystem.class);
    }

    /**
     * 加载所有依赖项
     * @param pomFile pom.xml
     * @return
     */
    public List<File> loadDependencies(File pomFile) throws IOException, XmlPullParserException, DependencyCollectionException, DependencyResolutionException {
        try(final InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(pomFile), StandardCharsets.UTF_8)) {
            final Model model = mavenXpp3Reader.read(inputStreamReader);
            MavenProject project = new MavenProject(model);

            // 获取 repositorys
            List<RemoteRepository> remoteRepositories = new ArrayList<>();
            for (int i = 0; i < remoteRepositoryConfig.getRepositories().size(); i++) {
                final RemoteRepositoryConfig.RemoteRepository repository = remoteRepositoryConfig.getRepositories().get(i);
                final RemoteRepository remoteRepository = new RemoteRepository.Builder(repository.getId(), repository.getType(), repository.getUrl())
                        .build();
                remoteRepositories.add(remoteRepository);
            }

            // 生成 session
            DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
            session.setConfigProperty(ConflictResolver.CONFIG_PROP_VERBOSE, true);
            session.setConfigProperty(DependencyManagerUtils.CONFIG_PROP_VERBOSE, true);
            session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(session, localRepository));

            // 获取所有依赖项 jar 包
            final List<Dependency> dependencies = project.getDependencies();

            Set<File> jarFiles = new HashSet<>();
            for (Dependency dependency : dependencies) {
                final DefaultArtifact defaultArtifact = new DefaultArtifact(dependency.getGroupId() + ":" + dependency.getArtifactId() + ":" + dependency.getVersion());
                org.eclipse.aether.graph.Dependency root = new org.eclipse.aether.graph.Dependency(defaultArtifact, null);

                CollectRequest collectRequest = new CollectRequest();
                collectRequest.setRoot(root);
                collectRequest.setRepositories(remoteRepositories);
                DependencyNode node = repositorySystem.collectDependencies(session, collectRequest).getRoot();
                DependencyRequest dependencyRequest = new DependencyRequest();
                dependencyRequest.setRoot(node);

                repositorySystem.resolveDependencies(session, dependencyRequest);

                PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
                node.accept(nlg);

                jarFiles.addAll(nlg.getFiles());
            }

            return new ArrayList<>(jarFiles);
        }

    }
}
