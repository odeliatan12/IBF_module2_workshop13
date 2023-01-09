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
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import simpleaddressbook.workshop13.models.Contact;

@Component("contacts")
public class Contacts {
    private static final Logger logger = LoggerFactory.getLogger(Contacts.class); 

    public void saveContact(Contact ctc, Model model, ApplicationArguments appArgs, String defaultDataDir) {
        
        String dataFilename = ctc.getId();
        PrintWriter writer = null;

        try {
            FileWriter file = new FileWriter(getDataDir(appArgs, defaultDataDir) + "/" + dataFilename);

            writer = new PrintWriter(file);

            // printing all the form fields
            writer.println(ctc.getName());
            writer.println(ctc.getEmail());
            writer.println(ctc.getPhoneNumber());
            writer.println(ctc.getDateofbirth().toString());
            writer.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        model.addAttribute("contact", ctc);
    }

    public void getContactID(Model model, ApplicationArguments appArgs, String defaultDataDir, String contactId){

        // Instantiating a new Contact instance
        Contact ctc = new Contact();

        // Converting into the appropriate time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Path filePath = new File(getDataDir(appArgs, defaultDataDir) + "/" + contactId).toPath();

        // Instantiating the charset instance to encode and decode utf-8 characters
        Charset charset = Charset.forName("UTF-8");

        // inserting them into the list 
        List<String> stringlist;
        try {
            stringlist = Files.readAllLines(filePath, charset);

            ctc.setId(contactId);
            ctc.setName(stringlist.get(0));
            ctc.setEmail(stringlist.get(1));
            ctc.setPhoneNumber(Integer.parseInt(stringlist.get(2)));

            LocalDate date = LocalDate.parse(stringlist.get(3), formatter);
            ctc.setDateofbirth(date);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private String getDataDir(ApplicationArguments appArgs, String DefaultDatadir){
        String dataDirResult = " ";
        List<String> optValues = null;
        String[] optValuesArr = null;

        // obtaining the command line arguments
        Set<String> opsNames = appArgs.getOptionNames();

        String[] opsNamesArr = opsNames.toArray(new String[opsNames.size()]);

        if(opsNamesArr.length > 0){
            
            // getting the value of the first option
            optValues = appArgs.getOptionValues(opsNamesArr[0]);

            // form that into an array
            optValuesArr = optValues.toArray(new String[optValues.size()]);

            // getting the first element
            dataDirResult = optValuesArr[0];
        } else {
            dataDirResult = DefaultDatadir;
        }

        return dataDirResult;

    }

}
