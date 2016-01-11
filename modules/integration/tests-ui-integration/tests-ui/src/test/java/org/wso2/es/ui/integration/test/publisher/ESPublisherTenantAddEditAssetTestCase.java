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

package org.wso2.es.ui.integration.test.publisher;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.automation.extensions.selenium.BrowserManager;
import org.wso2.es.integration.common.clients.ResourceAdminServiceClient;
import org.wso2.es.ui.integration.util.BaseUITestCase;
import org.wso2.es.ui.integration.util.ESUtil;
import org.wso2.es.ui.integration.util.ESWebDriver;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

/**
 * Add and Edit asset test for Tenant:Tenant Admin & Tenant User
 */
public class ESPublisherTenantAddEditAssetTestCase extends BaseUITestCase {

    private ResourceAdminServiceClient resourceAdminServiceClient;
    private TestUserMode userMode;
    private static final String ASSET_VERSION = "1.0.0";
    private static final String ASSET_CREATED_TIME = "12";
    private static final String ASSET_URL_1 = "http://test";
    private static final String ASSET_URL_2 = "http://wso2.com/";
    private static final String ASSET_VERSION_1 = "1.0.0";
    private static final String ASSET_VERSION_2 = "2.0.0";
    private static final String ASSET_DESCRIPTION_1 = "for store";
    private static final String ASSET_DESCRIPTION_2 = "Edited Test description";
    private static final String ASSET_CATEGORY_1 = "Google";
    private static final String ASSET_CATEGORY_2 = "WSO2";
    private static final int MAX_POLL_COUNT = 30;
    private String assetName;

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
        driver = new ESWebDriver(BrowserManager.getWebDriver());
        baseUrl = getStorePublisherUrl();
        AutomationContext automationContext = new AutomationContext(PRODUCT_GROUP_NAME, TestUserMode.TENANT_ADMIN);
        adminUserName = automationContext.getContextTenant().getTenantAdmin().getUserName();
        adminUserPwd = automationContext.getContextTenant().getTenantAdmin().getPassword();
        backendURL = automationContext.getContextUrls().getBackEndUrl();
        resourceAdminServiceClient = new ResourceAdminServiceClient(backendURL, adminUserName, adminUserPwd);
        this.providerName = currentUserName.split("@")[0];
        this.resourcePath = GADGET_REGISTRY_BASE_PATH + providerName + "/" + assetName + "/" + ASSET_VERSION;
        ESUtil.login(driver, baseUrl, PUBLISHER_APP, currentUserName, currentUserPwd);
    }

    @Test(groups = "wso2.es.publisher", description = "Testing adding a new asset")
    public void testAddAsset() throws Exception {
        driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Addgadget")));
        driver.findElement(By.id("Addgadget")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("overview_name")));
        driver.findElement(By.name("overview_name")).clear();
        driver.findElement(By.name("overview_name")).sendKeys(assetName);
        driver.findElement(By.name("overview_version")).clear();
        driver.findElement(By.name("overview_version")).sendKeys(ASSET_VERSION_1);
        driver.findElement(By.name("overview_url")).clear();
        driver.findElement(By.name("overview_url")).sendKeys(ASSET_URL_1);
        driver.findElement(By.name("overview_description")).clear();
        driver.findElement(By.name("overview_description")).sendKeys(ASSET_DESCRIPTION_1);
        driver.findElement(By.name("images_thumbnail")).sendKeys(FrameworkPathUtil.getReportLocation()
                                                                 +"/../src/test/resources/images/thumbnail.jpg");
        driver.findElement(By.name("images_banner")).sendKeys(FrameworkPathUtil.getReportLocation()
                                                              + "/../src/test/resources/images/banner.jpg");
        driver.findElement(By.id("btn-create-asset")).click();

        //driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Addgadget")));

        driver.findElementPoll(By.linkText(assetName), MAX_POLL_COUNT);
        //check if the created gadget is shown
        assertTrue(isElementPresent(driver, By.linkText(assetName)), "Adding an asset failed for user:" + currentUserName);
        driver.findElement(By.linkText(assetName)).click();
        assertEquals(assetName, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(2) div:nth-child(2)")).getText());
        assertEquals(ASSET_VERSION_1, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(3) div:nth-child(2)")).getText(),
                     "Incorrect version");
        assertEquals(ASSET_CATEGORY_1, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(4) div:nth-child(2)")).getText());
        assertEquals(ASSET_URL_1, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(5) div:nth-child(2)")).getText(),
                     "Incorrect URL");
        assertEquals(ASSET_DESCRIPTION_1, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(6) div:nth-child(2)")).getText(),
                     "Incorrect description");
    }

    @Test(groups = "wso2.es.publisher", description = "Testing editing an asset",
            dependsOnMethods = "testAddAsset")
    public void testEditAsset() throws Exception {
        driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(assetName)));
        driver.findElement(By.linkText(assetName)).click();
        //TODO edit is not visible in the UI for tenant admins ( check this and uncomment this code segment )
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Edit")));
        driver.findElement(By.id("Edit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("overview_category")));
        new Select(driver.findElement(By.name("overview_category"))).selectByVisibleText(ASSET_CATEGORY_2);
        driver.findElement(By.name("overview_url")).clear();
        driver.findElement(By.name("overview_url")).sendKeys(ASSET_URL_2);
        driver.findElement(By.name("overview_description")).clear();
        driver.findElement(By.name("overview_description")).sendKeys(ASSET_DESCRIPTION_2);
        driver.findElement(By.id("editAssetButton")).click();
        //closeAlertAndGetItsText(driver, true);
        //check updated info
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#collapseOverview div:nth-child(2) div:nth-child(2)")));

        assertEquals(assetName, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(2) div:nth-child(2)")).getText());
        assertEquals(ASSET_CATEGORY_2, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(4) div:nth-child(2)")).getText());
        assertEquals(ASSET_URL_2, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(5) div:nth-child(2)")).getText(),
                     "Incorrect URL");
        assertEquals(ASSET_DESCRIPTION_2, driver.findElement(By.cssSelector("#collapseOverview div:nth-child(6) div:nth-child(2)")).getText(),
                     "Incorrect description");

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        //delete resources and logout
        resourceAdminServiceClient.deleteResource(resourcePath);
        driver.get(baseUrl + PUBLISHER_LOGOUT_URL);
        driver.quit();
    }

    @DataProvider(name = "userMode")
    private static Object[][] userModeProvider() {
        return new Object[][]{{TestUserMode.TENANT_ADMIN, "Add Edit asset - TenantAdmin"},
                {TestUserMode.TENANT_USER, "Add Edit asset - TenantUser"}};
    }

}
