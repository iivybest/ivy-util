/**
 *
 */
package org.ivy.xutil.FieldValidator;

/**
 * <p>FieldValidateException</p>
 *
 * @author miao.xl
 * @date 2016年4月8日-下午5:17:26
 * @version 1.0
 */
public class FieldValidateException extends Exception {

    private static final long serialVersionUID = 7814415608760317475L;

    /**
     *
     */
    public FieldValidateException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param message message
     * @param cause cause
     * @param enableSuppression enableSuppression
     * @param writableStackTrace writableStackTrace
     */
    public FieldValidateException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message message
     * @param cause cause
     */
    public FieldValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message message
     */
    public FieldValidateException(String message) {
        super(message);
    }

    /**
     * @param cause cause
     */
    public FieldValidateException(Throwable cause) {
        super(cause);
    }


}
