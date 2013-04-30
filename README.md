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

2.  Add a profile to your maven settings.xml file.  Include the tfinternal profile shown in the sample /config/settings.xml file.  This will allow you to
run a build locally that publishes to TestFlight.

Although generally you don't want to run this from a local build, mainly because we want the build server to create the version,
you can test your configuration with the following maven command:

mvn clean install -Psign,tfinternal,testflight -Dapp.versioncode=[some version value] -Dsign.storepass=[key store password] -Dsign.keypass=[key store password]

Note the use of multiple profiles to achieve the plugin invocation with specific inputs.

You can add as many project-specific profiles as needed to accommodate dev or production deployments.

<em>Team City Integration</em>

Adding this capability to your project's Team City build requires the addition of the 'testflight' profile to the
'install' step of your build.  In addition, the three parameters set as properties in your settings file are
here going to be passed on the command line.

MTSU-Android build example

Changed From:

-Dapp.versioncode=%build.number% -Dsign.storepass=[pword] -Dsign.keypass=[pword] -Psign -Ppublish-apk -Plint


Changed To:

-Dapp.versioncode=%build.number% -Dsign.storepass=[pword] -Dsign.keypass=[pword] -Psign,publish-apk,testflight,lint -DtestFlightApiToken=[api token] -DtestFlightTeamToken=[Internal team token] -DtestFlightDistroList=Internal -e

Note the '-e' on the end of the command.  It will give you the information you need when the REST call to TestFlight fails.