public interface Base {
    String toString();

    Object diff();

    boolean equal(Base newBase);

    String check();
}
