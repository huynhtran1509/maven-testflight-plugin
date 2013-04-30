package com.willowtreeapps.maven.plugins.testflight;

import com.willowtreeapps.request.UploadRequest;
import com.willowtreeapps.uploader.testflight.TestFlightUploader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


@Mojo(name = "upload")
public class TestFlightMojo extends AbstractMojo {

    @Parameter(property = "upload.testFlightApiToken", required = true)
    private String testFlightApiToken;

    @Parameter(property = "upload.testFlightTeamToken", required = true)
    private String testFlightTeamToken;

    @Parameter(property = "upload.testFlightDistroList", required = true)
    private String testFlightDistroList;

    @Parameter(property = "upload.apkPath", required = true)
    private String apkPath;

    public void execute() throws MojoExecutionException {

        getLog().info("TestFlight upload - [STARTED]");

        try {
            TestFlightUploader uploader = new TestFlightUploader();
            UploadRequest request = new UploadRequest();
            request.apiToken = testFlightApiToken;
            request.teamToken = testFlightTeamToken;
            request.buildNotes = "New Build!";
            request.lists = testFlightDistroList;
            request.notifyTeam = true;
            request.replace = true;
            request.file = new File(apkPath);
            uploader.upload(request);
        } catch (Exception e) {
            throw new MojoExecutionException("There was an error executing the TestFlight upload goal in the maven-testflight-plugin.", e);
        }

        getLog().info("TestFlight upload - [COMPLETE]");
    }
}
