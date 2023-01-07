/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The Dockerfile resource.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("/dockerFiles")
@RequestScoped
public class DockerfileResource {
    
    /**
     * Stores the one and only REST application.
     */
    @Inject
    private ApplicationBean application;
    
    /**
     * Create the Dockerfile resource.
     * 
     * @param name the name of the Dockerfile.
     * @param data the content of the Dockerfile file.
     */
    @PUT
    @Path("{name}")
    public void create(@PathParam("name") String name, byte[] data) {
        File resourceDirectory = new File(application.getRootDirectory(), "dockerFiles" + File.separator + name);
        File dockerFile = new File(resourceDirectory, "Dockerfile");
        if (!dockerFile.exists()) {
            try {
                if (!resourceDirectory.exists()) {
                    resourceDirectory.mkdirs();
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream(dockerFile)) {
                    fileOutputStream.write(data);
                    fileOutputStream.flush();
                }
            } catch (IOException ioe) {
                throw new WebApplicationException(ioe, 500);
            }
        }
    }
}
