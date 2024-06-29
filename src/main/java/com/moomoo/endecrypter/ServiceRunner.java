package com.moomoo.endecrypter;

import com.moomoo.endecrypter.command.CommandInfo;
import com.moomoo.endecrypter.util.CommandUtil;
import lombok.extern.slf4j.Slf4j;

import static com.moomoo.endecrypter.util.AESUtil.*;

@Slf4j
public class ServiceRunner {
    private CommandInfo cmdInfo;
    public void run(String[] args){
        try {
            cmdInfo = CommandUtil.parseCommandLine(args);
            if (cmdInfo == null) {
                return;
            }
            String plainText = cmdInfo.getInput();
            String key = generateKey();
            log.info("Plain Text: {}", plainText);
            log.info("Generate Key: {}", key);

            String encryptedText = encrypt(plainText, key);
            log.info("Encrypted Text: {}", encryptedText);

            String decryptedText = decrypt(encryptedText, key);
            log.info("Decrypted Text: {}", decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
