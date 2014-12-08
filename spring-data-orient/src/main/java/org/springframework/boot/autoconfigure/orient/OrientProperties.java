package org.springframework.boot.autoconfigure.orient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.orm.orient.AbstractOrientDatabaseFactory;

@ConfigurationProperties(prefix = "spring.data.orient")
public class OrientProperties {

    private String url;
    
    private String username;
    
    private String password;
    
    private int minPoolSize = AbstractOrientDatabaseFactory.DEFAULT_MIN_POOL_SIZE;
    
    private int maxPoolSize = AbstractOrientDatabaseFactory.DEFAULT_MAX_POOL_SIZE;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

}
