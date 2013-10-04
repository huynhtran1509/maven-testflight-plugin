package com.willowtreeapps.maven.plugins.testflight;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;


/**
 * Push a mobile build artifact to TestFlight.
 *
 * @goal upload
 * @phase package
 */
@Mojo(name = "upload")
public class TestFlightMojo extends AbstractMojo {

    /**
     * This is Upload API Token found in your TestFlight Account Settings on the
     * Upload API page.  It is a required element to use this plugin.
     * http://help.testflightapp.com/customer/portal/articles/829956
     *
     * @parameter
     */
    @Parameter(property = "upload.testFlightApiToken", required = true)
    private String testFlightApiToken;

    /**
     * After adding an application to TestFlight you will get your Team Token
     * from the Team Info page.  It is a required element to use this plugin.
     * http://help.testflightapp.com/customer/portal/articles/829942
     *
     * @parameter
     */
    @Parameter(property = "upload.testFlightTeamToken", required = true)
    private String testFlightTeamToken;

    /**
     * Add a Distribution List in TestFlight under the People management page.
     * If you want users to be notified when a build is posted then use this
     * parameter together with the testFlightNotifyDistroList property set to 'true'.
     * It is a required element to use this plugin.
     *
     * @parameter
     */
    @Parameter(property = "upload.testFlightDistroList", required = true)
    private String testFlightDistroList;

    /**
     * You do not have to notify users when you post a build. And in some cases this is desired.
     * Use this boolean to enable the notification of the configured distribution list.
     * It is a required element to use this plugin.
     * <p/>
     * This value should be entered as true or false.
     *
     * @parameter
     */
    @Parameter(property = "upload.testFlightNotifyDistroList", required = true)
    private boolean testFlightNotifyDistroList;

    /**
     * Notes are not required when posting a build to TestFlight, but it is helpful
     * for testers if you want to inform them about the build using the notification
     * settings above.
     *
     * @parameter
     */
    @Parameter(property = "upload.testFlightBuildNotes", required = false)
    private String testFlightBuildNotes;

    /**
     * Provide the path to the Android .apk or iOS .ipa file you wish to upload to TestFlight.
     * This is a required element to use this plugin.  An example of this property's
     * configuration is:
     *
     * @parameter
     */
    @Parameter(property = "upload.filePath", required = true)
    private String filePath;

    public void execute() throws MojoExecutionException {

        getLog().info("TestFlight upload is: STARTED.");
        getLog().info(String.format("API token: %s", testFlightApiToken));
        getLog().info(String.format("Team token: %s", testFlightTeamToken));
        getLog().info(String.format("Distro list: %s", testFlightDistroList));
        getLog().info(String.format("Notify distro list: %s", testFlightNotifyDistroList));
        getLog().info(String.format("Notes: %s", testFlightBuildNotes));
        getLog().info(String.format("File path: %s", filePath));

        try {
            TestFlightUploader uploader = new TestFlightUploader();
            UploadRequest request = new UploadRequest(testFlightApiToken,
                    testFlightTeamToken, testFlightBuildNotes,
                    testFlightDistroList, new File(filePath), testFlightNotifyDistroList);

            UploadResult result = uploader.upload(request);
            if (result.isSucceeded()) {
                getLog().info(result.getMessage());
            } else {
                getLog().info(String.format("The TestFlight upload failed: %s", result.getMessage()));
            }
        } catch (Exception e) {
            String err = "There was an error executing the TestFlight upload goal in the maven-testflight-plugin.";
            getLog().error(err, e);
            throw new MojoExecutionException(err, e);
        }

        getLog().info("TestFlight upload is: COMPLETE.");
    }
}
