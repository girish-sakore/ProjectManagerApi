package com.gsoft.projectManager.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDetails {
    private String type;
    private String token;

    public TokenDetails(String token){
        this.type = "Bearer";
        this.token = token;
    }
}
