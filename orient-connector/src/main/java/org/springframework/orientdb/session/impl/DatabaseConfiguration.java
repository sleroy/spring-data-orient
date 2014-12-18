package org.springframework.orientdb.session.impl;

import java.util.Collections;
import java.util.Map;

/**
 * This class defines the required settings to initialize a database connection
 * (to OrientDB). Main parameters are the URL, the username and password.
 *
 * @author sleroy
 *
 */
public class DatabaseConfiguration {
	/**
	 * URL to initialize the orient db database.
	 */
	private String	            url;

	/** Default minimum pool size. */
	public static final int	    DEFAULT_MIN_POOL_SIZE	               = 1;

	/** Default maximum pool size. */
	public static final int	    DEFAULT_MAX_POOL_SIZE	               = 20;

	/** The username. */
	private String	            username;

	/** The password. */
	private String	            password;

	/** The min pool size. */
	private int	                minPoolSize	                           = DEFAULT_MIN_POOL_SIZE;

	/** The max pool size. */
	private int	                maxPoolSize	                           = DEFAULT_MAX_POOL_SIZE;

	private boolean	            autoInitializeCurrentThreadTransaction	= true;

	private boolean	            autocreateDatabase	                   = true;

	private Map<String, Object>	extraConfiguration	                   = Collections.emptyMap();

	public DatabaseConfiguration() {
		super();
	}

	public Map<String, Object> getExtraConfiguration() {
		return this.extraConfiguration;
	}

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

	public boolean isAutocreateDatabase() {
		return this.autocreateDatabase;
	}

	public boolean isAutoInitializeCurrentThreadTransaction() {
		return this.autoInitializeCurrentThreadTransaction;
	}

	public void setAutocreateDatabase(final boolean _autocreateDatabase) {
		this.autocreateDatabase = _autocreateDatabase;
	}

	public void setAutoInitializeCurrentThreadTransaction(final boolean _autoInitializeCurrentThreadTransaction) {
		this.autoInitializeCurrentThreadTransaction = _autoInitializeCurrentThreadTransaction;
	}

	public void setExtraConfiguration(final Map<String, Object> _extraConfiguration) {
		this.extraConfiguration = _extraConfiguration;
	}

	public void setMaxPoolSize(final int _maxPoolSize) {
		this.maxPoolSize = _maxPoolSize;
	}

	public void setMinPoolSize(final int _minPoolSize) {
		this.minPoolSize = _minPoolSize;
	}

	public void setPassword(final String _password) {
		this.password = _password;
	}

	public void setUrl(final String _url) {
		this.url = _url;
	}

	public void setUsername(final String _username) {
		this.username = _username;
	}

	@Override
	public String toString() {
		return "DatabaseConfiguration [url=" + this.url + ", username=" + this.username + ", minPoolSize="
		        + this.minPoolSize + ", maxPoolSize=" + this.maxPoolSize + "]";
	}
}
