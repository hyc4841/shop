package love.shop.controller;

import lombok.Data;

@Data
public class ReAccessToken {

    private String accessToken;

    public ReAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
