package com.moomoo.endecrypter.command;

import com.moomoo.endecrypter.util.CommandUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CommandTest {

    @Test
    public void commandOptionTest(){
        String[] args = {
                "-i", "moomooProject",
                "-o", "file.txt"};
        CommandInfo cmdInfo = CommandUtil.parseCommandLine(args);
        assertThat(cmdInfo.getMode()).isEqualTo("encrypt");
        assertThat(cmdInfo.getInput()).isEqualTo("moomooProject");
        assertThat(cmdInfo.getInputType()).isEqualTo("text");
        assertThat(cmdInfo.getOutput()).isEqualTo("file.txt");
    }
}
