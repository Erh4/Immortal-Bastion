package gamePackages.exceptions;

/**
 * Custom exception to indicate illegal operations involving items in the game.
 *
 * The `IllegalItemException` is thrown when an invalid or restricted operation is performed
 * on or with an item, such as adding an incompatible item to an inventory.
 */
public class IllegalItemException extends Exception{

    /**
     * Constructs an `IllegalItemException` with the specified error message.
     *
     * @param message The error message describing the cause of the exception.
     */
    public IllegalItemException(String message){
        super(message);
    }
}
