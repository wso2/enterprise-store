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

package org.wso2.es.ui.integration.test.common;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.carbon.integration.common.admin.client.UserManagementClient;
import org.wso2.es.ui.integration.util.BaseUITestCase;
import org.wso2.es.ui.integration.util.ESWebDriver;

import static org.testng.Assert.assertTrue;

/**
 * Register a new user for ES
 * check login for store and publisher
 */
public class ESRegisterUserTestCase extends BaseUITestCase {

    private UserManagementClient userManagementClient;
    private String newUserName = "testusernew";
    private String newUserPwd = "testusernew";

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        super.init();
        driver = new ESWebDriver();
        baseUrl = getWebAppURL();
        AutomationContext automationContext = new AutomationContext("ES",
                TestUserMode.SUPER_TENANT_ADMIN);
        backendURL = automationContext.getContextUrls().getBackEndUrl();
        userManagementClient = new UserManagementClient(backendURL, userInfo.getUserName(),
                userInfo.getPassword());
    }

    @Test(groups = "wso2.es.common", description = "Testing user registration")
    public void testESRegisterUserTestCase() throws Exception {
        //Register new user
        driver.get(baseUrl + "/store");
        driver.findElement(By.id("btn-register")).click();
        driver.findElement(By.id("reg-username")).clear();
        driver.findElement(By.id("reg-username")).sendKeys(newUserName);
        driver.findElement(By.id("reg-password")).clear();
        driver.findElement(By.id("reg-password")).sendKeys(newUserPwd);
        driver.findElement(By.id("reg-password2")).clear();
        driver.findElement(By.id("reg-password2")).sendKeys(newUserPwd);
        driver.findElement(By.id("registrationSubmit")).click();
        //check login for store
        assertTrue(isElementPresent(By.linkText("My Items")), "Login failed for Store");
        assertTrue(isElementPresent(By.linkText(newUserName)), "Login failed for Store");
        //check login for publisher
        driver.get(baseUrl + "/publisher");
        assertTrue(isElementPresent(By.linkText(newUserName)), "Login failed for Publisher");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        //logout and delete new user
        driver.get(baseUrl + "/publisher/logout");
        userManagementClient.deleteUser(newUserName);
        driver.quit();
    }

}