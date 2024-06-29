package com.moomoo.endecrypter.util;

import com.moomoo.endecrypter.command.CommandInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

@Slf4j
public class CommandUtil {
    private static Options opts;

    private CommandUtil() {
        throw new IllegalStateException("Utility class");
    }
    public static CommandInfo parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandInfo cmdInfo;
        try {
            CommandLine cmd = parser.parse(createOptions(), args);
            cmdInfo = new CommandInfo(cmd);
            if (cmd.hasOption("h")) {
                new HelpFormatter().printHelp("endecrypter.jar [OPTIONS] (see -h options)", createOptions());
                return null;
            }

        } catch (Exception e) {
            log.error("CommandParser.Exception ",  e);
            new HelpFormatter().printHelp("endecrypter.jar [OPTIONS] (see -h options)", createOptions());
            return null;
        }
        return cmdInfo;
    }

    private static Options createOptions() {
        if(opts != null) return opts;
        opts = new Options();

        // Service
        opts.addOption(new Option("h", "display help text"));
        opts.addOption(Option.builder("m").argName("mode").hasArg().desc("The Convert Mode. [encrypt(default) | decrypt]").build());
        opts.addOption(Option.builder("i").argName("input").hasArg().desc("Input data. [text | file name]").required().build());
        opts.addOption(Option.builder("it").argName("input type").hasArg().desc("Input data type. [text(default) | file]").build());
        opts.addOption(Option.builder("o").argName("output").hasArg().desc("Output data file name. Output to log if not set.").build());

        return opts;
    }
}
