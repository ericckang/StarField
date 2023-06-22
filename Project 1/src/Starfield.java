import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*CR STATEMENT
*
* None
*
* */

// Starfield class
public class Starfield {

    // Star nested class
    public static class Star {
        public int row;			// Row index
        public int col;			// Column index
        public double mag;		// Apparent magnitude of the star

        // Links to nearest star in each direction, or null if none
        public Star left;
        public Star right;
        public Star up;
        public Star down;

        // Star constructor
        public Star(int row, int col, double mag) {
            this.row = row;
            this.col = col;
            this.mag = mag;
        }


        // Return a glyph corresponding to the star's apparent magnitude
        public String toString() {
            if (mag >= 7)
                return " ";
            else if (mag >= 6)
                return "`";
            else if (mag >= 5.5)
                return ".";
            else if (mag >= 5)
                return ",";
            else if (mag >= 4)
                return "\'";
            else if (mag >= 3)
                return "\"";
            else if (mag >= 2)
                return "*";
            else if (mag >= 1)
                return "o";
            else
                return "@";
        }
    }

    // Arrays of links to stars in rows and columns of matrix
    public Star[] RA;
    public Star[] CA;


    // Starfield constructors
    // Create an empty starfield with specified size
    public Starfield(int rows, int cols) {
        assert(rows > 0 && cols > 0);
        RA = new Star[rows];
        CA = new Star[cols];
    }
    // Read starfield from an input stream
    public Starfield(InputStream istr) {
        Scanner in = new Scanner(istr);

        // Read the number of rows and cols, and number of stars to insert
        int nrows = in.nextInt();
        int ncols = in.nextInt();
        int nstars = in.nextInt();

        // Create the data arrays
        RA = new Star[nrows];
        CA = new Star[ncols];

        // Keep track of last star inserted and last star in each column
        Star lastStar = null;
        Star[] FA = new Star[ncols];

        // Process each star
        for (int i = 0; i < nstars; i++) {
            // Read the row, column, and magnitude
            int r = in.nextInt();
            int c = in.nextInt();
            double m = in.nextDouble();

            // Create a new star
            Star newStar = new Star(r, c, m);

            // If first star or starting a new row, set the row link
            if (lastStar == null || lastStar.row != r) {
                RA[r] = newStar;
                // Otherwise if in same row as last star, set left/right links
            } else {
                lastStar.right = newStar;
                newStar.left = lastStar;
            }

            // If first star in this column, set the column link
            if (FA[c] == null) {
                CA[c] = newStar;
                // Otherwise set up/down links
            } else {
                FA[c].down = newStar;
                newStar.up = FA[c];
            }

            // Update last inserted and last in column
            lastStar = newStar;
            FA[c] = newStar;
        }
    }
    // Read starfield from a file (do not modify)
    public Starfield(String filename) throws FileNotFoundException {
        this(new FileInputStream(filename));	// Calls the constructor above
    }

    // Starfield properties
    public int numRows() {
        return RA.length;
    }
    public int numCols() {
        return CA.length;
    }
    public int numStarsInRow(int r) {
        int count = 0;
        Star star = RA[r];
        while (star != null) {
            count++;
            star = star.right;
        }
        return count;
    }
    public int numStarsInCol(int c) {
        int count = 0;
        Star star = CA[c];
        while (star != null) {
            count++;
            star = star.down;
        }
        return count;
    }
    public float density() {
        int m = numRows();
        int n = numCols();
        int numStars = 0;
        for (int i = 0; i < m; i++) {
            numStars += numStarsInRow(i);
        }
        return (float) numStars / (m * n);
    }

    // Return a string representation of the starfield
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < numRows(); r++) {
            int c = 0;
            Star currStar = RA[r];
            // Iterate over stars in rows
            while (currStar != null) {
                // Add empty cells to the left of this one
                for (; c < currStar.col; c++)
                    sb.append(" ");
                // Add this star
                sb.append(currStar.toString());
                currStar = currStar.right;
                c++;
            }
            // Add any remaining empty cells in row
            for (; c < numCols(); c++)
                sb.append(" ");
            sb.append("\n");
        }

        return sb.toString();
    }


    // Return the Star at row r and column c if it exists
    // HELPER METHOD -- not given to students, nor expected to implement




    // Retrieve the magnitude of the star at row r and column c
    public double getMagnitude(int r, int c) {
        Star star = RA[r];
        while (star != null && star.col < c) {
            star = star.right;
        }
        if (star != null && star.col == c) {
            return star.mag;
        }
        return 0;
    }

    // Set the magnitude at row r and column c, creating a Star if necessary


    public void setMagnitude(int r, int c, double mag) {
        Star currentStar = RA[r];
        while (currentStar != null && currentStar.col != c) {
            currentStar = currentStar.right;
        }
        if (currentStar != null) {
            currentStar.mag = mag;
        } else {
            Star newStar = new Star(r, c, mag);
            newStar.right = RA[r];
            if (RA[r] != null) {
                RA[r].left = newStar;
            }
            RA[r] = newStar;
            if (CA[c].up == null) {
                CA[c].up = newStar;
                CA[c].down = newStar;
                newStar.up = CA[c];
                newStar.down = CA[c];
            } else {
                newStar.up = CA[c].up;
                newStar.down = CA[c];
                CA[c].up.down = newStar;
                CA[c].up = newStar;
            }
        }
    }


    // Remove the star at row r and column c
    public void unsetMagnitude(int r, int c) {
        Star star = RA[r];
        while (star != null && star.col != c) {
            star = star.right;
        }
        if (star != null) {
            if (star.left != null) {
                star.left.right = star.right;
            } else {
                RA[r] = star.right;
            }
            if (star.right != null) {
                star.right.left = star.left;
            }
            if (star.up != null) {
                star.up.down = star.down;
            } else {
                CA[c] = star.down;
            }
            if (star.down != null) {
                star.down.up = star.up;
            }
        }

    }

    // Transpose the starfield in-place
    public void transpose() {
        int m = numRows();
        int n = numCols();
        //setting row and col for each star

        for(int i = 0; i < m; i++){
            Star star = RA[i];
            while(star != null) {
                Star nextstar = star.right;
                Star temp = star.down;
                star.down = star.right;
                star.right = temp;
                Star temp3 = star.left ;
                star.left = star.up;
                star.up = temp3;


               int temp2 = star.row;
               star.row = star.col;
               star.col = temp2;
               star = nextstar;
            }
        }
        Star[] temp = RA;
        RA = CA;
        CA = temp;

        /*
        for (int i = 0; i < n; i++) {
            Star star = RA[i];
            while (star != null) {
                int temp2 = star.row;
                star.row = star.col;
                star.col = temp2;
                star = star.right;
            }
        }

        for (int i = 0; i < m; i++) {
            Star star = CA[i];
            while (star != null) {
                int temp2 = star.row;
                star.row = star.col;
                star.col = temp2;
                star = star.down;
            }
        }*/

    }

    // Multiply starfield by dense vector
    public double[] multiplyVector(double[] vector) {
        int m = numRows();
        int n = numCols();
        double total = 0.0;
        double[] arrtotal;
        arrtotal = new double[numCols()];
        double mag;
        int arrcount = 0;
        for(int i = 0; i < m; i++){
            int counter = 0;
            for(int d = 0; d < n; d++) {
                mag = getMagnitude(i,d);
                total += mag * vector[counter];
                counter++;
            }
            arrtotal[arrcount] = total;
            arrcount++;
            total = 0;
        }
        return arrtotal;
    }

    // Combine two starfields and return the result


    public static Starfield combine(Starfield S, Starfield T) {
        int rows = Math.max(S.numRows(), T.numRows());
        int cols = Math.max(S.numCols(), T.numCols());
        Starfield W = new Starfield(rows, cols);
        Star[] totalRA = new Star[rows];
        Star[] totalCA = new Star[cols];

        for (int i = 0; i < rows; i++) {
            Star ScurrentStar = S.RA[i];
            Star TcurrentStar = T.RA[i];
            Star currentStar;
            Star prevStar = null;
            while (ScurrentStar != null || TcurrentStar != null) {
                if (ScurrentStar == null) {
                    currentStar = new Star(TcurrentStar.row, TcurrentStar.col, TcurrentStar.mag);
                    TcurrentStar = TcurrentStar.right;
                } else if (TcurrentStar == null) {
                    currentStar = new Star(ScurrentStar.row, ScurrentStar.col, ScurrentStar.mag);
                    ScurrentStar = ScurrentStar.right;
                } else if (ScurrentStar.col < TcurrentStar.col) {
                    currentStar = new Star(ScurrentStar.row, ScurrentStar.col, ScurrentStar.mag);
                    ScurrentStar = ScurrentStar.right;
                } else if (ScurrentStar.col == TcurrentStar.col) {
                    double newmag = -2.5 * Math.log10(Math.pow(10,-ScurrentStar.mag * 0.4) + Math.pow(10,-TcurrentStar.mag * 0.4));
                    currentStar = new Star(ScurrentStar.row, ScurrentStar.col, ScurrentStar.mag);
                    ScurrentStar = ScurrentStar.right;
                    TcurrentStar = TcurrentStar.right;
                }
                else {
                    currentStar = new Star(TcurrentStar.row, TcurrentStar.col, TcurrentStar.mag);
                    TcurrentStar = TcurrentStar.right;
                }
                if (prevStar == null) {
                    totalRA[i] = currentStar;
                    prevStar = currentStar;
                } else {
                    prevStar.right = currentStar;
                    prevStar.right.left = prevStar;
                    prevStar = currentStar;
                }
            }
        }
        for (int i = 0; i < cols; i++) {
            Star currentStar = null;
            Star prevStar = null;
            for (int j = 0; j < rows; j++) {
                Star currentRA = totalRA[j];
                while (currentRA != null && currentRA.col < i) {
                    currentRA = currentRA.right;
                }
                if (currentRA != null && currentRA.col == i) {
                    if (prevStar == null) {
                        totalCA[i] = currentRA;
                        prevStar = currentRA;
                    } else {
                        prevStar.down = currentRA;
                        prevStar.down.up = prevStar;
                        prevStar = currentRA;
                    }
                }
            }
        }
        W.RA = totalRA;
        W.CA = totalCA;
        return W;
    }



    public static void main(String[] args) {
        Starfield s = null;

        if (args.length > 0) {
            try {
                s = new Starfield(args[0]);

            } catch (FileNotFoundException e) {
                System.out.println();
                return;
            }
        } else {
            s = new Starfield(System.in);
        }

        System.out.print(s.toString());
    }
}