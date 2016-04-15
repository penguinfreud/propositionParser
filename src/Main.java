import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
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
            PropositionParser parser = new PropositionParser();
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

    private static void testValidCase(PropositionParser parser, String str) {
        if (parser.parse(str) == null) {
            System.err.println("Test failed: " + str);
        }
    }

    private static void testInvalidCase(PropositionParser parser, String str) {
        if (parser.parse(str) != null) {
            System.err.println("Test failed: " + str);
        }
    }

    public static void test() {
        PropositionParser parser = new PropositionParser();
        testValidCase(parser, "a");
        testValidCase(parser, " AjfdD\t");
        testValidCase(parser, "A _ {  00327 }");
        testValidCase(parser, "(\\not a)");
        testValidCase(parser, " ( \\not (\\not A_{3})  ) ");
        testValidCase(parser, "((\\not a) \\and (Z_{0} \\eq p))");
        testValidCase(parser, "(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not(\\not a))))))))))))))");
        testValidCase(parser, "((z \\eq (\\not (c \\imply (\\not (Ds \\eq JI))))) \\or ((\\not a) \\imply (\\not B)))");

        testInvalidCase(parser, "");
        testInvalidCase(parser, "\\");
        testInvalidCase(parser, "(a)");
        testInvalidCase(parser, "((\\not a))");
        testInvalidCase(parser, "((a \\imply b))");
        testInvalidCase(parser, "((a \\and b");
        testInvalidCase(parser, "s_{}");
        testInvalidCase(parser, "(\\not Se_{32)");
        testInvalidCase(parser, "(a \\f b)");
        testInvalidCase(parser, "(a a \\and b)");
        testInvalidCase(parser, "a_{32 3}");
        testInvalidCase(parser, "((\\not jf_) \\imply b)");
        testInvalidCase(parser, "(a \\imply (b))");
    }
}
