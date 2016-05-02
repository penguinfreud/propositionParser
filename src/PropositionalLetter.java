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

    private static final String subNums = "₀₁₂₃₄₅₆₇₈₉";
    @Override
    public String toString() {
        if (hasSub) {
            StringBuffer sb = new StringBuffer();
            int x = sub;
            while (x > 0) {
                int d = x % 10;
                sb.insert(0, subNums.charAt(d));
                x /= 10;
            }
            sb.insert(0, id);
            return sb.toString();
        } else {
            return id;
        }
    }
}
