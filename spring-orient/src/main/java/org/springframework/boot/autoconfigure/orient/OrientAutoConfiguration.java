package org.springframework.boot.autoconfigure.orient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orientdb.orm.session.IOrientSessionFactory;
import org.springframework.orientdb.session.impl.DatabaseConfiguration;
import org.springframework.orientdb.session.impl.OrientSessionFactory;
import org.springframework.orm.orient.OrientTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties(OrientProperties.class)
public class OrientAutoConfiguration {

	@Autowired
	private OrientProperties	properties;

	@Bean
	@ConditionalOnMissingBean(OrientSessionFactory.class)
	public OrientSessionFactory documentDatabaseFactory() {
		final OrientSessionFactory factory = new OrientSessionFactory();

		this.configure(factory);

		return factory;
	}

	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ConditionalOnMissingBean(PlatformTransactionManager.class)
	public PlatformTransactionManager transactionManager(final IOrientSessionFactory factory) {
		return new OrientTransactionManager(factory);
	}

	@SuppressWarnings("rawtypes")
	protected void configure(final OrientSessionFactory _factory) {
		final DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
		databaseConfiguration.setUrl(this.properties.getUrl());
		databaseConfiguration.setUsername(this.properties.getUsername());
		databaseConfiguration.setPassword(this.properties.getPassword());
		databaseConfiguration.setMaxPoolSize(this.properties.getMaxPoolSize());
		databaseConfiguration.setMinPoolSize(this.properties.getMinPoolSize());
		databaseConfiguration.setAutocreateDatabase(this.properties.isAutoCreateDatabase());
		databaseConfiguration.setAutoInitializeCurrentThreadTransaction(this.properties
		        .isAutoInitializeCurrenThreadSession());
		_factory.init(databaseConfiguration);
	}
}
