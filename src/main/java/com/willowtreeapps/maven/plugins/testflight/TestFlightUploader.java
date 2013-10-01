package com.willowtreeapps.maven.plugins.testflight;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TestFlightUploader {
    private static final String HOST = "testflightapp.com";

    private static final String POST = "/api/builds.json";

    public UploadResult upload(UploadRequest ur) {
        UploadResult uploadResult = new UploadResult();
        try {
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
            entity.addPart("replace", new StringBody("True"));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(targetHost, httpPost);
            HttpEntity resEntity = response.getEntity();

            InputStream is = resEntity.getContent();

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200 && statusCode != 201) {
                String responseBody = new Scanner(is).useDelimiter("\\A").next();
                uploadResult.setSucceeded(false);
                uploadResult.setMessage(responseBody);
            }

            uploadResult.setSucceeded(true);
            uploadResult.setMessage("The TestFlight upload was successful.");

        } catch (IOException e) {
            e.printStackTrace();
            uploadResult.setSucceeded(false);
            uploadResult.setMessage("An error occurred with the Test Flight upload.  Check the logs.");
        }
        return uploadResult;
    }
}

class UploadRequest {
    private String apiToken, teamToken, buildNotes, distributionLists;
    private File file;
    private boolean notifyDistributionList;

    UploadRequest(final String apiToken, final String teamToken, final String buildNotes,
                  final String distributionLists, final File file, final boolean notifyDistributionList) {
        this.apiToken = StringUtils.trim(apiToken);
        this.teamToken = StringUtils.trim(teamToken);
        this.buildNotes = StringUtils.trim(buildNotes);
        this.distributionLists = StringUtils.trim(distributionLists);
        this.file = file;
        this.notifyDistributionList = notifyDistributionList;
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
        return buildNotes;
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
}

class UploadResult {
    private boolean succeeded;
    private String message;

    UploadResult() {
    }

    UploadResult(boolean succeeded, String message) {
        this.succeeded = succeeded;
        this.message = message;
    }

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