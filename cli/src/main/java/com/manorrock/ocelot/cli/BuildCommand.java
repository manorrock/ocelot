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
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> issue-29
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * The build command.
 *
 * <p>
 *  This command will build a project into an image. If will try to use sensible
 *  defaults to build the image. If it fails to determine those defaults it will
 *  echo what is is unable to determine with a suggested course of action.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Command(name = "build", mixinStandardHelpOptions = true)
public class BuildCommand implements Callable<Integer> {
    
    /**
     * Stores the list of files/directories to be build.
     */
<<<<<<< HEAD
    @Parameters(index = "0..*",
            description = "The list of files/directories to build\n"
            + "When not supplied the current directory will be used")
    private List<File> files;

    @Override
    public Integer call() throws Exception {
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                processFile(file);
            });
        }
        return 0;
    }

    private void processFile(File file) {
        System.out.println("Processing - " + file.getName());
        System.out.println("Absolute path : " + file.getAbsolutePath());
        System.out.println("Directory : " + file.isDirectory());
    }
=======
    @Parameters(index = "0", description = "The file/directory to build for.")
    private File file;
    
    @Override
    public Integer call() throws Exception {
        if (file == null) {
            file = new File(".");
        }
        System.out.println("Processing " + file.getName());
        return 0;
    }
>>>>>>> issue-29
}
