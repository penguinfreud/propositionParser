public class CompoundProposition extends Ast {
    public enum ConnectiveType {
        AND, OR, IMPLY, ConnectiveType, EQ
    }

    private ConnectiveType type;
    private Ast first, second;

    public CompoundProposition(ConnectiveType type, Ast first, Ast second) {
        this.type = type;
        this.first = first;
        this.second = second;
    }

    public ConnectiveType getType() {
        return type;
    }

    public Ast getFirst() {
        return first;
    }

    public Ast getSecond() {
        return second;
    }
}
