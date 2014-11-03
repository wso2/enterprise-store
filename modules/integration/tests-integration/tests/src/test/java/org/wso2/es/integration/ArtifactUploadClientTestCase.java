/*
 * Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.es.integration;

import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.carbon.integration.common.utils.LoginLogoutClient;
import org.wso2.es.integration.common.utils.ESIntegrationTest;
import org.wso2.es.integration.common.utils.ESIntegrationTestConstants;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class ArtifactUploadClientTestCase extends ESIntegrationTest {

	protected static final Logger log = Logger.getLogger(ArtifactUploadClientTestCase.class);
	private String esHost;
	private String esPort;
	private String esUser;
	private String esPwd;
	private String carbonHome = "";

	@BeforeClass(alwaysRun = true)
	public void init()
			throws XPathExpressionException, IOException, URISyntaxException, SAXException,
			       XMLStreamException, LoginAuthenticationExceptionException {

		esContext = new AutomationContext(ESIntegrationTestConstants.ES_PRODUCT_NAME,
		                                  TestUserMode.SUPER_TENANT_ADMIN);
		LoginLogoutClient loginLogoutClient = new LoginLogoutClient(esContext);
		sessionCookie = loginLogoutClient.login();
		carbonHome = System.getProperty("carbon.home");
		esUser = esContext.getContextTenant().getContextUser().getUserName();
		esPwd = esContext.getContextTenant().getContextUser().getPassword();
		esHost = esContext.getDefaultInstance().getHosts().get("default");
		esPort = esContext.getDefaultInstance().getPorts().get("https");
	}

	@Test(groups = "wso2.es")
	public void publisherTest() throws Exception {

		Process proc = Runtime.getRuntime().exec("java -jar " + carbonHome +
		                                         "/lib/asset-client-2.0.0-SNAPSHOT-jar-with-dependencies.jar -host " +
		                                         esHost + " -port " + esPort + " -user " +
		                                         esUser +
		                                         " -pwd " + esPwd + " -location " + carbonHome +
		                                         File.separator + "samples");
		InputStream in = proc.getInputStream();
		InputStream errorIn =     proc.getErrorStream();

		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, "utf-8");
		String consoleMsg = writer.toString();

		writer = new StringWriter();
		IOUtils.copy(errorIn, writer, "utf-8");
		String errorString = writer.toString();
		log.info(consoleMsg);
		Assert.assertEquals("Assets not uploaded successfully", 0, errorString.length());
	}

}
