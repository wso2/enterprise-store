/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.es.ui.integration.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.carbon.automation.engine.context.beans.Tenant;
import org.wso2.es.integration.common.clients.ResourceAdminServiceClient;
import org.wso2.es.integration.common.utils.ESIntegrationUITest;

import static org.testng.Assert.fail;

/**
 * This is the Basic UI test class by which all the other ui-test-cases are extended.
 * This class contains common methods called in ui-test cases and basic variables used in all
 * test cases
 */
public abstract class BaseUITestCase extends ESIntegrationUITest {

    private static final Log LOG = LogFactory.getLog(BaseUITestCase.class);
    protected ESWebDriver driver;
    protected String baseUrl;
    protected String backendURL;
    protected WebDriverWait wait;
    protected boolean acceptNextAlert = true;

    protected static final String PRODUCT_GROUP_NAME = "ES";
    protected static final String PUBLISHER_APP = "publisher";
    protected static final String STORE_APP = "store";
    protected static final String STORE_URL = "/store";
    protected static final String PUBLISHER_URL = "/publisher";
    protected static final String MANAGEMENT_CONSOLE_URL = "/carbon/";
    protected static final String PUBLISHER_LOGOUT_URL = "/publisher/logout";
    protected static final String STORE_LOGOUT_URL = "/store/logout";
    protected static final String PUBLISHER_GADGET_LIST_PAGE = "/publisher/assets/gadget/list";
    protected static final String PUBLISHER_GADGET_CREATE_PAGE = "/publisher/assets/gadget/create";
    protected static final String STORE_GADGET_LIST_PAGE = "/store/assets/gadget/list";
    protected static final String STORE_TOP_ASSETS_PAGE = "/store/pages/top-assets";
    protected static final String GADGET_REGISTRY_BASE_PATH = "/_system/governance/gadgets/";
    protected static final String SITE_REGISTRY_BASE_PATH = "/_system/governance/sites/";
    protected static final String NONE_EXIST_TENANT_DOMAIN = "foo.com";
    protected static final String ERROR_404 = "Error 404";

    protected static final int MAX_DRIVER_WAIT_TIME_SEC = 30;

    protected String currentUserName;
    protected String currentUserPwd;
    protected String adminUserName;
    protected String adminUserPwd;
    protected String providerName;
    protected Tenant tenantDetails;

    protected String resourcePath;
    protected String smtpPropertyLocation;
    protected ResourceAdminServiceClient resourceAdminServiceClient;

    /**
     * This method check whether the given element is present in the current driver instance
     *
     * @param by By element to be present
     * @return boolean true/false
     */
    protected static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested element is not present", e);
            }
            return false;
        } catch (TimeoutException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested element is not present", e);
            }
            return false;
        }
    }

    /**
     * This method check whether a alert is present
     *
     * @return boolean true/false
     */
    protected static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No alert found", e);
            }
            return false;
        }
    }

    /**
     * This method close the alert and return its text
     *
     * @return String - the text of the alert
     */
    protected static String closeAlertAndGetItsText(WebDriver driver, boolean acceptAlert) {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        if (acceptAlert) {
            alert.accept();
        } else {
            alert.dismiss();
        }
        return alertText;
    }

    protected void buildTenantDetails(TestUserMode userMode) throws Exception {
        AutomationContext automationContext = new AutomationContext(PRODUCT_GROUP_NAME, userMode);
        tenantDetails = automationContext.getContextTenant();
    }
}
