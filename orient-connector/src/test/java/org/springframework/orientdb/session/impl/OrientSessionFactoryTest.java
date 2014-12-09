package org.springframework.orientdb.session.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OrientSessionFactoryTest {
	@Test
	public final void testOrientGraphDatabaseFactory() throws Exception {
		final OrientSessionFactory orientGraphDatabaseFactory = new OrientSessionFactory(
		        new TestDatabaseConfiguration());
		assertNotNull(orientGraphDatabaseFactory.getGraphTx());
		orientGraphDatabaseFactory.close();
	}

}
