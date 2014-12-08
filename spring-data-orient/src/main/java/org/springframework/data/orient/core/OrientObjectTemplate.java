package org.springframework.data.orient.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.data.orient.object.repository.DetachMode;
import org.springframework.orientdb.session.impl.OrientObjectDatabaseFactory;
import org.springframework.transaction.annotation.Transactional;

import com.orientechnologies.common.exception.OException;
import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.cache.OLocalRecordCache;
import com.orientechnologies.orient.core.command.OCommandOutputListener;
import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabase.ATTRIBUTES;
import com.orientechnologies.orient.core.db.ODatabase.OPERATION_MODE;
import com.orientechnologies.orient.core.db.ODatabase.STATUS;
import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.db.ODatabaseListener;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.object.ODatabaseObject;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.dictionary.ODictionary;
import com.orientechnologies.orient.core.entity.OEntityManager;
import com.orientechnologies.orient.core.exception.OTransactionException;
import com.orientechnologies.orient.core.hook.ORecordHook;
import com.orientechnologies.orient.core.hook.ORecordHook.HOOK_POSITION;
import com.orientechnologies.orient.core.hook.ORecordHook.RESULT;
import com.orientechnologies.orient.core.hook.ORecordHook.TYPE;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.intent.OIntent;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.security.OSecurityUser;
import com.orientechnologies.orient.core.metadata.security.OUser;
import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.ORecordInternal;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.storage.ORecordCallback;
import com.orientechnologies.orient.core.storage.ORecordMetadata;
import com.orientechnologies.orient.core.storage.OStorage;
import com.orientechnologies.orient.core.storage.OStorage.LOCKING_STRATEGY;
import com.orientechnologies.orient.core.tx.OTransaction;
import com.orientechnologies.orient.core.tx.OTransaction.TXTYPE;
import com.orientechnologies.orient.core.version.ORecordVersion;
import com.orientechnologies.orient.object.db.ODatabasePojoAbstract;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.object.enhancement.OObjectMethodFilter;
import com.orientechnologies.orient.object.iterator.OObjectIteratorClass;
import com.orientechnologies.orient.object.iterator.OObjectIteratorCluster;
import com.orientechnologies.orient.object.metadata.OMetadataObject;

@Transactional
public class OrientObjectTemplate implements OrientObjectOperations {

	private final OrientObjectDatabaseFactory	dbf;

	private Set<String>	                      defaultClusters;

	public OrientObjectTemplate(final OrientObjectDatabaseFactory dbf) {
		this.dbf = dbf;
	}

	public int addCluster(final String iClusterName) {
		return this.dbf.db().addCluster(iClusterName);
	}

	public int addCluster(final String iClusterName, final Object... iParameters) {
		return this.dbf.db().addCluster(iClusterName, iParameters);
	}

	public int addCluster(final String iType, final String iClusterName, final int iRequestedId,
			final String iLocation, final String iDataSegmentName, final Object... iParameters) {
		return this.dbf.db().addCluster(iType, iClusterName, iRequestedId, iLocation, iDataSegmentName, iParameters);
	}

	public int addCluster(final String iType, final String iClusterName, final String iLocation,
			final String iDataSegmentName, final Object... iParameters) {
		return this.dbf.db().addCluster(iType, iClusterName, iLocation, iDataSegmentName, iParameters);
	}

	public void attach(final Object iPojo) {
		this.dbf.db().attach(iPojo);
	}

	public <RET> RET attachAndSave(final Object iPojo) {
		return this.dbf.db().attachAndSave(iPojo);
	}

	public void backup(final OutputStream out, final Map<String, Object> options, final Callable<Object> callable,
			final OCommandOutputListener iListener, final int compressionLevel, final int bufferSize)
					throws IOException {
		this.dbf.db().backup(out, options, callable, iListener, compressionLevel, bufferSize);
	}

	public ODatabase<Object> begin() {
		return this.dbf.db().begin();
	}

	public ODatabase<Object> begin(final OTransaction iTx) {
		return this.dbf.db().begin(iTx);
	}

	public ODatabase<Object> begin(final TXTYPE iType) {
		return this.dbf.db().begin(iType);
	}

	@Override
	public <RET> OObjectIteratorClass<RET> browseClass(final Class<RET> iClusterClass) {
		return this.dbf.db().browseClass(iClusterClass);
	}

	public <RET> OObjectIteratorClass<RET> browseClass(final Class<RET> iClusterClass, final boolean iPolymorphic) {
		return this.dbf.db().browseClass(iClusterClass, iPolymorphic);
	}

	public <RET> OObjectIteratorClass<RET> browseClass(final String iClassName) {
		return this.dbf.db().browseClass(iClassName);
	}

	public <RET> OObjectIteratorClass<RET> browseClass(final String iClassName, final boolean iPolymorphic) {
		return this.dbf.db().browseClass(iClassName, iPolymorphic);
	}

	public <RET> OObjectIteratorCluster<RET> browseCluster(final String iClusterName) {
		return this.dbf.db().browseCluster(iClusterName);
	}

	public RESULT callbackHooks(final TYPE iType, final OIdentifiable iObject) {
		return this.dbf.db().callbackHooks(iType, iObject);
	}

	public <V> V callInLock(final Callable<V> iCallable, final boolean iExclusiveLock) {
		return this.dbf.db().callInLock(iCallable, iExclusiveLock);
	}

	public <RET extends OCommandRequest> RET command(final OCommandRequest iCommand) {
		return this.dbf.db().command(iCommand);
	}

	@Override
	public <RET> RET command(final OCommandSQL command, final Object... args) {
		return this.dbf.db().command(command).execute(args);
	}

	@Override
	public <RET> RET command(final String sql, final Object... args) {
		return this.command(new OCommandSQL(sql), args);
	}

	public ODatabasePojoAbstract<Object> commit() {
		return this.dbf.db().commit();
	}

	public ODatabasePojoAbstract<Object> commit(final boolean force) throws OTransactionException {
		return this.dbf.db().commit(force);
	}

	@Override
	public Long count(final OSQLQuery<?> query, final Object... values) {
		return ((ODocument) this.dbf.db().query(query, values).get(0)).field("count");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.data.orient.core.OrientOperations#countClass(java
	 * .lang.Class)
	 */
	@Override
	public long countClass(final Class<?> iClass) {
		return this.dbf.db().countClass(iClass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.data.orient.core.OrientOperations#countClass(java
	 * .lang.String)
	 */
	@Override
	public long countClass(final String iClassName) {
		return this.dbf.db().countClass(iClassName);
	}

	public long countClusterElements(final int iClusterId) {
		return this.dbf.db().countClusterElements(iClusterId);
	}

	public long countClusterElements(final int iClusterId, final boolean countTombstones) {
		return this.dbf.db().countClusterElements(iClusterId, countTombstones);
	}

	public long countClusterElements(final int[] iClusterIds) {
		return this.dbf.db().countClusterElements(iClusterIds);
	}

	public long countClusterElements(final int[] iClusterIds, final boolean countTombstones) {
		return this.dbf.db().countClusterElements(iClusterIds, countTombstones);
	}

	@Override
	public long countClusterElements(final String iClusterName) {
		return this.dbf.db().countClusterElements(iClusterName);
	}

	public OObjectDatabaseTx database() {
		return this.dbf.db();
	}

	public boolean declareIntent(final OIntent iIntent) {
		return this.dbf.db().declareIntent(iIntent);
	}

	@Override
	public ODatabaseObject delete(final Object iPojo) {
		return this.dbf.db().delete(iPojo);
	}

	@Override
	public ODatabaseObject delete(final ORecordInternal iRecord) {
		return this.dbf.db().delete(iRecord);
	}

	@Override
	public ODatabaseObject delete(final ORID iRID) {
		return this.dbf.db().delete(iRID);
	}

	public ODatabaseObject delete(final ORID iRID, final ORecordVersion iVersion) {
		return this.dbf.db().delete(iRID, iVersion);
	}

	public void deregisterClassMethodFilter(final Class<?> iClass) {
		this.dbf.db().deregisterClassMethodFilter(iClass);
	}

	public <RET> RET detach(final Object iPojo) {
		return this.dbf.db().detach(iPojo);
	}

	public <RET> RET detach(final Object iPojo, final boolean returnNonProxiedInstance) {
		return this.dbf.db().detach(iPojo, returnNonProxiedInstance);
	}

	@SuppressWarnings("unchecked")
	public <RET extends List<?>> RET detach(final RET list) {
		final OObjectDatabaseTx db = this.dbf.db();
		final List<Object> pojos = new ArrayList<Object>(list.size());

		for (final Object object : list) {
			pojos.add(db.detach(object, true));
		}

		return (RET) pojos;
	}

	@Override
	public <RET> RET detachAll(final Object iPojo, final boolean returnNonProxiedInstance) {
		return this.dbf.db().detachAll(iPojo, returnNonProxiedInstance);
	}

	@SuppressWarnings("unchecked")
	public <RET extends List<?>> RET detachAll(final RET list) {
		final OObjectDatabaseTx db = this.dbf.db();
		final List<Object> pojos = new ArrayList<Object>(list.size());

		for (final Object object : list) {
			pojos.add(db.detachAll(object, true));
		}

		return (RET) pojos;
	}

	@Override
	public boolean equals(final Object iOther) {
		return this.dbf.db().equals(iOther);
	}

	@Override
	public boolean existsClass(final Class<?> clazz) {
		return this.existsClass(clazz.getSimpleName());
	}

	@Override
	public boolean existsClass(final String className) {
		return this.dbf.db().getMetadata().getSchema().existsClass(className);
	}

	public boolean existsCluster(final String iClusterName) {
		return this.dbf.db().existsCluster(iClusterName);
	}

	public boolean existsUserObjectByRID(final ORID iRID) {
		return this.dbf.db().existsUserObjectByRID(iRID);
	}

	public void freeze() {
		this.dbf.db().freeze();
	}

	public void freeze(final boolean throwException) {
		this.dbf.db().freeze(throwException);
	}

	public void freezeCluster(final int iClusterId) {
		this.dbf.db().freezeCluster(iClusterId);
	}

	public void freezeCluster(final int iClusterId, final boolean throwException) {
		this.dbf.db().freezeCluster(iClusterId, throwException);
	}

	public Object get(final ATTRIBUTES iAttribute) {
		return this.dbf.db().get(iAttribute);
	}

	public int getClusterIdByName(final String iClusterName) {
		return this.dbf.db().getClusterIdByName(iClusterName);
	}

	@Override
	public int getClusterIdByName(final String clusterName, final Class<?> aClass) {
		final OClass oClass = this.dbf.db().getMetadata().getSchema().getClass(aClass);
		for (final int clusterId : oClass.getClusterIds()) {
			if (this.getClusterNameById(clusterId).equals(clusterName)) { return clusterId; }
		}

		throw new OException("Cluster " + clusterName + " not found");
	}

	@Override
	public String getClusterNameById(final int iClusterId) {
		return this.dbf.db().getClusterNameById(iClusterId);
	}

	@Override
	public String getClusterNameByRid(final String rid) {
		return this.getClusterNameById(new ORecordId(rid).getClusterId());
	}

	public Collection<String> getClusterNames() {
		return this.dbf.db().getClusterNames();
	}

	@Override
	public List<String> getClusterNamesByClass(final Class<?> entityClass, final boolean showDefault) {
		final int[] clusterIds = this.dbf.db().getMetadata().getSchema().getClass(entityClass).getClusterIds();
		final int defaultCluster = this.getDefaultClusterId(entityClass);

		final List<String> clusters = new ArrayList<>(clusterIds.length);
		for (final int clusterId : clusterIds) {
			if (showDefault || clusterId != defaultCluster) {
				clusters.add(this.getClusterNameById(clusterId));
			}
		}

		return clusters;
	}

	public long getClusterRecordSizeById(final int iClusterId) {
		return this.dbf.db().getClusterRecordSizeById(iClusterId);
	}

	public long getClusterRecordSizeByName(final String iClusterName) {
		return this.dbf.db().getClusterRecordSizeByName(iClusterName);
	}

	public int getClusters() {
		return this.dbf.db().getClusters();
	}

	public ODatabaseInternal<?> getDatabaseOwner() {
		return this.dbf.db().getDatabaseOwner();
	}

	public int getDefaultClusterId() {
		return this.dbf.db().getDefaultClusterId();
	}

	@Override
	public int getDefaultClusterId(final Class<?> domainClass) {
		return this.dbf.db().getMetadata().getSchema().getClass(domainClass).getDefaultClusterId();
	}

	public ODictionary<Object> getDictionary() {
		return this.dbf.db().getDictionary();
	}

	public OEntityManager getEntityManager() {
		return this.dbf.db().getEntityManager();
	}

	public Map<ORecordHook, HOOK_POSITION> getHooks() {
		return this.dbf.db().getHooks();
	}

	public ORID getIdentity(final Object iPojo) {
		return this.dbf.db().getIdentity(iPojo);
	}

	public OLocalRecordCache getLevel2Cache() {
		return this.dbf.db().getLocalCache();
	}

	public OMetadataObject getMetadata() {
		return this.dbf.db().getMetadata();
	}

	public String getName() {
		return this.dbf.db().getName();
	}

	public Iterator<Entry<String, Object>> getProperties() {
		return this.dbf.db().getProperties();
	}

	public Object getProperty(final String iName) {
		return this.dbf.db().getProperty(iName);
	}

	public ODocument getRecordById(final ORID iRecordId) {
		return this.dbf.db().getRecordById(iRecordId);
	}

	public ODocument getRecordByUserObject(final Object iPojo, final boolean iCreateIfNotAvailable) {
		return this.dbf.db().getRecordByUserObject(iPojo, iCreateIfNotAvailable);
	}

	public ORecordMetadata getRecordMetadata(final ORID rid) {
		return this.dbf.db().getRecordMetadata(rid);
	}

	@Override
	public String getRid(final Object entity) {
		Class<?> clazz = entity.getClass();
		while (clazz != Object.class) {
			for (final Field field : clazz.getDeclaredFields()) {
				final OId ridAnnotation = field.getAnnotation(OId.class);
				if (ridAnnotation != null) {
					field.setAccessible(true);
					try {
						Object rid = field.get(entity);
						if (rid == null) {
							final Method method = clazz.getDeclaredMethod(this.getterName(field.getName()));
							rid = method.invoke(entity);
						}
						return rid != null ? rid.toString() : null;
					} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException
							| InvocationTargetException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public long getSize() {
		return this.dbf.db().getSize();
	}

	public STATUS getStatus() {
		return this.dbf.db().getStatus();
	}

	public OStorage getStorage() {
		return this.dbf.db().getStorage();
	}

	public OTransaction getTransaction() {
		return this.dbf.db().getTransaction();
	}

	public String getType() {
		return this.dbf.db().getType();
	}

	public ODatabaseDocument getUnderlying() {
		return this.dbf.db().getUnderlying();
	}

	public String getURL() {
		return this.dbf.db().getURL();
	}

	public OSecurityUser getUser() {
		return this.dbf.db().getUser();
	}

	public Object getUserObjectByRecord(final OIdentifiable iRecord, final String iFetchPlan) {
		return this.dbf.db().getUserObjectByRecord(iRecord, iFetchPlan);
	}

	public Object getUserObjectByRecord(final OIdentifiable iRecord, final String iFetchPlan, final boolean iCreate) {
		return this.dbf.db().getUserObjectByRecord(iRecord, iFetchPlan, iCreate);
	}

	public ORecordVersion getVersion(final Object iPojo) {
		return this.dbf.db().getVersion(iPojo);
	}

	@Override
	public int hashCode() {

		return this.dbf.db().hashCode();
	}

	public boolean isAutomaticSchemaGeneration() {
		return this.dbf.db().isAutomaticSchemaGeneration();
	}

	public boolean isClosed() {
		return this.dbf.db().isClosed();
	}

	@Override
	public boolean isDefault(final String clusterName) {
		this.loadDefaultClusters();
		return this.defaultClusters.contains(clusterName);
	}

	public boolean isLazyLoading() {
		return this.dbf.db().isLazyLoading();
	}

	public boolean isManaged(final Object iEntity) {
		return this.dbf.db().isManaged(iEntity);
	}

	public boolean isMVCC() {
		return this.dbf.db().isMVCC();
	}

	public boolean isRetainObjects() {
		return this.dbf.db().isRetainObjects();
	}

	public boolean isSaveOnlyDirty() {
		return this.dbf.db().isSaveOnlyDirty();
	}

	public <RET> RET load(final Object iPojo) {
		return this.dbf.db().load(iPojo);
	}

	public <RET> RET load(final Object iPojo, final String iFetchPlan) {
		return this.dbf.db().load(iPojo, iFetchPlan);
	}

	public <RET> RET load(final Object iPojo, final String iFetchPlan, final boolean iIgnoreCache) {
		return this.dbf.db().load(iPojo, iFetchPlan, iIgnoreCache);
	}

	public <RET> RET load(final Object iPojo, final String iFetchPlan, final boolean iIgnoreCache,
			final boolean loadTombstone, final LOCKING_STRATEGY iLockingStrategy) {
		return this.dbf.db().load(iPojo, iFetchPlan, iIgnoreCache, loadTombstone, iLockingStrategy);
	}

	@Override
	public <RET> RET load(final ORID recordId) {
		return this.dbf.db().load(recordId);
	}

	public <RET> RET load(final ORID iRecordId, final String iFetchPlan) {
		return this.dbf.db().load(iRecordId, iFetchPlan);
	}

	public <RET> RET load(final ORID iRecordId, final String iFetchPlan, final boolean iIgnoreCache) {
		return this.dbf.db().load(iRecordId, iFetchPlan, iIgnoreCache);
	}

	public <RET> RET load(final ORID iRecordId, final String iFetchPlan, final boolean iIgnoreCache,
			final boolean loadTombstone, final LOCKING_STRATEGY iLockingStrategy) {
		return this.dbf.db().load(iRecordId, iFetchPlan, iIgnoreCache, loadTombstone, iLockingStrategy);
	}

	@Override
	public final <RET> RET load(final String recordId) {
		return this.load(new ORecordId(recordId));
	}

	public Object newInstance() {
		return this.dbf.db().newInstance();
	}

	public <T> T newInstance(final Class<T> iType) {
		return this.dbf.db().newInstance(iType);
	}

	public <T> T newInstance(final Class<T> iType, final Object... iArgs) {
		return this.dbf.db().newInstance(iType, iArgs);
	}

	public <RET> RET newInstance(final String iClassName) {
		return this.dbf.db().newInstance(iClassName);
	}

	public <RET> RET newInstance(final String iClassName, final Object iEnclosingClass, final Object... iArgs) {
		return this.dbf.db().newInstance(iClassName, iEnclosingClass, iArgs);
	}

	public <RET> RET newInstance(final String iClassName, final Object iEnclosingClass, final ODocument iDocument,
			final Object... iArgs) {
		return this.dbf.db().newInstance(iClassName, iEnclosingClass, iDocument, iArgs);
	}

	public <THISDB extends ODatabase<?>> THISDB open(final String iUserName, final String iUserPassword) {
		return this.dbf.db().open(iUserName, iUserPassword);
	}

	public ODocument pojo2Stream(final Object iPojo, final ODocument iRecord) {
		return this.dbf.db().pojo2Stream(iPojo, iRecord);
	}

	@Override
	public <RET extends List<?>> RET query(final OQuery<?> query, final DetachMode detachMode, final Object... args) {
		final RET result = this.query(query, args);

		switch (detachMode) {
		case ENTITY:
			return this.detach(result);
		case ALL:
			return this.detachAll(result);
		case NONE:
		}

		return result;
	}

	@Override
	public <RET extends List<?>> RET query(final OQuery<?> iCommand, final Object... iArgs) {
		return this.dbf.db().query(iCommand, iArgs);
	}

	@Override
	public <RET> RET queryForObject(final OSQLQuery<?> query, final DetachMode detachMode, final Object... values) {
		final RET result = this.queryForObject(query, values);

		switch (detachMode) {
		case ENTITY:
			return this.dbf.db().detach(result, true);
		case ALL:
			return this.dbf.db().detachAll(result, true);
		case NONE:
		}

		return result;
	}

	public <RET> RET queryForObject(final OSQLQuery<?> query, final Object... values) {
		final List<RET> list = this.query(query, values);

		return list.isEmpty() ? null : list.get(0);
	}

	public void registerClassMethodFilter(final Class<?> iClass, final OObjectMethodFilter iMethodFilter) {
		this.dbf.db().registerClassMethodFilter(iClass, iMethodFilter);
	}

	@Override
	public void registerEntityClass(final Class<?> domainClass) {
		try (OObjectDatabaseTx db = this.dbf.getOrCreateDatabaseSession()) {
			db.getEntityManager().registerEntityClass(domainClass);
		}
	}

	public <DBTYPE extends ODatabase<?>> DBTYPE registerHook(final ORecordHook iHookImpl) {
		return this.dbf.db().registerHook(iHookImpl);
	}

	public <DBTYPE extends ODatabase<?>> DBTYPE registerHook(final ORecordHook iHookImpl, final HOOK_POSITION iPosition) {
		return this.dbf.db().registerHook(iHookImpl, iPosition);
	}

	public void registerListener(final ODatabaseListener iListener) {
		this.dbf.db().registerListener(iListener);
	}

	public void registerUserObject(final Object iObject, final ORecord iRecord) {
		this.dbf.db().registerUserObject(iObject, iRecord);
	}

	public void registerUserObjectAfterLinkSave(final ORecord iRecord) {
		this.dbf.db().registerUserObjectAfterLinkSave(iRecord);
	}

	public void release() {
		this.dbf.db().release();
	}

	public void releaseCluster(final int iClusterId) {
		this.dbf.db().releaseCluster(iClusterId);
	}

	public void reload() {
		this.dbf.db().reload();
	}

	public <RET> RET reload(final Object iPojo) {
		return this.dbf.db().reload(iPojo);
	}

	public <RET> RET reload(final Object iPojo, final boolean iIgnoreCache) {
		return this.dbf.db().reload(iPojo, iIgnoreCache);
	}

	public <RET> RET reload(final Object iPojo, final String iFetchPlan, final boolean iIgnoreCache) {
		return this.dbf.db().reload(iPojo, iFetchPlan, iIgnoreCache);
	}

	public void restore(final InputStream in, final Map<String, Object> options, final Callable<Object> callable,
			final OCommandOutputListener iListener) throws IOException {
		this.dbf.db().restore(in, options, callable, iListener);
	}

	public ODatabasePojoAbstract<Object> rollback() {
		return this.dbf.db().rollback();
	}

	public ODatabasePojoAbstract<Object> rollback(final boolean force) throws OTransactionException {
		return this.dbf.db().rollback(force);
	}

	@Override
	public <RET> RET save(final Object iContent) {
		return this.dbf.db().save(iContent);
	}

	public <RET> RET save(final Object iContent, final OPERATION_MODE iMode, final boolean iForceCreate,
			final ORecordCallback<? extends Number> iRecordCreatedCallback,
			final ORecordCallback<ORecordVersion> iRecordUpdatedCallback) {
		return this.dbf.db().save(iContent, iMode, iForceCreate, iRecordCreatedCallback, iRecordUpdatedCallback);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.data.orient.core.OrientOperations#save(java.lang.
	 * Object, java.lang.String)
	 */
	@Override
	public <RET> RET save(final Object iPojo, final String iClusterName) {
		return this.dbf.db().save(iPojo, iClusterName);
	}

	public <RET> RET save(final Object iPojo, final String iClusterName, final OPERATION_MODE iMode,
			final boolean iForceCreate, final ORecordCallback<? extends Number> iRecordCreatedCallback,
			final ORecordCallback<ORecordVersion> iRecordUpdatedCallback) {
		return this.dbf.db().save(iPojo, iClusterName, iMode, iForceCreate, iRecordCreatedCallback,
				iRecordUpdatedCallback);
	}

	public <THISDB extends ODatabase<?>> THISDB set(final ATTRIBUTES attribute, final Object iValue) {
		return this.dbf.db().set(attribute, iValue);
	}

	public void setAutomaticSchemaGeneration(final boolean automaticSchemaGeneration) {
		this.dbf.db().setAutomaticSchemaGeneration(automaticSchemaGeneration);
	}

	public ODatabaseInternal<?> setDatabaseOwner(final ODatabaseInternal<?> iOwner) {
		return this.dbf.db().setDatabaseOwner(iOwner);
	}

	public void setDirty(final Object iPojo) {
		this.dbf.db().setDirty(iPojo);
	}

	public void setInternal(final ATTRIBUTES attribute, final Object iValue) {
		this.dbf.db().setInternal(attribute, iValue);
	}

	public void setLazyLoading(final boolean lazyLoading) {
		this.dbf.db().setLazyLoading(lazyLoading);
	}

	public <DBTYPE extends ODatabase<?>> DBTYPE setMVCC(final boolean iMvcc) {
		return this.dbf.db().setMVCC(iMvcc);
	}

	public Object setProperty(final String iName, final Object iValue) {
		return this.dbf.db().setProperty(iName, iValue);
	}

	public ODatabasePojoAbstract<Object> setRetainObjects(final boolean iValue) {
		return this.dbf.db().setRetainObjects(iValue);
	}

	public void setSaveOnlyDirty(final boolean saveOnlyDirty) {
		this.dbf.db().setSaveOnlyDirty(saveOnlyDirty);
	}

	public <THISDB extends ODatabase<?>> THISDB setStatus(final STATUS iStatus) {
		return this.dbf.db().setStatus(iStatus);
	}

	public void setUser(final OUser user) {
		this.dbf.db().setUser(user);
	}

	public Object stream2pojo(final ODocument iRecord, final Object iPojo, final String iFetchPlan) {
		return this.dbf.db().stream2pojo(iRecord, iPojo, iFetchPlan);
	}

	public Object stream2pojo(final ODocument iRecord, final Object iPojo, final String iFetchPlan,
			final boolean iReload) {
		return this.dbf.db().stream2pojo(iRecord, iPojo, iFetchPlan, iReload);
	}

	@Override
	public String toString() {
		return this.dbf.db().toString();
	}

	public <DBTYPE extends ODatabase<?>> DBTYPE unregisterHook(final ORecordHook iHookImpl) {
		return this.dbf.db().unregisterHook(iHookImpl);
	}

	public void unregisterListener(final ODatabaseListener iListener) {
		this.dbf.db().unregisterListener(iListener);
	}

	public void unregisterPojo(final Object iObject, final ODocument iRecord) {
		this.dbf.db().unregisterPojo(iObject, iRecord);
	}

	public void unsetDirty(final Object iPojo) {
		this.dbf.db().unsetDirty(iPojo);
	}

	private String getterName(final String propertyName) {
		return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1).toLowerCase();
	}

	private void loadDefaultClusters() {
		if (this.defaultClusters == null) {
			synchronized (this) {
				if (this.defaultClusters == null) {
					this.defaultClusters = new HashSet<>();
					for (final OClass oClass : this.dbf.db().getMetadata().getSchema().getClasses()) {
						final String defaultCluster = this.getClusterNameById(oClass.getDefaultClusterId());
						this.defaultClusters.add(defaultCluster);
					}
				}
			}
		}
	}
}