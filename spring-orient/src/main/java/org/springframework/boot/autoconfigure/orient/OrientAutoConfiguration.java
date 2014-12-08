package org.springframework.boot.autoconfigure.orient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orientdb.session.impl.AbstractOrientDatabaseFactory;
import org.springframework.orientdb.session.impl.DatabaseConfiguration;
import org.springframework.orientdb.session.impl.OrientDocumentDatabaseFactory;
import org.springframework.orientdb.session.impl.OrientGraphDatabaseFactory;
import org.springframework.orm.orient.OrientTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Configuration
@ConditionalOnClass(OObjectDatabaseTx.class)
@EnableConfigurationProperties(OrientProperties.class)
public class OrientAutoConfiguration {

	@Autowired
	private OrientProperties	properties;

	@Bean
	@ConditionalOnMissingClass(ODatabaseDocumentTx.class)
	@ConditionalOnMissingBean(OrientDocumentDatabaseFactory.class)
	public OrientDocumentDatabaseFactory documentDatabaseFactory() {
		final OrientDocumentDatabaseFactory factory = new OrientDocumentDatabaseFactory();

		this.configure(factory);

		return factory;
	}

	@Bean
	@ConditionalOnMissingBean(OrientGraphDatabaseFactory.class)
	public OrientGraphDatabaseFactory graphDatabaseFactory() {
		final OrientGraphDatabaseFactory factory = new OrientGraphDatabaseFactory();

		this.configure(factory);

		return factory;
	}

	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ConditionalOnMissingBean(PlatformTransactionManager.class)
	public PlatformTransactionManager transactionManager(final AbstractOrientDatabaseFactory factory) {
		return new OrientTransactionManager(factory);
	}

	@SuppressWarnings("rawtypes")
	protected void configure(final AbstractOrientDatabaseFactory _factory) {
		final DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
		databaseConfiguration.setUrl(this.properties.getUrl());
		databaseConfiguration.setUsername(this.properties.getUsername());
		databaseConfiguration.setPassword(this.properties.getPassword());
		databaseConfiguration.setMaxPoolSize(this.properties.getMaxPoolSize());
		databaseConfiguration.setMinPoolSize(this.properties.getMinPoolSize());
		_factory.init(databaseConfiguration);
	}
}
