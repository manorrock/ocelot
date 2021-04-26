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

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The builder used to build images and push them to Azure Container Registry.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AzureCliBuilder {
    
    /**
     * Stores the ACR name.
     */
    private String acrName;

    /**
     * Stores the image name.
     */
    private String imageName;
    
    /**
     * Stores the resource group location.
     */
    private final String rgLocation = "westus2";
    
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
     * Stores the verbose flag.
     */
    private boolean verbose;

    /**
     * Stores the working directory.
     */
    private File workingDirectory;

    /**
     * Build using ACR.
     *
     * @return the exit value.
     */
    private int acrBuild() throws Exception {
        System.out.println("[Builder] Building '" + imageName + "' image using ACR '" + acrName + "'");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("az");
        processArguments.add("acr");
        processArguments.add("build");
        processArguments.add("--image");
        processArguments.add(imageName);
        processArguments.add("--registry");
        processArguments.add(acrName);
        processArguments.add(".");
        if (verbose) {
            builder = builder.inheritIO();
        }
        if (workingDirectory != null) {
            builder = builder.directory(workingDirectory);
        }
        Process process = builder.command(processArguments).start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue();
    }

    /**
     * Does the ACR exist.
     *
     * @return true if it does, false otherwise.
     */
    private boolean acrExists() throws Exception {
        System.out.println("[Builder] Checking if ACR '" + acrName + "' exists");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("/usr/local/bin/az");
        processArguments.add("acr");
        processArguments.add("show");
        processArguments.add("--name");
        processArguments.add(acrName);
        processArguments.add("--resource-group");
        processArguments.add(rgName);
        Process process = builder.command(processArguments).start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue() == 0;
    }

    /**
     * Provision the ACR.
     *
     * @return the exit value.
     * @throws Exception when a serious error occurs.
     */
    private int acrProvision() throws Exception {
        if (!rgExists()) {
            rgProvision();
        }
        System.out.println("[Builder] Provisioning ACR '" + acrName + "'");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("az");
        processArguments.add("acr");
        processArguments.add("create");
        processArguments.add("--name");
        processArguments.add(acrName);
        processArguments.add("--resource-group");
        processArguments.add(rgName);
        processArguments.add("--sku");
        processArguments.add("Standard");
        processArguments.add("--admin-enabled");
        Process process = builder.command(processArguments).inheritIO().start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue();
    }

    /**
     * Execute the build.
     *
     * @return the exit value.
     * @throws Exception when a serious error occurs.
     */
    public int build() throws Exception {
        int exitValue = 0;
        if (acrName == null) {
            acrName = imageName;
        }
        if (rgName == null) {
            rgName = imageName;
        }
        if (!acrExists()) {
            exitValue = acrProvision();
        }
        if (exitValue == 0) {
            acrBuild();
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
     * Get the working directory.
     *
     * @return the working directory.
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Does the resource group exist.
     *
     * @return true if it does, false otherwise.
     */
    private boolean rgExists() throws Exception {
        System.out.println("[Builder] Checking if resource group '" + acrName + "' exists");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("az");
        processArguments.add("group");
        processArguments.add("show");
        processArguments.add("--name");
        processArguments.add(rgName);
        Process process = builder.command(processArguments).start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue() == 0;
    }

    /**
     * Provision the resource group.
     *
     * @return the exit value.
     * @throws Exception when a serious error occurs.
     */
    private int rgProvision() throws Exception {
        System.out.println("[Builder] Provisioning resource group '" + acrName + "'");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("az");
        processArguments.add("group");
        processArguments.add("create");
        processArguments.add("--name");
        processArguments.add(rgName);
        processArguments.add("--location");
        processArguments.add(rgLocation);
        Process process = builder.command(processArguments).inheritIO().start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue();
    }

    /**
     * Set the image name.
     *
     * @param imageName the imageName.
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

    /**
     * Set the verbose flag.
     * 
     * @param verbose the verbose flag.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Set the working directory.
     *
     * @param workingDirectory the working directory.
     */
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}
