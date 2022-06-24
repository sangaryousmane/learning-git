package com.shopme.admin.user.export;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(
            List<User> userList,
            HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "text/csv",".csv", "users_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"USER ID", "EMAIL", "FIRST NAME", "LAST NAME", "ROLES", "ENABLED"};
        String [] fieldMapping={"id", "email", "firstName", "lastName", "roles", "enabled"};
        csvBeanWriter.writeHeader(csvHeader);

        // Writing each field Name as columns and iterate through the total content.
        for (User user: userList){
            csvBeanWriter.write(user, fieldMapping);
        }
        csvBeanWriter.close();

    }

}
