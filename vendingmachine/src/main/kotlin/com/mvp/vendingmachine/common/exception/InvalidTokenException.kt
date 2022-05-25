package com.mvp.vendingmachine.common.exception

import org.springframework.security.core.AuthenticationException

/**
 * ApplicationException is the class that defines the Exception handling to used by through out the project.
 */
class InvalidTokenException : AuthenticationException {
    /**
     * Gets the error code.
     *
     * @return the error code
     */
    /** The error code.  */
    var errorCode: ErrorCode
    /**
     * Gets the error msg.
     *
     * @return the error msg
     */
    /** The error msg.  */
    var errorMsg: String? = null

    /**
     * Sets the error code.
     *
     * @param code
     * the code
     * @param message
     * the message
     * @return the error msg
     */
    constructor(code: ErrorCode, message: String?) : super(message) {
        errorCode = code
        errorMsg = message
    }



    /**
     * Instantiates a new application exception.
     *
     * @param code
     * the code
     * @param message
     * the message
     * @param e
     * the e
     */
    constructor(code: ErrorCode, message: String?, e: Exception?) : super(message, e) {
        errorCode = code
        errorMsg = message
    }

}
