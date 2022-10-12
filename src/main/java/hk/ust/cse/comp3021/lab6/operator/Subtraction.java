package hk.ust.cse.comp3021.lab6.operator;

import hk.ust.cse.comp3021.lab6.structure.Expression;
import hk.ust.cse.comp3021.lab6.structure.Operation;
import hk.ust.cse.comp3021.lab6.structure.Operator;
import hk.ust.cse.comp3021.lab6.structure.Value;
import hk.ust.cse.comp3021.lab6.value.IntNumber;

import java.math.BigInteger;
import java.util.List;

/**
 * TODO implement this class as needed.
 * Subtraction should implement {@link Operator}, and will be used to construct {@link Operation} objects.
 * All operands are instances of {@link IntNumber}.
 * Hint: BigInteger.subtract(BigInteger) are useful for implementing eval()
 * Note: If the number of operands is 1, return the negation. Otherwise, subtract the rest of the operands from
 * the first operand
 */

public class Subtraction implements Operator {
    @Override
    public Value operate(List<Expression> operands) {
        if (operands.get(0) == null) {
            operands.set(0, new IntNumber("0"));
        }
        return new IntNumber(new BigInteger(((IntNumber)operands.get(0).eval()).toString()).subtract(new BigInteger(((IntNumber)operands.get(1).eval()).toString())));
    }

    @Override
    public String symbol() {
        return "-";
    }
}
