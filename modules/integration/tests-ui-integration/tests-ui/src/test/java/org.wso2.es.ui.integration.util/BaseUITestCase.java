/*
 * Copyright (c) 2014, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.es.ui.integration.util;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.wso2.es.integration.common.clients.ResourceAdminServiceClient;
import org.wso2.es.integration.common.utils.ESIntegrationUITest;

import static org.testng.Assert.fail;

/**
 * This is the Basic UI test class by which all the other ui-test-cases are extended.
 * This class contains common methods called in ui-test cases and basic variables used in all
 * test cases
 */
public abstract class BaseUITestCase extends ESIntegrationUITest {
    protected ESWebDriver driver;
    protected String baseUrl;
    protected String backendURL;
    protected WebDriverWait wait;
    protected boolean acceptNextAlert = true;
    protected String publisherApp = "publisher";
    protected String storeApp = "store";

    protected String currentUserName;
    protected String currentUserPwd;
    protected String adminUserName;
    protected String adminUserPwd;
    protected String assetName;
    protected String providerName;

    protected String resourcePath;
    protected String smtpPropertyLocation;
    protected ResourceAdminServiceClient resourceAdminServiceClient;

    /**
     * This method check whether the given element is present in the current driver instance
     * @param by By element to be present
     * @return boolean true/false
     */
    protected boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * This method check whether a alert is present
     * @return boolean true/false
     */
    protected boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    /**
     * This method close the alert and return its text
     * @return String - the text of the alert
     */
    protected String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    /**
     * This method helps to use alerts even if the earlier asserts in the same test case fails
     * @param verificationErrors
     * @return new verificationErrors buffer
     */
    protected StringBuffer failOnError(StringBuffer verificationErrors) {
        String verificationErrorString = verificationErrors.toString();
        StringBuffer errorBuffer = verificationErrors;
        if (!"".equals(verificationErrorString)) {
            errorBuffer = new StringBuffer();
            fail(verificationErrorString);
        }
        return errorBuffer;
    }

}
