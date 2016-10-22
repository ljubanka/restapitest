package com.tasj.resources;

import com.tasj.containers.Task;
import com.tasj.containers.TaskContainer;
import com.tasj.containers.TasksContainer;
import com.tasj.core.Configuration;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static com.tasj.core.RestHelpers.authorized;
import static com.tasj.core.RestHelpers.requestTo;
import static org.junit.Assert.assertEquals;

public class TasksApi {
    public static String uri = Configuration.baseUri + "/todo/api/v1.0/tasks";

    public static Invocation.Builder authorizedRequestTo(String uri) {
        return authorized(requestTo(uri), "miguel", "python");
    }

    public static void reset() {
        Response response =  authorizedRequestTo(uri + "/reset").get();
        assertEquals(200, response.getStatus());
    }

    public static List<Task> get() {
        Response response = authorizedRequestTo(uri).get();
        assertEquals(200, response.getStatus());
        return response.readEntity(TasksContainer.class).getTasks();
    }

    public static Task getById(int taskId) {
        Response response = authorizedRequestTo(uri + "/" + taskId).get();
        assertEquals(200, response.getStatus());
        return response.readEntity(TaskContainer.class).getTask();
    }

    public static Task create(Task task) {
        Response response = authorizedRequestTo(uri).post(Entity.entity(task, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());
        return response.readEntity(TaskContainer.class).getTask();
    }

    public static Task create(String title) {
        return create(new Task(title));
    }

    public static Task update(Task task) {
        Response response =  authorizedRequestTo(task.getUri()).put(Entity.entity(task, MediaType.APPLICATION_JSON));
        assertEquals(200, response.getStatus());
        return response.readEntity(TaskContainer.class).getTask();
    }

    public static void deleteById(int taskId) {
        Response response = authorizedRequestTo(uri + "/"+ taskId).delete();
        assertEquals(200, response.getStatus());
    }

    public static void assertTasks(Task... expectedTasks) {
        List<Task> actualTasks = get();
        assertEquals(Arrays.asList(expectedTasks), actualTasks);
    }

}
