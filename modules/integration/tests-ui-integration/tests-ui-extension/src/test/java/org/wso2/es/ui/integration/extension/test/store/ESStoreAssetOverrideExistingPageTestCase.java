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

package org.wso2.es.ui.integration.extension.test.store;

import static org.testng.Assert.*;

import org.openqa.selenium.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.extensions.selenium.BrowserManager;
import org.wso2.es.ui.integration.util.BaseUITestCase;
import org.wso2.es.ui.integration.util.ESWebDriver;

public class ESStoreAssetOverrideExistingPageTestCase extends BaseUITestCase {

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        super.init();
        driver = new ESWebDriver(BrowserManager.getWebDriver());
        baseUrl = getWebAppURL();
        driver.get(baseUrl + "/" + STORE_APP);
        //ESUtil.login(driver, baseUrl, webApp);
    }

    @Test(groups = "wso2.es.extensions", description = "Overriding list page via extensions test case")
    public void testESStoreAssetOverrideExistingPageTestCase() throws Exception {

        driver.get(baseUrl + "/store/asts/servicex/list");
        assertTrue(isElementPresent(By.id("assetListingPageH1")));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        // ESUtil.logout(driver, baseUrl,webApp);
        driver.quit();
    }

}
