package com.ansai.uparking.entity.body;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public class LoginBody {
    public LoginBody(String cellPhone, String code) {
        this.cellphone = cellPhone;
        this.code = code;
    }

    public String cellphone;
    public String code;
}
