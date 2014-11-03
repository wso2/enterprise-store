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

package org.wso2.es.ui.integration.test.store;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.es.integration.common.clients.ResourceAdminServiceClient;
import org.wso2.es.ui.integration.util.AssetUtil;
import org.wso2.es.ui.integration.util.BaseUITestCase;
import org.wso2.es.ui.integration.util.ESUtil;
import org.wso2.es.ui.integration.util.ESWebDriver;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Category and sorting test for Anonymous store
 */
public class ESStoreAnonCategorySortingTestCase extends BaseUITestCase {
    private ResourceAdminServiceClient resourceAdminServiceClient;

    private String popularAsset1;
    private String popularAsset2;
    private String review1 = "cool!";
    private String review2 = "awesome!";
    private String rating1 = "4";
    private String rating2 = "2";
    private String assetVersion = "1.0.0";
    private String assetCreatedTime = "12";
    private String assetType = "gadget";

    private static final String BAR_CHART = "Bar Chart";
    private static final String BUBBLE_CHART = "Bubble Chart";

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        super.init();
        assetName = "Asset Recent";
        driver = new ESWebDriver();
        wait = new WebDriverWait(driver, 30);
        baseUrl = getWebAppURL();
        currentUserName = userInfo.getUserName();
        currentUserPwd = userInfo.getPassword();

        resourcePath = "/_system/governance/gadgets/" + currentUserName + "/" + this
                .assetName + "/" + assetVersion;
        AutomationContext automationContext = new AutomationContext("ES",
                TestUserMode.SUPER_TENANT_ADMIN);
        String backendURL = automationContext.getContextUrls().getBackEndUrl();
        resourceAdminServiceClient = new ResourceAdminServiceClient(backendURL, currentUserName,
                currentUserPwd);

        //Rating two assets to check sorting on popularity
        driver.get(baseUrl + "/store/asts/gadget/list");
        popularAsset1 = driver.findElement(By.xpath
                ("//div[@id='assets-container']/div/div[4]/div/div/a/h4")).getText();
        driver.findElement(By.cssSelector("h4")).click();
        driver.findElement(By.linkText("User Reviews")).click();
        driver.switchTo().frame(driver.findElement(By.id("socialIfr")));
        driver.switchTo().defaultContent();
        ESUtil.login(driver, baseUrl, storeApp, currentUserName, currentUserPwd);
        driver.get(baseUrl + "/store/asts/gadget/list");
        driver.findElement(By.xpath("//div[@id='assets-container']/div/div[4]/div/div/a/h4"))
                .click();
        AssetUtil.addRatingsAndReviews(driver, review1, rating1);
        driver.get(baseUrl + "/store/asts/gadget/list");
        popularAsset2 = driver.findElement(By.xpath
                ("//div[@id='assets-container']/div[2]/div[3]/div/div/a/h4"))
                .getText();
        driver.findElement(By.xpath("//div[@id='assets-container']/div[2]/div[3]/div/div/a/h4"))
                .click();
        AssetUtil.addRatingsAndReviews(driver, review2, rating2);

        //navigate to publisher and add and publish a new gadget to support sort by created time
        driver.get(baseUrl + "/publisher");
        AssetUtil.addNewAsset(driver, baseUrl, assetType, currentUserName, assetName,
                assetVersion, assetCreatedTime);
        driver.findElementPoll(By.linkText(assetName), 30);
        AssetUtil.publishAssetToStore(driver, assetName);
        driver.get(baseUrl + "/publisher/logout");
        //navigate to store and wait for the new gadget to be visible in store
        driver.get(baseUrl + "/store/asts/gadget/list");
        driver.findElementPoll(By.xpath("//a[contains(.,'Asset Recent')]"), 5);
    }

    //TODO-disabled bug
    @Test(groups = "wso2.es.store", description = "Testing sorting on popularity", enabled = false)
    public void testStoreSortOnPopularity() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        //sort by popularity
        driver.findElement(By.cssSelector("i.icon-star")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h4"),
                popularAsset1));
        assertEquals(driver.findElement(By.cssSelector("h4")).getText(), popularAsset1,
                "Popularity Sort failed");
        assertEquals(driver.findElement(By.xpath
                ("//div[@id='assets-container']/div/div[2]/div/div/a/h4")).getText(),
                popularAsset2, "Popularity Sort failed");
    }

    @Test(groups = "wso2.es.store", description = "Testing sorting on alphabetical order")
    public void testStoreSortOnAlphabeticalOrder() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        //sort by alphabetical order
        driver.findElement(By.cssSelector("i.icon-sort-alphabetical")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath
                ("//div[@id='assets-container']/div/div[3]/div/div/a/h4"), BUBBLE_CHART));
        assertEquals(assetName, driver.findElement(By.cssSelector("h4")).getText(),
                "Alphabetical Sort failed");
        assertEquals(BAR_CHART, driver.findElement(By.xpath
                ("//div[@id='assets-container']/div/div[2]/div/div/a/h4")).getText(),
                "Alphabetical Sort failed");
    }

    @Test(groups = "wso2.es.store", description = "Testing sorting on created time")
    public void testStoreSortOnCreatedTime() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        //sort by created time
        driver.findElement(By.cssSelector("i.icon-calendar")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h4"),
                assetName));
        assertEquals(assetName, driver.findElement(By.cssSelector("h4")).getText()
                , "Recent Sort failed");
    }

    @Test(groups = "wso2.es.store", description = "Testing category Google")
    public void testCategoryGoogle() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        //google category
        driver.findElement(By.cssSelector("i.icon-caret-down")).click();
        driver.findElement(By.linkText("Google")).click();
        assertEquals(3, driver.findElements(By.cssSelector("div.asset-details")).size(),
                "Google Category wrong");
    }

    @Test(groups = "wso2.es.store", description = "Testing category WSO2")
    public void testCategoryWSO2() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        driver.findElement(By.cssSelector("i.icon-caret-down")).click();
        driver.findElement(By.linkText("WSO2")).click();
        assertEquals(5, driver.findElements(By.cssSelector("div.asset-details")).size(),
                "WSO2 Category wrong");
    }

    @Test(groups = "wso2.es.store", description = "Testing category template")
    public void testCategoryTemplate() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        driver.findElement(By.cssSelector("i.icon-caret-down")).click();
        driver.findElement(By.linkText("Templates")).click();
        assertEquals(6, driver.findElements(By.cssSelector("div.span3.asset")).size(),
                "Templates Category wrong");
    }

    //TODO-BUG
    @Test(groups = "wso2.es.store", description = "Testing category drop down", enabled = false)
    public void testCategoryMenu() throws Exception {
        driver.get(baseUrl + "/store/asts/gadget/list");
        driver.findElement(By.cssSelector("i.icon-caret-down")).click();
        driver.findElement(By.linkText("Templates")).click();
        assertEquals("Templates", driver.findElement(By.cssSelector("div.breadcrumb-head"))
                .getText(), "Category drop down not showing selected category ");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        resourceAdminServiceClient.deleteResource(resourcePath);
        driver.quit();
    }
}
