package org.springframework.data.orient.object.person.cluster;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.orient.core.OrientObjectTemplate;
import org.springframework.data.orient.repository.config.EnableOrientRepositories;
import org.springframework.orientdb.session.impl.LocalDiskDatabaseConfiguration;
import org.springframework.orientdb.session.impl.OrientObjectDatabaseFactory;
import org.springframework.orm.orient.OrientTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.test.data.Address;
import org.test.data.Person;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Configuration
@EnableTransactionManagement
@EnableOrientRepositories(basePackages = "org.springframework.data.orient.object.person.cluster")
public class PersonClusteredRepositoryTestConfiguration {

	@Bean
	public OrientObjectDatabaseFactory factory() {
		final OrientObjectDatabaseFactory factory = new OrientObjectDatabaseFactory(new LocalDiskDatabaseConfiguration(
		        "test/", "spring-data-test-cluster"));

		return factory;
	}

	@Bean
	@Qualifier("personClusterTemplate")
	public OrientObjectTemplate objectTemplate() {
		return new OrientObjectTemplate(this.factory());
	}

	@PostConstruct
	public void registerEntities() {
		this.factory().db().getEntityManager().registerEntityClass(Person.class);
		this.factory().db().getEntityManager().registerEntityClass(Address.class);

		final OObjectDatabaseTx db = this.factory().getOrCreateDatabaseSession();
		if (!db.existsCluster("person_temp")) {
			final int id = db.addCluster("person_temp");
			db.getMetadata().getSchema().getClass(Person.class).addClusterId(id);
		}

		db.close();
	}

	@Bean
	public OrientTransactionManager transactionManager() {
		return new OrientTransactionManager(this.factory());
	}
}
