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

package org.wso2.es.integration.common.utils.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes an installation manifest file consisting of multiple packages
 * with resources
 */
public class InstallationManifest {
    //private Collection<ResourcePackage> packages;
    private Map<String,ResourcePackage> packages;

    public InstallationManifest() {
        this.packages = new HashMap<String, ResourcePackage>(); //new ArrayList<ResourcePackage>();
    }

    /**
     * Returns a string list of package names
     * @return
     */
    public Collection<String> getResourcePackageNames() {
        return packages.keySet();
    }

    /**
     * Returns the ResourcePackage instance with the provided package name
     * @param packageName   The name of the package to be returned
     * @return
     */
    public ResourcePackage getResourcePackage(String packageName){
        ResourcePackage resourcePackage=null;
        if(this.packages.containsKey(packageName)){
                resourcePackage = this.packages.get(packageName);
        }
        return resourcePackage;
    }

    /**
     * Returns the list of packages to be installed
     * @return
     */
    public Collection<ResourcePackage> getResourcePackages() {
        return packages.values();
        //return packages;
    }
}
