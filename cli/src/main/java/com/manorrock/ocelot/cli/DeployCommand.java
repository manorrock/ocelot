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

import static com.manorrock.ocelot.cli.DeployDestTarget.DOCKER;
import static com.manorrock.ocelot.cli.DeployDestType.CONTAINER;
import static com.manorrock.ocelot.cli.DeploySourceType.JAR;
import static com.manorrock.ocelot.cli.DeploySourceType.UNKNOWN;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * The deploy command.
 *
 * <p>
 * This command will deploy the image to a target runtime. It will try to use
 * sensible defaults. If it fails to determine the defaults it will suggest a
 * course of action.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Command(name = "deploy", mixinStandardHelpOptions = true)
public class DeployCommand implements Callable<Integer> {

    /**
     * Stores the destination target.
     */
    private DeployDestTarget destTarget = DOCKER;

    /**
     * Stores the destination type.
     */
    private DeployDestType destType = CONTAINER;

    /**
     * Stores the filename.
     */
    private String filename;

    /**
     * Stores the filenames of the files/directories to execute the command
     * against.
     */
    @Parameters(description = "The file/directory to deploy. When not supplied the"
            + "current directory will be used.")
    private List<String> filenames;

    /**
     * Stores the source directory.
     */
    private File sourceDirectory;

    /**
     * Stores the image name.
     */
    @Option(names = "--image", description = "The image name.")
    private String imageName;

    /**
     * Stores the name.
     */
    private String name;

    /**
     * Stores the source file (if any).
     */
    private File sourceFile;

    /**
     * Stores the source type.
     */
    private DeploySourceType sourceType = UNKNOWN;

    /**
     * Stores the skip build flag.
     */
    @Option(names = {"--skip-build"}, description = "Skip the build")
    private boolean skipBuild = true;

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
     * Execute the deploy.
     *
     * @return the status code.
     */
    @Override
    public Integer call() {
        int result = 0;

        normalizeFilenames();

        for (String currentFilename : filenames) {
            filename = currentFilename;
            result = determineSourceType();

            if (result == 0) {
                switch (sourceType) {
                    case JAR:
                        result = deployJar();
                        break;
                }
            }

            if (result != 0) {
                break;
            }
        }

        return result;
    }

    /**
     * Deploy the JAR file.
     *
     * The following steps are taken when deploying a JAR file.
     *
     * <ol>
     * <li>Determine the source file</li>
     * <li>Determine the source directory</li>
     * <li>Determine the destination type</li>
     * </ol>
     *
     * @return the result.
     */
    private int deployJar() {
        int result;

        determineSourceFile();
        determineSourceDirectory();

        result = determineDestType();
        if (result == 0) {
            result = determineDestTarget();
        }
        switch (destTarget) {
            case DOCKER:
                result = deployJarOnDocker();
        }

        return result;
    }

    /**
     * Deploy JAR on Docker.
     */
    private int deployJarOnDocker() {

        determineName();
        determineImageName();

        DockerDeployer deployer = new DockerDeployer();
        deployer.setName(name);
        deployer.setImageName(imageName);
        deployer.setTimeout(timeout);
        deployer.setTimeoutUnit(timeoutUnit);

        return deployer.deploy();
    }

    /**
     * Determine the destination target.
     *
     * @return the status code.
     */
    private int determineDestTarget() {
        System.out.println("[Deployer] Destination target is DOCKER");
        return 0;
    }

    /**
     * Determine the destination type.
     *
     * <p>
     * If no destination type is passed then we return CONTAINER as the
     * destination type.
     * </p>
     *
     * @return the status code.
     */
    private int determineDestType() {
        System.out.println("[Deployer] Destination type is CONTAINER");
        return 0;
    }

    /**
     * Determine the image name.
     */
    private void determineImageName() {
        if (imageName == null) {
            imageName = name;
        }
        System.out.println("[Deployer] Image name is " + imageName);
    }

    /**
     * Determine the name.
     *
     * <p>
     * If the (application) name is not specified it is take from the filename
     * by stripping off the extension if it exists. Otherwise the filename is
     * taken as-is.
     * </p>
     */
    private void determineName() {
        if (name == null && filename.contains(".")) {
            name = filename.substring(0, filename.indexOf("."));
        } else if (name == null) {
            name = filename;
        }
        System.out.println("[Deployer] Name is " + name);
    }

    /**
     * Determine the source directory.
     *
     * <p>
     * If there is a source file the source directory will be the directory in
     * which the source file resides.
     * </p>
     */
    private void determineSourceDirectory() {
        sourceDirectory = new File(filename);
        if (!sourceDirectory.isDirectory()) {
            sourceDirectory = sourceDirectory.getAbsoluteFile().getParentFile();
        }
        System.out.println("[Deployer] Source directory is " + sourceDirectory.getAbsolutePath());
    }

    /**
     * Determine the source file.
     *
     * <p>
     * If the command line arguments specified a directory to deploy then the
     * source file will be null. Otherwise the source file will be the file
     * passed in on the comand line.
     * </p>
     */
    private void determineSourceFile() {
        sourceFile = new File(filename);
        if (sourceFile.isDirectory()) {
            sourceFile = null;
        } else {
            System.out.println("[Deployer] Source file is " + sourceFile.getAbsolutePath());
        }
    }

    /**
     * Determine the source type.
     *
     * <p>
     * If the filename passed in is a file then we look at the extension to see
     * if it ends with ".jar" and if it does we will return JAR as the source
     * type.
     * </p>
     *
     */
    private int determineSourceType() {
        int result = 0;
        if (filename.toLowerCase().endsWith(".jar")) {
            System.out.println("[Deployer] Source type is JAR");
            sourceType = JAR;
        }
        if (sourceType == UNKNOWN) {
            System.out.println("[Deployer] Unable to determine source type");
            result = 1;
        }
        return result;
    }

    /**
     * Normalize the filenames.
     *
     * <p>
     * If no filename was passed we are going to assume the user wants to deploy
     * the current directory.
     * </p>
     */
    private void normalizeFilenames() {
        if (filenames == null) {
            System.out.println("[Deployer] No deployment file / directory found");
            System.out.println("[Deployer] Defaulting to deploying the current directory");
            filenames = new ArrayList<>();
            filenames.add("");
        }
    }
}
