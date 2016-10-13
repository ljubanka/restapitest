package com.tasj;
import com.tasj.core.Helpers;
import com.tasj.core.Task;
import com.tasj.pages.TasksPage;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.*;

import static com.tasj.pages.TasksPage.URI;
import static com.tasj.pages.TasksPage.defaultTasks;
import static org.junit.Assert.*;


public class RESTfulTasksTest {
    TasksPage page = new TasksPage();

    @BeforeClass
    public static void initTasks() {
       //straight way to save our default tasks
        Task firstTask = new Task("Buy groceries", "Milk, Cheese, Pizza, Fruit, Tylenol", false);
        firstTask.setUri("http://localhost:5000/todo/api/v1.0/tasks/1");
        Task secondTask = new Task("Learn Python", "Need to find a good Python tutorial on the web", false);
        secondTask.setUri("http://localhost:5000/todo/api/v1.0/tasks/2");

        //List<Task> defaultTasks = new ArrayList<Task>();
        defaultTasks.add(firstTask);
        defaultTasks.add(secondTask);

       //tried this way also but got an error from IDEA
//       List<Task> defaultTasksNewWay = Arrays.asList(new Task("Buy groceries", "Milk, Cheese, Pizza, Fruit, Tylenol", false)
//               .setUri("http://localhost:5000/todo/api/v1.0/tasks/1"),
//            new Task("Learn Python", "Need to find a good Python tutorial on the web", false).setUri("http://localhost:5000/todo/api/v1.0/tasks/2"));
   }

    @Test
    public void testResetTasks() {
        Response response = page.resetTasks();

        page.assertStatus(200, response);
        page.assertEqualListsOfTasks(defaultTasks, page.tasksFromResponse(response));
    }

    @Test
    public void testUnauthorizedReadTasks() {
        Response response = page.sendGetRequest(URI);

        page.assertStatus(403, response);
        assertEquals(new ErrorContainer("Unauthorized access"), response.readEntity(ErrorContainer.class));
    }

    @Test
    public void testReadTasks() {
        page.resetTasks();

        Response response = page.sendAuthorizedGetRequest(URI);

        page.assertStatus(200, response);
        page.assertEqualListsOfTasks(defaultTasks, page.tasksFromResponse(response));
    }

    @Test
    public void testCreateTaskWithTitle() {
        page.resetTasks();

        Task newTask = new Task("give lesson");
        Response response = page.sendAuthorizedPostRequest(URI, newTask);

        page.assertStatus(201, response);
        page.assertEqualTasks(newTask, page.taskFromResponse(response));

        List<Task> expectedTasks = page.joinTasks(defaultTasks, newTask);
        page.assertEqualListsOfTasks(expectedTasks, page.availableTasks());
    }

    @Test
    public void testCreateTaskWithFullInformation() {
        page.resetTasks();

        Task newTask = new Task("give lesson", "RESTful API testing", false);
        Response response = page.sendAuthorizedPostRequest(URI, newTask);

        page.assertStatus(201, response);
        page.assertEqualTasks(newTask, page.taskFromResponse(response));

        List<Task> expectedTasks = page.joinTasks(defaultTasks, newTask);
        page.assertEqualListsOfTasks(expectedTasks, page.availableTasks());
    }

    @Test
    public void testUpdateTaskTitle() {
        page.resetTasks();

        Task newTask = new Task(Helpers.getUniqueText("t "));
        String id ="2";

        Response response = page.sendAuthorizedPutRequest(URI + "/" + id, newTask);

        page.assertStatus(200, response);
        page.assertEqualTasks(newTask, page.taskFromResponse(response));

        List<Task> expectedTasks = page.joinTasks(defaultTasks.get(0), newTask);
        page.assertEqualListsOfTasks(expectedTasks, page.availableTasks());
    }

    @Test
    public void testUpdateTaskFullInformation() {
        page.resetTasks();

        Task newTask = new Task(Helpers.getUniqueText("t "), Helpers.getUniqueText("d "), true);
        String id ="2";

        Response response = page.sendAuthorizedPutRequest(URI + "/" + id, newTask);

        page.assertStatus(200, response);
        page.assertEqualTasks(newTask, page.taskFromResponse(response));

        List<Task> expectedTasks = page.joinTasks(defaultTasks.get(0), newTask);
        page.assertEqualListsOfTasks(expectedTasks, page.availableTasks());
    }

    @Test
    public void testDelete() {
        page.resetTasks();

        String id = "2";
        Response response = page.sendAuthorizedDeleteRequest(URI + "/" + id);

        page.assertStatus(200, response);

        List<Task> expectedTasks = page.joinTasks(defaultTasks.get(0));
        page.assertEqualListsOfTasks(expectedTasks, page.availableTasks());
    }

    @Test
    public void testCreateUpdateDelete() {
        page.resetTasks();
        Task testTask = new Task("give lesson");

        Response response = page.sendAuthorizedPostRequest(URI, testTask);
        //page.assertStatus(201, response);
        //page.assertEqualListsOfTasks(page.joinTasks(defaultTasks, testTask), page.availableTasks());

        String addedTaskUri = page.taskFromResponse(response).getUri();
        testTask.setTitle("stop lesson");
        response = page.sendAuthorizedPutRequest(addedTaskUri, testTask);
        //page.assertStatus(200, response);
        //page.assertEqualListsOfTasks(page.joinTasks(defaultTasks, testTask), page.availableTasks());

        page.sendAuthorizedDeleteRequest(addedTaskUri);
        page.assertEqualListsOfTasks(defaultTasks, page.availableTasks());
    }

    static class ErrorContainer {
        String error;

        public ErrorContainer(String error) {
            this.error = error;
        }

        public ErrorContainer() {
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        //*override is needed
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ErrorContainer that = (ErrorContainer) o;

            return error != null ? error.equals(that.error) : that.error == null;

        }

        @Override
        public int hashCode() {
            return error != null ? error.hashCode() : 0;
        }
    }


}
