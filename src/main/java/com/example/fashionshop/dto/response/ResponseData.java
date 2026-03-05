package com.example.fashionshop.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {

    private int status;
    private Object data;
    private String description;
    private boolean success = true;


}
