/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jelly.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import java.net.URL;

import org.apache.commons.jelly.Jelly;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Makes sure that nested includes work correctly
 *
 * @author Morgan Delagrange
 * @version $Revision$
 */
public class TestIncludeTag extends TestCase {

	public JellyContext mockJellyContext1() {
		JellyContext mockInstance = spy(JellyContext.class);
		doAnswer((stubInvo) -> {
			String namespaceURI = stubInvo.getArgument(0);
			if (namespaceURI.equals("jelly:core")) {
				return stubInvo.callRealMethod();
			}
			throw new NoClassDefFoundError("Unexpected tag library uri: " + namespaceURI);
		}).when(mockInstance).getTagLibrary(any());
		return mockInstance;
	}

	Jelly jelly = null;
	JellyContext context = null;
	XMLOutput xmlOutput = null;

	public TestIncludeTag(String name) {
		super(name);
	}

	public static TestSuite suite() throws Exception {
		return new TestSuite(TestIncludeTag.class);
	}

	public void setUp(String scriptName) throws Exception {
		URL url = this.getClass().getResource(scriptName);
		if (url == null) {
			throw new Exception("Could not find Jelly script: " + scriptName + " in package of class: "
					+ this.getClass().getName());
		}
		setUpFromURL(url);
	}

	public void setUpFromURL(URL url) throws Exception {
		context = mockJellyContext1();
		xmlOutput = XMLOutput.createDummyXMLOutput();

		jelly = new Jelly();

		jelly.setUrl(url);

		String exturl = url.toExternalForm();
		int lastSlash = exturl.lastIndexOf("/");
		String extBase = exturl.substring(0, lastSlash + 1);
		URL baseurl = new URL(extBase);
		context.setCurrentURL(baseurl);
	}

	public void testInnermost() throws Exception {
		// performs no includes
		setUp("c.jelly");
		Script script = jelly.compileScript();
		script.run(context, xmlOutput);
		assertTrue("should have set 'c' variable to 'true'", context.getVariable("c").equals("true"));
	}

	public void testMiddle() throws Exception {
		// performs one include
		setUp("b.jelly");
		Script script = jelly.compileScript();
		script.run(context, xmlOutput);
		assertTrue("should have set 'c' variable to 'true'", context.getVariable("c").equals("true"));
		assertTrue("should have set 'b' variable to 'true'", context.getVariable("b").equals("true"));
	}

	public void testOutermost() throws Exception {
		// performs one nested include
		setUp("a.jelly");
		Script script = jelly.compileScript();
		script.run(context, xmlOutput);
		assertTrue("should have set 'c' variable to 'true'", context.getVariable("c").equals("true"));
		assertTrue("should have set 'b' variable to 'true'", context.getVariable("b").equals("true"));
		assertTrue("should have set 'a' variable to 'true'", context.getVariable("a").equals("true"));
	}

	/**
	 * Insure that includes happen correctly when Jelly scripts are referenced as a
	 * file (rather than as a classpath element). Specifically checks to make sure
	 * includes succeed when the initial script is not in the user.dir directory.
	 */
	public void testFileInclude() throws Exception {
		// testing outermost
		setUpFromURL(new URL("file:src/test/resources/org/apache/commons/jelly/core/a.jelly"));
		Script script = jelly.compileScript();
		script.run(context, xmlOutput);
		assertTrue("should have set 'c' variable to 'true'", context.getVariable("c").equals("true"));
		assertTrue("should have set 'b' variable to 'true'", context.getVariable("b").equals("true"));
		assertTrue("should have set 'a' variable to 'true'", context.getVariable("a").equals("true"));
	}

}
