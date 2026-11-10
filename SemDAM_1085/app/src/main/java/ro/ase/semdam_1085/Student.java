package ro.ase.semdam_1085;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {

    private String nume;
    private Date dataNasterii;
    private float medie;
    private String facultate; //CSIE, FABIZ, REI, MRK
    private boolean areBursa; //DA = true, NU = false

    public Student(){}

    public Student(String nume, Date dataNasterii, float medie, String facultate, boolean areBursa) {
        this.nume = nume;
        this.dataNasterii = dataNasterii;
        this.medie = medie;
        this.facultate = facultate;
        this.areBursa = areBursa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Date getDataNasterii() {
        return dataNasterii;
    }

    public void setDataNasterii(Date dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    public float getMedie() {
        return medie;
    }

    public void setMedie(float medie) {
        this.medie = medie;
    }

    public String getFacultate() {
        return facultate;
    }

    public void setFacultate(String facultate) {
        this.facultate = facultate;
    }

    public boolean isAreBursa() {
        return areBursa;
    }

    public void setAreBursa(boolean areBursa) {
        this.areBursa = areBursa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", dataNasterii=" + dataNasterii +
                ", medie=" + medie +
                ", facultate='" + facultate + '\'' +
                ", areBursa=" + areBursa +
                '}';
    }
}
