import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            if (args.length != 0) {
                server.run(Integer.parseInt(args[1]));
            } else {
                server.run(8080);
            }
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }
}