package hk.ust.cse.comp3021.lab6.structure;

import hk.ust.cse.comp3021.lab6.value.IntNumber;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringJoiner;


/**
 * TODO implement this class as needed.
 * Operation should implement {@link Expression}.
 * Operation consists of {@link Operator} and a list of {@link Expression} as its operands.
 */

public class Operation implements Expression {
    private Operator operator;
    private ArrayList<Expression> operands;

    public Operation(Operator operator, ArrayList<Expression> operands) {
        this.operator = operator;
        this.operands = operands;
    }

    @Override
    public Value eval() {
        var op = new ArrayList<Expression>();
        op.add(null);
        op.add(null);
        if (this.operands.size() == 1) {
            op.set(1, this.operands.get(0).eval());
            return this.operator.operate(op);
        }
        op.set(0, this.operands.get(0).eval());
        for (int i = 1; i < this.operands.size(); i++) {
            op.set(1, this.operands.get(i).eval());
            op.set(0, this.operator.operate(op));
        }
        return op.get(0).eval();
    }

    @Override
    public String toString() {
        return "(" + this.operator.symbol() + " " + this.operands.get(0).toString() + " " + this.operands.get(1).toString() + ")";
    }
}