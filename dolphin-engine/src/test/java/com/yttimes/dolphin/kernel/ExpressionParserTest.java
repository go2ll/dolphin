/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yttimes.dolphin.kernel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.yttimes.dolphin.exception.ExpressionException;

/**
 *
 * @author liyongchao
 */
public class ExpressionParserTest {

	private ExpressionParser parser;

	@Before
	public void setUp() {
		parser = new ExpressionParser();
	}

	@Test
	public void testEval() {
		Assert.assertEquals(Boolean.valueOf("true"), parser.eval("1>0"));
	}

	@Test
	public void testEvalHasParam() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("a", "true");
		params.put("b", "3");
		Assert.assertEquals(true, parser.evaluate("a == 'true'", params));
		Assert.assertEquals(true, parser.evaluate("b>2", params));
	}

	@Test(expected = ExpressionException.class)
	public void testEvaluateException() {
		Map<String, String> params = new HashMap<String, String>();
		Assert.assertEquals(true, parser.evaluate("$a == 'true'", params));
	}

	@Test
	public void testEvaluateCatchException() {
		Map<String, String> params = new HashMap<String, String>();
		try {
			parser.evaluate("$a == 'true' && $num >= 4", params);
		} catch (ExpressionException e) {
			Assert.assertEquals(true, e.getKeys().split(",").length == 2);
			Assert.assertEquals(true, e.getKeys().split(",")[0].equals("a"));
			Assert.assertEquals(true, e.getKeys().split(",")[1].equals("num"));
		}
	}
}
