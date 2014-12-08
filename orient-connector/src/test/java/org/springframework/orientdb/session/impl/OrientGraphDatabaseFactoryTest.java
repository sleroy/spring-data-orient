package org.springframework.orientdb.session.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OrientGraphDatabaseFactoryTest {

	@Test
	public final void testOrientGraphDatabaseFactory() throws Exception {
		final OrientGraphDatabaseFactory orientGraphDatabaseFactory = new OrientGraphDatabaseFactory(
		        new TestDatabaseConfiguration());
		assertNotNull(orientGraphDatabaseFactory.getGraph());
		orientGraphDatabaseFactory.close();
	}
}
