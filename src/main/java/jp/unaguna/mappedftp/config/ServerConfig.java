package jp.unaguna.mappedftp.config;

import jp.unaguna.mappedftp.encrypt.PasswordEncryptorType;
import jp.unaguna.mappedftp.filesystem.ConfigurableFileSystemFactory;
import jp.unaguna.mappedftp.map.AttributeHashMap;
import jp.unaguna.mappedftp.user.ConfigurableUserManagerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A configuration of servers.
 * <p>
 *     This instance is usually created by loading an XML file using {@link ServerConfigLoader#load(java.nio.file.Path)}.
 *     Although they can be dynamically created or modified,
 *     be aware that doing so after the server has started may cause the server to behave in an unexpected manner.
 * </p>
 */
public class ServerConfig {
    private final List<AttributeHashMap> files = new ArrayList<>();
    private String userPropertiesPath = null;
    private PasswordEncryptorType encryptPasswords = null;
    private Class<? extends ConfigurableFileSystemFactory> fileSystemFactoryClass = null;
    private Class<? extends ConfigurableUserManagerFactory> userManagerFactoryClass = null;

    public Class<? extends ConfigurableFileSystemFactory> getFileSystemFactoryClass() {
        return fileSystemFactoryClass;
    }

    public void setFileSystemFactoryClass(
            Class<? extends ConfigurableFileSystemFactory> fileSystemFactoryClass
    ) {
        this.fileSystemFactoryClass = fileSystemFactoryClass;
    }

    public Class<? extends ConfigurableUserManagerFactory> getUserManagerFactoryClass() {
        return userManagerFactoryClass;
    }

    public void setUserManagerFactoryClass(Class<? extends ConfigurableUserManagerFactory> userManagerFactoryClass) {
        this.userManagerFactoryClass = userManagerFactoryClass;
    }

    public String getUserPropertiesPath() {
        return userPropertiesPath;
    }

    public void setUserPropertiesPath(String userPropertiesPath) {
        this.userPropertiesPath = userPropertiesPath;
    }

    public PasswordEncryptorType getEncryptPasswords() {
        return encryptPasswords;
    }

    public void setEncryptPasswords(PasswordEncryptorType encryptPasswords) {
        this.encryptPasswords = encryptPasswords;
    }

    /**
     * Add attributes of a file to serve on the server
     *
     * <p>
     *     This method is intended to be used when loading configuration files.
     *     Can also be used to dynamically edit settings, but see notes at {@link ServerConfig}.
     * </p>
     *
     * @param attributes attributes of a file to serve on the server
     */
    public void putFile(AttributeHashMap attributes) {
        files.add(attributes);
    }

    /**
     * Returns the iterator of attributes of files to serve on the server
     *
     * <p>
     *     This function can be used to retrieve a list of files that have been set into this configuration.
     *     Intuitively, you might expect to be able to get an instance of List,
     *     but it provides an iterator to prohibit modification of the list.
     * </p>
     * <p>
     *     You cannot directly manipulate instances of the list for editing,
     *     but you can add elements with {@link #putFile(AttributeHashMap)}.
     * </p>
     *
     * @return the iterator of attributes of files to serve on the server
     */
    public Iterator<AttributeHashMap> getFilesIterator() {
        return files.iterator();
    }
}
