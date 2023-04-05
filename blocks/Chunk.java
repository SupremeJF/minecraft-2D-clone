package blocks;

public class Chunk {
    
    double cx;
    double cy;

    public Chunk(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
    }

    @Override 
    public boolean equals(Object o){
        if(o instanceof Chunk){
            Chunk c = (Chunk) o;
            return c.cx == this.cx && c.cy == this.cy;
        }
        return false;
    }

    public double[] toDouble() {
        return new double[]{cx, cy};
    }
}
