package org.waterfallio.doh4j.specification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result {
  @JsonProperty("Status")
  private int status;

  @JsonProperty("Answer")
  private List<Answer> answer = new ArrayList<>();

  public List<Answer> getAnswer() {
    return answer;
  }

  public void setAnswer(List<Answer> answer) {
    this.answer = answer;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Result{" +
        "status=" + status +
        ", answer=" + answer +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Result result = (Result) o;

    return status == result.status &&
        Objects.equals(answer, result.answer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, answer);
  }
}
