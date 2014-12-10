package org.springframework.orientdb.session.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

public class OrientSessionFactoryTest {
	@Test
	public final void testDbConnections() throws Exception {
		final TestDatabaseConfiguration configuration = new TestDatabaseConfiguration();
		configuration.setAutoInitializeCurrentThreadTransaction(false);
		final OrientSessionFactory orientGraphDatabaseFactory = new OrientSessionFactory(configuration);
		final ODatabaseDocumentTx first = orientGraphDatabaseFactory.db();
		for (int i = 0; i < 10000; ++i) {
			final ODatabaseDocumentTx iter = orientGraphDatabaseFactory.db();
			assertNotNull(iter);
			assertEquals(first, iter);
		}
	}

	@Test
	public final void testGetOrCreateDB() throws Exception {
		final TestDatabaseConfiguration configuration = new TestDatabaseConfiguration();
		configuration.setAutoInitializeCurrentThreadTransaction(false);
		final OrientSessionFactory orientGraphDatabaseFactory = new OrientSessionFactory(configuration);
		final ODatabaseDocumentTx first = orientGraphDatabaseFactory.getOrCreateDB();
		for (int i = 0; i < 10000; ++i) {
			final ODatabaseDocumentTx iter = orientGraphDatabaseFactory.getOrCreateDB();
			assertNotNull(iter);
			assertEquals(first, iter);
		}
	}

	@Test
	public final void testOrientGraphDatabaseFactory() throws Exception {
		final OrientSessionFactory orientGraphDatabaseFactory = new OrientSessionFactory(
		        new TestDatabaseConfiguration());
		assertNotNull(orientGraphDatabaseFactory.getGraphTx());
		orientGraphDatabaseFactory.close();
	}
}
