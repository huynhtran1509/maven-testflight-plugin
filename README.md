maven-testflight-plugin
=======================

Use this plugin to deploy an app to TestFlight. It requires you configure values for the following
four properties:

<ul>
    <li>testFlightApiToken</li>
    <li>testFlightTeamToken</li>
    <li>testFlightDistroList</li>
    <li>apkPath</li>
</ul>

<code>
       <profile>
            <!--
            This profile needs to run after the 'sign' profile also present in your project's pom.  It is dependent
            on the outputApk file path created in the alignApk execution which runs in the package phase.

            To test from your local build run:

            mvn clean install -Psign,testflight -Dapp.versioncode=[some version value] -Dsign.storepass=[key store password] -Dsign.keypass=[key store password]

            -->
            <id>testflight</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>com.willowtreeapps.maven.plugins</groupId>
                        <artifactId>maven-testflight-plugin</artifactId>
                        <version>${testflight-maven-plugin.version}</version>
                        <executions>
                            <execution>
                                <phase>package</phase>
                                <goals>
                                    <goal>upload</goal>
                                </goals>
                                <configuration>
                                    <testFlightApiToken>[String - WTA api token configured in the TestFlight account.]</testFlightApiToken>
                                    <testFlightTeamToken>[String - The specific TestFlight team token.]</testFlightTeamToken>
                                    <testFlightDistroList>[String - Name of the TestFlight distro list.]</testFlightDistroList>

                                    <!--
                                    The apkPath is based on the output of the alignApk execution and may be different for your project.
                                    This project.build.directory is 'target'.
                                    -->
                                    <apkPath>${project.build.directory}/${project.build.finalName}-signed-aligned.apk</apkPath>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
</code>