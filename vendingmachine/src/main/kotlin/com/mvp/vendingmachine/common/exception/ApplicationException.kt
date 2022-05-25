package com.mvp.vendingmachine.common.exception

/**
 * ApplicationException is the class that defines the Exception handling to used by through out the project.
 */
class ApplicationException : RuntimeException {
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
     * Instantiates a new application exception.
     *
     * @param code
     * the code
     */
    constructor(code: ErrorCode) : super() {
        errorCode = code
    }

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
     * @param e
     * the e
     */
    constructor(code: ErrorCode, e: Exception?) : super(e) {
        errorCode = code
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

    constructor(code: ErrorCode, cause: Throwable?) : super(cause) {
        errorCode = code
    }
}
