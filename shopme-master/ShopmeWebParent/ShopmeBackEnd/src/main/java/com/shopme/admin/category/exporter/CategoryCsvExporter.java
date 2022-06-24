package com.shopme.admin.category.exporter;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CategoryCsvExporter extends AbstractExporter {

    public void export(
            Iterable<Category> categoryList,
            HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(
                httpServletResponse, "text/csv",
                ".csv", "categories_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"CATEGORY ID", "CATEGORY NAME"};
        String[] fieldMapping = {"id", "name"};
        csvBeanWriter.writeHeader(csvHeader);

        // Writing each field Name as columns and iterate through the total content.
        for (Category category : categoryList) {
            category.setName(category.getName().replace("--", " "));
            csvBeanWriter.write(category, fieldMapping);
        }
        csvBeanWriter.close();


    }

}
