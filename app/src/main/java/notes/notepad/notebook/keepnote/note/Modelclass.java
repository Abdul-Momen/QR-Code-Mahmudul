package notes.notepad.notebook.keepnote.note;

public class Modelclass {
    String t,d,dt,k;
    int color;

    public Modelclass() {
    }

    public Modelclass(String t, String d, String dt, String k, int color) {
        this.t = t;
        this.d = d;
        this.dt = dt;
        this.k = k;
        this.color = color;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
