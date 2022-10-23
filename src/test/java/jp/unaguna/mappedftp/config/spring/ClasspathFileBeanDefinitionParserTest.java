package jp.unaguna.mappedftp.config.spring;

import jp.unaguna.mappedftp.TestUtils;
import jp.unaguna.mappedftp.UserStub;
import jp.unaguna.mappedftp.filesystem.LinkedFileSystemView;
import jp.unaguna.mappedftp.filesystem.MappingFileSystemFactory;
import jp.unaguna.mappedftp.filesystem.tree.FileTreeItemFromClasspath;
import jp.unaguna.mappedftp.filesystem.tree.FileTreeNode;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClasspathFileBeanDefinitionParserTest {
    @Test
    public void testParse(TestInfo testInfo) {
        final URL configPath = TestUtils.getInputResource("config.xml", testInfo);

        // TODO: できれば Parser を直接テストして、他のタグやファイルシステムクラスの仕様変更の影響を受けないようにしたい
        final FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(configPath.toString());
        final DefaultFtpServer actualServer = (DefaultFtpServer) ctx.getBean("testServer");
        final MappingFileSystemFactory fileSystemFactory = (MappingFileSystemFactory) actualServer.getFileSystem();

        try {
            final LinkedFileSystemView fileSystemView = fileSystemFactory.createFileSystemView(new UserStub());
            final FileTreeNode fileTreeNode = (FileTreeNode) fileSystemView.getFile("/file1");
            final FileTreeItemFromClasspath file = (FileTreeItemFromClasspath) fileTreeNode.getFile();
            assertEquals("dummy.txt", file.getSource());
            assertNull(file.getOwnerName());
            assertNull(file.getGroupName());

        } catch (FtpException e) {
            fail(e);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "config__owner.xml, test_owner, ",
            "config__group.xml, , test_group",
            "config__owner_and_group.xml, test_owner, test_group",
    })
    public void testParse__with_owner_and_group(
            String inputResourceName, String expectedOwnerName, String expectedGroupName, TestInfo testInfo
    ) {
        final URL configPath = TestUtils.getInputResource(inputResourceName, testInfo);

        final FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(configPath.toString());
        final DefaultFtpServer actualServer = (DefaultFtpServer) ctx.getBean("testServer");
        final MappingFileSystemFactory fileSystemFactory = (MappingFileSystemFactory) actualServer.getFileSystem();

        try {
            final LinkedFileSystemView fileSystemView = fileSystemFactory.createFileSystemView(new UserStub());
            final FileTreeNode fileTreeNode = (FileTreeNode) fileSystemView.getFile("/file1");
            final FileTreeItemFromClasspath file = (FileTreeItemFromClasspath) fileTreeNode.getFile();
            assertEquals("dummy.txt", file.getSource());
            assertEquals(expectedOwnerName, file.getOwnerName());
            assertEquals(expectedGroupName, file.getGroupName());

        } catch (FtpException e) {
            fail(e);
        }
    }
}
