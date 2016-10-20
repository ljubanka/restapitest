package com.tasj.resources;

import com.tasj.containers.Task;
import com.tasj.containers.TaskContainer;
import com.tasj.containers.TasksContainer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static com.tasj.core.Configuration.baseUri;
import static com.tasj.core.RestHelpers.authorized;
import static com.tasj.core.RestHelpers.requestTo;
import static org.junit.Assert.assertEquals;

public class TasksApi {
    public static String uri = baseUri + "/todo/api/v1.0/tasks";

    public static Invocation.Builder authorizedRequestTo(String uri) {
        return authorized(requestTo(uri), "miguel", "python");
    }

    public static void reset() {
        Response response =  authorizedRequestTo(uri + "/reset").get();
        assertEquals(200, response.getStatus());
    }

    public static List<Task> get() {
        return authorizedRequestTo(uri).get().readEntity(TasksContainer.class).getTasks();
    }

//    public static Response sendGetRequest(String uri) {
//        return requestTo(uri).get();
//    }

//    public static Response sendAuthorizedGetRequest(String uri) {
//        return authorized(requestTo(uri)).get();
//    }

    public static Task create(Task task) {
        Response response = authorizedRequestTo(uri).post(Entity.entity(task, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());
        return response.readEntity(TaskContainer.class).getTask();
    }

    public static Task create(String title) {
        return create(new Task(title));
    }

//    public static Response create(Task task) {
//        return authorizedRequestTo(uri).post(Entity.entity(task, MediaType.APPLICATION_JSON));
//    }

//    public static Response sendAuthorizedPutRequest(String uri, Task task) {
//        return authorizedRequestTo(uri).put(Entity.entity(task, MediaType.APPLICATION_JSON));
//
//    }

    public static Task update(Task task) {
        Response response =  authorizedRequestTo(task.getUri()).put(Entity.entity(task, MediaType.APPLICATION_JSON));
        assertEquals(200, response.getStatus());
        return response.readEntity(TaskContainer.class).getTask();
    }

//    public static Response sendAuthorizedDeleteRequest(String uri) {
//        return authorizedRequestTo(uri).deleteById();
//    }

    public static void deleteById(int taskId) {
        Response response = authorizedRequestTo(uri + "/"+ taskId).delete();
        assertEquals(200, response.getStatus());
    }

//    public static List<Task> tasksFromResponse(Response response) {
//        return response.readEntity(TasksContainer.class).getTasks();
//    }
//
//    public static Task taskFromResponse (Response response) {
//        return response.readEntity(TaskContainer.class).getTask();
//    }

    public static void assertTasks(Task... expectedTasks) {
        List<Task> actualTasks = get();
        assertEquals(Arrays.asList(expectedTasks), actualTasks);
    }

//    public static List<Task> join(Task... tasksToJoin) {
//        List<Task> tasks = new ArrayList<Task>();
//        for (Task task: tasksToJoin) {
//            tasks.add(task);
//        }
//        return tasks;
//    }
//
//    public static List<Task> join(List<Task> tasks, Task... tasksToJoin) {
//        for (Task task: tasksToJoin) {
//            tasks.add(task);
//        }
//        return tasks;
//    }

//    public static void assertEqualListsOfTasks(List<Task> expectedTasks, List<Task> actualTasks) {
//        if (expectedTasks.size() == actualTasks.size()) {
//            for (int i=0; i<expectedTasks.size(); i++) {
//                assertEqualTasks(expectedTasks.get(i), actualTasks.get(i));
//                //System.out.println(actualTasks.get(i));
//            }
//        }
//    }
//
//    public static void assertEqualTasks(Task expected, Task actual) {
//        assertEquals(expected.getTitle(), actual.getTitle());
//        assertEquals(expected.getDescription(), actual.getDescription());
//        //assertEquals(expected.getUri(), actual.getUri());
//        assertEquals(expected.isDone(), actual.isDone());
//        //System.out.println(actual);
//    }

//    public static void assertStatus(int statusCode, Response response) {
//        assertEquals(statusCode, response.getStatus());
//    }
}
