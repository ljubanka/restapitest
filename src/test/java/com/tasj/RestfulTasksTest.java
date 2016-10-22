package com.tasj;
import com.tasj.containers.Task;
import com.tasj.containers.ErrorContainer;
import com.tasj.resources.TasksApi;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.*;

import static com.tasj.core.RestHelpers.requestTo;
import static com.tasj.resources.TasksApi.uri;
import static org.junit.Assert.*;


public class RestfulTasksTest {

    public final Task[] DEFAULT_TASKS = {
            new Task("Buy groceries", "Milk, Cheese, Pizza, Fruit, Tylenol", false, TasksApi.uri + "/1"),
            new Task("Learn Python", "Need to find a good Python tutorial on the web", false, TasksApi.uri + "/2")
    };

    @Before
    public void reset() {
        TasksApi.reset();
    }

    @Test
    public void testUnauthorizedRead() {
        Response response = requestTo(uri).get();
        assertEquals(403, response.getStatus());
        assertEquals(new ErrorContainer("Unauthorized access"), response.readEntity(ErrorContainer.class));
    }

    @Test
    public void testReadAll() {
        List<Task> actualTasks = TasksApi.get();
        assertEquals(Arrays.asList(DEFAULT_TASKS), actualTasks);
    }

    @Test
    public void testRead() {
        Task task = TasksApi.getById(2);
        assertEquals(DEFAULT_TASKS[1], task);
    }

    @Test
    public void testCreateTaskWithTitle() {
        TasksApi.create("give lesson");
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1], new Task("give lesson", "", false, TasksApi.uri + "/3"));
    }

    @Test
    public void testCreateTaskWithFullInformation() {
        Task task = new Task("give lesson", "RESTful API testing", false, uri + "/3");
        TasksApi.create(task);
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1], task);
    }

    @Test
    public void testUpdate() {
        Task task = new Task("t title", "d description", true, uri + "/1");
        TasksApi.update(task);
        TasksApi.assertTasks(task, DEFAULT_TASKS[1]);
    }

    @Test
    public void testDelete() {
        TasksApi.deleteById(1);
        TasksApi.assertTasks(DEFAULT_TASKS[1]);
    }

    @Test
    public void testCreateUpdateDelete() {
        Task task = new Task("t title", "d description", false, TasksApi.uri + "/3");

        TasksApi.create(task);
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1], task);

        Task editedTask = new Task("t title edited", "d description edited", true, TasksApi.uri + "/3");
        TasksApi.update(editedTask);
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1], editedTask);

        TasksApi.deleteById(3);
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1]);
    }

}
