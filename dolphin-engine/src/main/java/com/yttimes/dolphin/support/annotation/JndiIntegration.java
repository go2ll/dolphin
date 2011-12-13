package com.yttimes.dolphin.support.annotation;

import javax.naming.NamingException;

public class JndiIntegration {

	private JndiIntegration() {
	}

	public static <T> T fromJndi(Class<T> type, String name) {
		return new JndiProvider<T>(type, name).get();
	}

	private static class JndiProvider<T>  {
		final Class<T> type;
		final String name;

		public JndiProvider(Class<T> type, String name) {
			this.type = type;
			this.name = name;
		}

		public T get() {
			try {
				System.out.println("look up jndi name:"+name);
				return type.cast(ContextFactory.getInstance().getContext().lookup(name));
			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
