package simpleaddressbook.workshop13.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 64, message = "Name must be between 3 and 64 characters")
    private String name;

    @Email(message = "Invalid Email")
    private String email;

    @Min(value = 8, message = "Phone number must be 8 digit")
    private int phoneNumber;

    private String id;

    @Past(message = "Date of birth must not be future")
    @NotNull(message = "Date of Birth must be mandatory")
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    // if use localdate age is not captured
    private LocalDate dateOfBirth;

    public Contact() {
        this.id = this.generateId(8);
    }

    public Contact(String name, String email, int phoneNumber) {
        this.id = this.generateId(8);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Contact(String id, String name, String email, int phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private synchronized String generateId(int numChars) {
        Random r = new Random();

        // allows to modify the string such as append, delete, or inserting characters
        // e.g. sb.append("hello") sb.append("world") --> "hello world"
        StringBuilder sb = new StringBuilder();

        // As long as the number of characters is more than the length of the string, append/add the characters to the stringbuilder. traslate the integer to hex string
        while (sb.length() < numChars) {

            // Integer.toHexString() is a method that converts an integer to its corresponding hexadecimal string representation. For example, the integer 10 would be converted to the hexadecimal string "A", and the integer 255 would be converted to the hexadecimal string "FF".
            sb.append(Integer.toHexString(r.nextInt()));
        }

        // slicing method
        return sb.toString().substring(0, numChars);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
