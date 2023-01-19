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
 * The Azure Resource Group deployer.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AzureResourceGroupDeployer implements Deployer {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(AzureResourceGroupDeployer.class.getName());

    /**
     * Stores the name of the resource group.
     */
    private String name;
    
    /**
     * Stores the location.
     */
    private String location;
    
    @Override
    public int deploy() {
        int exitValue = -1;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            ArrayList<String> processArguments = new ArrayList<>();
            processArguments.add("az");
            processArguments.add("group");
            processArguments.add("create");
            processArguments.add("--name");
            processArguments.add(name);
            processArguments.add("--location");
            processArguments.add(location);
            Process process = builder.command(processArguments).inheritIO().start();
            process.waitFor(600, SECONDS);        
            exitValue =  process.exitValue();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(WARNING, "Unable to deploy resource group", e);
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
            processArguments.add("group");
            processArguments.add("delete");
            processArguments.add("--name");
            processArguments.add(name);
            Process process = builder.command(processArguments).inheritIO().start();
            process.waitFor(600, SECONDS);        
            exitValue =  process.exitValue();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(WARNING, "Unable to undeploy resource group", e);
        }
        return exitValue;
    }
}