package com.augmentum.excep;

/**
 * @author lzh
 * @createe 2023-02-14-15:04
 */

//自定义的运行时异常
public class NotEnoughExeception extends RuntimeException{


    public NotEnoughExeception() {
        super();
    }

    public NotEnoughExeception(String message) {
        super(message);
    }
}
