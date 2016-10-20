package com.tasj.containers;

public class ErrorContainer {
    String error;

    public ErrorContainer(String error) {
        this.error = error;
    }

    public ErrorContainer() {
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorContainer that = (ErrorContainer) o;

        return error != null ? error.equals(that.error) : that.error == null;
    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }
}
