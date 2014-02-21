maven-testflight-plugin
=======================

<p>
Support for this plugin is discontinued as TestFlight has discontinued support for Android.  Please see: http://help.testflightapp.com/customer/portal/articles/1450414.
</p>
<p></p>

<a href="http://willowtreeapps.github.io/maven-testflight-plugin/index.html">Site Documentation</a>

Use this plugin to deploy an app to TestFlight. It requires you configure values for the following TestFlight properties:

<ul>
    <li>testFlightApiToken</li>
    <li>testFlightTeamToken</li>
    <li>testFlightDistroList</li>
    <li>testFlightNotifyDistroList</li>
    <li>testFlightBuildNotes</li>
    <li>filePath</li>
    <li>replace</li>
</ul>

These may be added as properties inside a project's pom.  However to avoid hard coding the tokens and checking
in critical data such as API keys, the properties may also be configured externally in your maven settings.xml file.


<em>Continuous Integration</em>

Adding this capability to your project's continuous integration maven build step requires the addition of the profile to
your maven command line.   This configuration will push a your artifact with every build.

Also checkout the <a href="https://github.com/willowtreeapps/ci-teamcity-testflight-plugin">ci-teamcity-testflight-plugin</a>
to incorporate a plugin into your TeamCity installation.  This plugin will give you a form on the Build Results tab that
allows you to click a button, tag and push a build on demand to TestFlight.

