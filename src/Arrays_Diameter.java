import java.util.Arrays;

public class Arrays_Diameter {

    public static void main(String[] args) {
//        int[] A = {-1,-2,10,0,1,2,-3,-4,5,-6,-7,1,-2};
//        int[] A = {1,2,-10,3,4,-5,6,7,-1,2};
//        int[] A = {1,0,1,0,0,-1,-1,-1,10,-10,2,-1,0,199,0};
//        int[] A = {8,-8, 9, -9, 20, -20, 5, -4};

        int A[] = {3, -2, 5, 1}; // example array. should print { 10, -2, 12, 12, 12 }.
        MaxSubInterval(A);   // O(n)
        MinSubInterval(A);   // O(n)
        CyclicDiam(A);       // O(n^2)
        CyclicDiamByLevit(A); // O(n^2)
        CyclicDiam_Best(A);  // O(n) - MAX{ Diam(A), SUM(A)-(-Diam(-A)) } // 4x O(n).
    }



    public static void MaxSubInterval(int[] A) {System.out.println("(Diameter(A)) MaxSubInterval(" + Arrays.toString(A) + ") = " + Diam(A));
    } // return Diam(A)
    public static void MinSubInterval(int[] A) {
        int[] B = new int[A.length];
        for (int i = 0; i < B.length; ++i)
            B[i] = -A[i];
        System.out.println("(-Diameter(-A)) MinSubInterval(" + Arrays.toString(A) + ") = " + -Diam(B));
    } // return -Diam(-A)

    /**
     * A method that calculates the `Diameter` of an array A.
     * The `Diameter` of an array is the Maximum sum of all sub-arrays of A, with indices i<=j
     * @param A - An array of size n
     * @returns the `Diameter` of A.
     * @runtime: O(n)
     */
    public static int Diam(int[] A) {
        int sum = 0, MAX_SUM = 0;
        int fromI = 0, toJ = 0;
        int n = A.length;
        int[][] D = new int[2][n];
        // Create a table `D` of 2 rows:
        // 0. A[i]: a0,     a1 , ...  ,  an
        // 1. Sums: 0+a0   a0+a1 , ... , a(n-1)+an

        for (int j = 0; j < n; ++j) // init first row. O(n)
            D[0][j] = A[j];

        int i = 0; // begin of best sub-interval
        for (int k = 0; k < n; ++k) { // O(n)

            sum += A[k];
            D[1][k] = sum;

            if (sum < 0) { // if sum is under 0 - then discard it
                sum = 0;
                i = k+1; // restart indexing from next sub-interval
            }

            if (sum >= MAX_SUM) { // If interval-sum has been improved (A[k] >= 0)
                MAX_SUM = sum;    // Remember Max SubInterval
                fromI = i; // beginning of best sub-interval.
                toJ = k;   // toJ moves every "good" iteration. meaning whenever we improve MAX_SUM.
            }
        }
//        System.out.println("SubInterval: [" + fromI + "," + toJ + "], Max Sum: " + MAX_SUM);
        return MAX_SUM;
    }

    // O((n^2)/2) + O(n) = O(n^2)
    public static int[][] IntervalsSumMatrix(int[] A) {

        int n = A.length;
        int[][] ans = new int[n][n];

        for (int i = 0; i < n; ++i) // init main diagonal with A[i]
            ans[i][i] = A[i];

        for (int i = 0; i < n; ++i) // O(n^2/2) = O(n)
            for (int j = i+1; j < n; ++j)
                ans[i][j] = ans[i][j-1] + A[j]; // all sums of sub-intervals [i,j]

//        System.out.println("Intervals Matrix created in O(n^2)");
        return ans;
    }

    /**
     * Given a "Cyclic" array `A`, find the maximum sum of all sub-intervals
     * Example: A:= {3, -2, 5, 1}
     *          CycleDiam(A) = 9
     *          The sum of the sub-interval [2,0] = [2,3,0] = 5 + 1 + 3 = 9
     * @param A - Integer array of size n
     * @return The "Diameter" of the cyclic array A.
     * @runtime O(n^2)
     */
    public static void CyclicDiam(int[] A) {

        // Building matrix of all sub-array sums, where 0 <= (i<=j) <= n
        int D[][] = IntervalsSumMatrix(A); // O(n^2)
        int n = A.length;

        // Filling the bottom triangle of the matrix with all sub-array sums, where i>j.
        for (int i = n-1; i > 0; --i)  // O(n^2)
            for (int j = 0; j < i; ++j)
                D[i][j] = D[0][j] + D[i][n-1];

        // Looking for the diameter by traversing the matrix.
        int diam = 0;
        for (int i = 0; i < n; ++i) // O(n^2)
            for (int j = 0; j < n; ++j)
                diam = Math.max(diam, D[i][j]);

        System.out.println("CyclicDiam("+Arrays.toString(A)+") = " + diam);
    }

    /**
     * Given a "Cyclic" array A, find the maximum sum
     * of all sub-intervals of A, such that 0 <= i,j <= n.
     * example: A:= {3,-2,5,1}
     *          CyclicDiam(A) = 9
     *              [i,j] = [2,0] = [2, 3, 0] = 5 + 1 + 3 = 9
     *              *
     * @param A - An array of size n
     * @runtime - O(n+n+n) = o(n)
     * @prints the Diameter of the "Cyclic" array A
     */
    public static void CyclicDiam_Best(int[] A) {

        int B[] = new int[A.length];
        int sum = 0;
        for (int i = 0; i < A.length; i++) { // O(n)
            B[i] = -A[i];
            sum += A[i];
        }

        int diamA = Diam(A); // O(n)
        int diamB = Diam(B); // O(n) Diam(-A)
        int diameter = Math.max(diamA, sum + diamB); // Max { Diam(A), SUM(A)+Diam(-A)
        System.out.println("CyclicDiamBest("+Arrays.toString(A)+ ") = "+ diameter);
    }

    public static void CyclicDiamByLevit(int[] A) {
        int n = A.length;
        int[][] D = new int[n][n];
        // turn the array into n arrays such as:
        // a1 a2 ... an
        // a2 a3 ... a1
        // ...
        // an an-1 ... a1

        int k;
        int diameter = 0;
        for (int i = 0; i < n; ++i) { // O(n^2)
            k = i;  // A very helpful extra index. k > n most of the time.
            for (int j = 0; j < n; ++j)
                D[i][j] = A[(k++)%n];
        }

        for (int i = 0; i < n; ++i)
            diameter = Math.max(diameter, Diam(D[i]));


//        System.out.println("Cyclic Matrix (By Prof. Levit)");
//        for (int[] a : D)
//            System.out.println(Arrays.toString(a));

        System.out.println("CyclicDiamByLevit(" + Arrays.toString(A) + ") = " + diameter);
    }




}
