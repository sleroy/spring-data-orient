package org.springframework.orientdb.orm.session;

import java.io.Closeable;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public interface IObjectSessionFactory extends Closeable {

	/**
	 * Obtains the database session. Creates it if not already created through a
	 * pool of connexion.
	 */
	public OObjectDatabaseTx getOrCreateDatabaseSession();

}