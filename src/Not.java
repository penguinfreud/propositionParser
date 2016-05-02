public class Not extends Ast {
    private Ast ast;

    public Not(Ast ast) {
        this.ast = ast;
    }

    public Ast getAst() {
        return ast;
    }

    @Override
    public String toString() {
        String s = ast.toString();
        return "(¬" +
                (PropositionParser.isAlpha(s.charAt(0))? " ": "") +
                s + ")";
    }
}
