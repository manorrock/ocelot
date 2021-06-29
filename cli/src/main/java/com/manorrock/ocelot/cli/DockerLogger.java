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

import java.util.ArrayList;

/**
 * The Docker logger.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DockerLogger {
    
    /**
     * Stores the application name.
     */
    private String applicationName;

    /**
     * Execute the logger.
     *
     * @return the exit value.
     * @throws Exception when a serious error occurs.
     */
    public int log() throws Exception {
        System.out.println("[Logger] Attaching to Docker container '" + applicationName + "' to stream logs");
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("docker");
        processArguments.add("logs");
        processArguments.add("-f");
        processArguments.add(applicationName);
        Process process = builder.command(processArguments).inheritIO().start();
        process.waitFor();
        return process.exitValue();
    }

    /**
     * Set the application name.
     * 
     * @param applicationName the application name.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
