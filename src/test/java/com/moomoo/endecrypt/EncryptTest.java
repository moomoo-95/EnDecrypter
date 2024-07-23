package com.moomoo.endecrypt;

import com.moomoo.endecrypter.service.ServiceRunner;
import com.moomoo.endecrypter.info.ResultInfo;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;


public class EncryptTest {

    @DisplayName("text를 Key로 암복호화하는 테스트")
    @Test
    public void mainTest(){
        String plainText = "askjdlksahdjjdhsfk";
        String key = "moomooProject";
        String[] encArgs = {
                "-i", plainText,
                "-k", key
        };
        ResultInfo encResult = new ServiceRunner().run(encArgs);
        assertThat(encResult.getInput()).isEqualTo(plainText);
        assertThat(encResult.getKey()).isEqualTo(key);

        String[] decArgs = {
                "-m", "decrypt",
                "-i", encResult.getOutput(),
                "-k", encResult.getKey()
        };
        ResultInfo decResult = new ServiceRunner().run(decArgs);
        assertThat(decResult.getInput()).isEqualTo(encResult.getOutput());
        assertThat(decResult.getKey()).isEqualTo(key);
        assertThat(decResult.getOutput()).isEqualTo(plainText);

    }
}
