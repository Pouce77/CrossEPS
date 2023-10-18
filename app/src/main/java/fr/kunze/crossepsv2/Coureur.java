package fr.kunze.crossepsv2;

public class Coureur {
    String nom;
    int place;
    String temps;
    String nomCourse;
    public Coureur(String nom, int place, String temps, String nomCourse) {
        this.nom = nom;
        this.place = place;
        this.temps = temps;
        this.nomCourse=nomCourse;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public int getPlace() {
        return place;
    }
    public void setPlace(int place) {
        this.place = place;
    }
    public String getTemps() {
        return temps;
    }
    public void setTemps(String temps) {
        this.temps = temps;
    }
    public String getNomCourse() {
        return nomCourse;
    }
    public void setNomCourse(String nomCourse) {
        this.nomCourse = nomCourse;
    }
}
