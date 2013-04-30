package com.willowtreeapps.maven.plugins.testflight;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo( name = "upload" )
public class TestFlightMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        getLog().info("Stub for testflight mojo upload goal has been executed.");
    }
}
