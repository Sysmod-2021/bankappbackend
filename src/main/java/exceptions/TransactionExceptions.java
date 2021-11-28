package exceptions;

public class TransactionExceptions {
    public static class TransactionCanNotBeRevoked extends Exception {
        public TransactionCanNotBeRevoked(String message) {
            super(message);
        }
    }
}
