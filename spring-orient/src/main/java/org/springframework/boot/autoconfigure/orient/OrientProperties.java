package org.springframework.boot.autoconfigure.orient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.orientdb.session.impl.DatabaseConfiguration;

@ConfigurationProperties(prefix = "spring.data.orient")
public class OrientProperties {

	private String	url;

	private String	username;

	private String	password;

	private boolean	autoCreateDatabase	              = true;

	private boolean	autoInitializeCurrenThreadSession	= true;

	private int	    minPoolSize	                      = DatabaseConfiguration.DEFAULT_MIN_POOL_SIZE;

	private int	    maxPoolSize	                      = DatabaseConfiguration.DEFAULT_MAX_POOL_SIZE;

	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	public int getMinPoolSize() {
		return this.minPoolSize;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUrl() {
		return this.url;
	}

	public String getUsername() {
		return this.username;
	}

	public boolean isAutoCreateDatabase() {
		return this.autoCreateDatabase;
	}

	public boolean isAutoInitializeCurrenThreadSession() {
		return this.autoInitializeCurrenThreadSession;
	}

	public void setAutoCreateDatabase(final boolean _autoCreateDatabase) {
		this.autoCreateDatabase = _autoCreateDatabase;
	}

	public void setAutoInitializeCurrenThreadSession(final boolean _autoInitializeCurrenThreadSession) {
		this.autoInitializeCurrenThreadSession = _autoInitializeCurrenThreadSession;
	}

	public void setMaxPoolSize(final int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public void setMinPoolSize(final int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

}
