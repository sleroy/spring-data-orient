package org.springframework.orientdb.session.impl;

import org.springframework.orientdb.orm.session.IObjectSessionFactory;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

/**
 * A specific factory for creating OrientDocumentDatabaseFactory objects that
 * handle {@link ODatabaseDocumentTx}.
 *
 * @author Dzmitry_Naskou
 * @see ODatabaseDocumentTx
 */
public class OrientObjectDatabaseFactory extends AbstractOrientDatabaseFactory<OObjectDatabaseTx, OObjectDatabasePool>
        implements IObjectSessionFactory {

	public OrientObjectDatabaseFactory() {
		super();
	}

	public OrientObjectDatabaseFactory(final DatabaseConfiguration _configuration) {
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
	protected OObjectDatabasePool doCreatePool(final DatabaseConfiguration _configuration) {
		return new OObjectDatabasePool(_configuration.getUrl(), _configuration.getUsername(),
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
	protected OObjectDatabaseTx newDatabase(final DatabaseConfiguration _configuration) {
		return new OObjectDatabaseTx(_configuration.getUrl());
	}
}