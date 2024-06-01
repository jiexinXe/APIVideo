package com.apivideo.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rest {
    @JsonProperty("code")
    private Integer code;
    private Object data;
    @JsonProperty("msg")
    private String msg;

    public Rest(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
