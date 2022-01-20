package com.revature.models.accounts;

import java.util.Objects;

public class ExerciseAccountRequest {
    public int requestNumber;
    public String decision;

    public ExerciseAccountRequest() {super();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseAccountRequest that = (ExerciseAccountRequest) o;
        return requestNumber == that.requestNumber && Objects.equals(decision, that.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestNumber, decision);
    }

    @Override
    public String toString() {
        return "ExerciseAccountRequest{" +
                "requestNumber=" + requestNumber +
                ", decision='" + decision + '\'' +
                '}';
    }
}
