package org.springframework.orientdb.session.impl;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * A base factory for creating {@link ODatabase} objects.
 *
 * @author Dzmitry_Naskou
 * @author Sylvain Leroy
 * @param <TDatabase>
 *            the type of database to handle
 */
public abstract class AbstractOrientDatabaseFactory<TDatabase extends ODatabaseDocumentTx> implements Closeable {
	protected static final Logger	               LOGGER	= LoggerFactory
	                                                              .getLogger(AbstractOrientDatabaseFactory.class);

	private final ThreadLocal<ODatabaseDocumentTx>	db	  = new ThreadLocal<>();

	private ODatabaseDocumentPool	               pool;

	public AbstractOrientDatabaseFactory() {
		super();
	}

	public AbstractOrientDatabaseFactory(final DatabaseConfiguration _configuration) {
		this.init(_configuration);
	}

	@Override
	public void close() throws IOException {
		LOGGER.trace("Closing connexion pool ");
		if (this.pool != null) {
			this.pool.close();
		}
		LOGGER.trace("Closing database connection");
		if (this.db.get() != null) {
			this.db.get().close();
		}
	}

	public TDatabase db() {
		return (TDatabase) ODatabaseRecordThreadLocal.INSTANCE.get().getDatabaseOwner();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#openDatabase
	 * ()
	 */
	public final TDatabase getOrCreateDatabaseSession() {
		this.db.set(this.pool.acquire());
		return (TDatabase) this.db.get();
	}

	public ODatabaseDocumentPool getPool() {
		return this.pool;
	}

	@SuppressWarnings("resource")
	public void init(final DatabaseConfiguration _configuration) {

		Validate.notNull(_configuration, "A database configuration is required");
		OGlobalConfiguration.setConfiguration(_configuration.getExtraConfiguration());
		LOGGER.debug("Accessing to the database in{} ", _configuration.getUrl());
		final ODatabase createdDB = this.newDatabase(_configuration);
		this.createDatabase(createdDB, _configuration);
		LOGGER.debug("Creation of the connexion pool {} ", _configuration.getUrl());
		this.createPool(_configuration);
		if (_configuration.isAutoInitializeCurrentThreadTransaction()) {
			LOGGER.warn("Autoinitialize the database for the current thread");
			this.getOrCreateDatabaseSession();
		}
	}

	public void setPool(final ODatabaseDocumentPool pool) {
		this.pool = pool;
	}

	/**
	 * Initializes the database.
	 *
	 * @param _database
	 * @param _configuration
	 */
	private final void createDatabase(final ODatabase _database, final DatabaseConfiguration _configuration) {
		if (!this.isRemoteDatabaseUrl(_configuration)) {

			if (!_database.exists()) {
				LOGGER.trace("Creating local database");
				_database.create();
			} else {
				LOGGER.trace("Reusing local database");
			}
		} else {
			LOGGER.debug("Working on a remote database");
		}
		_database.close();
	}

	/**
	 * Creates the connexion pool.
	 *
	 * @param _configuration
	 *            the database configuration
	 */
	private final void createPool(final DatabaseConfiguration _configuration) {
		LOGGER.debug("Configuration of the connexion pool min={}, max={}", _configuration.getMinPoolSize(),
		        _configuration.getMaxPoolSize());
		this.pool = new ODatabaseDocumentPool(_configuration.getUrl(), _configuration.getUsername(),
		        _configuration.getPassword());

	}

	private boolean isRemoteDatabaseUrl(final DatabaseConfiguration _configuration) {
		return _configuration.getUrl().startsWith("remote:");
	}

	protected abstract TDatabase newDatabase(DatabaseConfiguration _configuration);

}