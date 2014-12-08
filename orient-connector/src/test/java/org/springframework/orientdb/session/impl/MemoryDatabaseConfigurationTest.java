package org.springframework.orientdb.session.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.orientechnologies.orient.core.exception.ODatabaseException;

public class MemoryDatabaseConfigurationTest {

	@Test
	public void testMemoryDatabaseConfiguration() throws Exception {

		// WHEN DOUBLE INIT
		final OrientDocumentDatabaseFactory documentDatabaseFactory = new OrientDocumentDatabaseFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		documentDatabaseFactory.close();

		final OrientDocumentDatabaseFactory documentDatabaseFactory2 = new OrientDocumentDatabaseFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		documentDatabaseFactory2.close();

	}

	@Test(expected = ODatabaseException.class)
	public void testMemoryDatabaseConfiguration_getAndFail() throws Exception {

		// WHEN DOUBLE INIT
		final OrientDocumentDatabaseFactory documentDatabaseFactory = new OrientDocumentDatabaseFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		assertNotNull(documentDatabaseFactory.db());
		documentDatabaseFactory.close();

	}

	@Test
	public void testMemoryDatabaseConfiguration_getOrCreate() throws Exception {

		// WHEN DOUBLE INIT
		final OrientDocumentDatabaseFactory documentDatabaseFactory = new OrientDocumentDatabaseFactory(
		        new MemoryDatabaseConfiguration("testDatabase"));

		assertNotNull(documentDatabaseFactory.getOrCreateDatabaseSession());
		documentDatabaseFactory.close();

	}
}
