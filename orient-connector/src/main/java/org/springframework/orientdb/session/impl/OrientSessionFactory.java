package org.springframework.orientdb.session.impl;

import java.io.IOException;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orientdb.orm.session.IOrientSessionFactory;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * A base factory for creating {@link ODatabase} objects.
 *
 * @author Dzmitry_Naskou
 * @author Sylvain Leroy
 * @param <TDatabase>
 *            the type of database to handle
 */
public class OrientSessionFactory<TDatabase extends ODatabaseDocumentTx> implements IOrientSessionFactory {
	protected static final Logger	LOGGER	= LoggerFactory.getLogger(OrientSessionFactory.class);

	private ODatabaseDocumentPool	pool;

	public OrientSessionFactory() {
		super();
	}

	public OrientSessionFactory(final DatabaseConfiguration _configuration) {
		this.init(_configuration);
	}

	@Override
	public void close() throws IOException {
		LOGGER.trace("Closing connexion pool ");
		if (this.pool != null) {
			this.pool.close();
		}

	}

	@Override
	public TDatabase db() {
		return (TDatabase) this.pool.acquire();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.komea.product.eventory.database.conf.IOrientDbGraphSessionFactory
	 * #getGraph()
	 */
	@Override
	public OrientGraph getGraph() {
		final OrientGraph orientGraph = new OrientGraph(this.getOrCreateDB(), false);
		orientGraph.setUseLightweightEdges(false);
		return orientGraph;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.komea.product.eventory.database.conf.IOrientDbGraphSessionFactory
	 * #getGraph()
	 */
	@Override
	public OrientGraph getGraphTx() {
		final OrientGraph orientGraph = new OrientGraph(this.getOrCreateDB(), true);
		orientGraph.setUseLightweightEdges(false);
		return orientGraph;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#openDatabase
	 * ()
	 */
	@Override
	public final TDatabase getOrCreateDB() {
		return (TDatabase) this.pool.acquire();
	}

	/**
	 * Returns the document pool.
	 *
	 * @return the document pool.
	 */
	public ODatabaseDocumentPool getPool() {
		return this.pool;
	}

	@SuppressWarnings("resource")
	public void init(final DatabaseConfiguration _configuration) {

		Validate.notNull(_configuration, "A database configuration is required");
		LOGGER.debug("Initialisation of OrientDB -> requires to create database if it does not exist");
		OGlobalConfiguration.setConfiguration(_configuration.getExtraConfiguration());

		if (_configuration.isAutocreateDatabase()) {
			LOGGER.info("Auto-creation of the database {} ", _configuration.getUrl());
			new AutoCreateDatabase(_configuration).autoCreate();
		}

		LOGGER.debug("Creation of the connexion pool {} ", _configuration.getUrl());
		this.createPool(_configuration);

		if (_configuration.isAutoInitializeCurrentThreadTransaction()) {
			LOGGER.warn("Autoinitialize the database for the current thread");
			this.getOrCreateDB();
		}
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

}