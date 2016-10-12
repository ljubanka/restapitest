package com.tasj.pages;

import com.tasj.core.Task;
import com.tasj.core.TaskContainer;
import com.tasj.core.TasksContainer;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TasksPage {
    public static final String URI = "http://localhost:5000/todo/api/v1.0/tasks";
    public static final List<Task> defaultTasks = new ArrayList<Task>();

    public Invocation.Builder requestTo(String uri) {
        return ClientBuilder.newClient().target(uri).request();
    }

    public Invocation.Builder authorized(Invocation.Builder requestBuilder) {
        return requestBuilder.header("Authorization", "Basic " + Base64.getEncoder().encodeToString("miguel:python".getBytes()));
    }

    public Invocation.Builder authorizedRequestTo(String uri) {
        return authorized(requestTo(uri));
    }

    public Response resetTasks() {
        return sendAuthorizedGetRequest(URI + "/reset");
    }

    public List<Task> availableTasks() {
        Response response = authorized(requestTo(URI)).get();
        return response.readEntity(TasksContainer.class).getTasks();
    }

    public Response sendGetRequest(String uri) {
        return requestTo(uri).get();
    }

    public Response sendAuthorizedGetRequest(String uri) {
        return authorized(requestTo(uri)).get();
    }

    public Response sendAuthorizedPostRequest(String uri, Task task) {
        return authorized(requestTo(uri)).post(Entity.entity(task, MediaType.APPLICATION_JSON));
    }

    public Response sendAuthorizedPutRequest(String uri, Task task) {
        return authorized(requestTo(uri)).put(Entity.entity(task, MediaType.APPLICATION_JSON));
    }

    public Response sendAuthorizedDeleteRequest(String uri) {
        return authorized(requestTo(uri)).delete();
    }

    public List<Task> tasksFromResponse(Response response) {
        return response.readEntity(TasksContainer.class).getTasks();
    }

    public Task taskFromResponse (Response response) {
        return response.readEntity(TaskContainer.class).getTask();
    }

    public List<Task> joinTasks(Task... tasksToJoin) {
        List<Task> tasks = new ArrayList<Task>();
        for (Task task: tasksToJoin) {
            tasks.add(task);
        }
        return tasks;
    }

    public List<Task> joinTasks(List<Task> tasks, Task... tasksToJoin) {
        for (Task task: tasksToJoin) {
            tasks.add(task);
        }
        return tasks;
    }

    public void assertEqualListsOfTasks(List<Task> expectedTasks, List<Task> actualTasks) {
        if (expectedTasks.size() == actualTasks.size()) {
            for (int i=0; i<expectedTasks.size(); i++) {
                assertEqualTasks(expectedTasks.get(i), actualTasks.get(i));
                //System.out.println(actualTasks.get(i));
            }
        }
    }

    public void assertEqualTasks(Task expected, Task actual) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        //assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.isDone(), actual.isDone());
        //System.out.println(actual);
    }

    public void assertStatus(int statusCode, Response response) {
        assertEquals(statusCode, response.getStatus());
    }
}
