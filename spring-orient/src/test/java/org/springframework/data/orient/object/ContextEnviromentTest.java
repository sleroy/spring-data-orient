package org.springframework.data.orient.object;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.orientdb.orm.session.IOrientSessionFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@SpringApplicationConfiguration(classes = ContextEnviromentTest.class)
public class ContextEnviromentTest extends AbstractTestNGSpringContextTests {

	@BeforeClass
	public static void start() {
		// OGlobalConfiguration.STORAGE_KEEP_OPEN.setValue(true);
	}

	@Autowired
	ApplicationContext	  context;

	@Autowired
	IOrientSessionFactory	dbf;

	@Test
	public void checkApplicationContext() {
		Assert.assertNotNull(this.context);
	}

	@Test
	public void checkOrientObjectDatabaseFactory() {
		Assert.assertNotNull(this.dbf);
	}

	/**
	 * Test method for
	 * {@link org.springframework.orm.orient.OrientTransactionManager#OrientTransactionManager(org.springframework.orientdb.orm.session.IOrientSessionFactory)}
	 * .
	 */
	@SuppressWarnings("resource")
	@Test
	public void testOrientTransactionManager() throws Exception {
		this.testGraph();

	}

	/**
	 * Test method for
	 * {@link org.springframework.orm.orient.OrientTransactionManager#OrientTransactionManager(org.springframework.orientdb.orm.session.IOrientSessionFactory)}
	 * .
	 */
	@SuppressWarnings("resource")
	@Test(expectedExceptions = { IllegalArgumentException.class })
	@Transactional
	public void testOrientTransactionManager_failTransaction() throws Exception {
		this.testGraph();

	}

	private void testGraph() {
		final OrientGraph graph = this.dbf.getGraph();
		final OrientVertex luke = graph.addVertex(null);
		final OrientVertex darthVader = graph.addVertex(null);
		graph.addEdge(null, darthVader, luke, "father");
		graph.commit();
		graph.shutdown();
	}
}