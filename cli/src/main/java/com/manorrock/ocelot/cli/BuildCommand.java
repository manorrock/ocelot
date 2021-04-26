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
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * The build command.
 *
 * <p>
 * This command will build a project into an image. It will try to use sensible
 * defaults to build the image. If it fails to determine those defaults it will
 * echo what is is unable to determine with a suggested course of action.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Command(name = "build", mixinStandardHelpOptions = true)
public class BuildCommand extends AbstractCommand implements Callable<Integer> {

    /**
     * Stores the image name.
     */
    @Option(names = "--image", description = "The image name.")
    private String imageName;

    /**
     * Stores the runtime.
     */
    @Option(names = "--runtime",
            description = "The build runtime (e.g. Docker, ACR).",
            defaultValue = "docker")
    private String runtime;

    /**
     * Stores the timeout.
     */
    @Option(names = "--timeout",
            description = "The timeout before aborting the deploy.")
    private long timeout = 600;

    /**
     * Stores the timeout unit.
     */
    @Option(names = "--timeout-unit",
            description = "The timeout unit (e.g. seconds, minutes, hours, days).")
    private String timeoutUnit = "seconds";

    /**
     * Stores the working directory.
     */
    private File workingDirectory;

    /**
     * Build the given image locally using Docker.
     */
    private int buildOnDocker() throws Exception {
        DockerBuilder builder = new DockerBuilder();
        builder.setImageName(imageName);
        builder.setTimeout(timeout);
        builder.setTimeoutUnit(timeoutUnit);
        builder.setWorkingDirectory(workingDirectory);
        builder.setVerbose(verbose);
        return builder.build();
    }

    /**
     * Build the image on Azure Container Registry.
     */
    private int buildOnAzure() throws Exception {
        AzureCliBuilder builder = new AzureCliBuilder();
        builder.setImageName(imageName);
        builder.setTimeout(timeout);
        builder.setTimeoutUnit(timeoutUnit);
        builder.setWorkingDirectory(workingDirectory);
        builder.setVerbose(verbose);
        return builder.build();
    }

    /**
     * Call the command.
     *
     * @return 0 when completed successfully.
     * @throws Exception when a serious error occurs.
     */
    @Override
    public Integer call() throws Exception {

        determineName();
        determineImageName();
        determineWorkingDirectory();

        if (runtime != null) {
            switch (runtime.toLowerCase()) {
                case "docker":
                    return buildOnDocker();
                case "acr":
                    return buildOnAzure();
                default:
                    break;
            }
        }
        return 0;
    }

    /**
     * Determine the image name.
     *
     * <p>
     * If no image name was set we use the name and normalize it to construct
     * the image name.
     */
    private void determineImageName() {
        if (imageName == null) {
            imageName = name;
            if (imageName.contains(".")) {
                imageName = imageName.substring(0, imageName.indexOf("."));
            }
            imageName = imageName.toLowerCase();
        }
        System.out.println("[Builder] Determined image name to be '" + imageName + "'");
    }

    /**
     * Determine the working directory.
     */
    private void determineWorkingDirectory() {
        if (file != null) {
            workingDirectory = new File(file.get(0));
        } else {
            workingDirectory = new File("");
        }
        System.out.println("[Builder] Determined working directory to be '" + workingDirectory + "'");
    }
}
