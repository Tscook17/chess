import ui.consolerepls.PreLoginRepl;

public class Main {
    public static void main(String[] args) {
        String serverUrl;
        if (args.length != 0) {
            serverUrl = args[1];
        } else {
            serverUrl = "http://localhost:8080";
        }
        new PreLoginRepl(serverUrl).run();
    }
}