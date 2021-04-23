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
package com.manorrock.ocelot.cli;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import static java.lang.ProcessBuilder.Redirect.PIPE;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The deployer used to deploy workloads to Azure.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AzureCliDeployer {

    /**
     * Stores the ACR password.
     */
    private String acrPassword;

    /**
     * Stores the ACR username.
     */
    private String acrUsername;

    /**
     * Store the image name.
     */
    private String imageName;
    
    /**
     * Stores the resource group name.
     */
    private String rgName;

    /**
     * Stores the timeout.
     */
    private long timeout;

    /**
     * Stores the timeout unit.
     */
    private String timeoutUnit;

    /**
     * Deploy to ACI.
     *
     * @return the exit value.
     */
    private int aciDeploy() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("az");
        processArguments.add("container");
        processArguments.add("create");
        processArguments.add("--resource-group");
        processArguments.add(rgName);
        processArguments.add("--name");
        processArguments.add(imageName);
        processArguments.add("--image");
        processArguments.add(imageName + ".azurecr.io/" + imageName);
        processArguments.add("--registry-username");
        processArguments.add(acrUsername);
        processArguments.add("--registry-password");
        processArguments.add(acrPassword);
        processArguments.add("--ip-address");
        processArguments.add("Public");
        processArguments.add("--ports");
        processArguments.add("8080");
        Process process = builder.command(processArguments).inheritIO().start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        
        if (process.exitValue() == 0) {
            builder = new ProcessBuilder();
            processArguments = new ArrayList<>();
            processArguments.add("az");
            processArguments.add("container");
            processArguments.add("show");
            processArguments.add("--resource-group");
            processArguments.add(rgName);
            processArguments.add("--name");
            processArguments.add(imageName);
            processArguments.add("--query");
            processArguments.add("ipAddress.ip");
            processArguments.add("--output");
            processArguments.add("tsv");
            process = builder.command(processArguments).redirectOutput(PIPE).start();
            process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(process.getInputStream()));
            String ipAddress = reader.readLine();
            System.out.println("[Deployer] Application is available at http://" + ipAddress + ":8080");
        }
        
        return process.exitValue();
    }

    /**
     * Get the ACR password.
     *
     * @return the exit value.
     */
    private int acrGetPassword() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("az");
        processArguments.add("acr");
        processArguments.add("credential");
        processArguments.add("show");
        processArguments.add("--name");
        processArguments.add(imageName);
        processArguments.add("--query");
        processArguments.add("passwords[0].value");
        processArguments.add("--output");
        processArguments.add("tsv");
        Process process = builder.command(processArguments).redirectOutput(PIPE).start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(process.getInputStream()));
        acrPassword = reader.readLine();
        return process.exitValue();
    }

    /**
     * Deploy.
     *
     * @return the exit value.
     * @throws Exception when a serious error occurs.
     */
    public int deploy() throws Exception {
        int exitValue;
        System.out.println("[Deployer] Starting deployment of '" + imageName + "' image");
        exitValue = acrGetPassword();
        if (exitValue == 0) {
            acrUsername = imageName;
            rgName = imageName;
            exitValue = aciDeploy();
        } else {
            System.out.println("[Deployer] Unable to deploy '" + imageName + "' image");
        }
        return exitValue;
    }

    /**
     * Get the image name.
     *
     * @return the image name.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Get the timeout.
     *
     * @return the timeout.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Get the timeout unit.
     *
     * @return the timeout unit.
     */
    public String getTimeoutUnit() {
        return timeoutUnit;
    }

    /**
     * Set the image name.
     *
     * @param imageName the image name.
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Set the timeout.
     *
     * @param timeout the timeout.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Set the timeout unit.
     *
     * @param timeoutUnit the timeout unit.
     */
    public void setTimeoutUnit(String timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }
}
