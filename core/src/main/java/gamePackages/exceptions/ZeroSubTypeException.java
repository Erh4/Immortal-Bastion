package gamePackages.exceptions;


/**
 * Custom exception to indicate that a required subtype is missing or not defined.
 *
 * The `ZeroSubTypeException` is thrown when an operation or entity requires a subtype,
 * but no valid subtype is provided or available.
 */
public class ZeroSubTypeException extends Exception {

    /**
     * Constructs a `ZeroSubTypeException` with the specified error message.
     *
     * @param message The error message describing the cause of the exception.
     */
    public ZeroSubTypeException(String message){super(message);}
}
