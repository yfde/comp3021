package hk.ust.cse.comp3021.lab2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Lab2 {

    public static List<List<Integer>> matMul(
            @NotNull final List<List<Integer>> A,
            @NotNull final List<List<Integer>> B) {
        // TODO: Implement this method
        if(A.size() == 0 && B.size() == 0){
            return new ArrayList<>();
        }
        if(A.size() == 0 || A.get(0).size() != B.size()){
            throw new IllegalArgumentException("Matrix dimensions do not match");
        }
        int vector_length = B.size();
        if(vector_length == 0){
            return new ArrayList<>();
        }

        int result_rows = A.size();
        int result_cols = B.get(0).size();

        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i < result_rows; i++)
        {
            List<Integer> result_i = new ArrayList<Integer>();
            for(int j = 0; j < result_cols; j++)
            {
                var result_i_j = 0;
                for(int k = 0; k < vector_length; k++)
                {
                    result_i_j = result_i_j + A.get(i).get(k) * B.get(k).get(j);
                }
                result_i.add(result_i_j);
            }
            result.add(result_i);
        }
        return result;
    }

}
