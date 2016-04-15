public class PropositionalLetter extends Ast {
    private String id;
    private boolean hasSub;
    private int sub;

    public PropositionalLetter(String id) {
        this.id = id;
        hasSub = false;
    }

    public PropositionalLetter(String id, int sub) {
        this.id = id;
        hasSub = true;
        this.sub = sub;
    }

    public String getId() {
        return id;
    }

    public boolean hasSubscription() {
        return hasSub;
    }

    public int getSubscription() {
        return sub;
    }
}
