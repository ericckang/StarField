import java.io.*;
import java.sql.SQLOutput;

public class StarfieldTest {

    public static final double eps = 0.00001;
    public static final String[] testFiles = {"aquarius.txt", "aries.txt", "cancer.txt", "capricornus.txt"};
    public static final String[] ansFiles = {"aquarius_ascii.txt", "aries_ascii.txt", "cancer_ascii.txt", "capricornus_ascii.txt"};
    public static final String[] ansFiles2 = {"aquarius", "aries", "cancer", "capricornus"};

    public static void main(String[] args) throws FileNotFoundException {

//        PART 1 TEST CASES:
        int totalScore1 = testPart1();
        int totalScore2 = testPart2();

        System.out.println("\nTotal Score PART 1: " + totalScore1 + "/40");
        System.out.println("Total Score PART 2: " + totalScore2 + "/27");
        System.out.println("Note: Set and unSet have not been tested (9 + 9)");

        System.out.println();
        System.out.println("***************************************************************");
        System.out.println("* These scores are only an indicator and NOT the final scores *");
        System.out.println("***************************************************************");

    }



    /*
     *  Part 1
     *    - These test cases are very trivial and do not Check the constructor and the getMagnitude functions.
     *    - They are an indicator of if your implementation is along the right directions or not
     *    - A correct toString method will have certain implications which will help you test your functions
     *    - You are encouraged to develop your own test cases.
     */
    private static int testPart1() throws FileNotFoundException {
        int totalScore = 0;
        int[] numRows = {23, 32, 38, 30, 45};
        int[][] rowsNumStars = {
                {2, 3, 3, 2, 1, 0, 2, 2, 1, 3, 5, 2, 0, 3, 3, 2, 3, 2, 4, 4, 2, 1, 1},
                {1, 2, 3, 3, 0, 5, 2, 1, 3, 3, 2, 2, 1, 2, 1, 5, 3, 2, 3, 0, 3, 2, 1, 0, 0, 3, 0, 0, 2, 2, 1, 1},
                {1, 2, 3, 0, 2, 0, 2, 2, 3, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 0, 0, 3, 2, 2, 2, 2, 1, 0, 1, 3, 2, 1, 1, 2, 1, 0, 2},
                {0, 4, 4, 0, 0, 2, 3, 2, 1, 2, 4, 0, 4, 2, 3, 3, 2, 2, 2, 3, 3, 1, 2, 0, 0, 2, 1, 2, 2, 1}};
        double[] densities = {0.02772, 0.02305, 0.02368, 0.02375};
        System.out.println("Testing Part 1:");
        for (int i = 0; i < 4; i++) {
            System.out.println(testFiles[i] + ":");
            String ans = "";
            Starfield solution = new Starfield("./inputs/" + testFiles[i]);
            String answer = readFile("./outputs/" + ansFiles[i]);
            if (answer.equals(solution.toString())) {
                System.out.println("    toString: pass");
                totalScore += 4;
            } else {
                System.out.println("    toString: FAIL");
            }
            if (numRows[i] == solution.numRows()) {
                System.out.println("    numRows(): pass");
                boolean flag = true;
                for (int j = 0; j < solution.numRows(); j++) {
                    if (rowsNumStars[i][j] != solution.numStarsInRow(j)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    totalScore += 3;
                    System.out.println("    numStarsInRow(): pass");
                } else {
                    System.out.println("    numStarsInRow(): FAIL");
                }
            } else {
                System.out.println("    numRows(): FAIL");
                System.out.println("    numStarsInRow() NOT CHECKED");
            }

            if (Math.abs(densities[i] - solution.density()) < 0.0001) {
                totalScore += 3;
                System.out.println("    density(): pass");
            } else {
                System.out.println("    density(): FAIL");
            }
        }
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println();
        return  totalScore;
    }


    /*
     *  Part 2: PLEASE READ CAREFULLY
     *    - These test cases are very trivial and do not Check the insert and the delete functions.
     *
     *    - They are an indicator of if your implementation is along the right directions or not
     *
     *    - YOU NEED A CORRECT TOSTRING AND CONSTRUCTOR FROM PART 1 FOR THESE TEST CASES TO RUN CORRECTLY
     *
     *    - THE TEXT FILES PROVIDED WILL ALL GO IN THE INPUTS FOLDER/ OUTPUT FOLDER(combine_ans.txt and )
     *
     *    - You are encouraged to develop your own test cases.
     *
     *    - See how the test cases are testing your code
     *
     *    - more importantly see what important aspect of the field is NOT being tested
     */
    private static int testPart2() throws FileNotFoundException {
        // part 2 tests
        int totalScore2 = 0;
        System.out.println("Testing Part 2:");

        // test multiplication
        double[] vector = {0.11, 0.5, 0.9, 0.7, 0.43, 1.85, 1.03, 0.59, 0.82, 0.10,
                0.11, 0.5, 0.9, 0.7, 0.43, 1.85, 1.03, 0.59, 0.82, 0.10,
                0.2, 0.1, 0.3, 0.6, 0.33, 1.98, 0.01, 0.12, 0.4, 0.16,
                0.2, 0.1, 0.3, 0.6, 0.33, 1.98, 0.01, 0.12, 0.4, 0.16};

        double[] ansC = {2.4459999999999997, 0.0, 5.3956, 3.2632, 2.5600000000000005, 0.0, 0.0, 2.372, 9.5832, 0.0, 4.146,
                3.5730000000000004, 0.0, 3.0589999999999997, 5.6942, 0.0, 3.439, 0.0, 5.6246, 0.3756, 0.0,
                2.5120000000000005, 0.0, 0.12190000000000001, 0.0, 6.159400000000001, 0.0, 0.0, 0.0, 0.0, 0.916, 0.0,
                2.288, 5.8915999999999995, 0.0, 0.0, 0.0, 1.268, 3.6697999999999995, 0.0};

        double[] ansS = (new Starfield("./inputs/multVec.txt")).multiplyVector(vector);
        // Check each value in the vector
        boolean flag = true;
        for (int i = 0; i < ansC.length; i++) {
            if (Math.abs(ansS[i] - ansC[i]) > eps) {
                flag = false;
                break;
            }
        }
        if (flag) {
            totalScore2 += 9;
            System.out.println("    Multiplication: pass");
        } else {
            System.out.println("    Multiplication: FAIL");
        }


        // testing combine
        String combined = (Starfield.combine(new Starfield("./inputs/comb1.txt"),
                new Starfield("./inputs/comb2.txt"))).toString();
        String ans = readFile("./outputs/combine_ans.txt");

        if (combined.equals(ans)) {
            totalScore2 += 9;
            System.out.println("    Combine: pass");
        } else {
            System.out.println("    Combine: FAIL");
        }

        // testing transpose
        flag = true;
        String temp = "";
        for (int i = 0; i < 4; i++) {
            Starfield solution = new Starfield("./inputs/" + testFiles[i]);
            solution.transpose();
            String transpose = solution.toString();
            String answer = readFile("./outputs/" + ansFiles2[i] + "_transpose_ascii.txt");
            if (!transpose.equals(answer)) {
                flag = false;
                temp = temp + "\n        " + testFiles[i] + ": FAIL";
            }
        }
        if (flag) {
            totalScore2 += 9;
            System.out.println("    Transpose: pass");
        } else {
            System.out.print("    Transpose: FAIL");
            System.out.println(temp);
        }

        return totalScore2;
    }





    private static String readFile(String s) {
        String answer = "";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            while ((line = br.readLine()) != null) {
                answer = answer + line + '\n';
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }


}
