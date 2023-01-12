package simpleaddressbook.workshop13.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import simpleaddressbook.workshop13.models.Contact;

// @Component is used for autodetection...
@Component("contacts")
public class Contacts {
    // logger class is mainly used to log messages at different levels of security (e.g. SEVERE, WARNING, INFO, etc.)
    private static final Logger logger = LoggerFactory.getLogger(Contacts.class);

    public void saveContact(Contact ctc, Model model, ApplicationArguments appArgs, String defaultDataDir) {
        String dataFilename = ctc.getId();
        PrintWriter prntWriter = null;
        try {
            // get the location of the file, read the file
            FileWriter fileWriter = new FileWriter(getDataDir(appArgs, defaultDataDir) + "/" + dataFilename);

            // Instantiate the print writer, obtaining the file location
            prntWriter = new PrintWriter(fileWriter);

            // from the contacts object, get the various attributes of the contact
            // printing every line into the file itself
            prntWriter.println(ctc.getName());
            prntWriter.println(ctc.getEmail());
            prntWriter.println(ctc.getPhoneNumber());
            prntWriter.println(ctc.getDateOfBirth().toString());
            prntWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        // Sending the attributes to the model (can also be just new Contact())
        model.addAttribute("contact", new Contact(ctc.getId(), ctc.getName(), ctc.getEmail(), ctc.getPhoneNumber()));
    }

    // method to obtain the form fields in order to display them into the file
    public void getContactById(Model model, String contactId, ApplicationArguments appArgs, String defaultDataDir) {

        // Instantiate a new contact object
        Contact ctc = new Contact();

        // Formatting the date into yyyy-MM-dd format
        // Thinking process: how can i format the date so that i can noe that age is between 10 and 100
        // setting the date into the contact object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {

            // getting the location of the file, 
            // converting the file location into a file path
            // contactId refers to the id generated in the contacts
            // default function is to get the file from the file path and to read all the lines of the file
            Path filePath = new File(getDataDir(appArgs, defaultDataDir) + "/" + contactId).toPath();

            // retrieving the data from the form (can also be in chinese characters...)
            // behind the scenes of JVM
            // JVM will look up the specific type of charset (UTF-8) and return a reference to it
            // .forName is used to get a Charset instance by the name 
            // Once the charset has been instantiated, it can be used to encode or decode character data using the UTF-8 character encoding scheme
            Charset charset = Charset.forName("UTF-8");

            // Reading all the files and adding them into a list
            List<String> stringList = Files.readAllLines(filePath, charset);

            ctc.setId(contactId);

            // Based on the file, get the first line of the file to be the name, set them into the contact object
            ctc.setName(stringList.get(0));

            // for email addresses
            ctc.setEmail(stringList.get(1));

            // for phone numbers have to convert to integer
            ctc.setPhoneNumber(Integer.parseInt(stringList.get(2)));

            // Obtaining the date of birth
            LocalDate dob = LocalDate.parse(stringList.get(3), formatter);
            ctc.setDateOfBirth(dob);

        } catch (IOException e) { // when there is an exception
            logger.error(e.getMessage());

            // Returns a status response (404 error message)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Contact info not found");
        }

        // add the attributes into the model
        model.addAttribute("contact", ctc);
    }

    // Read in the file from the command line argument
    // ApplicationArguments represents the arguments passed to a SpringApplication, it is used to access the command line arguments passed to a springboot application
    // The getOptionNames() method of the ApplicationArguments interface returns a Set of all the option names.
    private String getDataDir(ApplicationArguments appArgs, String defaultDataDir) {
        String dataDirResult = "";
        List<String> optValues = null;
        String[] optValuesArr = null;

        // java MyApp --option1 --option2 value2 --option3 value3 on the command line
        // Then the opsNames set would contain the following strings: "option1", "option2", "option3".
        // .getOptionNames() is from the ApplicationArguments interface
        Set<String> opsNames = appArgs.getOptionNames();
        String[] optNamesArr = opsNames.toArray(new String[opsNames.size()]); // do not need to use this can just use set size instead, a preference to indicate size of the array or set
        // might be more troublesome if its just using the set size, as u might have to use the get method instead which might include other unnecessary codes
        if (optNamesArr.length > 0) {

            // appArgs.getOptionValues is a set of strings, optNamesArr[0] gets the first command string
            optValues = appArgs.getOptionValues(optNamesArr[0]);
            // for preference, do not have to be included, same as above
            optValuesArr = optValues.toArray(new String[optValues.size()]);
            dataDirResult = optValuesArr[0];
        } else {
            dataDirResult = defaultDataDir;
        }

        return dataDirResult;
    }
}
