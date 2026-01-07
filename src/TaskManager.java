import java.util.*;

public class TaskManager {
    private List<Task> tasks;
    private FileManager fileManager;
    private int nextId;

    public TaskManager(FileManager fileManager) {
        this.tasks = new ArrayList<>();
        this.fileManager = fileManager;
        this.nextId = 1;
    }

    public void loadTasks() {
        this.tasks = fileManager.readTasks();
        // Set nextId to be one more than the highest existing ID
        int maxId = 0;
        for(Task task : tasks) {
            if(task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        nextId = maxId + 1;
        System.out.println("Loaded " + tasks.size() + " tasks from file.");
    }

    public void saveTasks() {
        fileManager.writeTasks(tasks);
    }

    public Task addTask(String title, String description, String category) {
        Task task = new Task(nextId++, title, description, category);
        tasks.add(task);
        saveTasks();
        return task;
    }

    public boolean removeTask(int id) {
        for(int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getId() == id) {
                tasks.remove(i);
                saveTasks();
                return true;
            }
        }
        return false;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByCategory(String category) {
        List<Task> result = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getCategory().equalsIgnoreCase(category)) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> getTasksByStatus(boolean completed) {
        List<Task> result = new ArrayList<>();
        for(Task task : tasks) {
            if(task.isCompleted() == completed) {
                result.add(task);
            }
        }
        return result;
    }

    public boolean markTaskComplete(int id) {
        for(Task task : tasks) {
            if(task.getId() == id) {
                task.setCompleted(true);
                saveTasks();
                return true;
            }
        }
        return false;
    }

    public boolean markTaskIncomplete(int id) {
        for(Task task : tasks) {
            if(task.getId() == id) {
                task.setCompleted(false);
                saveTasks();
                return true;
            }
        }
        return false;
    }

    public Task findTaskById(int id) {
        for(Task task : tasks) {
            if(task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        for(Task task : tasks) {
            String category = task.getCategory();
            if(!categories.contains(category)) {
                categories.add(category);
            }
        }
        return categories;
    }

}