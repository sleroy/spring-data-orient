package org.springframework.orientdb.session.impl;

import org.springframework.orientdb.orm.session.IDocumentSessionFactory;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * A specific factory for creating OrientDocumentDatabaseFactory objects that
 * handle {@link ODatabaseDocumentTx}.
 *
 * @author Dzmitry_Naskou
 * @see ODatabaseDocumentTx
 */
public class OrientDocumentDatabaseFactory extends
AbstractOrientDatabaseFactory<ODatabaseDocumentTx, ODatabaseDocumentPool> implements IDocumentSessionFactory {

	public OrientDocumentDatabaseFactory() {
		super();
	}

	public OrientDocumentDatabaseFactory(final DatabaseConfiguration _configuration) {
		super(_configuration);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#doCreatePool
	 * ()
	 */
	@Override
	protected ODatabaseDocumentPool doCreatePool(final DatabaseConfiguration _configuration) {
		return new ODatabaseDocumentPool(_configuration.getUrl(), _configuration.getUsername(),
				_configuration.getPassword());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#newDatabase
	 * ()
	 */
	@Override
	protected ODatabaseDocumentTx newDatabase(final DatabaseConfiguration _configuration) {
		return new ODatabaseDocumentTx(_configuration.getUrl());
	}

}