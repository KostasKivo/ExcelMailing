package com.company;

public class ErasmusStudents {

    private String name,surname;
    private String email;
    private String telephoneNumber;

    public ErasmusStudents(String name , String surname, String email, String telephoneNumber ) {
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEmail() { return  email; }


}
