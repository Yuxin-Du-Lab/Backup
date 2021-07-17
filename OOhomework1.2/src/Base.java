public interface Base {
    String toString();

    Factor diff();

    boolean equal(Base newBase);

    String check();
}
