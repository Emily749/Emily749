package qnmc.model;

public class MinTerm {

    public static final char NOT_CH = '0';
    public static final char SET_CH = '1';
    public static final char ANY_CH = '_';

    protected static final int NOT = 0;
    protected static final int SET = 1;
    protected static final int ANY = -1;

    protected int count;
    protected int[] term;

    public MinTerm(String str) {
        term = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case NOT_CH -> term[count++] = NOT;
                case SET_CH -> term[count++] = SET;
                case ANY_CH -> term[count++] = ANY;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            switch (term[i]) {
                case NOT -> buf.append(NOT_CH);
                case SET -> buf.append(SET_CH);
                case ANY -> buf.append(ANY_CH);
            }
        }
        return buf.toString();
    }

    public boolean isSame(MinTerm a) throws ExceptionQuine {
        if (count != a.count)
            throw new ExceptionQuine("MinTerm::isSame()");
        for (int i = 0; i < count; i++) {
            if (term[i] != a.term[i])
                return false;
        }
        return true;
    }

    public int resolutionCount(MinTerm a) throws ExceptionQuine {
        if (count != a.count)
            throw new ExceptionQuine("MinTerm::resolutionCount()");
        int resCount = 0;
        for (int i = 0; i < count; i++) {
            if (term[i] != a.term[i])
                resCount++;
        }
        return resCount;
    }

    public int resolutionPosition(MinTerm a) throws ExceptionQuine {
        if (count != a.count)
            throw new ExceptionQuine("MinTerm::resolutionPos()");
        for (int i = 0; i < count; i++) {
            if (term[i] != a.term[i])
                return i;
        }

        return -1;
    }

    public static MinTerm combine(MinTerm a, MinTerm b) throws ExceptionQuine {
        if (a.count != b.count)
            throw new ExceptionQuine("MinTerm::combine()");
        StringBuilder buf = new StringBuilder(a.count);
        for (int i = 0; i < a.count; i++) {
            if (a.term[i] != b.term[i])
                buf.append(ANY_CH);
            else
                buf.append(a.toString().charAt(i));
        }
        return new MinTerm(buf.toString());
    }
}