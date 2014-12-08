package org.springframework.orientdb.session.impl;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePoolFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * A base factory for creating {@link ODatabase} objects.
 *
 * @author Dzmitry_Naskou
 * @author Sylvain Leroy
 * @param <TDatabase>
 *            the type of database to handle
 */
public abstract class AbstractOrientDatabaseFactory<TDatabase extends ODatabaseInternal<?>> implements Closeable {

	protected final OPartitionedDatabasePoolFactory	POOL_FACTORY	= new OPartitionedDatabasePoolFactory();
	private OPartitionedDatabasePool	            pool;

	private ODatabaseDocumentTx	                    db;

	protected static final Logger	                LOGGER	     = LoggerFactory
	                                                                     .getLogger(AbstractOrientDatabaseFactory.class);

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
		if (this.db != null) {
			this.db.close();
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
		this.db = this.pool.acquire();
		return (TDatabase) this.db;
	}

	public OPartitionedDatabasePool getPool() {
		return this.pool;
	}

	@SuppressWarnings("resource")
	public void init(final DatabaseConfiguration _configuration) {

		Validate.notNull(_configuration, "A database configuration is required");
		this.POOL_FACTORY.setMaxPoolSize(_configuration.getMaxPoolSize());
		OGlobalConfiguration.setConfiguration(_configuration.getExtraConfiguration());
		LOGGER.debug("Accessing to the database in{} ", _configuration.getUrl());
		final ODatabase createdDB = this.newDatabase(_configuration);
		this.createDatabase(createdDB, _configuration);
		LOGGER.debug("Creation of the connexion pool {} ", _configuration.getUrl());
		this.createPool(_configuration);
	}

	public void setPool(final OPartitionedDatabasePool pool) {
		this.pool = pool;
	}

	private boolean isRemoteDatabaseUrl(final DatabaseConfiguration _configuration) {
		return _configuration.getUrl().startsWith("remote:");
	}

	protected void createDatabase(final ODatabase _database, final DatabaseConfiguration _configuration) {
		if (!this.isRemoteDatabaseUrl(_configuration)) {

			if (!_database.exists()) {
				LOGGER.trace("Creating local database");
				_database.create();
				_database.close();
			} else {
				LOGGER.trace("Reusing local database");
			}
		} else {
			LOGGER.debug("Working on a remote database");
		}
	}

	protected void createPool(final DatabaseConfiguration _configuration) {
		LOGGER.debug("Configuration of the connexion pool min={}, max={}", _configuration.getMinPoolSize(),
		        _configuration.getMaxPoolSize());
		this.pool = this.doCreatePool(_configuration);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#doCreatePool
	 * ()
	 */
	protected final OPartitionedDatabasePool doCreatePool(final DatabaseConfiguration _configuration) {

		return this.POOL_FACTORY.get(_configuration.getUrl(), _configuration.getUsername(),
				_configuration.getPassword());
	}

	protected abstract TDatabase newDatabase(DatabaseConfiguration _configuration);

}