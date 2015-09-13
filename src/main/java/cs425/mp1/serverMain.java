package cs425.mp1;

/**
 * main class for launching server
 */
public class serverMain {

    public static void main(String [] args) {
        if ((args.length!=1)) {throw new AssertionError("Number of args expected 1. " +
                "Number of args received "+args.length);}
        grepServer.getNewInstance(Integer.parseInt(args[0])).runServer();
    }
}
