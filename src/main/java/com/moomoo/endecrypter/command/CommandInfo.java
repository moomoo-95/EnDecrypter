package com.moomoo.endecrypter.command;

import lombok.Getter;
import org.apache.commons.cli.CommandLine;

@Getter
public class CommandInfo {
    // Required
    private String mode;
    private String input;
    private String inputType;
    private String output;
    public CommandInfo(CommandLine cmd) {
        loadServiceOptions(cmd);
    }

    private void loadServiceOptions(CommandLine cmd) {
        this.mode = cmd.getOptionValue("m", "encrypt");
        this.input = cmd.getOptionValue("i");
        this.inputType = cmd.getOptionValue("it", "text");
        this.output = cmd.getOptionValue("o", "");
    }
}
