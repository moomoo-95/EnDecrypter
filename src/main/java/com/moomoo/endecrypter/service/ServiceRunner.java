package com.moomoo.endecrypter.service;

import com.moomoo.endecrypter.command.CommandInfo;
import com.moomoo.endecrypter.info.ResultInfo;
import com.moomoo.endecrypter.util.CommandUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.moomoo.endecrypter.util.AESUtil.*;

/**
 * @author Hyeon seong, Lim
 * 서비스를 실행시키는 클래스
 */
@Slf4j
public class ServiceRunner {
    public ResultInfo run(String[] args){
        try {
            // 1. parsing option
            CommandInfo cmdInfo = CommandUtil.parseCommandLine(args);
            if (cmdInfo == null) return null;
            // 2. parsing input data
            String plainText = getPlainText(cmdInfo.getInput(), cmdInfo.getInputType().toLowerCase());
            if(plainText == null) return null;
            // 3. create key
            String key = customKey(cmdInfo.getKey());

            // 4. processing encrypt or decrypt
            String result;
            if(cmdInfo.getMode().equalsIgnoreCase("encrypt")){
                result = encrypt(plainText, key);
            } else {
                result = decrypt(plainText, key);
            }

            // 5. result output
            ResultInfo output = new ResultInfo(plainText, cmdInfo.getKey(), result);

            String filePath = cmdInfo.getOutput();
            if(filePath.isBlank()){
                log.info("[{} SUCCESS]\n{}", cmdInfo.getMode().toUpperCase(), output);
            } else {
                writeFile(filePath, output.toString());
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPlainText(String input, String type){
        switch (type){
            case "text":
                return input;
            case "file":
                try {
                    byte[] inputData = Files.readAllBytes(Paths.get(input));
                    return new String(inputData, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    log.error("ServiceRunner.getPlainText File read fail ", e);
                    return null;
                }
            default:
                log.error("ServiceRunner.getPlainText Unexpected input type");
                return null;
        }
    }

    private void writeFile(String filePath, String data) {
        try {
            Files.write(Paths.get(filePath), data.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error("ServiceRunner.processingOutput File write fail ", e);
        }
    }

}
