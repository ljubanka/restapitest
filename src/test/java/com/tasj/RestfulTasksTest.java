package com.tasj;
import com.tasj.containers.TaskContainer;
import com.tasj.core.RestHelpers;
import com.tasj.containers.Task;
import com.tasj.containers.ErrorContainer;
import com.tasj.resources.TasksApi;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.*;

import static com.tasj.resources.TasksApi.authorizedRequestTo;
import static com.tasj.resources.TasksApi.uri;
import static org.junit.Assert.*;


public class RestfulTasksTest {

    public final Task[] DEFAULT_TASKS = {
            new Task("Buy groceries", "Milk, Cheese, Pizza, Fruit, Tylenol", false, TasksApi.uri + "/1"),
            new Task("Learn Python", "Need to find a good Python tutorial on the web", false, TasksApi.uri + "/2")
    };

    @Before
    public void testReset() {
        TasksApi.reset();
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1]);
    }

    @Test
    public void testUnauthorizedRead() {
        Response response = RestHelpers.requestTo(uri).get();

        assertEquals(403, response.getStatus());
        assertEquals(new ErrorContainer("Unauthorized access"), response.readEntity(ErrorContainer.class));
    }

    @Test
    public void testReadAll() {
        //TasksApi.reset();
        List<Task> actualTasks = TasksApi.get();
        assertEquals(Arrays.asList(DEFAULT_TASKS), actualTasks);
    }

    @Test
    public void testRead() {
        Task task = authorizedRequestTo(uri + "/2").get().readEntity(TaskContainer.class).getTask();
        assertEquals(DEFAULT_TASKS[1], task);
    }

    @Test
    public void testCreateTaskWithTitle() {
        //TasksApi.reset();

        TasksApi.create("give lesson");
//        Task newTask = new Task("give lesson");
//        newTask.setUri(uri + "/3");
        //Task addedTask = TasksApi.create("give lesson");
        //assertEquals(newTask, addedTask);
        //TasksApi.assertEqualTasks(newTask, addedTask);

        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1], new Task("give lesson", "", false, TasksApi.uri + "/3"));
    }

    @Test
    public void testCreateTaskWithFullInformation() {
        //TasksApi.reset();

        Task task = new Task("give lesson", "RESTful API testing", false, uri + "/3");
        TasksApi.create(task);
        //Task addedTask = TasksApi.create(newTask);
        //assertEquals(newTask, addedTask);
        TasksApi.assertTasks(DEFAULT_TASKS[0], DEFAULT_TASKS[1], task);

    }

//    @Test
//    public void testUpdateTitle() {
//        //TasksApi.reset();
//
//        Task task = new Task("t title");
//        task.setUri(uri + "/2");
//        TasksApi.update(task);
//
//        //Task updatedTask = TasksApi.update(task);
//        //assertEquals(task, updatedTask);
//        TasksApi.assertTasks(DEFAULT_TASKS[0], task);
//    }

    @Test
    public void testUpdateFullInformation() {
        //TasksApi.reset();

        Task task = new Task("t title", "d description", true, uri + "/2");
        task.setUri(uri + "/2");

        TasksApi.update(task);

        //Task updatedTask = TasksApi.update(newTask);
        //assertEquals(newTask, updatedTask);
        TasksApi.assertTasks(DEFAULT_TASKS[0], task);

    }

    @Test
    public void testDelete() {
        //TasksApi.reset();

        TasksApi.deleteById(2);
        TasksApi.assertTasks(DEFAULT_TASKS[0]);
    }

    @Test
    public void testCreateUpdateDelete() {
        //TasksApi.reset();
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
