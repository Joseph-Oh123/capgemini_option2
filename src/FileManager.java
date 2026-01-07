import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileManager {
    private String filePath;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public FileManager(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        File directory = file.getParentFile();

        try {
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    public List<Task> readTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if(!file.exists() || file.length() == 0) {
            return tasks;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.trim().isEmpty()) continue;

                Task task = parseTask(line);
                if(task != null) {
                    tasks.add(task);
                }
            }
        } catch(IOException e) {
            System.err.println("Error reading tasks: " + e.getMessage());
        }

        return tasks;
    }

    public void writeTasks(List<Task> tasks) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(taskToString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing tasks: " + e.getMessage());
        }
    }

    private String taskToString(Task task) {
        return String.join("|",
                String.valueOf(task.getId()),
                task.getTitle(),
                task.getDescription(),
                task.getCategory(),
                String.valueOf(task.isCompleted()),
                task.getCreatedAt().format(DATE_FORMATTER)
        );
    }

    private Task parseTask(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 6) return null;

            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            String description = parts[2];
            String category = parts[3];
            boolean completed = Boolean.parseBoolean(parts[4]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[5], DATE_FORMATTER);

            Task task = new Task(id, title, description, category);
            task.setCompleted(completed);
            task.setCreatedAt(createdAt);

            return task;
        } catch (Exception e) {
            System.err.println("Error parsing task: " + e.getMessage());
            return null;
        }
    }
}