package org.springframework.orientdb.session.impl;

import org.springframework.orientdb.orm.session.IDocumentSessionFactory;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * A specific factory for creating OrientDocumentDatabaseFactory objects that
 * handle {@link ODatabaseDocumentTx}.
 *
 * @author Dzmitry_Naskou
 * @see ODatabaseDocumentTx
 */
public class OrientDocumentDatabaseFactory extends AbstractOrientDatabaseFactory<ODatabaseDocumentTx> implements
IDocumentSessionFactory {

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
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#newDatabase
	 * ()
	 */
	@Override
	protected ODatabaseDocumentTx newDatabase(final DatabaseConfiguration _configuration) {
		return new ODatabaseDocumentTx(_configuration.getUrl());
	}

}