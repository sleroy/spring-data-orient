package org.springframework.orientdb.orm.session;

import java.io.Closeable;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public interface IOrientSessionFactory extends Closeable {

	/**
	 * Returns the active database (does not create it).
	 *
	 * @return the active database or null.
	 */
	public ODatabaseDocumentTx db();

	/**
	 * Obtains a graph representation of the database with transaction disabled.
	 *
	 * @return the orient graph
	 */
	public OrientGraph getGraph();

	/**
	 * Obtains a graph representation of the database with autotransaction
	 * enabled.
	 *
	 * @return the orient graph
	 */
	public OrientGraph getGraphTx();

	/**
	 * Obtains the database session. Creates it if not already created through a
	 * pool of connexion. Don't forget to close the resource with the close()
	 * method.
	 */
	public ODatabaseDocumentTx getOrCreateDatabaseSession();

}