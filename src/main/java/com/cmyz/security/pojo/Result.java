package com.cmyz.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：cmyz
 * @date ：2021/1/10 12:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    public Result(Integer code,String msg){
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    private Integer code;

    private String msg;

    private T data;

}
