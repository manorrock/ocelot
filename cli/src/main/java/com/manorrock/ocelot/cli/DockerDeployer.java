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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The Docker deployer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DockerDeployer {
    
    /**
     * Stores the imag name.
     */
    private String imageName;
    
    /**
     * Stores the name.
     */
    private String name;
    
    /**
     * Stores the timeout.
     */
    private long timeout;
    
    /**
     * Stores the timeout unit.
     */
    private String timeoutUnit;

    /**
     * Set the image name.
     * 
     * @param imageName the image name.
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    /**
     * Set the name.
     * 
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the timeout.
     * 
     * @param timeout the timout.
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
     * Deploy.
     * 
     * @return the status code.
     */
    public int deploy() {
        try {
            System.out.println("[Deployer] Starting deployment of '" + imageName + "' image");
            
            ProcessBuilder builder = new ProcessBuilder();
            ArrayList<String> processArguments = new ArrayList<>();
            processArguments.add("docker");
            processArguments.add("run");
            processArguments.add("--rm");
            processArguments.add("--name");
            processArguments.add(name);
            processArguments.add("-d");
            processArguments.add("-p");
            processArguments.add("8080:8080");
            processArguments.add(imageName);
            if (false) {
                builder = builder.inheritIO();
            }
            
            Process process = builder.command(processArguments).start();
            process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
            
            if (process.exitValue() == 0) {
                System.out.println("[Deployer] Application is available on http://localhost:8080");
            }
            
            return process.exitValue();
        } catch (InterruptedException ex) {
            return 2;
        } catch (IOException ex) {
            return 1;
        }
    }
}
