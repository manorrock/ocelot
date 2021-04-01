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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.INFO;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * The run command.
 *
 * <p>
 * This command will initiate a run of an image and echo the output to the local
 * terminal. It will use the build command to build the image if it was not
 * previously build.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Command(name = "run", mixinStandardHelpOptions = true)
public class RunCommand implements Callable<Integer> {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(RunCommand.class.getName());

    /**
     * Stores the file/directory to run.
     */
    @Parameters(index = "0",
            description = "The file/directory to run. When not supplied the current directory will be used.")
    private List<String> file;

    /**
     * Stores the image name.
     */
    @Option(names = "--image", description = "The image name.")
    private String imageName;

    /**
     * Stores the runtime.
     */
    @Option(names = "--runtime", description = "The execution runtime (e.g. Docker, Kubernetes).", defaultValue = "docker")
    private String runtime;

    /**
     * Stores the skip build flag.
     */
    @Option(names = "--skip-build", description = "Skip the build step")
    private boolean skipBuild = false;

    /**
     * Stores the timeout.
     */
    @Option(names = "--timeout", description = "The timeout before aborting the run.")
    private long timeout = 600;

    /**
     * Stores the timeout unit.
     */
    @Option(names = "--timeout-unit", description = "The timeout unit (e.g. seconds, minutes, hours, days).")
    private String timeoutUnit = "seconds";

    /**
     * Stores the verbose flag.
     */
    @Option(names = {"-v", "--verbose"}, description = "Output more verbose.")
    private boolean verbose = false;

    /**
     * Run the given image locally using Docker.
     *
     * @param imageName the image name.
     */
    private int runOnDocker(String imageName) throws Exception {
        if (verbose) {
            LOGGER.log(INFO, "Running " + imageName);
        }
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> processArguments = new ArrayList<>();
        processArguments.add("docker");
        processArguments.add("run");
        processArguments.add(imageName);
        Process process = builder.command(processArguments).inheritIO().start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue();
    }

    /**
     * Run the given image on Kubernetes.
     *
     * @param imageName the image name.
     */
    private int runOnK8s(String imageName) throws Exception {
        if (isKubectlAvailable()) {
            if (verbose) {
                LOGGER.log(INFO, "Determined kubectl is available.");
            }
        } else {
            if (verbose) {
                LOGGER.log(INFO, "Determined kubectl is NOT available.");
            }
            System.out.println("It appears 'kubectl' is NOT available, please "
                    + "go to https://kubernetes.io/docs/tasks/tools/#kubectl and "
                    + "follow the directions to install it.");
            return 1;
        }

        String currentContext = getCurrentContext();
        if (currentContext != null) {
            if (verbose) {
                LOGGER.log(INFO, "Using context: " + currentContext);
            }
        } else {
            if (verbose) {
                LOGGER.log(INFO, "Determined current context is not set, which "
                        + "most likely means your .kube/config  is not configured "
                        + "properly. ");
            }
        }
        
        // call the deploy command to deploy the job.
        // call the log command to get the log for the job.
        // call the undeploy command to undeploy the job.        
        
        return 0;
    }

    /**
     * Validate if kubectl is available.
     */
    private boolean isKubectlAvailable() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        Process process = builder.command("kubectl", "version").start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        return process.exitValue() == 0;
    }
    
    /**
     * Get the Kubernetes context.
     */
    private String getCurrentContext() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        Process process = builder.command("kubectl", "config", "current-context").start();
        process.waitFor(timeout, TimeUnit.valueOf(timeoutUnit.toUpperCase()));
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Call the command.
     *
     * @return 0 when completed successfully.
     * @throws Exception when a serious error occurs.
     */
    @Override
    public Integer call() throws Exception {
        if (imageName != null) {
        } else if (file == null) {
            imageName = new File("").getCanonicalFile().getName();
        } else {
            imageName = file.get(0);
        }
        if (runtime != null) {
            switch (runtime.toLowerCase()) {
                case "docker":
                    return runOnDocker(imageName);
                case "kubernetes":
                    return runOnK8s(imageName);
                default:
                    break;
            }
        }
        return 0;
    }
}
