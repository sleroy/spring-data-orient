/**
 *
 */
package org.springframework.orm.orient;

import org.springframework.orientdb.session.impl.OrientSessionFactory;
import org.springframework.orientdb.session.impl.TestDatabaseConfiguration;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.testng.annotations.Test;

import com.orientechnologies.orient.core.exception.OSchemaException;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * @author sleroy
 *
 */
public class OrientTransactionManagerTest {

	/**
	 * Test method for
	 * {@link org.springframework.orm.orient.OrientTransactionManager#OrientTransactionManager(org.springframework.orientdb.orm.session.IOrientSessionFactory)}
	 * .
	 */
	@SuppressWarnings("resource")
	@Test
	public void testOrientTransactionManager() throws Exception {
		try (final OrientSessionFactory osf = new OrientSessionFactory(new TestDatabaseConfiguration())) {

			final OrientGraph graph = osf.getGraph();
			final OrientVertex luke = graph.addVertex(null);
			final OrientVertex darthVader = graph.addVertex(null);
			graph.addEdge(null, darthVader, luke, "father");
			graph.commit();
			graph.shutdown();

		}

	}

	/**
	 * Test method for
	 * {@link org.springframework.orm.orient.OrientTransactionManager#OrientTransactionManager(org.springframework.orientdb.orm.session.IOrientSessionFactory)}
	 * .
	 */
	@SuppressWarnings("resource")
	@Test(expectedExceptions = { OSchemaException.class })
	public void testOrientTransactionManager_failSchemaTransaction() throws Exception {
		try (final OrientSessionFactory osf = new OrientSessionFactory(new TestDatabaseConfiguration())) {
			final OrientTransactionManager orientTransactionManager = new OrientTransactionManager(osf);
			orientTransactionManager.getTransaction(new DefaultTransactionAttribute());

			final OrientGraph graph = osf.getGraph();
			graph.addVertex(null);

		}

	}
}
