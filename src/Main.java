import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
}
