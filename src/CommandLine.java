import java.util.List;
import java.util.Scanner;

public class CommandLine {
    private TaskManager taskManager;
    private Scanner scanner;

    public CommandLine(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while(running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();

            switch(choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    listAllTasks();
                    break;
                case 3:
                    listByCategory();
                    break;
                case 4:
                    listByStatus();
                    break;
                case 5:
                    markTaskComplete();
                    break;
                case 6:
                    markTaskIncomplete();
                    break;
                case 7:
                    removeTask();
                    break;
                case 8:
                    viewCategories();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("========================================");
        System.out.println("           TODO LIST MENU");
        System.out.println("========================================");
        System.out.println("1. Add new task");
        System.out.println("2. List all tasks");
        System.out.println("3. List tasks by category");
        System.out.println("4. List tasks by status (complete/incomplete)");
        System.out.println("5. Mark task as complete");
        System.out.println("6. Mark task as incomplete");
        System.out.println("7. Remove task");
        System.out.println("8. View all categories");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    private void addTask() {
        System.out.println("--- Add New Task ---");

        System.out.print("Enter task title: ");
        String title = scanner.nextLine();

        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        System.out.print("Enter category (e.g., Work, Personal, Shopping, Health): ");
        String category = scanner.nextLine();

        if(title.trim().isEmpty()) {
            System.out.println("Error: Title cannot be empty!");
            return;
        }

        Task task = taskManager.addTask(title, description, category);
        System.out.println("Task added successfully! (ID: " + task.getId() + ")");
    }

    private void listAllTasks() {
        System.out.println("--- All Tasks ---");
        List<Task> tasks = taskManager.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found. Your list is empty.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
                System.out.println("----------------------------------------");
            }
            System.out.println("Total tasks: " + tasks.size());
        }
    }

    private void listByCategory() {
        System.out.println("--- List by Category ---");
        System.out.print("Enter category name: ");
        String category = scanner.nextLine();

        List<Task> tasks = taskManager.getTasksByCategory(category);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found in category: " + category);
        } else {
            System.out.println("\nTasks in category '" + category + "':");
            for (Task task : tasks) {
                System.out.println(task);
                System.out.println("----------------------------------------");
            }
            System.out.println("Total: " + tasks.size() + " task(s)");
        }
    }

    private void listByStatus() {
        System.out.println("--- List by Status ---");
        System.out.println("1. Completed tasks");
        System.out.println("2. Incomplete tasks");
        int choice = getIntInput("Enter choice: ");

        boolean showCompleted = (choice == 1);
        List<Task> tasks = taskManager.getTasksByStatus(showCompleted);

        if (tasks.isEmpty()) {
            System.out.println("No " + (showCompleted ? "completed" : "incomplete") + " tasks found.");
        } else {
            System.out.println("\n" + (showCompleted ? "Completed" : "Incomplete") + " tasks:");
            for (Task task : tasks) {
                System.out.println(task);
                System.out.println("----------------------------------------");
            }
            System.out.println("Total: " + tasks.size() + " task(s)");
        }
    }

    private void markTaskComplete() {
        System.out.println("--- Mark Task as Complete ---");
        int id = getIntInput("Enter task ID: ");

        if (taskManager.markTaskComplete(id)) {
            System.out.println("Task marked as complete");
        } else {
            System.out.println("Error: Task with ID " + id + " not found.");
        }
    }

    private void markTaskIncomplete() {
        System.out.println("--- Mark Task as Incomplete ---");
        int id = getIntInput("Enter task ID: ");

        if (taskManager.markTaskIncomplete(id)) {
            System.out.println("Task marked as incomplete!");
        } else {
            System.out.println("Error: Task with ID " + id + " not found.");
        }
    }

    private void removeTask() {
        System.out.println("--- Remove Task ---");
        int id = getIntInput("Enter task ID to remove: ");

        Task task = taskManager.findTaskById(id);
        if (task != null) {
            System.out.println("Task to remove: " + task.getTitle());
            System.out.print("Are you sure? (yes/no): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                taskManager.removeTask(id);
                System.out.println("Task removed successfully!");
            } else {
                System.out.println("Removal cancelled.");
            }
        } else {
            System.out.println("Error: Task with ID " + id + " not found.");
        }
    }

    private void viewCategories() {
        System.out.println("--- All Categories ---");
        List<String> categories = taskManager.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("No categories found. Add some tasks first!");
        } else {
            System.out.println("Available categories:");
            for (String category : categories) {
                int count = taskManager.getTasksByCategory(category).size();
                System.out.println(category + " (" + count + " task(s))");
            }
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}