/*
 *  Copyright (c) 2002-2021, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *      3. Neither the name of the copyright holder nor the names of its 
 *         contributors may be used to endorse or promote products derived from
 *         this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.ocelot.azure;

import java.io.IOException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.WARNING;
import java.util.ArrayList;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The Azure Container Instance deployer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AzureContainerInstanceDeployer implements Deployer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(AzureContainerInstanceDeployer.class.getName());
    
    /**
     * Stores the image.
     */
    private String image;

    /**
     * Stores the name.
     */
    private String name;
    
    /**
     * Stores the registry username.
     */
    private String registryUsername;
    
    /**
     * Stores the registry password.
     */
    private String registryPassword;

    /**
     * Stores the resource group.
     */
    private String resourceGroup;
    
    @Override
    public int deploy() {
        int exitValue = -1;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            ArrayList<String> processArguments = new ArrayList<>();
            processArguments.add("az");
            processArguments.add("container");
            processArguments.add("create");
            processArguments.add("--resource-group");
            processArguments.add(resourceGroup);
            processArguments.add("--name");
            processArguments.add(name);
            processArguments.add("--image");
            processArguments.add(image);
            processArguments.add("--registry-username");
            processArguments.add(registryUsername);
            processArguments.add("--registry-password");
            processArguments.add(registryPassword);
            processArguments.add("--ip-address");
            processArguments.add("Public");
            processArguments.add("--ports");
            processArguments.add("8080");
            Process process = builder.command(processArguments).inheritIO().start();
            process.waitFor(600, SECONDS);
        } catch (IOException | InterruptedException e) {
            LOGGER.log(WARNING, "Unable to deploy the container instance", e);
        }
        return exitValue;
    }

    @Override
    public int undeploy() {
        int exitValue = -1;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            ArrayList<String> processArguments = new ArrayList<>();
            processArguments.add("az");
            processArguments.add("container");
            processArguments.add("delete");
            processArguments.add("--name");
            processArguments.add(name);
            Process process = builder.command(processArguments).inheritIO().start();
            process.waitFor(600, SECONDS);
            exitValue = process.exitValue();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(WARNING, "Unable to undeploy the container instance", e);
        }
        return exitValue;
    }
}
