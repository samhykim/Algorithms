public class EuclidAlgorithm {

	public static void GCD(int x, int y) {
	}
	
	public static int doGCD(int x, int y) {
		if (y == 0)
			return x;
		return doGCD(y, x % y);
	}
	
	public static void extendedGCD(int x, int y) {
		int[] result = doExtendedGCD(x, y);
	}
	
	public static int[] doExtendedGCD(int x, int y) {
		int d, a, b;
		if (y == 0)  {
			int[] vals = {x, 1, 0};
			return vals;
		} else {
			int[] vals = doExtendedGCD(y, x % y);
			d = vals[0]; a = vals[1]; b = vals[2];
			int[] results = {d, b, a - ((x / y) * b)}; 
			return results;
		}
	}
	
	public static int[] performRowOperation(int[] row1, int[] row2, int colPos, int modVal) {
		int multFactor = row2[colPos] * doExtendedGCD(modVal, row1[colPos])[2]; 
		for (int i = 0; i < 3; i++) {
			row2[i] = (row2[i] - (multFactor * row1[i]) % modVal) % modVal;
		}
        changeToPositiveAndMod(row2, modVal);
		return row2;
	}
	
	public static int[] performRowOperation2(int[] row1, int[] row2, int colPos, int modVal) {
		int multFactor = row2[colPos] * doExtendedGCD(modVal, row1[colPos])[2]; 
		for (int i = 0; i < 3; i++) {
			row1[i] = (row2[i] - (multFactor * row1[i]) % modVal) % modVal;
		}
        changeToPositiveAndMod(row1, modVal);
		return row1;
	}
	
    public static int solveForSecondVar(int[] row, int modVal, int b) {
        int x2 = (row[2] - row[0] * b) % modVal;
        if (x2 < 0)
            x2 += modVal;
        int inverse = modInverse(row[1], modVal);
        x2 = (inverse * x2) % modVal;
        return x2;
    }

    public static int solveForFirstVar(int[] row, int modVal, int d) {
        int x1 = (row[2] - row[1] * d) % modVal;
        if (x1 < 0)
            x1 += modVal;
        int inverse = modInverse(row[0], modVal);
        x1 = (inverse * x1) % modVal;
        return x1;
    }

	public static int[] solveTwoEquations(int[] row1, int[] row2, int modVal) {
        performRowOperation(row1, row2, 0, modVal);
        performRowOperation2(row1, row2, 1, modVal);
        changeToPositiveAndMod(row1, modVal);
        changeToPositiveAndMod(row2, modVal);

        if (row1[0] == 0 && row1[1] == 0) {
            return null;
        }
        if (row2[0] == 0 && row2[1] == 0) {
            return null;
        }
		int x0 = row1[2] * doExtendedGCD(modVal, row1[0])[2]; 
		int x1 = row2[2] * doExtendedGCD(modVal, row2[1])[2];
		int[] result = {x0, x1};
        changeToPositiveAndMod(result, modVal);
		return result;
	}
	
    public static int[] solveTwoEquations2(int[] row1, int[] row2, int modVal) {
        int firstVal = row1[0];
        int inverse = modInverse(firstVal, modVal);
        row1[0] = 1;
        row1[1] = inverse * row1[1] % modVal;
        row1[2] = inverse * row1[2] % modVal;
        int secondVal = row2[0];
        int inverse2 = modInverse(secondVal, modVal);
        row2[0] = 1;
        row2[1] = inverse2 * row2[1] % modVal;
        row2[2] = inverse2 * row2[2] % modVal;
        int x2 = (row1[1] - row2[1]) % modVal;
        if (x2 < 0)
            x2 += modVal;
        if (x2 == 0)
            return null;
        int b = (row1[2] - row2[2]) % modVal;
        if (b < 0)
            b += modVal;
        int inverse3 = modInverse(x2, modVal);
        x2 = inverse3 * b % modVal;
        int x1 = (row1[2] - row1[1] * x2) % modVal;
        if (x1 < 0) {
            x1 += modVal;
        }
        int[] sol = { x1, x2 };
        return sol;

    }

    public static int modInverse(int a, int m) {
        a %= m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1)
                return x;
        }
        return 0;
    }

	public static void printArr(int[] arr) {
		String s = "[ ";
		for (int i = 0; i < arr.length; i++) {
			s += arr[i] + "  "; 
		}
		s += "]";
		System.out.println(s);
	}
	
	public static void changeToPositiveAndMod(int[] arr, int modVal) {
		for (int i = 0; i < arr.length; i++) {
			while (arr[i] < 0 ) {
				arr[i] += modVal;
			}
			arr[i] %= modVal;
		}
	}
	
	/**
	 * Example usage 
	 */
	public static void main(String[] args) {

	}
}
