package com.willowtreeapps.maven.plugins.testflight;

import com.sun.java.swing.plaf.gtk.resources.gtk_it;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TestFlightUploader {
    private static final String HOST = "testflightapp.com";
    private static final String POST = "/api/builds.json";

    public UploadResult upload(final UploadRequest ur) throws Exception {
        UploadResult uploadResult = new UploadResult();

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpHost targetHost = new HttpHost(HOST);
        HttpPost httpPost = new HttpPost(POST);
        FileBody fileBody = new FileBody(ur.getFile());

        MultipartEntity entity = new MultipartEntity();
        entity.addPart("api_token", new StringBody(ur.getApiToken()));
        entity.addPart("team_token", new StringBody(ur.getTeamToken()));
        entity.addPart("notes", new StringBody(ur.getBuildNotes()));
        entity.addPart("file", fileBody);

        if (!StringUtils.isEmpty(ur.getDistributionLists())) {
            entity.addPart("distribution_lists", new StringBody(ur.getDistributionLists()));
        }

        entity.addPart("notify", new StringBody(ur.isNotifyDistributionList() && !StringUtils.isEmpty(ur.getDistributionLists()) ? "True" : "False"));
        entity.addPart("replace", new StringBody(ur.isReplace() ? "True" : "False"));
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(targetHost, httpPost);
        HttpEntity resEntity = response.getEntity();

        InputStream is = resEntity.getContent();

        int statusCode = response.getStatusLine().getStatusCode();

        // Keeping it simple we don't really care about the JSON that comes back from a
        // successful TestFlight upload. We just want to know if it succeeded or if it
        // failed, and catch failure exceptions if they occur.
        if (statusCode != 200 && statusCode != 201) {
            String responseBody = new Scanner(is).useDelimiter("\\A").next();
            uploadResult.setSucceeded(false);
            uploadResult.setMessage(responseBody);
        } else {
            uploadResult.setSucceeded(true);
            uploadResult.setMessage("The TestFlight upload was successful.");
        }
        return uploadResult;
    }
}

class UploadRequest {
    private String apiToken, teamToken, buildNotes, distributionLists;
    private File file;
    private boolean notifyDistributionList;
    private final boolean replace;

    UploadRequest(final String apiToken, final String teamToken, final String buildNotes,
                  final String distributionLists, final File file, final boolean notifyDistributionList, final boolean replace) {
        this.apiToken = StringUtils.trim(apiToken);
        this.teamToken = StringUtils.trim(teamToken);
        this.buildNotes = StringUtils.trim(buildNotes);
        this.distributionLists = StringUtils.trim(distributionLists);
        this.file = file;
        this.notifyDistributionList = notifyDistributionList;
        this.replace = replace;
    }

    String getApiToken() {
        return apiToken;
    }

    void setApiToken(final String apiToken) {
        this.apiToken = StringUtils.trim(apiToken);
    }

    String getTeamToken() {
        return teamToken;
    }

    void setTeamToken(final String teamToken) {
        this.teamToken = StringUtils.trim(teamToken);
    }

    String getBuildNotes() {
        if(buildNotes.startsWith("git log")){
            try {
                Process process = new ProcessBuilder("git", "log", "--oneline", "--since", "\"24 hours ago\"", "--no-merges").start();
                process.waitFor();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(
                                process.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }
                String gitReturn = stringBuilder.toString();
                if(gitReturn.length() == 0){
                    return "No Changes";
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                System.out.println(e.getMessage());
               return "";
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return "";
            }
        }else{
            return buildNotes;
        }
    }

    void setBuildNotes(final String buildNotes) {
        this.buildNotes = StringUtils.trim(buildNotes);
    }

    String getDistributionLists() {
        return distributionLists;
    }

    void setDistributionLists(final String distributionLists) {
        this.distributionLists = StringUtils.trim(distributionLists);
    }

    File getFile() {
        return file;
    }

    void setFile(final File file) {
        this.file = file;
    }

    boolean isNotifyDistributionList() {
        return notifyDistributionList;
    }

    void setNotifyDistributionList(final boolean notifyDistributionList) {
        this.notifyDistributionList = notifyDistributionList;
    }

    public boolean isReplace() {
        return replace;
    }
}

class UploadResult {
    private boolean succeeded;
    private String message;

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(final boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = StringUtils.trim(message);
    }
}
