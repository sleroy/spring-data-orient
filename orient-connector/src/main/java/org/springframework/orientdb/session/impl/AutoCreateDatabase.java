package org.springframework.orientdb.session.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orientdb.session.CouldNotCreateDatabaseException;

import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * This component is in charge to autocreate the database whern required.
 *
 * @author sleroy
 *
 */
public class AutoCreateDatabase {

	private final DatabaseConfiguration	configuration;
	private static final Logger	        LOGGER	= LoggerFactory.getLogger(AutoCreateDatabase.class);

	public AutoCreateDatabase(final DatabaseConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

	public void autoCreate() {
		if (this.isRemoteDatabaseUrl(this.configuration)) {
			LOGGER.warn("Attempt to auto-create a database on a remote server");
		}
		try (ODatabase createdDB = this.newDatabase(this.configuration)) {
			if (!createdDB.exists()) {
				LOGGER.warn("Creating local database");
				createdDB.create();
			} else {
				LOGGER.info("Reusing local database");
			}
		} catch (final Exception e) {
			throw new CouldNotCreateDatabaseException(this.configuration, e);

		}
	}

	/**
	 * Tests if a database url is remote.
	 *
	 * @param _configuration
	 *            the configuration
	 * @return true if the url indicates an remote database.
	 */
	public boolean isRemoteDatabaseUrl(final DatabaseConfiguration _configuration) {
		return _configuration.getUrl().startsWith("remote:");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#newDatabase
	 * ()
	 */
	public ODatabaseDocumentTx newDatabase(final DatabaseConfiguration _configuration) {
		return new ODatabaseDocumentTx(_configuration.getUrl());
	}

}
