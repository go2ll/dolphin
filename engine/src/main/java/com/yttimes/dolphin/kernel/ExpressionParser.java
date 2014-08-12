/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yttimes.dolphin.kernel;

import com.yttimes.dolphin.exception.ExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * @author liyongchao
 */
public class ExpressionParser {

  Logger logger = LoggerFactory.getLogger(ExpressionParser.class);

  private static ScriptEngineManager SM = new ScriptEngineManager();
  private ScriptEngine se;

  public ExpressionParser() {
    se = SM.getEngineByName("JavaScript");
  }

  public boolean evaluate(String condition) {
    Object o = eval(condition);
    if (o instanceof Boolean) {
      return Boolean.valueOf(o.toString());
    } else {
      throw new ExpressionException("condition can't resolve to boolean");
    }

  }

  public boolean evaluate(String condition, Map<String, String> params) {
    logger.debug("condition=" + condition);
    logger.debug("params=" + params);
    Bindings bindings = new SimpleBindings();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      bindings.put(entry.getKey(), entry.getValue());
    }
    Object o = evalHasBindings(condition, bindings);
    if (o instanceof Boolean) {
      return Boolean.valueOf(o.toString());
    } else {
      throw new ExpressionException("condition can't resolve to boolean");
    }
  }

  public Object eval(String condition) {
    try {
      return se.eval(filterCondition(condition));
    } catch (ScriptException e) {
      throw new ExpressionException("script eval error, condition=" + condition, e);
    }
  }

  public Object evalHasBindings(String condition, Bindings bindings) {
    try {
      return se.eval(filterCondition(condition), bindings);
    } catch (ScriptException e) {
      Pattern p = Pattern.compile("\\$([a-zA-Z]|[0-9])+");
      Matcher m = p.matcher(condition);
      String keys = "";
      while (m.find()) {
        String str = condition.substring(m.start(), m.end()).replaceAll("\\$", "");
        if (!bindings.containsKey(str)) {
          keys += str + ",";
        }
      }
      throw new ExpressionException("script eval error, condition=" + condition, keys, e);
    }
  }

  private String filterCondition(String condition) {
    if (condition != null && !"".equals(condition)) {
      return condition.replaceAll("\\$", "");
    }
    throw new ExpressionException("condition cannot be null");
  }
}
