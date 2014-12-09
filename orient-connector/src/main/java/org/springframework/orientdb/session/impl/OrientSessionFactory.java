package org.springframework.orientdb.session.impl;

import org.springframework.orientdb.orm.session.IOrientSessionFactory;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * A specific factory for creating OrientDocumentDatabaseFactory objects that
 * handle {@link ODatabaseDocumentTx}.
 *
 * @author Dzmitry_Naskou
 * @see ODatabaseDocumentTx
 */
public class OrientSessionFactory extends AbstractOrientDatabaseFactory<ODatabaseDocumentTx> implements
IOrientSessionFactory {

	public OrientSessionFactory() {
		super();
	}

	public OrientSessionFactory(final DatabaseConfiguration _configuration) {
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
		final OrientGraph orientGraph = new OrientGraph(this.getOrCreateDatabaseSession(), false);
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
		final OrientGraph orientGraph = new OrientGraph(this.getOrCreateDatabaseSession(), true);
		orientGraph.setUseLightweightEdges(false);
		return orientGraph;
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