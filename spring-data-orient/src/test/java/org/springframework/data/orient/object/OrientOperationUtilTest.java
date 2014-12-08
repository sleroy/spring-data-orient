package org.springframework.data.orient.object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.orient.core.OrientObjectOperations;
import org.springframework.orientdb.session.impl.OrientObjectDatabaseFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.test.data.Address;
import org.test.data.Employee;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@TransactionConfiguration
@SpringApplicationConfiguration(classes = OrientOperationUtilTest.class)
public class OrientOperationUtilTest extends AbstractTestNGSpringContextTests {

	@Autowired
	OrientObjectOperations	    template;

	@Autowired
	OrientObjectDatabaseFactory	factory;

	@BeforeClass
	public void before() {
		try (OObjectDatabaseTx db = this.factory.getOrCreateDatabaseSession()) {
			db.getEntityManager().registerEntityClass(Employee.class);
		}
	}

	@Test
	public void getRidFromParentTest() {
		final Employee employee = new Employee();
		Assert.assertNull(this.template.getRid(employee));

		employee.setRid("123");
		Assert.assertEquals(this.template.getRid(employee), "123");
	}

	@Test
	public void getRidFromProxy() {
		final Employee employee = new Employee();
		final Employee savedEmployee = this.template.save(employee);

		Assert.assertNotSame(savedEmployee.getClass(), Employee.class);
		Assert.assertEquals(this.template.getRid(savedEmployee), savedEmployee.getRid());
	}

	@Test
	public void getRidTest() {
		final Address address = new Address();
		Assert.assertNull(this.template.getRid(address));

		address.setId("123");
		Assert.assertEquals(this.template.getRid(address), "123");
	}
}
