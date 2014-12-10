package org.springframework.orientdb.session;

import org.springframework.orientdb.session.impl.DatabaseConfiguration;

/**
 * This exception is thrown when a local or memory database could not be
 * created.
 *
 * @author sleroy
 *
 */
public class CouldNotCreateDatabaseException extends RuntimeException {

	private static final long	        serialVersionUID	= -6870158107186093090L;
	private final DatabaseConfiguration	configuration;

	public CouldNotCreateDatabaseException(final DatabaseConfiguration _configuration, final Exception _throwable) {
		super("Could not create the database " + _configuration, _throwable);
		this.configuration = _configuration;

	}

	@Override
	public String toString() {
		return "CouldNotCreateDatabaseException [configuration=" + this.configuration + ", toString()="
				+ super.toString() + "]";
	}

}
