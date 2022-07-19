package com.enation.app.javashop.model.base.vo;

/**
 * SuccessMessage
 *
 * @author chopper
 * @version v1.0
 * @since v7.0
 * 2018-05-11 上午9:03
 */
public class SuccessMessage {

    private Object message;

    public SuccessMessage() {
    }

    public SuccessMessage( Object message ) {

        this.message = message;

    }


    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "SuccessMessage{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SuccessMessage that = (SuccessMessage) o;

        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
