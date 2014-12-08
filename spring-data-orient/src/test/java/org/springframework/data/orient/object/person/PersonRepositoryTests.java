package org.springframework.data.orient.object.person;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.orient.core.OrientOperations;
import org.springframework.orientdb.session.impl.OrientObjectDatabaseFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.test.data.Address;
import org.test.data.Employee;
import org.test.data.Person;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orientechnologies.orient.core.entity.OEntityManager;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@TransactionConfiguration(defaultRollback = true)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class PersonRepositoryTests extends AbstractTestNGSpringContextTests {

	@Autowired
	PersonRepository	        repository;

	@Autowired
	OrientObjectDatabaseFactory	factory;

	@Autowired
	OrientOperations	        operations;

	@BeforeClass
	public void before() {
		try (OObjectDatabaseTx db = this.factory.getOrCreateDatabaseSession()) {
			final OEntityManager manager = db.getEntityManager();
			manager.registerEntityClass(Person.class);
			manager.registerEntityClass(Address.class);
			manager.registerEntityClass(Employee.class);
		}

		final Address esenina = this.operations
		        .command("insert into Address (country, city, street) values ('Belarus', 'Minsk', 'Esenina')");

		this.operations.command("insert into Person (firstName, lastName, active, address) values (?, ?, ?, ?)",
		        "Dzmitry", "Naskou", true, esenina);
		this.operations.command("insert into Person (firstName, lastName, active) values ('Koby', 'Eliot', true)");
		this.operations.command("insert into Person (firstName, lastName, active) values ('Ronny', 'Carlisle', true)");
		this.operations.command("insert into Person (firstName, lastName, active) values ('Jameson', 'Matthew', true)");
		this.operations.command("insert into Person (firstName, lastName, active) values ('Roydon', 'Brenden', false)");
	}

	@Test
	public void countByFirstName() {
		Assert.assertEquals(this.repository.countByFirstName("Dzmitry"), Long.valueOf(1));
	}

	@Test
	public void countPerson() {
		Assert.assertEquals(this.repository.count(), 5);
	}

	@Test
	public void findAllPersons() {
		Assert.assertFalse(this.repository.findAll().isEmpty());
	}

	@Test
	public void findByActiveIsFalse() {
		for (final Person person : this.repository.findByActiveIsFalse()) {
			Assert.assertFalse(person.getActive());
		}
	}

	@Test
	public void findByActiveIsTrue() {
		for (final Person person : this.repository.findByActiveIsTrue()) {
			Assert.assertTrue(person.getActive());
		}
	}

	@Test
	public void findByCityTest() {
		final List<Person> persons = this.repository.findByAddress_City("Minsk");

		Assert.assertFalse(persons.isEmpty());

		for (final Person person : persons) {
			Assert.assertEquals(person.getAddress().getCity(), "Minsk");
		}
	}

	@Test
	public void findByFirstName() {
		Assert.assertFalse(this.repository.findByFirstName("Dzmitry").isEmpty());
	}

	@Test
	public void findByFirstNameAndLastName() {
		for (final Person person : this.repository.findByFirstNameOrLastName("Dzmitry", "Naskou")) {
			Assert.assertTrue(person.getFirstName().equals("Dzmitry") && person.getLastName().equals("Naskou"));
		}
	}

	@Test
	public void findByFirstNameLike() {
		for (final Person person : this.repository.findByFirstNameLike("Dzm%")) {
			Assert.assertTrue(person.getFirstName().startsWith("Dzm"));
		}
	}

	@Test
	public void findByFirstNameOrLastName() {
		for (final Person person : this.repository.findByFirstNameOrLastName("Dzmitry", "Eliot")) {
			Assert.assertTrue(person.getFirstName().equals("Dzmitry") || person.getLastName().equals("Eliot"));
		}
	}

	@Test
	public void findByFirstNamePage() {
		for (final Person person : this.repository.findByFirstName("Dzmitry", new PageRequest(1, 5)).getContent()) {
			Assert.assertEquals(person.getFirstName(), "Dzmitry");
		}
	}

	@Test
	public void findByLastName() {
		Assert.assertFalse(this.repository.findByLastName("Naskou").isEmpty());
	}

	@Test
	public void findByLastNameLike() {
		for (final Person person : this.repository.findByLastNameLike("Na%")) {
			Assert.assertTrue(person.getLastName().startsWith("Na"));
		}
	}

	@Test
	public void printFindByLastName() {
		for (final Person person : this.repository.findByLastName("Naskou")) {
			Assert.assertEquals(person.getLastName(), "Naskou");
		}
	}

	@Test
	public void repositoryAutowiring() {
		Assert.assertNotNull(this.repository);
	}

	@Test
	public void savePerson() {
		final Person person = new Person();
		person.setFirstName("Jay");
		person.setLastName("Miner");

		final String rid = this.repository.save(person).getRid();

		final Person result = this.repository.findOne(rid);

		Assert.assertEquals(result.getFirstName(), person.getFirstName());
		Assert.assertEquals(result.getLastName(), person.getLastName());
	}
}
