package com.willowtreeapps.maven.plugins.testflight;

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
            UploadRequest request = new UploadRequest(testFlightApiToken,
                    testFlightTeamToken, "New build!",
                    testFlightDistroList, new File(apkPath), true);
            uploader.upload(request);
        } catch (Exception e) {
            String err = "There was an error executing the TestFlight upload goal in the maven-testflight-plugin.";
            getLog().error(err, e);
            throw new MojoExecutionException(err, e);
        }

        getLog().info("TestFlight upload - [COMPLETE]");
    }
}
