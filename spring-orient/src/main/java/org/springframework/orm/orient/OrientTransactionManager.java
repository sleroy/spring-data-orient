package org.springframework.orm.orient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orientdb.orm.session.IOrientSessionFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;

/**
 * {@link org.springframework.transaction.PlatformTransactionManager}
 * implementation for OrientDB.
 *
 * @author Dzmitry_Naskou
 */
public class OrientTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager {

	private static final long	  serialVersionUID	= 1L;

	/** The logger. */
	private static Logger	      log	           = LoggerFactory.getLogger(OrientTransactionManager.class);

	/** The database factory. */
	private IOrientSessionFactory	dbf;

	/**
	 * Instantiates a new {@link OrientTransactionManager}.
	 *
	 * @param dbf
	 *            the dbf
	 */
	public OrientTransactionManager(final IOrientSessionFactory dbf) {
		super();
		this.dbf = dbf;
	}

	/**
	 * Gets the database factory for the database managed by this transaction
	 * manager.
	 *
	 * @return the database
	 */
	public IOrientSessionFactory getDatabaseFactory() {
		return this.dbf;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.transaction.support.ResourceTransactionManager#
	 * getResourceFactory()
	 */
	@Override
	public Object getResourceFactory() {
		return this.dbf;
	}

	/**
	 * Sets the database factory for the database managed by this transaction
	 * manager.
	 *
	 * @param databaseFactory
	 *            the database to set
	 */
	public void setDatabaseManager(final IOrientSessionFactory databaseFactory) {
		this.dbf = databaseFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #doBegin(java.lang.Object,
	 * org.springframework.transaction.TransactionDefinition)
	 */
	@Override
	protected void doBegin(final Object transaction, final TransactionDefinition definition)
			throws TransactionException {
		final OrientTransaction tx = (OrientTransaction) transaction;

		ODatabaseInternal<?> db = tx.getDatabase();
		if (db == null || db.isClosed()) {
			db = this.dbf.getOrCreateDatabaseSession();
			tx.setDatabase(db);
			TransactionSynchronizationManager.bindResource(this.dbf, db);
		}

		log.debug("beginning transaction, db.hashCode() = {}", db.hashCode());

		db.begin();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #doCleanupAfterCompletion(java.lang.Object)
	 */
	@Override
	protected void doCleanupAfterCompletion(final Object transaction) {
		final OrientTransaction tx = (OrientTransaction) transaction;

		if (!tx.getDatabase().isClosed()) {
			tx.getDatabase().close();
		}

		TransactionSynchronizationManager.unbindResource(this.dbf);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #
	 * doCommit(org.springframework.transaction.support.DefaultTransactionStatus
	 * )
	 */
	@Override
	protected void doCommit(final DefaultTransactionStatus status) throws TransactionException {
		final OrientTransaction tx = (OrientTransaction) status.getTransaction();
		final ODatabaseInternal<?> db = tx.getDatabase();

		log.debug("committing transaction, db.hashCode() = {}", db.hashCode());

		db.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #doGetTransaction()
	 */
	@Override
	protected Object doGetTransaction() throws TransactionException {
		final OrientTransaction tx = new OrientTransaction();

		final ODatabaseInternal<?> db = (ODatabaseInternal<?>) TransactionSynchronizationManager.getResource(this
				.getResourceFactory());

		if (db != null) {
			tx.setDatabase(db);
			tx.setTx(db.getTransaction());
		}

		return tx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #doResume(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void doResume(final Object transaction, final Object suspendedResources) throws TransactionException {
		final OrientTransaction tx = (OrientTransaction) transaction;
		final ODatabaseInternal<?> db = tx.getDatabase();

		if (!db.isClosed()) {
			db.close();
		}

		final ODatabaseInternal<?> oldDb = (ODatabaseInternal<?>) suspendedResources;
		TransactionSynchronizationManager.bindResource(this.dbf, oldDb);
		ODatabaseRecordThreadLocal.INSTANCE.set((ODatabaseDocumentInternal) oldDb.getUnderlying());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #
	 * doRollback(org.springframework.transaction.support.DefaultTransactionStatus
	 * )
	 */
	@Override
	protected void doRollback(final DefaultTransactionStatus status) throws TransactionException {
		final OrientTransaction tx = (OrientTransaction) status.getTransaction();
		final ODatabaseInternal<?> db = tx.getDatabase();

		log.debug("committing transaction, db.hashCode() = {}", db.hashCode());

		db.rollback();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #doSetRollbackOnly(org.springframework.transaction.support.
	 * DefaultTransactionStatus)
	 */
	@Override
	protected void doSetRollbackOnly(final DefaultTransactionStatus status) throws TransactionException {
		status.setRollbackOnly();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #doSuspend(java.lang.Object)
	 */
	@Override
	protected Object doSuspend(final Object transaction) throws TransactionException {
		final OrientTransaction tx = (OrientTransaction) transaction;
		final ODatabaseInternal<?> db = tx.getDatabase();

		return db;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.transaction.support.AbstractPlatformTransactionManager
	 * #isExistingTransaction(java.lang.Object)
	 */
	@Override
	protected boolean isExistingTransaction(final Object transaction) throws TransactionException {
		final OrientTransaction tx = (OrientTransaction) transaction;

		return tx.getTx() != null && tx.getTx().isActive();
	}
}
