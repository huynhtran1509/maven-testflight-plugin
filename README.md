maven-testflight-plugin
=======================

Use this plugin to deploy an app to TestFlight. It requires you configure values for the following
three TestFlight properties:

<ul>
    <li>testFlightApiToken</li>
    <li>testFlightTeamToken</li>
    <li>testFlightDistroList</li>
</ul>

They may be set as properties inside a project's pom.  However to avoid hard coding the tokens and checking
in critical data such as API keys, the properties may also be configured externally in your maven settings.xml file.

To include this plugin in your Maven project:

1.  Add the profile provided in the pom snippet located in the /config directory to your pom.  Be sure to review the
apkPath and set it accordingly.  The snippet includes the stock configuration for the output Android apk file generated
by the 'sign' profile's zipalign step.

2.  Add a profile to your maven settings.xml file.  Include the tfinternal profile shown in the sample /config/settings.xml file.

Although generally you don't want to run this from a local build, mainly because we want the build server to create the version,
you can test your configuration with the following maven command:

mvn clean install -Psign,tfinternal,testflight -Dapp.versioncode=[some version value] -Dsign.storepass=[key store password] -Dsign.keypass=[key store password]

Note the use of multiple profiles to achieve the plugin invocation with specific inputs.

You can add as many project-specific profiles as needed to accommodate dev or production deployments.



