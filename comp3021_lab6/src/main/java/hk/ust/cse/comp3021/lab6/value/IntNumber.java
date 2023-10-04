package hk.ust.cse.comp3021.lab6.value;


import hk.ust.cse.comp3021.lab6.structure.Value;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;


/**
 * TODO implement this class as needed.
 * This class implement {@link Value} (since an integer number is technically a value in expression).
 * There can be other type of values, e.g. float number, but in this lab we only implement integer numbers.
 *
 */
public class IntNumber implements Value {
    private final BigInteger val;

    public IntNumber(BigInteger val) {
        this.val = val;
    }

    public IntNumber(String val) {
        this.val = new BigInteger(val);
    }

    @Override
    public String toString() {
        return this.val.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntNumber) {
            return ((IntNumber) o).val.equals(this.val);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.val.hashCode();
    }
}