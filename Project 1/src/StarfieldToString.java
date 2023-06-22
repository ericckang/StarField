import java.io.FileNotFoundException;

public class StarfieldToString {
    public static final String[] testFiles = {"aquarius.txt", "aries.txt", "cancer.txt", "capricornus.txt"};
    public static void main(String[] args) throws FileNotFoundException {
        Starfield solution = new Starfield("./inputs/" + "aries.txt");
        solution.setMagnitude(0,11,6);
   //         Starfield combined = Starfield.combine(new Starfield("./inputs/comb1.txt"),
    //            new Starfield("./inputs/comb2.txt"));
    //    solution.toString();
        System.out.println(solution);
    }
}
