package org.springframework.data.orient.object;

import junit.framework.Assert;

import org.springframework.aop.SpringProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.orient.core.OrientObjectOperations;
import org.springframework.orientdb.session.impl.OrientObjectDatabaseFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testng.annotations.Test;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@SpringApplicationConfiguration(classes = ContextEnviromentTests.class)
public class ContextEnviromentTests extends AbstractTestNGSpringContextTests {

	@Autowired
	ApplicationContext	        context;

	@Autowired
	OrientObjectDatabaseFactory	dbf;

	@Autowired
	OrientObjectOperations	    operations;

	@Test
	public void checkApplicationContext() {
		Assert.assertNotNull(this.context);
	}

	@Test
	public void checkOrientObjectDatabaseFactory() {
		Assert.assertNotNull(this.dbf);
	}

	@Test
	public void checkOrientOperations() {
		Assert.assertNotNull(this.operations);
	}

	@Test
	public void checkTransactionalOrientObjectTemplate() {
		Assert.assertTrue(SpringProxy.class.isAssignableFrom(this.operations.getClass()));
	}
}
