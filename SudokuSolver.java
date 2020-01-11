import java.util.*;

public class SudokuSolver {
    // TODO: make this an interface and try implementing it using BitSet
    static class Note {

        private boolean[] s; // boolean array representing possible solutions
        private int nt; // number of true entries in s

        public Note(int n) { // creates array of all true values
            s = new boolean[n];
            for (int i = 0; i < n; i++) {
                s[i] = !s[i];
            }
            nt = s.length;
        }

        public Note(Note x) {
            s = x.s.clone();
            nt = x.nt;
        }
        /*
        public boolean has(int n) {
            if (n < 1 || n > s.length) throw new IllegalArgumentException("Note value out of range");
            return s[n - 1];
        }

        public void flipAll() {
            for (int i = 0; i < s.length; i++) {
                s[i] = !s[i];
            }
            nt = s.length - nt;
        }
        */
        public void off(int n) { // only turns off value
            if (n < 1 || n > s.length) throw new IllegalArgumentException("Note value out of range");
            nt = (s[n - 1]) ? nt - 1 : nt;
            s[n - 1] = false;
        }

        public void minus(Note x) { // turns off all things true in x
            if (x == null) throw new IllegalArgumentException("input Note is null");
            if (x.s.length != s.length) throw new IllegalArgumentException("input Note of different length");
            for (int i = 0; i < s.length; i++) {
                if (x.s[i]) this.off(i + 1);
            }
        }

        public int getNt() {
            return nt;
        }

        public int getFirst() { // first true value in note
            for (int i = 0; i < s.length; i++) {
                if (s[i]) return i + 1;
            }
            throw new RuntimeException("no true elements in Note");
        }

        public int getFirst(int n) { // first true value in note after n
            for (int i = n; i < s.length; i++) {
                if (s[i]) return i + 1;
            }
            throw new RuntimeException("no true elements in Note after index " + n);
        }

        public String toString() {
            return Arrays.toString(s);
        }
    }

    // checks if valid sudoku board (right size, no rules already violated, right numbers)
    public static void checkBoard(Integer[][] b) {
        if (b == null) throw new NullPointerException("null input");
        int len = b.length;
        if (len == 0) throw new IllegalArgumentException("empty 2d array");
        else if (len != b[0].length) throw new IllegalArgumentException("unequal board dimensions");
        else if (Math.sqrt(len) % 1 > 0) throw new IllegalArgumentException("board dimensions must be squares");
        int dim = (int) Math.sqrt(len);
        for (int i = 0; i < len; i++) {
            Note x = new Note(len);
            Note y = new Note(len);
            Note z = new Note(len);
            int xc = 0; int yc = 0; int zc = 0;
            for (int j = 0; j < len; j++) {
                // i'th row
                if (b[i][j] != null) {
                    try {
                        x.off(b[i][j]);
                        xc++;
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("input board has invalid entry at (" + i + "," + j +")");
                    }
                }
                // i'th column
                if (b[j][i] != null) {
                    try {
                        y.off(b[j][i]);
                        yc++;
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("input board has invalid entry at (" + j + "," + i +")");
                    }
                }
            }
            // i'th block
            for (int k = (i * dim) % len; k < (i * dim) % len + dim; k++) {
                for (int l = (i / dim) * dim; l < (i / dim) * dim + dim; l++) {
                    if (b[k][l] != null) {
                        try {
                            z.off(b[k][l]);
                            zc++;
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("input board has invalid entry at (" + k + "," + l +")");
                        }
                    }
                }
            }
            // checking the notes
            if (len - x.getNt() < xc) {
                throw new IllegalArgumentException("duplicate values in row " + i);
            } else if (len - y.getNt() < yc) {
                throw new IllegalArgumentException("duplicate values in column " + i);
            } else if (len - z.getNt() < zc) {
                throw new IllegalArgumentException("duplicate values in block " + i);
            }
        }
    }

    // plug in correct value at b[i][j] and properly update the notes
    private static void insert(Integer[][] b, Note[][] a, int i, int j, int n) {
        if (b == null || a == null) throw new IllegalArgumentException("board or note array is null");
        int len = a.length;
        int dim = (int) Math.sqrt(len);
        // not sure if all this error checking is necessary since it's private and i only use it in solver anyway
        b[i][j] = n;
        a[i][j] = null;
        for (int k = 0; k < len; k++) {
            if (a[i][k] != null) a[i][k].off(n);
            if (a[k][j] != null) a[k][j].off(n);
        }
        for (int k = (i/dim)*dim; k < (i/dim+1)*dim; k++) {
            for (int l = (j/dim)*dim; l < (j/dim+1)*dim; l++) {
                if (a[k][l] != null) a[k][l].off(n);
            }
        }
    }

    private static int subsolver(Integer[][] b, Note[][] a, int bctr) {
        int len = b.length;
        int dim = (int) Math.sqrt(len);
        // TODO: many opportunities for optimization below
        // need to go through each note to see if it has unique values in row, col, block
        int guess = 0;
        int ctr = 0;
        while (bctr < len * len) {
            for (int i = 0; i < len; i ++) {
                for (int j = 0; j < len; j++) {
                    if (a[i][j] == null) continue;
                    if (a[i][j].getNt() == 1) {
                        insert(b, a, i, j, a[i][j].getFirst());
                        ctr++;
                        continue;
                    }
                    Note x = new Note(a[i][j]);
                    Note y = new Note(a[i][j]);
                    Note z = new Note(a[i][j]);
                    for (int k = 0; k < len; k++) {
                        if (a[i][k] != null && a[i][k] != a[i][j]) x.minus(a[i][k]);
                        if (a[k][j] != null && a[k][j] != a[i][j]) y.minus(a[k][j]);
                    }
                    for (int k = (i/dim)*dim; k < (i/dim+1)*dim; k++) {
                        for (int l = (j/dim)*dim; l < (j/dim+1)*dim; l++) {
                            if (a[k][l] != null && a[k][l] != a[i][j]) z.minus(a[k][l]);
                        }
                    }
                    if (x.getNt() > 1 || y.getNt() > 1 || z.getNt() > 1) {
                        throw new IllegalArgumentException("Board is overspecified");
                    } else if (x.getNt() == 1) {
                        insert(b, a, i, j, x.getFirst());
                        ctr ++;
                    } else if (y.getNt() == 1) {
                        insert(b, a, i, j, y.getFirst());
                        ctr++;
                    } else if (z.getNt() == 1) {
                        insert(b, a, i, j, z.getFirst());
                        ctr++;
                    }
                }
            }
            if (ctr == 0) {
                // gotta guess at this point, the determinism has ended
                int min = len + 1;
                int mi = -1;
                int mj = -1;
                for (int i = 0; i < len; i ++) {
                    for (int j = 0; j < len; j++) {
                        if (min == 2) break;
                        if (a[i][j] != null && a[i][j].getNt() < min) {
                            min = a[i][j].getNt();
                            mi = i; mj = j;
                        }
                    }
                }
                assert a[mi][mj] != null;
                Note g = new Note(a[mi][mj]);
                // these deep copies suck, wish I didn't have to do it
                Integer[][] bo = b.clone();
                Note[][] ao = new Note[len][len];
                for (int i = 0; i < len; i ++) {
                    for (int j = 0; j < len; j++) {
                        if (a[i][j] != null) ao[i][j] = new Note(a[i][j]);
                    }
                }
                //System.out.println("g: " + g.toString());
                int f = 0;
                for (int k = 0; k < g.getNt(); k++) {
                    int s = g.getFirst(f);
                    try {
                        // guess one of the values
                        insert(b, a, mi, mj, s);
                        bctr++;
                        guess += subsolver(b, a, bctr) + 1;
                        return guess;
                    } catch (Exception e) {
                        // roll back all edits
                        b = bo.clone();
                        for (int i = 0; i < len; i ++) {
                            for (int j = 0; j < len; j++) {
                                if (ao[i][j] != null) a[i][j] = new Note(ao[i][j]);
                            }
                        }
                        bctr--;
                        guess++;
                        f = s;
                    }
                }
                //return guess;
                throw new IllegalArgumentException("Board is underspecified");
            }
            bctr += ctr;
            ctr = 0;
        }
        return guess;
    }

    // the main function
    public static int solver(Integer[][] b) {
        checkBoard(b);
        int len = b.length;
        int dim = (int) Math.sqrt(len);
        // start by keeping lists of potential values
        Note[][] a = new Note[len][len];
        // looping through each cell
        int bctr = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (b[i][j] != null) {
                    bctr++;
                    continue;
                }
                // note will mark all possible solutions for this cell
                a[i][j] = new Note(len);
                // going through row and column to remove existing values from note
                for (int k = 0; k < len; k++) {
                    if (b[i][k] != null) a[i][j].off(b[i][k]);
                    if (b[k][j] != null) a[i][j].off(b[k][j]);
                }
                // going through block to remove existing values from note
                for (int k = (i/dim)*dim; k < (i/dim+1)*dim; k++) {
                    for (int l = (j/dim)*dim; l < (j/dim+1)*dim; l++) {
                        if (b[k][l] != null) a[i][j].off(b[k][l]);
                    }
                }
            }
        }
        // at this point, notes should reflect all possible solutions for each cell
        return subsolver(b, a, bctr);
    }
    
    public static void main(String[] args) {
        Integer[][] arr = new Integer[9][9];
        arr[0][0] = 3; arr[0][1] = 4; arr[0][2] = 5;
        arr[1][2] = 6; arr[1][5] = 1;
        arr[2][0] = 8; arr[2][2] = 1; arr[2][4] = 7; arr[2][6] = 2;
        arr[3][2] = 3; arr[3][5] = 8;
        arr[4][0] = 6; arr[4][7] = 5;
        arr[5][2] = 4; arr[5][3] = 1; arr[5][4] = 9; arr[5][6] = 6;
        arr[6][3] = 6; arr[6][5] = 5; arr[6][6] = 1; arr[6][8] = 3;
        arr[7][6] = 7;
        arr[8][5] = 4;
        /*
        arr[0][1] = 2; arr[0][3] = 9; arr[0][5] = 3; arr[0][7] = 6; arr[0][8] = 8;
        arr[1][0] = 6; arr[1][3] = 2; arr[1][4] = 5; arr[1][6] = 1;
        arr[2][2] = 5;
        arr[3][1] = 3; arr[3][7] = 8;
        arr[4][1] = 6; arr[4][2] = 4; arr[4][4] = 3; arr[4][5] = 8;
        arr[5][3] = 1; arr[5][4] = 6; arr[5][5] = 2; arr[5][7] = 3;
        arr[6][1] = 4; arr[6][5] = 6; arr[6][6] = 8;
        arr[7][0] = 2; arr[7][4] = 9; arr[7][6] = 4; arr[7][7] = 7;
        arr[8][6] = 9;
         */
        System.out.println(solver(arr));
        System.out.println(Arrays.deepToString(arr).replaceAll("],", "]," + System.getProperty("line.separator")));
    }
}
