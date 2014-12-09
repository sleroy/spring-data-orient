package org.springframework.orientdb.orm.session;

import java.io.Closeable;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public interface IOrientSessionFactory extends Closeable {

	/**
	 * Obtains a graph representation of the database with autotransaction
	 * enabled.
	 *
	 * @return the orient graph
	 */
	public OrientGraph getGraphTx();

	/**
	 * Obtains the database session. Creates it if not already created through a
	 * pool of connexion.
	 */
	public ODatabaseDocumentTx getOrCreateDatabaseSession();

	/**
	 * Obtains a graph representation of the database with transaction disabled.
	 *
	 * @return the orient graph
	 */
	OrientGraph getGraph();

}