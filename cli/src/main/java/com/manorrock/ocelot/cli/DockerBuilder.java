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
 * The Docker builder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DockerBuilder {

    /**
     * Stores the image name.
     */
    private String imageName;

    /**
     * Stores working directory.
     */
    private File workingDirectory;

    /**
     * Stores the timeout.
     */
    private long timeout;

    /**
     * Stores the timeout unit (e.g. seconds, minutes, hours, days).
     */
    private String timeoutUnit;
    
    /**
     * Stores the verbose flag.
     */
    private boolean verbose;

    /**
     * Execute the build.
     *
     * @return the exit value.
     * @throws Exception when a serious error occurs.
     */
    public int build() throws Exception {
        System.out.println("[Builder] Building '" + imageName + "' image");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("docker");
        processArguments.add("build");
        processArguments.add("-t");
        processArguments.add(imageName);
        processArguments.add("-f");
        processArguments.add("Dockerfile");
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

    /**
     * Set the working directory.
     *
     * @param workingDirectory the working directory.
     */
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * Set the verbose flag.
     * 
     * @param verbose the verbose flag.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
