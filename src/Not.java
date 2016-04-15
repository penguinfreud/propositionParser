public class Not extends Ast {
    private Ast ast;

    public Not(Ast ast) {
        this.ast = ast;
    }

    public Ast getAst() {
        return ast;
    }
}
