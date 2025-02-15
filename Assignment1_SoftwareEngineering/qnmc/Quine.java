package qnmc;

public class Quine {
	// macro
	protected static final int MAX_TERMS = 0xff;// 0xff=255
	// attribute
	public MinTerm[] terms = new MinTerm[MAX_TERMS];
	public int count = 0;

	// adding minterms
	public void addTerm(String str) throws ExceptionQuine {
		if (count == MAX_TERMS)
			throw new ExceptionQuine("Quine::addTerm()");
		terms[count++] = new MinTerm(str);
	}

	// converted to string
        @Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < count; i++) {
			buf.append(terms[i]).append("\n");
		}
		return buf.toString();
	}

	// see whether the element already exists
	public boolean hasTerm(MinTerm a) throws ExceptionQuine {
		for (int i = 0; i < count; i++) {
			if (a.isSame(terms[i]))
				return true;
		}
		return false;
	}

	// verification of the function
        @SuppressWarnings("empty-statement")
	public void simplify() throws ExceptionQuine {
		while (reduce() > 0)
			;
	}

	// reduction of the minterm
	private int reduce() throws ExceptionQuine {
		// variable
		int reducedCount = 0;
		MinTerm[] reducedTerms = new MinTerm[MAX_TERMS];
		boolean[] used = new boolean[MAX_TERMS];
		// working with all minterms
		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				// finding the terms which differs in one place
				if (terms[i].resolutionCount(terms[j]) == 1) {
					reducedTerms[reducedCount++] = MinTerm.combine(terms[i],
							terms[j]);
					used[i] = true;
					used[j] = true;
				}
			}
		}
		// copy the unchanged minterm in new list

		int totalReduced = reducedCount;
		for (int i = 0; i < count; i++) {
			if (used[i] == false) {
				reducedTerms[totalReduced++] = terms[i];
			}
		}
		// initialize
		count = 0;
		// storing in a list(except the double term)
		for (int i = 0; i < totalReduced; i++) {
			if (!hasTerm(reducedTerms[i]))
				terms[count++] = reducedTerms[i];
		}
		// number of reduction to produce
		// System.out.println(reducedCount);
		return reducedCount;
	}
}