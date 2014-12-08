package org.springframework.orientdb.session.impl;

import org.springframework.orientdb.orm.session.IGraphSessionFactory;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientGraphDatabaseFactory
extends
AbstractOrientDatabaseFactory<ODatabaseDocumentTx, ODatabaseDocumentPool>
implements IGraphSessionFactory {
	public OrientGraphDatabaseFactory() {
		super();
	}

	public OrientGraphDatabaseFactory(final DatabaseConfiguration _configuration) {
		super(_configuration);
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
		OrientGraph orientGraph = new OrientGraph(this.getOrCreateDatabaseSession());
		orientGraph.setUseLightweightEdges(false);
		return orientGraph;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#doCreatePool
	 * ()
	 */
	@Override
	protected ODatabaseDocumentPool doCreatePool(
			final DatabaseConfiguration _configuration) {
		return new ODatabaseDocumentPool(_configuration.getUrl(),
				_configuration.getUsername(), _configuration.getPassword());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.orm.orient.AbstractOrientDatabaseFactory#newDatabase
	 * ()
	 */
	@Override
	protected ODatabaseDocumentTx newDatabase(
			final DatabaseConfiguration _configuration) {
		return new ODatabaseDocumentTx(_configuration.getUrl());
	}
}