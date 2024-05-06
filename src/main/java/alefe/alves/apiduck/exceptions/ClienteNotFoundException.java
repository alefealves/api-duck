package alefe.alves.apiduck.exceptions;

public class ClienteNotFoundException extends RuntimeException{

    public ClienteNotFoundException() { super("Cliente não encontrado!");}

    public ClienteNotFoundException(String message) { super(message);}
}
