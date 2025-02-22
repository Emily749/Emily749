package qnmc.model;

public class Quine {
    public static final int MAX_TERMS = 0xff;
    private final MinTerm[] terms = new MinTerm[MAX_TERMS];
    private int count = 0;

    // Add a new minterm to the list
    public void addMinTerms(String str) throws ExceptionQuine {
        if (count == MAX_TERMS)
            throw new ExceptionQuine("Quine::addTerm() - Maximum terms reached.");
        terms[count++] = new MinTerm(str);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buf.append(terms[i]).append("\n");
        }
        return buf.toString();
    }

    // Check if a term exists in the list
    public boolean hasTerm(MinTerm a) throws ExceptionQuine {
        for (int i = 0; i < count; i++) {
            if (a.isSame(terms[i])) {
                return true;
            }
        }
        return false;
    }

    // Simplify the set of minterms
    public void simplify() throws ExceptionQuine {
        int reductionCount;
        do {
            reductionCount = reduce();
        } while (reductionCount > 0);
    }

    // Perform the reduction of terms
    private int reduce() throws ExceptionQuine {
        int reducedCount = 0;
        MinTerm[] reducedTerms = new MinTerm[MAX_TERMS];
        boolean[] used = new boolean[MAX_TERMS];

        // Compare each pair of minterms
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                // Check if the minterms differ by exactly one bit
                if (terms[i].resolutionCount(terms[j]) == 1) {
                    reducedTerms[reducedCount++] = MinTerm.combine(terms[i], terms[j]);
                    used[i] = true;
                    used[j] = true;
                }
            }
        }

        // Add non-reduced minterms back into the list
        int totalReduced = reducedCount;
        for (int i = 0; i < count; i++) {
            if (!used[i]) {
                reducedTerms[totalReduced++] = terms[i];
            }
        }

        // Update terms array with reduced terms
        count = 0;
        for (int i = 0; i < totalReduced; i++) {
            if (!hasTerm(reducedTerms[i])) {
                terms[count++] = reducedTerms[i];
            }
        }

        return reducedCount;  // Return the number of reduced terms
    }
}