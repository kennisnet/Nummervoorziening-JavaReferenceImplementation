package nl.kennisnet.nummervoorziening.client;

import school.id.eck.schemas.v1_0.Chain;

/**
 * Main class. Contains program entry point.
 */
public class Main {

    /**
     * The main entry point for the program.
     *
     * @param args Command line arguments to the program.
     */
    public static void main(String args[]) {
        new Chain(); // This line demonstrates that we can interact with generated from wsdl code here.
        System.out.println("Hello, World!");
    }
}
