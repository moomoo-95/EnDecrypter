package com.moomoo.endecrypter.info;

import lombok.Data;

/**
 * @author Hyeon seong, Lim
 * 실행 결과 정보를 담은 객체
 */
@Data
public class ResultInfo {
    private final String input;
    private final String key;
    private final String output;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INPUT : ").append(input).append("\n")
                .append("Key   : ").append(key).append("\n")
                .append("OUTPUT: ").append(output);
        return sb.toString();
    }
}
