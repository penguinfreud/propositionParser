import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static PropositionParser parser = new PropositionParser();
    
    public static void main(String[] args) {
        test();
        try {
            Scanner scanner;
            if (args.length >= 2) {
                System.out.println(args[1]);
                scanner = new Scanner(new FileInputStream(args[1]));
            } else {
                scanner = new Scanner(System.in);
            }
            while (scanner.hasNextLine()) {
                Ast ast = parser.parse(scanner.nextLine());
                if (ast == null) {
                    System.out.println("false");
                } else {
                    System.out.println("true");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void testValidCase(String str) {
        if (parser.parse(str) == null) {
            System.err.println("Test failed: " + str);
        }
    }

    private static void testInvalidCase(String str) {
        if (parser.parse(str) != null) {
            System.err.println("Test failed: " + str);
        }
    }

    public static void test() {
        PropositionParser parser = new PropositionParser();
        testValidCase("a");
        testValidCase(" AjfdD\t");
        testValidCase("A _ {  00327 }");
        testValidCase("(\\not a)");
        testValidCase(" ( \\not (\\not A_{3})  ) ");
        testValidCase("((\\not a) \\and (Z_{0} \\eq p))");
        testValidCase("(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not a))))))))))))))");
        testValidCase("((z \\eq (\\not (c \\imply (\\not (Ds \\eq JI))))) \\or ((\\not a) \\imply (\\not B)))");

        testInvalidCase("");
        testInvalidCase("\\");
        testInvalidCase("(a)");
        testInvalidCase("((\\not a))");
        testInvalidCase("((a \\imply b))");
        testInvalidCase("((a \\and b");
        testInvalidCase("s_{}");
        testInvalidCase("(\\not Se_{32)");
        testInvalidCase("(a \\f b)");
        testInvalidCase("(a a \\and b)");
        testInvalidCase("a_{32 3}");
        testInvalidCase("((\\not jf_) \\imply b)");
        testInvalidCase("(a \\imply (b))");
    }
}
