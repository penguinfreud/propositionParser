public class CompoundProposition extends Ast {
    public enum ConnectiveType {
        AND("∧"), OR("∨"), IMPLY("→"), EQ("↔");

        private String str;
        ConnectiveType(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
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

    @Override
    public String toString() {
        String s = second.toString();
        return "(" + first.toString() + " " + type.toString() + " " +
                s + ")";
    }
}
