package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.cli.*;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Main function to run the application
     * @param args optional arguments are -i for input file and -o for output file
     */
    public static void main(String[] args) {
        Options options = new Options();
        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);

        Option option = new Option("o", "output", true, "output file path");
        option.setRequired(false);
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String inputFile = cmd.getOptionValue("input");
            String outputFile = cmd.getOptionValue("output");
            new DeduplicationApplication().run(inputFile, outputFile);
        } catch (Exception e) {
            LOGGER.error("Exception in parsing args", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CommandLineExample", options);
        }
    }
}