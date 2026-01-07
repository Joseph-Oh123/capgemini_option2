
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Todo List Application ===");
        System.out.println();

        FileManager fileManager = new FileManager("tasks.txt");

        TaskManager taskManager = new TaskManager(fileManager);

        taskManager.loadTasks();

        CommandLine cli = new CommandLine(taskManager);
        cli.start();

        System.out.println("Thank you for using Todo List Application!");
    }
}
