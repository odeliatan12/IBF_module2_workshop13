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

// To form the validation of the contact
public class Contact implements Serializable {
    
    // Form the validation
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 64, message = "Please enter a name that is between 3 and 64 characters")
    private String name;

    // Do not require a non-null for email because it will auto validate the email
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotNull(message = "Phone number must not be null")
    @Min(value = 7, message = "phone digit must be 8")
    private Integer phoneNumber;

    // Creating a new id
    private String id;

    @Past(message = "date of birth must not be in the future")
    @NotNull(message = "date of birth cannot be null")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateofbirth;


    // Creating a new id for the contact
    public String getContactID(int numofChar){

        // Generating a unique number for the contact
        Random rand = new Random();

        // Forming like a data structure to store the contact
        StringBuilder str = new StringBuilder();

        // Appending the random generated number to the string which will be the number of characters
        while(str.length() < numofChar){
            str.append(Integer.toHexString(rand.nextInt()));
        }

        return id;
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

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    
}
