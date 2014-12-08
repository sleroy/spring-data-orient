package org.springframework.orientdb.orm.session;

import java.io.Closeable;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

public interface IDocumentSessionFactory extends Closeable {

	/**
	 * Obtains the database session. Creates it if not already created through a
	 * pool of connexion.
	 */
	public ODatabaseDocumentTx getOrCreateDatabaseSession();

}