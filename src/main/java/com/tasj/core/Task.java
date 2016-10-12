package com.tasj.core;

import java.util.ArrayList;
import java.util.List;

public class Task {
    String description;
    boolean done;
    String title;
    String uri;

    public Task(String title) {
        this.title = title;
        this.description = "";
    }

    public Task() {
    }

    public Task(String title, String description, boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    @Override
    public String toString() {
        return "title: " + title+ "\ndescription: " + description + "\ndone: " + done + "\nURI: " + uri + "\n";
    }
}
