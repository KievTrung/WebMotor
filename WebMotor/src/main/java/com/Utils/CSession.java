package com.Utils;

import org.hibernate.Session;

public class CSession implements AutoCloseable{//closeable session
	private final Session session;

	public CSession(Session session) {
		super();
		this.session = session;
	}

	public Session getSession() {
		return session;
	}
	
	@Override
	public void close() {
		session.close();
	}
}
