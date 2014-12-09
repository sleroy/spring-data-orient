package org.springframework.orientdb.session.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.orientechnologies.orient.core.exception.ODatabaseException;

public class MemoryDatabaseConfigurationTest {

	@Test
	public void testMemoryDatabaseConfiguration() throws Exception {

		// WHEN DOUBLE INIT
		final OrientSessionFactory documentDatabaseFactory = new OrientSessionFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		documentDatabaseFactory.close();

		final OrientSessionFactory documentDatabaseFactory2 = new OrientSessionFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		documentDatabaseFactory2.close();

	}

	@Test(expected = ODatabaseException.class)
	public void testMemoryDatabaseConfiguration_getAndFail() throws Exception {

		// WHEN DOUBLE INIT
		final OrientSessionFactory documentDatabaseFactory = new OrientSessionFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		assertNotNull(documentDatabaseFactory.db());
		documentDatabaseFactory.close();

	}

	@Test
	public void testMemoryDatabaseConfiguration_getOrCreate() throws Exception {

		// WHEN DOUBLE INIT
		final OrientSessionFactory documentDatabaseFactory = new OrientSessionFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		assertNotNull(documentDatabaseFactory.getOrCreateDatabaseSession());
		documentDatabaseFactory.close();

	}
}
