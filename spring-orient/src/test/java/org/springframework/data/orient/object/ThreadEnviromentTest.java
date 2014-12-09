package org.springframework.data.orient.object;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orientdb.orm.session.IOrientSessionFactory;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@SpringApplicationConfiguration(classes = ThreadEnviromentTest.class)
public class ThreadEnviromentTest extends AbstractTestNGSpringContextTests {

	public static class ExampleDAO implements IDAO {
		@Autowired
		private IOrientSessionFactory	osf;

		private int		              node		= 0;

		private static final Logger		LOGGER	= LoggerFactory.getLogger(ThreadEnviromentTest.class);

		@Transactional
		@Override
		public void addVertex(final String _name) {
			final OrientGraph graph = this.osf.getGraph();
			try {

				final String id = _name + this.node++;
				graph.addVertex(id);
			} finally {
				graph.shutdown();
			}

		}
	}

	public static interface IDAO {
		public void addVertex(String _name);
	}

	private static final int	THREADS	          = 100;

	private static final int	NUMBER_ITERATIONS	= 4000;

	@Autowired
	ApplicationContext	     context;

	@Autowired
	IOrientSessionFactory	 dbf;

	@Autowired
	IDAO	                 dao;

	@Test
	public void checkApplicationContext() {
		this.dbf.getGraph().addVertex("ORIGIN");

		this.dao.addVertex("GNI");
		final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS);
		for (int i = 0; i < NUMBER_ITERATIONS; ++i) {
			newFixedThreadPool.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {

					ThreadEnviromentTest.this.dao.addVertex("NODE-THREAD-" + Thread.currentThread().getName());
					return null;
				}
			});
		}
		newFixedThreadPool.shutdown();

	}

	@Bean
	IDAO newDAO() {
		return new ExampleDAO();
	}
}