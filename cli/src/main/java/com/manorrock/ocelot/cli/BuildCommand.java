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
import static java.lang.System.Logger.Level.INFO;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

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
public class BuildCommand implements Callable<Integer> {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(BuildCommand.class.getName());

    /**
     * Stores the file/directory to build.
     */
    @Parameters(index = "0",
            description = "The file/directory to build. When not supplied the current directory will be used.")
    private List<String> file;

    /**
     * Stores the image name.
     */
    @Option(names = "--image", description = "The image name.")
    private String imageName;

    /**
     * Stores the runtime.
     */
    @Option(names = "--runtime", description = "The build runtime (e.g. Docker, ACR).", defaultValue = "docker")
    private String runtime;

    /**
     * Stores the timeout.
     */
    @Option(names = "--timeout", description = "The timeout before aborting the deploy.")
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
     * Stores the working directory.
     */
    private File workingDirectory;

    /**
     * Build the given image locally using Docker.
     *
     * @param imageName the image name.
     */
    private int buildOnDocker(String imageName) throws Exception {
        DockerBuilder docker = new DockerBuilder();
        docker.setImageName(imageName);
        docker.setTimeout(timeout);
        docker.setTimeoutUnit(timeoutUnit);
        docker.setWorkingDirectory(workingDirectory);
        return docker.build();
    }
    
    /**
     * Build the image on Azure Container Registry.
     * 
     * @param imageName the image name.
     */
    private int buildOnAzure(String imageName) throws Exception {
        AzureCliBuilder builder = new AzureCliBuilder();
        builder.setImageName(imageName);
        builder.setTimeout(timeout);
        builder.setTimeoutUnit(timeoutUnit);
        builder.setWorkingDirectory(workingDirectory);
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
        if (imageName != null) {
        } else if (file == null) {
            imageName = new File("").getCanonicalFile().getName();
        } else {
            imageName = new File(file.get(0)).getCanonicalFile().getName();
        }
        if (file != null) {
            workingDirectory = new File(file.get(0));
        }
        if (runtime != null) {
            switch (runtime.toLowerCase()) {
                case "docker":
                    return buildOnDocker(normalizeImageName(imageName));
                case "acr" :
                    return buildOnAzure(normalizeImageName(imageName));
                default:
                    break;
            }
        }
        return 0;
    }

    /**
     * Normalize the image name.
     *
     * @param imageName the image name.
     * @return the normalized image name.
     */
    private String normalizeImageName(String imageName) {
        if (verbose) {
            LOGGER.log(INFO, "Normalizing image name: " + imageName);
        }
        if (imageName.contains(".")) {
            imageName = imageName.substring(0, imageName.indexOf("."));
        }
        imageName = imageName.toLowerCase();
        if (verbose) {
            LOGGER.log(INFO, "Normalized image name to: " + imageName);
        }
        return imageName;
    }
}
