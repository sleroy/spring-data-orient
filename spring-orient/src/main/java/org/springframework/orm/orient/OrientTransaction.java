package org.springframework.orm.orient;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.tx.OTransaction;

/**
 * The specific Orient Transaction.
 *
 * @author Dzmitry_Naskou
 */
public class OrientTransaction {

	/** The orient tx object. */
	private OTransaction	    tx;

	/** The database. */
	private ODatabaseDocumentTx	database;

	public ODatabaseDocumentTx getDatabase() {
		return this.database;
	}

	public OTransaction getTx() {
		return this.tx;
	}

	public void setDatabase(final ODatabaseDocumentTx database) {
		this.database = database;
	}

	public void setTx(final OTransaction tx) {
		this.tx = tx;
	}
}
