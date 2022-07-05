package com.example.springreact.ems.exporters;

import com.example.springreact.ems.entities.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// net.sf.super csv
public class CsvExporter extends AbstractExporter {

    public void exporter(List<User> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, ".csv", "text/csv");

        // configuring csv bean writer
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"USER ID", "NAME", "EMAIL", "ROLE", "ENABLED"};
        String[] fieldMapping = {"id", "name", "email", "roles", "enabled"};
        csvWriter.writeHeader(csvHeader);

        for (User user : userList) {
            csvWriter.write(user, fieldMapping);
        }
        csvWriter.close();
    }

//    public void export(List<User> users, HttpServletResponse response)throws IOException{
//        super.setResponseHeader(response, ".csv", "text/csv");
//
//        ICsvBeanWriter beanWriter=new CsvBeanWriter(response.getWriter(),
//                CsvPreference.STANDARD_PREFERENCE);
//        String [] csvHeader={"ID", "NAME", "EMAIL", "ROLES", "ENABLED"};
//        String [] fieldMapping={"id", "name", "email", "roles", "enabled"};
//        beanWriter.writeHeader(csvHeader);
//
//        for (User user: users){
//            beanWriter.write(user, fieldMapping);
//        }
//        beanWriter.close();
//    }
}
