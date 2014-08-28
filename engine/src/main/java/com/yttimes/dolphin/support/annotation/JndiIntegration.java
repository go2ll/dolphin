package com.yttimes.dolphin.support.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;

public class JndiIntegration {

  private static Logger logger = LoggerFactory.getLogger(JndiIntegration.class);

  private JndiIntegration() {
  }

  public static <T> T fromJndi(Class<T> type, String name) {
    return new JndiProvider<T>(type, name).get();
  }

  private static class JndiProvider<T> {

    final Class<T> type;
    final String name;

    public JndiProvider(Class<T> type, String name) {
      this.type = type;
      this.name = name;
    }

    public T get() {
      try {
        logger.info("look up jndi name : " + name);
        return type.cast(ContextFactory.getInstance().getContext().lookup(name));
      } catch (NamingException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
