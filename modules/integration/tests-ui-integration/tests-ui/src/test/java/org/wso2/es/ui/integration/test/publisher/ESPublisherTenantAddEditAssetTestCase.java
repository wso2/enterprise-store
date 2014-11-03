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

package org.wso2.es.ui.integration.test.publisher;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.es.integration.common.clients.ResourceAdminServiceClient;
import org.wso2.es.ui.integration.util.BaseUITestCase;
import org.wso2.es.ui.integration.util.ESUtil;
import org.wso2.es.ui.integration.util.ESWebDriver;
import java.io.File;
import static org.testng.Assert.*;

/**
 * Add and Edit asset test for Tenant:Tenant Admin & Tenant User
 */
public class ESPublisherTenantAddEditAssetTestCase extends BaseUITestCase {

    private ResourceAdminServiceClient resourceAdminServiceClient;
    private String resourceLocation;

    private String email = "esmailsample@gmail.com";
    private String emailPwd = "esMailTest";
    private TestUserMode userMode;

    private String assetVersion = "1.0.0";
    private String assetCreatedTime = "12";
    private String assetUrl1 = "http://test";
    private String assetUrl2 = "http://wso2.com/";
    private String assetDescription1= "for store";
    private String assetDescription2 = "Edited Test description";
    private String assetCategory1 = "Google";
    private String assetCategory2 = "WSO2";

    @Factory(dataProvider = "userMode")
    public ESPublisherTenantAddEditAssetTestCase(TestUserMode userMode, String assetName) {
        this.userMode = userMode;
        this.assetName = assetName;
    }

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        super.init(userMode);
        currentUserName = userInfo.getUserName();
        currentUserPwd = userInfo.getPassword();
        driver = new ESWebDriver();
        baseUrl = getStorePublisherUrl();
        AutomationContext automationContext = new AutomationContext("ES",
                TestUserMode.TENANT_ADMIN);
        adminUserName = automationContext.getContextTenant().getTenantAdmin().getUserName();
        adminUserPwd = automationContext.getContextTenant().getTenantAdmin().getPassword();
        backendURL = automationContext.getContextUrls().getBackEndUrl();
        resourceLocation = getResourceLocation();
        resourceAdminServiceClient = new ResourceAdminServiceClient(backendURL, adminUserName,
                adminUserPwd);
        this.providerName = currentUserName.split("@")[0];
        this.resourcePath = "/_system/governance/gadgets/" + providerName + "/" + assetName + "/" +
                assetVersion;
        smtpPropertyLocation = resourceLocation + File.separator + "notifications" + File
                .separator + "smtp.properties";

        ESUtil.login(driver, baseUrl, publisherApp, currentUserName, currentUserPwd);
    }

    @Test(groups = "wso2.es.publisher", description = "Testing adding a new asset")
    public void testAddAsset() throws Exception {
        driver.get(baseUrl + "/publisher/asts/gadget/list");
        driver.findElement(By.linkText("Add")).click();
        driver.findElement(By.name("overview_provider")).clear();
        driver.findElement(By.name("overview_provider")).sendKeys(providerName);
        driver.findElement(By.name("overview_name")).clear();
        driver.findElement(By.name("overview_name")).sendKeys(assetName);
        driver.findElement(By.name("overview_version")).clear();
        driver.findElement(By.name("overview_version")).sendKeys(assetVersion);
        driver.findElement(By.name("overview_createdtime")).clear();
        driver.findElement(By.name("overview_createdtime")).sendKeys(assetCreatedTime);
        new Select(driver.findElement(By.name("overview_category"))).selectByVisibleText
                (assetCategory1);
        driver.findElement(By.name("overview_url")).clear();
        driver.findElement(By.name("overview_url")).sendKeys(assetUrl1);
        driver.findElement(By.name("overview_description")).clear();
        driver.findElement(By.name("overview_description")).sendKeys(assetDescription1);
        driver.findElement(By.id("btn-create-asset")).click();

        driver.findElementPoll(By.linkText(assetName), 30);
        //check if the created gadget is shown
        assertTrue(isElementPresent(By.linkText(assetName)), "Adding an asset failed for user:" +
                currentUserName);
    }

    @Test(groups = "wso2.es.publisher", description = "Testing editing an asset",
            dependsOnMethods = "testAddAsset")
    public void testEditAsset() throws Exception {
        driver.get(baseUrl + "/publisher/asts/gadget/list");
        driver.findElement(By.linkText(assetName)).click();
        driver.findElement(By.linkText("Edit")).click();
        new Select(driver.findElement(By.name("overview_category"))).selectByVisibleText
                (assetCategory2);
        driver.findElement(By.name("overview_url")).clear();
        driver.findElement(By.name("overview_url")).sendKeys(assetUrl2);
        driver.findElement(By.name("overview_description")).clear();
        driver.findElement(By.name("overview_description")).sendKeys(assetDescription2);
        driver.findElement(By.id("editAssetButton")).click();
        closeAlertAndGetItsText();

        //check updated info
        driver.findElement(By.linkText("Overview")).click();
        assertEquals(assetName, driver.findElement(By.cssSelector("h4")).getText());
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='view']/div[2]/div/div/div[2]/table[2]/tbody/tr/td[2]")).getText
                (), providerName, "Incorrect provider");
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='view']/div[2]/div/div/div[2]/table[2]/tbody/tr[2]/td[2]"))
                .getText(), assetName, "Incorrect asset name");
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='view']/div[2]/div/div/div[2]/table[2]/tbody/tr[3]/td[2]"))
                .getText(), assetVersion, "Incorrect version");
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='view']/div[2]/div/div/div[2]/table[2]/tbody/tr[5]/td[2]"))
                .getText(), "WSO2", "Incorrect Category");
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='view']/div[2]/div/div/div[2]/table[2]/tbody/tr[6]/td[2]"))
                .getText(), assetUrl2, "Incorrect URL");
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='view']/div[2]/div/div/div[2]/table[2]/tbody/tr[7]/td[2]"))
                .getText(), assetDescription2, "Incorrect description");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        //delete resources and logout
        resourceAdminServiceClient.deleteResource(resourcePath);
        driver.get(baseUrl + "/publisher/logout");
        ESUtil.deleteAllEmail(smtpPropertyLocation, emailPwd, email);
        driver.quit();
    }

    @DataProvider(name = "userMode")
    private static Object[][] userModeProvider() {
        return new Object[][]{{TestUserMode.TENANT_ADMIN, "Add Edit asset - TenantAdmin"},
                {TestUserMode.TENANT_USER, "Add Edit asset - TenantUser"}};
    }

}
