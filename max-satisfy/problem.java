import java.io.*;
import java.net.URL;
import java.util.*;
public class ExtraCredit {
    
    public class Tuple {
        public final int b;
        public final int d;

        public Tuple(int b, int d) {
            this.b = b;
            this.d = d;
        }
    }
    
    public class Triple {
        public final int a;
        public final int c;
        public final int e;

        public Triple(int a, int c, int e) {
            this.a = a;
            this.c = c;
            this.e = e;
        }

        @Override
        public String toString() {
            return "a: " + a + " c: " + c + " e: " + e;
        }
    }


    public void ECProblem() throws IOException {
        int T = 20; // number of test cases
        PrintWriter out = new PrintWriter(new FileWriter(new File("answer.out")));
        URL url = getClass().getResource("official_input_files");
        File[] files = new File(url.getPath()).listFiles();


        HashMap<String, LinkedList<Triple>> map = new HashMap<String, LinkedList<Triple>>();

        for (int t = 1; t <= 30; t++) {
            String path = "src/official_input_files/";
            map.clear();
            Scanner in = new Scanner(new File(path + t + ".in"));
            int V = in.nextInt(), E = in.nextInt(), P = in.nextInt();
            for (int i = 0; i < E; i++) {
                int a = in.nextInt(), b = in.nextInt(), c = in.nextInt(), d = in.nextInt(), e = in.nextInt();
                putIntoBins(map, a, b, c, d, e);
            }
            Collection<LinkedList<Triple>> values = map.values();
            // sorted by size of each LinkedList<Triple>
            LinkedList<Triple>[] listOfBins = sort(values);
            int[] optimizedVariables = new int[V];
            LinkedList<LinkedList<Triple>> rotatedBins = new LinkedList<LinkedList<Triple>>();
            for (int i = 0; i < listOfBins.length; i++) {
                rotatedBins.add(listOfBins[i]);
            }
            int numEquationsSolved = 0;
            int maxEquationsSolved = Integer.MIN_VALUE;

            int[] assign = new int[V];

            for (int i = 0; i < V; i++) {
                optimizedVariables[i] = P;
            }
            numEquationsSolved = 0;
                for (int i = 0; i < rotatedBins.size(); i++) {
                    Triple BDValues = possiblePairsUsingGaussianElimination(rotatedBins.get(i), optimizedVariables, P);
                    Triple BDVariables = rotatedBins.get(i).getFirst();
                    if (BDValues != null) {
                        optimizedVariables[BDVariables.a] = BDValues.a;
                        optimizedVariables[BDVariables.c] = BDValues.c;
                        numEquationsSolved += BDValues.e;
                    }

                }
                if (numEquationsSolved > maxEquationsSolved) {
                    maxEquationsSolved = numEquationsSolved;
                    System.arraycopy(optimizedVariables, 0, assign, 0, V);
                }
 
            // find an assignment
            out.print(assign[0]);
            for (int i = 1; i < V; i++)
                out.print(" " + assign[i]);
            out.println();
        }
        out.close();
    }

    /**
     * Quicksort
     * 
     * @param values
     */
    public LinkedList<Triple>[] sort(Collection<LinkedList<Triple>> values) {
        // check for empty or null array
        LinkedList<Triple>[] arrValues = new LinkedList[values.size()];
        values.toArray(arrValues);
        quicksort(0, arrValues.length - 1, arrValues);
        return arrValues;
    }

    public void quicksort(int low, int high, LinkedList<Triple>[] arrValues) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = arrValues[low + (high - low) / 2].size();

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (arrValues[i].size() > pivot) {
                i++;
            }
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (arrValues[j].size() < pivot) {
                j--;
            }

            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j, arrValues);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j, arrValues);
        if (i < high)
            quicksort(i, high, arrValues);
    }

    private void exchange(int i, int j, LinkedList<Triple>[] arrValues) {
        LinkedList<Triple> temp = arrValues[i];
        arrValues[i] = arrValues[j];
        arrValues[j] = temp;
    }


    /**
     * 
     * @param eqns
     *            LinkedList of Triples that contain a,c,e values for every
     *            equation using x_b and x_d
     * @return Triple that contains {x_b, x_d, max_num_equations_satified}
     */

    public Triple possiblePairsUsingGaussianElimination(LinkedList<Triple> eqns, int[] vars, int P) {
        // look at every possible pair for b and d that satisfies every pair of two equations
        // in eqns. Then see how many equations those certain values for b and d satisfy
        // using numEquationsSatified and find the pair that maximizes this number.
        int maxEquationsSolved = 0;
        int equationsSolved = 0;
        Triple BDVars = eqns.getFirst();
        int b = BDVars.a;
        int d = BDVars.c;
        Triple maxTriple = new Triple(0, 0, 0);

        if (vars[b] != P && vars[d] != P) {
            return null;
        }

        if (vars[b] == P && vars[d] != P) {
            for (int i = 1; i < eqns.size(); i++) {
                Triple tripleRow = eqns.get(i);
                int[] row = { tripleRow.a, tripleRow.c, -tripleRow.e };
                int bVal = EuclidAlgorithm.solveForFirstVar(row, P, vars[d]);
                equationsSolved = numEquationsSatisfied(bVal, vars[d], eqns, P);
                if (maxEquationsSolved < equationsSolved) {
                    maxEquationsSolved = equationsSolved;
                    maxTriple = new Triple(bVal, vars[d], equationsSolved);
                }
            }
            return maxTriple;
        } else if (vars[b] != P && vars[d] == P) {
            for (int i = 1; i < eqns.size(); i++) {
                Triple tripleRow = eqns.get(i);
                int[] row = { tripleRow.a, tripleRow.c, -tripleRow.e };
                int dVal = EuclidAlgorithm.solveForSecondVar(row, P, vars[b]);
                equationsSolved = numEquationsSatisfied(vars[b], dVal, eqns, P);
                if (maxEquationsSolved < equationsSolved) {
                    maxEquationsSolved = equationsSolved;
                    maxTriple = new Triple(vars[b], dVal, equationsSolved);
                }
            }
            return maxTriple;
        }
        if (eqns.size() == 2) {
            Triple tripleRow = eqns.get(1);
            int[] row = { tripleRow.a, tripleRow.c, -tripleRow.e };
            int bVal = EuclidAlgorithm.solveForFirstVar(row, P, 0);
            equationsSolved = numEquationsSatisfied(bVal, 0, eqns, P);
            maxTriple = new Triple(bVal, 0, equationsSolved);
            return maxTriple;
        }
        // System.out.println(eqns.size());
        for (int i = 1; i < eqns.size() - 1; i++) {
            for (int j = i + 1; j < eqns.size(); j++) {
                equationsSolved = 0;
                Triple triple1 = eqns.get(i);
                Triple triple2 = eqns.get(j);
                int[] row1 = { triple1.a, triple1.c, -triple1.e };
                int[] row2 = { triple2.a, triple2.c, -triple2.e };
                int[] sol = EuclidAlgorithm.solveTwoEquations2(row1, row2, P);
                if (sol != null) {
                    equationsSolved += numEquationsSatisfied(sol[0], sol[1], eqns, P);
                    if (maxEquationsSolved < equationsSolved) {
                        maxEquationsSolved = equationsSolved;

                        maxTriple = new Triple(sol[0], sol[1], equationsSolved);
                    }
                }

            }
        }
        return maxTriple;

    }



    public int numEquationsSatisfied(int b, int d, LinkedList<Triple> eqns, int P) {
        int sum = 0;
        for (int i = 1; i < eqns.size(); i++) {
            Triple triple = eqns.get(i);
            sum += satisfiesEquations(triple.a, triple.c, triple.e, b, d, P);
        }
        return sum;
    }

    public int satisfiesEquations(int a, int c, int e, int x1, int x2, int P) {
        if ((a * x1 + c * x2 + e) % P == 0) {
            return 1;
        }
        return 0;
    }


    /**
     * HashMap that maps a String 'x_b + " " + x_d' to list of Triples that
     * contain variables a, c, e, of the equation that contains x_b and x_d
     */

    public void putIntoBins(HashMap<String, LinkedList<Triple>> map, int a, int b, int c, int d, int e) {
        int first = Math.min(b, d);
        int second = Math.max(b, d);
        String key = first + " " + second;
        LinkedList<Triple> listOfTriples = map.get(key);
        if (listOfTriples == null) {
            listOfTriples = new LinkedList<Triple>();
            // first entry in LinkedList contains values for b and d. 
            //Third value is irrelevant
            Triple firstTriple = new Triple(first, second, 0);
            listOfTriples.add(firstTriple);

        }
        Triple triple;
        if (b < d) {
            triple = new Triple(a, c, e);
        } else {
            triple = new Triple(c, a, e);
        }
        listOfTriples.add(triple);
        map.put(key, listOfTriples);

    }
    



    public static void main(String[] args) throws IOException {
        ExtraCredit ec = new ExtraCredit();
        HashMap<int[], int[]> map = new HashMap<int[], int[]>();
        int[] arr1 = { 1, 2 };
        int[] arr2 = { 1, 2, 3 };
        int[] arr3 = { 1, 3 };
        int[] arr4 = { 1, 3, 4 };
        map.put(arr1, arr2);
        map.put(arr3, arr4);
        System.out.println(map.keySet().size());
        System.out.println((-8) % 7);
        ec.ECProblem();
        
    }
}
