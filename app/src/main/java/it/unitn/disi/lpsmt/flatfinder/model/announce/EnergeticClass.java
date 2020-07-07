package it.unitn.disi.lpsmt.flatfinder.model.announce;

public enum EnergeticClass {

    A4(0),
    A3(1),
    A2(2),
    A1(3),
    B(4),
    C(5),
    D(6),
    E(7),
    F(8),
    G(9);


    private int position;

    EnergeticClass(int position){

        this.position = position;

    }


    public boolean isBetterThan(EnergeticClass other){

        return this.position < other.position;

    }

}
