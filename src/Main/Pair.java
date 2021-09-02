package Main;

public class Pair <A, B> implements Comparable<Pair<A, B>>{
    
    private A a;
    private B b;
    // bfs
    private Pair<A, B> parent;
    private Pair<A, B> child;
    
    public Pair(A a_, B b_) { a = a_; b = b_; }
    
    public A getA() { return a; }
    public B getB() { return b; }
    public void setParent(Pair<A, B> parent) { this.parent = parent; }
    public void setChild(Pair<A, B> child) { this.child = child; }
    public Pair<A, B> getParent() { return parent; }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair<A, B> pair = (Pair<A, B>) o;
            return pair.getA().equals(a) && pair.getB().equals(b);
        }
        return super.equals(o);
    }
    
    @Override
    public int hashCode() {
        return ((int)a + (int)b) * ((int)a * (int)b) + (int)a * 17 + (int) b * 11;
    }
    
    @Override
    public int compareTo(Pair<A, B> o) {
        if (this.equals(o)) return 0;
        return 1;
    }
    
    @Override
    public String toString() {
        return "[A=" + a + ", B=" + b + "]";
    }
    
    public String toString2() {
        return "[A=" + a + ", B=" + b +
                ", P=" + ((parent == null) ? "null" : "(A=" + parent.a + ", B=" + parent.b + ")") +
                ", C=" + ((child  == null) ? "null" : "(A=" + child.a  + ", B=" + child.b  + ")") + "]";
    }
}
