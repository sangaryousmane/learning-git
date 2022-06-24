package com.shopme.admin;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractExporter {

    public void setResponseHeader(
            HttpServletResponse response,
            String contentType,
            String extension, String prefix){

        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp=dateFormat.format(new Date());
        String fileName=prefix+timeStamp+extension;

        response.setContentType(contentType);
        String headerKey="Content-Disposition";
        String headerValue="attachment;fileName="+fileName;
        response.setHeader(headerKey, headerValue);
    }
}
