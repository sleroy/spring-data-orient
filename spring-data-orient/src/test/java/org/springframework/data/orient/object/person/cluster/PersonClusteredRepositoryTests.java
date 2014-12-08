package org.springframework.data.orient.object.person.cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.orient.core.OrientObjectOperations;
import org.springframework.data.orient.repository.DefaultCluster;
import org.springframework.orientdb.session.impl.OrientObjectDatabaseFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.test.data.Person;
import org.testng.annotations.Test;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(classes = PersonClusteredRepositoryTestConfiguration.class)
public class PersonClusteredRepositoryTests extends AbstractTestNGSpringContextTests {

	@Autowired
	OrientObjectDatabaseFactory	dbf;

	@Autowired
	PersonClusteredRepository	repository;

	@Autowired
	@Qualifier("personClusterTemplate")
	OrientObjectOperations	    operations;

	@Test
	public void checkClasses() {
		final OObjectDatabaseTx db = this.dbf.getOrCreateDatabaseSession();

		for (final OClass c : db.getMetadata().getSchema().getClasses()) {
			System.out.println(c);
		}

		db.close();
	}

	@Test
	public void checkClusters() {
		final OObjectDatabaseTx db = this.dbf.getOrCreateDatabaseSession();

		for (final String cluster : db.getClusterNames()) {
			System.out.println(cluster);
		}

		db.close();
	}

	@Test
	public void findAll() {
		System.out.println(this.repository.findAll());
	}

	@Test
	public void findAll2() {
		for (final Person person : this.repository.findAll()) {
			System.out.println(this.operations.getClusterNameByRid(person.getRid()));
		}
	}

	@Test
	public void findAllByCluster() {
		System.out.println(this.repository.findAll("person_temp"));
	}

	@Test
	public void findByFirstNameByCluster() {
		this.repository.findByFirstName("Dzmitry", new DefaultCluster("person_temp"));
	}

	@Test
	public void findByLastNameTest() {
		System.out.println(this.repository.findByLastName("Naskou"));
	}

	@Test
	public void getPersonClusters() {
		final OObjectDatabaseTx db = this.dbf.getOrCreateDatabaseSession();

		for (final int i : db.getMetadata().getSchema().getClass(Person.class).getClusterIds()) {
			System.out.println(i);
		}

		db.close();
	}

	@Test
	public void savePersonToClusterTest() {
		final Person person = new Person();
		person.setFirstName("Dzmitry");
		person.setLastName("Naskou");

		this.repository.save(person, "person_temp");
	}

	@Test
	public void savePersonToDefaultClusterTest() {
		final Person person = new Person();
		person.setFirstName("Ivan");
		person.setLastName("Ivanou");

		this.repository.save(person);
	}
}
